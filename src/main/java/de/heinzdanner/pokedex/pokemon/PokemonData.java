package de.heinzdanner.pokedex.pokemon;

import java.util.List;

public record PokemonData(
        String id,
        String pokemonId,
        String nickname,
        String pokemonName,
        String pictureUrl,
        int height,
        int weight,
        List<String> types
) {
}