package de.heinzdanner.pokedex.collection;

import de.heinzdanner.pokedex.pokemon.PokemonData;
import de.heinzdanner.pokedex.pokemon.PokemonService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class FavoriteService {

    private final FavoritePokemonRepository repository;
    private final PokemonService pokemonService;

    public FavoriteService(FavoritePokemonRepository repository, PokemonService pokemonService) {
        this.repository = repository;
        this.pokemonService = pokemonService;
    }

    public FavoritePokemon create(FavoriteCreateRequest request) {
        PokemonData pokemon = pokemonService.getByName(request.pokemonName());

        FavoritePokemon favorite = new FavoritePokemon();
        favorite.setId(UUID.randomUUID().toString());
        favorite.setPokemonId(pokemon.pokemonId());
        favorite.setNickname(request.nickname());
        favorite.setPokemonName(pokemon.pokemonName());
        favorite.setPictureUrl(pokemon.pictureUrl());
        favorite.setHeight(pokemon.height());
        favorite.setWeight(pokemon.weight());
        favorite.setTypes(pokemon.types());

        return repository.save(favorite);
    }

    public List<FavoritePokemon> getAll() {
        return repository.findAll();
    }

    public FavoritePokemon getById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new CollectionEntryNotFoundException(id));
    }

    public void deleteById(String id) {
        FavoritePokemon favorite = repository.findById(id)
                .orElseThrow(() -> new CollectionEntryNotFoundException(id));
        repository.delete(favorite);
    }

    public FavoritePokemon updateNickname(String id, FavoriteUpdateRequest request) {
        FavoritePokemon favorite = repository.findById(id)
                .orElseThrow(() -> new CollectionEntryNotFoundException(id));

        favorite.setNickname(request.nickname());
        return repository.save(favorite);
    }

}