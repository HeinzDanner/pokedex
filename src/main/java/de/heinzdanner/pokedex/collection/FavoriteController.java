package de.heinzdanner.pokedex.collection;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/collection")
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FavoritePokemon create(@Valid @RequestBody FavoriteCreateRequest request) {
        return favoriteService.create(request);
    }

    @GetMapping
    public List<FavoritePokemon> getAll() {
        return favoriteService.getAll();
    }

    @GetMapping("/{id}")
    public FavoritePokemon getById(@PathVariable String id) {
        return favoriteService.getById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable String id) {
        favoriteService.deleteById(id);
    }

    @PutMapping("/{id}")
    public FavoritePokemon updateNickname(
            @PathVariable String id,
            @Valid @RequestBody FavoriteUpdateRequest request
    ) {
        return favoriteService.updateNickname(id, request);
    }
}
