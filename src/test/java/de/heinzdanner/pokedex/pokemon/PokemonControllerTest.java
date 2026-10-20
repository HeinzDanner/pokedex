package de.heinzdanner.pokedex.pokemon;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(PokemonController.class)
class PokemonControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    PokemonService pokemonService;

    @Test
    void getPokemon_returnsSimplifiedData() throws Exception {
        when(pokemonService.getByName("clefairy")).thenReturn(new PokemonData(
                "1",
                "35",
                null,
                "clefairy",
                "https://example.com/35.png",
                6,
                75,
                List.of("fairy")
        ));

        mockMvc.perform(get("/api/pokemon/clefairy"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.pokemonId").value("35"))
                .andExpect(jsonPath("$.pokemonName").value("clefairy"))
                .andExpect(jsonPath("$.types[0]").value("fairy"));
    }
}