package de.heinzdanner.pokedex.pokemon;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PokemonService {

    private final PokemonClient pokemonClient;

    public PokemonService(PokemonClient pokemonClient) {
        this.pokemonClient = pokemonClient;
    }

    public PokemonData getByName(String name) {
        PokeApiPokemonResponse api = pokemonClient.fetchByName(name);

        List<String> types = api.types() == null ? List.of() :
                api.types().stream()
                        .map(t -> t.type().name())
                        .toList();

        String picture = null;
        if (api.sprites() != null
                && api.sprites().other() != null
                && api.sprites().other().officialArtwork() != null) {
            picture = api.sprites().other().officialArtwork().frontDefault();
        }

        return new PokemonData(
                String.valueOf(api.id()),   // id (lokale Sicht)
                String.valueOf(api.id()),   // pokemonId (externe Referenz)
                null,
                api.name(),
                picture,
                api.height(),
                api.weight(),
                types
        );
    }
}
