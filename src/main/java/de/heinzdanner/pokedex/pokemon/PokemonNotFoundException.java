package de.heinzdanner.pokedex.pokemon;

public class PokemonNotFoundException extends RuntimeException {
    public PokemonNotFoundException(String pokemonName) {
        super("Pokemon not found: " + pokemonName);
    }
}
