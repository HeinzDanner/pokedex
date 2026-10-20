package de.heinzdanner.pokedex.collection;

import de.heinzdanner.pokedex.pokemon.PokemonData;
import de.heinzdanner.pokedex.pokemon.PokemonNotFoundException;
import de.heinzdanner.pokedex.pokemon.PokemonService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FavoriteServiceTest {

    @Mock
    FavoritePokemonRepository repository;

    @Mock
    PokemonService pokemonService;

    @InjectMocks
    FavoriteService favoriteService;

    @Test
    void create_shouldBuildAndSaveFavoriteFromPokemonApiData() {
        // given
        FavoriteCreateRequest request = new FavoriteCreateRequest("pikachu", "Mein Starter");

        when(pokemonService.getByName("pikachu")).thenReturn(new PokemonData(
                "25",
                "25",
                null,
                "pikachu",
                "https://example.com/pika.png",
                4,
                60,
                List.of("electric")
        ));

        when(repository.save(any(FavoritePokemon.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // when
        FavoritePokemon saved = favoriteService.create(request);

        // then
        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotBlank();
        // UUID-Format prüfen:
        UUID.fromString(saved.getId());

        assertThat(saved.getPokemonId()).isEqualTo("25");
        assertThat(saved.getNickname()).isEqualTo("Mein Starter");
        assertThat(saved.getPokemonName()).isEqualTo("pikachu");
        assertThat(saved.getPictureUrl()).isEqualTo("https://example.com/pika.png");
        assertThat(saved.getHeight()).isEqualTo(4);
        assertThat(saved.getWeight()).isEqualTo(60);
        assertThat(saved.getTypes()).containsExactly("electric");

        ArgumentCaptor<FavoritePokemon> captor = ArgumentCaptor.forClass(FavoritePokemon.class);
        verify(repository, times(1)).save(captor.capture());
        FavoritePokemon entityToSave = captor.getValue();

        assertThat(entityToSave.getPokemonId()).isEqualTo("25");
        assertThat(entityToSave.getPokemonName()).isEqualTo("pikachu");
        assertThat(entityToSave.getNickname()).isEqualTo("Mein Starter");
    }

    @Test
    void create_shouldPropagatePokemonNotFound_andNotSave() {
        // given
        FavoriteCreateRequest request = new FavoriteCreateRequest("does-not-exist", "Nope");
        when(pokemonService.getByName("does-not-exist"))
                .thenThrow(new PokemonNotFoundException("does-not-exist"));

        // when + then
        assertThrows(PokemonNotFoundException.class, () -> favoriteService.create(request));
        verify(repository, never()).save(any());
    }

    @Test
    void getAll_shouldReturnAllFavorites() {
        FavoritePokemon first = new FavoritePokemon(
                "id-1", "25", "Starter", "pikachu", "url1", 4, 60, List.of("electric")
        );
        FavoritePokemon second = new FavoritePokemon(
                "id-2", "35", "Fairy", "clefairy", "url2", 6, 75, List.of("fairy")
        );

        when(repository.findAll()).thenReturn(List.of(first, second));

        List<FavoritePokemon> result = favoriteService.getAll();

        assertThat(result).hasSize(2);
        assertThat(result).extracting(FavoritePokemon::getId).containsExactly("id-1", "id-2");
        verify(repository, times(1)).findAll();
    }

    @Test
    void getById_shouldReturnFavorite_whenFound() {
        FavoritePokemon found = new FavoritePokemon(
                "id-1", "25", "Starter", "pikachu", "url1", 4, 60, List.of("electric")
        );
        when(repository.findById("id-1")).thenReturn(java.util.Optional.of(found));

        FavoritePokemon result = favoriteService.getById("id-1");

        assertThat(result.getId()).isEqualTo("id-1");
        assertThat(result.getPokemonName()).isEqualTo("pikachu");
        verify(repository, times(1)).findById("id-1");
    }

    @Test
    void getById_shouldThrowCollectionEntryNotFoundException_whenMissing() {
        when(repository.findById("missing")).thenReturn(java.util.Optional.empty());

        CollectionEntryNotFoundException ex = assertThrows(
                CollectionEntryNotFoundException.class,
                () -> favoriteService.getById("missing")
        );

        assertThat(ex.getMessage()).contains("missing");
        verify(repository, times(1)).findById("missing");
    }

    @Test
    void deleteById_shouldDelete_whenFound() {
        FavoritePokemon found = new FavoritePokemon(
                "id-1", "25", "Starter", "pikachu", "url1", 4, 60, List.of("electric")
        );
        when(repository.findById("id-1")).thenReturn(java.util.Optional.of(found));

        favoriteService.deleteById("id-1");

        verify(repository, times(1)).findById("id-1");
        verify(repository, times(1)).delete(found);
    }

    @Test
    void deleteById_shouldThrowCollectionEntryNotFoundException_whenMissing() {
        when(repository.findById("missing")).thenReturn(java.util.Optional.empty());

        assertThrows(CollectionEntryNotFoundException.class, () -> favoriteService.deleteById("missing"));

        verify(repository, times(1)).findById("missing");
        verify(repository, never()).delete(any());
    }

    @Test
    void updateNickname_shouldUpdateOnlyNickname_whenFound() {
        FavoritePokemon existing = new FavoritePokemon(
                "id-1", "25", "Old Nick", "pikachu", "url1", 4, 60, List.of("electric")
        );
        when(repository.findById("id-1")).thenReturn(java.util.Optional.of(existing));
        when(repository.save(any(FavoritePokemon.class))).thenAnswer(inv -> inv.getArgument(0));

        FavoriteUpdateRequest request = new FavoriteUpdateRequest("Arena-Champion");

        FavoritePokemon updated = favoriteService.updateNickname("id-1", request);

        assertThat(updated.getId()).isEqualTo("id-1");
        assertThat(updated.getNickname()).isEqualTo("Arena-Champion");

        // Unverändert:
        assertThat(updated.getPokemonId()).isEqualTo("25");
        assertThat(updated.getPokemonName()).isEqualTo("pikachu");
        assertThat(updated.getPictureUrl()).isEqualTo("url1");
        assertThat(updated.getHeight()).isEqualTo(4);
        assertThat(updated.getWeight()).isEqualTo(60);
        assertThat(updated.getTypes()).containsExactly("electric");

        verify(repository, times(1)).findById("id-1");
        verify(repository, times(1)).save(existing);
    }

    @Test
    void updateNickname_shouldThrowCollectionEntryNotFoundException_whenMissing() {
        when(repository.findById("missing")).thenReturn(java.util.Optional.empty());

        FavoriteUpdateRequest request = new FavoriteUpdateRequest("Arena-Champion");

        assertThrows(CollectionEntryNotFoundException.class, () -> favoriteService.updateNickname("missing", request));

        verify(repository, times(1)).findById("missing");
        verify(repository, never()).save(any());
    }
    
}