package de.heinzdanner.pokedex.common;

import de.heinzdanner.pokedex.pokemon.PokemonNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PokemonNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handlePokemonNotFound(PokemonNotFoundException ex) {
        return new ErrorResponse(ex.getMessage(), LocalDateTime.now());
    }

    public record ErrorResponse(String message, LocalDateTime timestamp) {
    }
}