package de.heinzdanner.pokedex.collection;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record FavoriteUpdateRequest(
        @NotBlank(message = "nickname must not be blank")
        @Size(min = 2, message = "nickname must have at least 2 characters")
        String nickname
) {
}
