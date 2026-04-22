package edu.movie.whitman.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import edu.movie.whitman.model.Movie;
import edu.movie.whitman.service.MovieService;

@RestController
@RequestMapping("/api/movies")
public class MovieController {
	private final MovieService movieService;

	public MovieController(MovieService movieService) {
		this.movieService = movieService;
	}

	@GetMapping
	public List<Movie> getAllMovies() {
		return movieService.getAllMovies();
	}

	@GetMapping("/{id}")
	public Movie getMovieById(@PathVariable Long id) {
		return movieService.getMovieById(id);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Movie createMovie(@RequestBody Movie movie) {
		return movieService.createMovie(movie);
	}

	@PutMapping("/{id}")
	public Movie updateMovie(@PathVariable Long id, @RequestBody Movie movie) {
		return movieService.updateMovie(id, movie);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteMovie(@PathVariable Long id) {
		movieService.deleteMovie(id);
	}
}