package edu.movie.whitman.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.movie.whitman.exception.GlobalExceptionHandler;
import edu.movie.whitman.exception.MovieNotFoundException;
import edu.movie.whitman.model.Movie;
import edu.movie.whitman.service.MovieService;

@WebMvcTest(MovieController.class)
@Import(GlobalExceptionHandler.class)
class MovieControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private MovieService movieService;

	@Test
	void getAllMoviesReturnsMovieList() throws Exception {
		given(movieService.getAllMovies()).willReturn(List.of(
				new Movie(1L, "Nope", "Jordan Peele", 2022),
				new Movie(2L, "Past Lives", "Celine Song", 2023)));

		mockMvc.perform(get("/api/movies"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].id").value(1))
				.andExpect(jsonPath("$[1].title").value("Past Lives"));
	}

	@Test
	void getMovieByIdReturnsMovie() throws Exception {
		given(movieService.getMovieById(1L)).willReturn(new Movie(1L, "Dune", "Denis Villeneuve", 2021));

		mockMvc.perform(get("/api/movies/1"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.title").value("Dune"))
				.andExpect(jsonPath("$.director").value("Denis Villeneuve"));
	}

	@Test
	void getMovieByIdReturnsNotFoundMessage() throws Exception {
		given(movieService.getMovieById(9L)).willThrow(new MovieNotFoundException(9L));

		mockMvc.perform(get("/api/movies/9"))
				.andExpect(status().isNotFound())
				.andExpect(content().string("Movie with ID 9 not found"));
	}

	@Test
	void createMovieReturnsCreatedMovie() throws Exception {
		Movie requestMovie = new Movie(99L, "Memento", "Christopher Nolan", 2000);
		Movie createdMovie = new Movie(1L, "Memento", "Christopher Nolan", 2000);
		given(movieService.createMovie(any(Movie.class))).willReturn(createdMovie);

		mockMvc.perform(post("/api/movies")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(requestMovie)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$.title").value("Memento"));
	}

	@Test
	void updateMovieReturnsUpdatedMovie() throws Exception {
		Movie updatedMovie = new Movie(3L, "The Batman", "Matt Reeves", 2022);
		given(movieService.updateMovie(eq(3L), any(Movie.class))).willReturn(updatedMovie);

		mockMvc.perform(put("/api/movies/3")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updatedMovie)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(3))
				.andExpect(jsonPath("$.director").value("Matt Reeves"));
	}

	@Test
	void updateMovieReturnsNotFoundMessage() throws Exception {
		doThrow(new MovieNotFoundException(3L)).when(movieService).updateMovie(eq(3L), any(Movie.class));

		mockMvc.perform(put("/api/movies/3")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(new Movie(null, "The Batman", "Matt Reeves", 2022))))
				.andExpect(status().isNotFound())
				.andExpect(content().string("Movie with ID 3 not found"));
	}

	@Test
	void deleteMovieReturnsNoContent() throws Exception {
		doNothing().when(movieService).deleteMovie(4L);

		mockMvc.perform(delete("/api/movies/4"))
				.andExpect(status().isNoContent());
	}

	@Test
	void deleteMovieReturnsNotFoundMessage() throws Exception {
		doThrow(new MovieNotFoundException(4L)).when(movieService).deleteMovie(4L);

		mockMvc.perform(delete("/api/movies/4"))
				.andExpect(status().isNotFound())
				.andExpect(content().string("Movie with ID 4 not found"));
	}
}