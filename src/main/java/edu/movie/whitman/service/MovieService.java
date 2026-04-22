package edu.movie.whitman.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Service;

import edu.movie.whitman.exception.MovieNotFoundException;
import edu.movie.whitman.model.Movie;

@Service
public class MovieService {
	private final List<Movie> movies = new ArrayList<>();
	private long nextId = 1L;

	public List<Movie> getAllMovies() {
		return new ArrayList<>(movies);
	}

	public Movie getMovieById(Long id) {
		return findMovieById(id);
	}

	public Movie createMovie(Movie movie) {
		Movie createdMovie = new Movie(nextId++, movie.getTitle(), movie.getDirector(), movie.getReleaseYear());
		movies.add(createdMovie);
		return createdMovie;
	}

	public Movie updateMovie(Long id, Movie movie) {
		Movie existingMovie = findMovieById(id);
		existingMovie.setTitle(movie.getTitle());
		existingMovie.setDirector(movie.getDirector());
		existingMovie.setReleaseYear(movie.getReleaseYear());
		return existingMovie;
	}

	public void deleteMovie(Long id) {
		Iterator<Movie> iterator = movies.iterator();
		while (iterator.hasNext()) {
			Movie movie = iterator.next();
			if (movie.getId().equals(id)) {
				iterator.remove();
				return;
			}
		}

		throw new MovieNotFoundException(id);
	}

	private Movie findMovieById(Long id) {
		for (Movie movie : movies) {
			if (movie.getId().equals(id)) {
				return movie;
			}
		}

		throw new MovieNotFoundException(id);
	}
}