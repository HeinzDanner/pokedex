package de.heinzdanner.pokedex.collection;

import de.heinzdanner.pokedex.common.GlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FavoriteController.class)
@Import(GlobalExceptionHandler.class)
class FavoriteControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    FavoriteService favoriteService;

    @Test
    void createFavorite_returnsCreated() throws Exception {
        when(favoriteService.create(any())).thenReturn(new FavoritePokemon(
                "uuid-1", "25", "Mein Starter", "pikachu", "https://example.com/pika.png",
                4, 60, List.of("electric")
        ));

        mockMvc.perform(post("/api/collection")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"pokemonName":"pikachu","nickname":"Mein Starter"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.pokemonId").value("25"))
                .andExpect(jsonPath("$.nickname").value("Mein Starter"));
    }

    @Test
    void createFavorite_withInvalidInput_returnsBadRequest() throws Exception {
        mockMvc.perform(post("/api/collection")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"pokemonName":"","nickname":"A"}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void getAll_returnsList() throws Exception {
        when(favoriteService.getAll()).thenReturn(List.of(
                new FavoritePokemon("id-1", "25", "Starter", "pikachu", "url1", 4, 60, List.of("electric")),
                new FavoritePokemon("id-2", "35", "Fairy", "clefairy", "url2", 6, 75, List.of("fairy"))
        ));

        mockMvc.perform(get("/api/collection"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void getById_returnsEntry() throws Exception {
        when(favoriteService.getById("id-1")).thenReturn(
                new FavoritePokemon("id-1", "25", "Starter", "pikachu", "url1", 4, 60, List.of("electric"))
        );

        mockMvc.perform(get("/api/collection/id-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("id-1"))
                .andExpect(jsonPath("$.pokemonName").value("pikachu"));
    }

    @Test
    void getById_whenMissing_returnsNotFound() throws Exception {
        when(favoriteService.getById("missing"))
                .thenThrow(new CollectionEntryNotFoundException("missing"));

        mockMvc.perform(get("/api/collection/missing"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void deleteById_returnsNoContent() throws Exception {
        doNothing().when(favoriteService).deleteById("id-1");

        mockMvc.perform(delete("/api/collection/id-1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteById_whenMissing_returnsNotFound() throws Exception {
        doThrow(new CollectionEntryNotFoundException("missing"))
                .when(favoriteService).deleteById("missing");

        mockMvc.perform(delete("/api/collection/missing"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateNickname_returnsUpdatedEntry() throws Exception {
        when(favoriteService.updateNickname(any(), any())).thenReturn(
                new FavoritePokemon("id-1", "25", "Arena-Champion", "pikachu", "url1", 4, 60, List.of("electric"))
        );

        mockMvc.perform(put("/api/collection/id-1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nickname":"Arena-Champion"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nickname").value("Arena-Champion"))
                .andExpect(jsonPath("$.pokemonName").value("pikachu"));
    }

    @Test
    void updateNickname_withInvalidInput_returnsBadRequest() throws Exception {
        mockMvc.perform(put("/api/collection/id-1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nickname":"A"}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }
}