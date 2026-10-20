package de.heinzdanner.pokedex.pokemon;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record PokeApiPokemonResponse(
        int id,
        String name,
        int height,
        int weight,
        Sprites sprites,
        List<TypeSlot> types
) {
    public record Sprites(
            Other other
    ) {
    }

    public record Other(
            @JsonProperty("official-artwork")
            OfficialArtwork officialArtwork
    ) {
    }

    public record OfficialArtwork(
            @JsonProperty("front_default")
            String frontDefault
    ) {
    }

    public record TypeSlot(
            TypeInfo type
    ) {
    }

    public record TypeInfo(
            String name
    ) {
    }
}