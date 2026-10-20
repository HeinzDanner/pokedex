package de.heinzdanner.pokedex.collection;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import de.heinzdanner.pokedex.common.GlobalExceptionHandler;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
}