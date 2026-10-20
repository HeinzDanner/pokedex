package de.heinzdanner.pokedex.collection;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface FavoritePokemonRepository extends MongoRepository<FavoritePokemon, String> {
}