package de.heinzdanner.pokedex.collection;

public class CollectionEntryNotFoundException extends RuntimeException {
    public CollectionEntryNotFoundException(String id) {
        super("Collection entry not found: " + id);
    }
}