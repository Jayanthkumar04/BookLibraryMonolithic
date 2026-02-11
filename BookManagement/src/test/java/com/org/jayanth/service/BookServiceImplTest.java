package com.org.jayanth.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import com.org.jayanth.dto.BookDto;
import com.org.jayanth.dtobestprac.MessageDto;
import com.org.jayanth.entity.Book;
import com.org.jayanth.entity.Category;
import com.org.jayanth.repo.BookRepo;
import com.org.jayanth.repo.CategoryRepo;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class BookServiceImplTest {

    @Mock
    private BookRepo bookRepo;

    @Mock
    private CategoryRepo categoryRepo;

    @InjectMocks
    private BookServiceImpl service;

    @Test
    void testAddBook() {

        BookDto dto = new BookDto();
        dto.setTitle("Spring Boot");
        dto.setAuthor("Jayanth");
        dto.setPrice(500.0);
        dto.setIsbn("ISBN123");
        dto.setStock(10);
        dto.setCategory(1L);

        Category category = new Category();
        category.setId(1L);

        Book savedBook = new Book();
        savedBook.setId(1L);
        savedBook.setTitle("Spring Boot");

        when(categoryRepo.findById(anyLong())).thenReturn(Optional.of(category));
        when(bookRepo.existsByIsbn(anyString())).thenReturn(false);
        when(bookRepo.save(any(Book.class))).thenReturn(savedBook);

        BookDto result = service.addBook(dto);

        assertNotNull(result);
        assertEquals("Spring Boot", result.getTitle());
    }

    @Test
    void testFindBookById() {

        Book book = new Book();
        book.setId(1L);
        book.setTitle("Java");

        when(bookRepo.findById(anyLong())).thenReturn(Optional.of(book));

        Book result = service.findBookById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void testUpdateBook() {

        BookDto dto = new BookDto();
        dto.setTitle("Updated Title");
        dto.setPrice(600.0);

        Book book = new Book();
        book.setId(1L);
        book.setTitle("Old Title");

        when(bookRepo.findById(anyLong())).thenReturn(Optional.of(book));
        when(bookRepo.save(any(Book.class))).thenReturn(book);

        BookDto result = service.updateBook(1L, dto);

        assertEquals("Updated Title", result.getTitle());
    }

    @Test
    void testDeleteBook() {

        Book book = new Book();
        book.setId(1L);

        when(bookRepo.findById(anyLong())).thenReturn(Optional.of(book));

        MessageDto result = service.deleteBook(1L);

        assertEquals("book has been deleted successfully", result.getMessage());
    }

    @Test
    void testIsBookAvailable() {

        when(bookRepo.existsById(anyLong())).thenReturn(true);

        boolean result = service.isBookAvailable(1L);

        assertEquals(true, result);
    }

    @Test
    void testGetStock() {

        Book book = new Book();
        book.setId(1L);
        book.setStock(20);

        when(bookRepo.findById(anyLong())).thenReturn(Optional.of(book));

        Integer stock = service.getStock(1L);

        assertEquals(20, stock);
    }

    @Test
    void testUpdateBookStock() {

        Book book = new Book();
        book.setId(1L);
        book.setStock(10);

        when(bookRepo.findById(anyLong())).thenReturn(Optional.of(book));
        when(bookRepo.save(any(Book.class))).thenReturn(book);

        MessageDto result = service.updateBookStock(1L, 3);

        assertEquals("stock has been updated successfully", result.getMessage());
        assertEquals(7, book.getStock());
    }

    @Test
    void testGetAllBooks() {

        when(bookRepo.findAll()).thenReturn(List.of(new Book(), new Book()));

        List<Book> books = service.getAllBooks();

        assertEquals(2, books.size());
    }

    @Test
    void testFindBooksByCategory() {

        Category category = new Category();
        category.setId(1L);

        when(categoryRepo.findById(anyLong())).thenReturn(Optional.of(category));
        when(bookRepo.findByCategoryId(anyLong())).thenReturn(List.of(new Book()));

        List<Book> books = service.findBooksByCategory(1L);

        assertEquals(1, books.size());
    }

    @Test
    void testSearchBooks() {

        when(bookRepo.searchByKeyword(anyString()))
                .thenReturn(List.of(new Book(), new Book()));

        List<Book> books = service.searchBooks("java");

        assertEquals(2, books.size());
    }

    @Test
    void testGetBooksByAuthor() {

        when(bookRepo.findByAuthorContainingIgnoreCase(anyString()))
                .thenReturn(List.of(new Book()));

        List<Book> books = service.getBooksByAuthor("Jayanth");

        assertEquals(1, books.size());
    }

    @Test
    void testExistsByIsbn() {

        when(bookRepo.existsByIsbn(anyString())).thenReturn(true);

        boolean result = service.existsByIsbn("ISBN123");

        assertEquals(true, result);
    }
}
