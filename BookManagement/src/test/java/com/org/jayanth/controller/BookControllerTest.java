package com.org.jayanth.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.org.jayanth.dto.BookDto;
import com.org.jayanth.dtobestprac.MessageDto;
import com.org.jayanth.entity.Book;
import com.org.jayanth.security.FirstLoginFilter;
import com.org.jayanth.security.JwtAuthenticationFilter;
import com.org.jayanth.service.BookService;

@WebMvcTest(
        controllers = BookController.class,
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = {
                                FirstLoginFilter.class,
                                JwtAuthenticationFilter.class
                        }
                )
        }
)
@AutoConfigureMockMvc(addFilters = false)
public class BookControllerTest {

    @MockBean
    private BookService service;

    @Autowired
    private MockMvc mockMvc;

    // ---------------- GET ALL BOOKS ----------------
    @Test
    void testGetAllBooks() throws Exception {

        List<Book> books = new ArrayList<>();
        books.add(new Book());
        books.add(new Book());

        when(service.getAllBooks()).thenReturn(books);

        MockHttpServletRequestBuilder req =
                MockMvcRequestBuilders.get("/api/books/all");

        MvcResult result = mockMvc.perform(req).andReturn();
        MockHttpServletResponse response = result.getResponse();

        assertEquals(200, response.getStatus());
    }

    // ---------------- GET BOOK BY ID ----------------
    @Test
    void testGetBookById() throws Exception {

        Book book = new Book();
        book.setId(1L);
        book.setTitle("Spring Boot");
        book.setAuthor("Jayanth");

        when(service.findBookById(anyLong())).thenReturn(book);

        MockHttpServletRequestBuilder req =
                MockMvcRequestBuilders.get("/api/books/{id}", 1L);

        MvcResult result = mockMvc.perform(req).andReturn();
        MockHttpServletResponse response = result.getResponse();

        ObjectMapper mapper = new ObjectMapper();
        String bookJson = mapper.writeValueAsString(book);

        assertEquals(200, response.getStatus());
        assertEquals(bookJson, response.getContentAsString());
    }

    // ---------------- GET BOOKS BY CATEGORY ----------------
    @Test
    void testGetBooksByCategory() throws Exception {

        List<Book> books = List.of(new Book(), new Book());

        when(service.findBooksByCategory(anyLong())).thenReturn(books);

        MockHttpServletRequestBuilder req =
                MockMvcRequestBuilders.get("/api/books/category/{id}", 1L);

        MvcResult result = mockMvc.perform(req).andReturn();
        MockHttpServletResponse response = result.getResponse();

        assertEquals(200, response.getStatus());
    }

    // ---------------- SEARCH BOOKS ----------------
    @Test
    void testSearchBooks() throws Exception {

        List<Book> books = List.of(new Book());

        when(service.searchBooks(any())).thenReturn(books);

        MockHttpServletRequestBuilder req =
                MockMvcRequestBuilders.get("/api/books/search")
                        .param("q", "spring");

        MvcResult result = mockMvc.perform(req).andReturn();
        MockHttpServletResponse response = result.getResponse();

        assertEquals(200, response.getStatus());
    }

    // ---------------- GET BOOKS BY AUTHOR ----------------
    @Test
    void testGetBooksByAuthor() throws Exception {

        List<Book> books = List.of(new Book());

        when(service.getBooksByAuthor(any())).thenReturn(books);

        MockHttpServletRequestBuilder req =
                MockMvcRequestBuilders.get("/api/books/author")
                        .param("name", "Jayanth");

        MvcResult result = mockMvc.perform(req).andReturn();
        MockHttpServletResponse response = result.getResponse();

        assertEquals(200, response.getStatus());
    }

    // ---------------- CREATE BOOK ----------------
    @Test
    void testCreateBook() throws Exception {

        BookDto dto = new BookDto();
        dto.setTitle("Spring Boot");
        dto.setAuthor("Jayanth");

        when(service.addBook(any(BookDto.class))).thenReturn(dto);

        ObjectMapper mapper = new ObjectMapper();
        String dtoJson = mapper.writeValueAsString(dto);

        MockHttpServletRequestBuilder req =
                MockMvcRequestBuilders.post("/api/books")
                        .contentType("application/json")
                        .content(dtoJson);

        MvcResult result = mockMvc.perform(req).andReturn();
        MockHttpServletResponse response = result.getResponse();

        assertEquals(201, response.getStatus());
        assertEquals(dtoJson, response.getContentAsString());
    }

    // ---------------- UPDATE BOOK ----------------
    @Test
    void testUpdateBook() throws Exception {

        BookDto dto = new BookDto();
        dto.setTitle("Updated Title");

        when(service.updateBook(anyLong(), any(BookDto.class))).thenReturn(dto);

        ObjectMapper mapper = new ObjectMapper();
        String dtoJson = mapper.writeValueAsString(dto);

        MockHttpServletRequestBuilder req =
                MockMvcRequestBuilders.put("/api/books/{id}", 1L)
                        .contentType("application/json")
                        .content(dtoJson);

        MvcResult result = mockMvc.perform(req).andReturn();
        MockHttpServletResponse response = result.getResponse();

        assertEquals(200, response.getStatus());
        assertEquals(dtoJson, response.getContentAsString());
    }

    // ---------------- DELETE BOOK ----------------
    @Test
    void testDeleteBook() throws Exception {

        MessageDto message = new MessageDto("Book deleted successfully");

        when(service.deleteBook(anyLong())).thenReturn(message);

        ObjectMapper mapper = new ObjectMapper();
        String msgJson = mapper.writeValueAsString(message);

        MockHttpServletRequestBuilder req =
                MockMvcRequestBuilders.delete("/api/books/{id}", 1L);

        MvcResult result = mockMvc.perform(req).andReturn();
        MockHttpServletResponse response = result.getResponse();

        assertEquals(200, response.getStatus());
        assertEquals(msgJson, response.getContentAsString());
    }

    // ---------------- UPDATE STOCK ----------------
    @Test
    void testUpdateStock() throws Exception {

        MessageDto message = new MessageDto("Stock updated");

        when(service.updateBookStock(anyLong(), any())).thenReturn(message);

        ObjectMapper mapper = new ObjectMapper();
        String msgJson = mapper.writeValueAsString(message);

        MockHttpServletRequestBuilder req =
                MockMvcRequestBuilders
                        .patch("/api/books/{id}/stock", 1L)
                        .param("qty", "10");

        MvcResult result = mockMvc.perform(req).andReturn();
        MockHttpServletResponse response = result.getResponse();

        assertEquals(200, response.getStatus());
        assertEquals(msgJson, response.getContentAsString());
    }
}
