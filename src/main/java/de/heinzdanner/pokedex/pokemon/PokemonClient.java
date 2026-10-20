package de.heinzdanner.pokedex.pokemon;

import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class PokemonClient {

    private final RestClient restClient;

    public PokemonClient(RestClient.Builder builder) {
        this.restClient = builder
                .baseUrl("https://pokeapi.co/api/v2")
                .build();
    }

    public PokeApiPokemonResponse fetchByName(String name) {
        return restClient.get()
                .uri("/pokemon/{name}", name.toLowerCase())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                    throw new PokemonNotFoundException(name);
                })
                .body(PokeApiPokemonResponse.class);
    }
}
