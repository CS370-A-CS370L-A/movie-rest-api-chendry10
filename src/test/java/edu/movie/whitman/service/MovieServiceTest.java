package edu.movie.whitman.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.movie.whitman.exception.MovieNotFoundException;
import edu.movie.whitman.model.Movie;

class MovieServiceTest {
	private MovieService movieService;

	@BeforeEach
	void setUp() {
		movieService = new MovieService();
	}

	@Test
	void getAllMoviesReturnsCreatedMovies() {
		movieService.createMovie(new Movie(99L, "Arrival", "Denis Villeneuve", 2016));
		movieService.createMovie(new Movie(null, "Moonlight", "Barry Jenkins", 2016));

		List<Movie> movies = movieService.getAllMovies();

		assertEquals(2, movies.size());
		assertEquals("Arrival", movies.get(0).getTitle());
		assertEquals("Moonlight", movies.get(1).getTitle());
	}

	@Test
	void createMovieAssignsIncrementingId() {
		Movie firstMovie = movieService.createMovie(new Movie(42L, "Alien", "Ridley Scott", 1979));
		Movie secondMovie = movieService.createMovie(new Movie(null, "Aliens", "James Cameron", 1986));

		assertEquals(1L, firstMovie.getId());
		assertEquals(2L, secondMovie.getId());
	}

	@Test
	void getMovieByIdReturnsMatchingMovie() {
		Movie createdMovie = movieService.createMovie(new Movie(null, "The Matrix", "The Wachowskis", 1999));

		Movie foundMovie = movieService.getMovieById(createdMovie.getId());

		assertEquals(createdMovie.getId(), foundMovie.getId());
		assertEquals("The Matrix", foundMovie.getTitle());
	}

	@Test
	void getMovieByIdThrowsWhenMovieDoesNotExist() {
		MovieNotFoundException exception = assertThrows(MovieNotFoundException.class,
				() -> movieService.getMovieById(99L));

		assertEquals("Movie with ID 99 not found", exception.getMessage());
	}

	@Test
	void updateMovieReplacesAllMutableFields() {
		Movie createdMovie = movieService.createMovie(new Movie(null, "Blade Runner", "Ridley Scott", 1982));

		Movie updatedMovie = movieService.updateMovie(createdMovie.getId(),
				new Movie(null, "Blade Runner 2049", "Denis Villeneuve", 2017));

		assertEquals(createdMovie.getId(), updatedMovie.getId());
		assertEquals("Blade Runner 2049", updatedMovie.getTitle());
		assertEquals("Denis Villeneuve", updatedMovie.getDirector());
		assertEquals(2017, updatedMovie.getReleaseYear());
	}

	@Test
	void deleteMovieRemovesMovie() {
		Movie createdMovie = movieService.createMovie(new Movie(null, "Get Out", "Jordan Peele", 2017));

		movieService.deleteMovie(createdMovie.getId());

		assertTrue(movieService.getAllMovies().isEmpty());
	}

	@Test
	void deleteMovieThrowsWhenMovieDoesNotExist() {
		MovieNotFoundException exception = assertThrows(MovieNotFoundException.class,
				() -> movieService.deleteMovie(7L));

		assertEquals("Movie with ID 7 not found", exception.getMessage());
	}
}