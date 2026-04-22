# Movie REST API

Create a new branch of your team and implement a simple REST API built with Spring Boot following the Model-View-Controller (MVC) pattern. The API manages a collection of movies stored in memory. Use AI as an assistant and push the changes into your branch.

---

## Project Structure

```
movie-api/
├── pom.xml
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── edu/
│   │   │       └── movie/
│   │   │           └── whitman/
│   │   │               ├── MovieApiApplication.java      ← Spring Boot entry point
│   │   │               ├── controller/
│   │   │               │   └── MovieController.java      ← HTTP layer (C in MVC)
│   │   │               ├── model/
│   │   │               │   └── Movie.java                ← Data structure (M in MVC)
│   │   │               └── service/
│   │   │                   └── MovieService.java         ← Business logic
│   │   └── resources/
│   │       └── application.properties                    ← App configuration
│   └── test/
│       └── java/
│           └── edu/
│               └── movie/
│                   └── whitman/
│                       └── service/
│                           └── MovieServiceTest.java     ← Unit tests (plain JUnit)
```

---

## Layer Responsibilities

| Layer | File | Role |
|---|---|---|
| **Model** | `Movie.java` | Defines the data structure |
| **Service** | `MovieService.java` | Business logic + in-memory storage |
| **Controller** | `MovieController.java` | HTTP routing + request/response handling |
| **Entry Point** | `MovieApiApplication.java` | Bootstraps the Spring Boot application |
| **Unit Tests** | `MovieServiceTest.java` | Tests service logic in isolation |
| **Integration Tests** | `MovieControllerTest.java` | Tests HTTP layer with mocked service |

---

## Data Model

Each movie has the following fields:

| Field | Type | Description |
|---|---|---|
| `id` | Long | Auto-assigned by the system on creation |
| `title` | String | Title of the movie |
| `director` | String | Name of the director |
| `releaseYear` | int | Year the movie was released |

---

## API Endpoints

| Method | Endpoint | Description | Response |
|---|---|---|---|
| GET | `/api/movies` | Retrieve all movies | 200 OK |
| GET | `/api/movies/{id}` | Retrieve a movie by ID | 200 OK / 404 Not Found |
| POST | `/api/movies` | Create a new movie | 201 Created |
| PUT | `/api/movies/{id}` | Update an existing movie | 200 OK / 404 Not Found |
| DELETE | `/api/movies/{id}` | Delete a movie by ID | 204 No Content / 404 Not Found |

---

## Functional Requirements

### Core CRUD Operations

| ID | Requirement |
|---|---|
| FR-01 | The system shall return a list of all movies via `GET /api/movies` |
| FR-02 | The system shall return a single movie by its ID via `GET /api/movies/{id}` |
| FR-03 | The system shall create a new movie via `POST /api/movies` and return it with HTTP 201 |
| FR-04 | The system shall update all fields of an existing movie via `PUT /api/movies/{id}` |
| FR-05 | The system shall delete a movie by ID via `DELETE /api/movies/{id}` and return HTTP 204 |

### Data Requirements

| ID | Requirement |
|---|---|
| FR-06 | Each movie shall have an `id` (Long), `title` (String), `director` (String), and `releaseYear` (int) |
| FR-07 | The system shall auto-assign a unique incrementing ID on creation — the client shall not provide it |

### Error Handling

| ID | Requirement |
|---|---|
| FR-08 | If a movie ID does not exist on GET, PUT, or DELETE, the system shall return HTTP 404 with the message `"Movie with ID {id} not found"` |

### Architecture

| ID | Requirement |
|---|---|
| FR-09 | The system shall follow MVC — Model holds data, Service handles logic, Controller handles HTTP |
| FR-10 | The system shall have unit tests covering all service methods |
| FR-11 | The system shall have integration tests covering all controller endpoints including error cases |

---

## Non-Functional Requirements

### Reliability

| ID | Requirement |
|---|---|
| NFR-01 | Storage is in-memory — data does not persist across server restarts |
| NFR-02 | The system is not thread-safe — concurrent writes may cause race conditions |

### Maintainability

| ID | Requirement |
|---|---|
| NFR-03 | The codebase shall follow a 3-layer MVC architecture to separate concerns |
| NFR-04 | Each layer (Model, Service, Controller) shall be independently testable |
| NFR-05 | Service tests shall run without loading the Spring context (plain JUnit) |
| NFR-06 | Controller tests shall use `@WebMvcTest` and mock the service with Mockito |

### Performance

| ID | Requirement |
|---|---|
| NFR-07 | All operations run in O(n) time — no indexing or database optimization |
| NFR-08 | Response time shall be fast due to in-memory storage with no I/O overhead |

### Security

| ID | Requirement |
|---|---|
| NFR-09 | No authentication or authorization — all endpoints are publicly accessible |
| NFR-10 | No input validation — malformed or missing fields are accepted without error |

### Portability

| ID | Requirement |
|---|---|
| NFR-11 | The application shall run on any platform with a JVM installed |
| NFR-12 | The only dependency is Spring Boot — no external database or infrastructure required |

### Scalability

| ID | Requirement |
|---|---|
| NFR-13 | The application is not horizontally scalable — each instance has its own isolated in-memory state |

---

## Running the Application

```bash
./mvnw spring-boot:run
```

The API will be available at `http://localhost:8080`.

## Running the Tests

```bash
./mvnw test
```

## Test the endpoints with curl in another terminal

If you are using Windows PowerShell, use `curl.exe` instead of `curl` so PowerShell does not rewrite the command.

These examples assume you just restarted the app, so the first created movie gets `id = 1`.

```powershell
# 1. POST — create a movie
@'
{"title":"Arrival","director":"Denis Villeneuve","releaseYear":2016}
'@ | Set-Content movie-create.json -NoNewline
curl.exe -s -X POST http://localhost:8080/api/movies `
	-H "Content-Type: application/json" `
	--data-binary "@movie-create.json" `
	-w "`nHTTP %{http_code}`n"
# Expect: {"id":1,"title":"Arrival",...}  HTTP 201

# 2. GET all movies
curl.exe -s http://localhost:8080/api/movies -w "`nHTTP %{http_code}`n"
# Expect: [{"id":1,...}]  HTTP 200

# 3. GET one movie
curl.exe -s http://localhost:8080/api/movies/1 -w "`nHTTP %{http_code}`n"
# Expect: {"id":1,...}  HTTP 200

# 4. GET missing — must NOT return 200 or 500
curl.exe -s http://localhost:8080/api/movies/99 -w "`nHTTP %{http_code}`n"
# Expect: Movie with ID 99 not found  HTTP 404

# 5. PUT — update the release year
@'
{"title":"Arrival","director":"Denis Villeneuve","releaseYear":2017}
'@ | Set-Content movie-update.json -NoNewline
curl.exe -s -X PUT http://localhost:8080/api/movies/1 `
	-H "Content-Type: application/json" `
	--data-binary "@movie-update.json" `
	-w "`nHTTP %{http_code}`n"
# Expect: {"id":1,"releaseYear":2017,...}  HTTP 200

# 6. DELETE
curl.exe -s -X DELETE http://localhost:8080/api/movies/1 -w "`nHTTP %{http_code}`n"
# Expect: (empty body)  HTTP 204

# 7. GET after DELETE — confirm it is gone
curl.exe -s http://localhost:8080/api/movies/1 -w "`nHTTP %{http_code}`n"
# Expect: Movie with ID 1 not found  HTTP 404

Remove-Item movie-create.json, movie-update.json
```

---

