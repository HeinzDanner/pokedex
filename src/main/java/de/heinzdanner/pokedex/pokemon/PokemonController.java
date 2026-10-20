package de.heinzdanner.pokedex.pokemon;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pokemon")
public class PokemonController {

    private final PokemonService pokemonService;

    public PokemonController(PokemonService pokemonService) {
        this.pokemonService = pokemonService;
    }

    @GetMapping("/{name}")
    public PokemonData getPokemon(@PathVariable String name) {
        return pokemonService.getByName(name);
    }
}