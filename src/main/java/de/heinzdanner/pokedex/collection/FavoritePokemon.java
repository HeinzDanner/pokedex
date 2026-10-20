package de.heinzdanner.pokedex.collection;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document("favorites")
public class FavoritePokemon {

    @Id
    private String id;
    private String pokemonId;
    private String nickname;
    private String pokemonName;
    private String pictureUrl;
    private int height;
    private int weight;
    private List<String> types;

    public FavoritePokemon() {
    }

    public FavoritePokemon(String id, String pokemonId, String nickname, String pokemonName,
                           String pictureUrl, int height, int weight, List<String> types) {
        this.id = id;
        this.pokemonId = pokemonId;
        this.nickname = nickname;
        this.pokemonName = pokemonName;
        this.pictureUrl = pictureUrl;
        this.height = height;
        this.weight = weight;
        this.types = types;
    }

    public String getId() {
        return id;
    }

    public String getPokemonId() {
        return pokemonId;
    }

    public String getNickname() {
        return nickname;
    }

    public String getPokemonName() {
        return pokemonName;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public int getHeight() {
        return height;
    }

    public int getWeight() {
        return weight;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPokemonId(String pokemonId) {
        this.pokemonId = pokemonId;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setPokemonName(String pokemonName) {
        this.pokemonName = pokemonName;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }
}