# Pokédex Service (Spring Boot + MongoDB)

## Überblick

Diese Anwendung lädt Pokémon-Daten aus der PokéAPI und verwaltet eine persönliche Favoriten-Sammlung in MongoDB.

## Tech Stack

- Java 25
- Spring Boot 4.1.1
- Spring Web MVC
- RestClient
- Spring Validation
- Spring Data MongoDB
- JUnit / MockMvc / Mockito

## Konfiguration

Die Mongo-URI wird per Umgebungsvariable gesetzt:

```bash
MONGODB_URI=mongodb+srv://<user>:<password>@<cluster>/<db_name>?retryWrites=true&w=majority
```

In `application.properties`:

```properties
spring.mongodb.uri=${MONGODB_URI}
```

## Starten

```bash
./mvnw clean test
./mvnw spring-boot:run
```

## Endpunkte

### Pokémon laden

`GET /api/pokemon/{name}`

Beispiel:
`GET /api/pokemon/clefairy`

---

### Favorit anlegen

`POST /api/collection`

Body:

```json
{
  "pokemonName": "pikachu",
  "nickname": "Mein Starter"
}
```

---

### Alle Favoriten

`GET /api/collection`

---

### Favorit per ID

`GET /api/collection/{id}`

> Wichtig: Hier muss die interne `id` (UUID) verwendet werden, nicht `pokemonId`.

---

### Favorit löschen

`DELETE /api/collection/{id}`

---

### Nickname ändern

`PUT /api/collection/{id}`

Body:

```json
{
  "nickname": "Arena-Champion"
}
```

## Validation

- `pokemonName` darf nicht leer sein
- `nickname` darf nicht leer sein
- `nickname` muss mindestens 2 Zeichen haben

## Fehlerbehandlung (`@RestControllerAdvice`)

Behandelte Exceptions:

- `PokemonNotFoundException` → 404
- `CollectionEntryNotFoundException` → 404
- `MethodArgumentNotValidException` → 400

Beispiel:

```json
{
  "message": "Pokemon not found: dd",
  "timestamp": "2026-10-20T12:09:11.7345054"
}
```