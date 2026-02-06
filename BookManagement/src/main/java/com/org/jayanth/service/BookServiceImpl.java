package com.org.jayanth.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.org.jayanth.dto.BookDto;
import com.org.jayanth.dtobestprac.MessageDto;
import com.org.jayanth.entity.Book;
import com.org.jayanth.entity.Category;
import com.org.jayanth.exceptions.BookNotFoundException;
import com.org.jayanth.exceptions.CategoryNotFoundException;
import com.org.jayanth.exceptions.DuplicateBookException;
import com.org.jayanth.repo.BookRepo;
import com.org.jayanth.repo.CategoryRepo;

@Service
public class BookServiceImpl implements BookService {

    private static final Logger logger = LoggerFactory.getLogger(BookServiceImpl.class);

    @Autowired
    private BookRepo bookRepo;

    @Autowired
    private CategoryRepo categoryRepo;

    @Override
    public BookDto addBook(BookDto dto) {

        logger.info("Add book initiated | title={} isbn={}", dto.getTitle(), dto.getIsbn());

        Category c = categoryRepo.findById(dto.getCategory())
                .orElseThrow(() -> new CategoryNotFoundException("category not found"));

        if (existsByIsbn(dto.getIsbn())) {
            logger.warn("Duplicate book detected | isbn={}", dto.getIsbn());
            throw new DuplicateBookException("duplicate of book is found");
        }

        Book b = new Book();
        b.setTitle(dto.getTitle());
        b.setDescription(dto.getDescription());
        b.setAuthor(dto.getAuthor());
        b.setPrice(dto.getPrice());
        b.setDiscount(dto.getDiscount() != null ? dto.getDiscount() : 0.0);
        b.setIsbn(dto.getIsbn());
        b.setStock(dto.getStock() != null ? dto.getStock() : 0);
        b.setImageUrls(dto.getImageUrls());
        b.setCategory(c);

        bookRepo.save(b);

        logger.info("Add book successful | bookId={} title={}", b.getId(), b.getTitle());

        return dto;
    }

    @Override
    public Book findBookById(Long bookId) {

        logger.info("Find book by id initiated | bookId={}", bookId);

        Book book = bookRepo.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("book not found"));

        logger.info("Find book by id successful | bookId={}", bookId);

        return book;
    }

    @Override
    public BookDto updateBook(Long bookId, BookDto dto) {

        logger.info("Update book initiated | bookId={}", bookId);

        Book book = findBookById(bookId);

        if (book == null) {
            throw new BookNotFoundException("book not found");
        }

        if (dto.getTitle() != null) book.setTitle(dto.getTitle());
        if (dto.getAuthor() != null) book.setAuthor(dto.getAuthor());
        if (dto.getPrice() != null) book.setPrice(dto.getPrice());
        if (dto.getStock() != null) book.setStock(dto.getStock());
        if (dto.getDescription() != null) book.setDescription(dto.getDescription());

        bookRepo.save(book);

        logger.info("Update book successful | bookId={}", bookId);

        return dto;
    }

    @Override
    public MessageDto deleteBook(Long bookId) {

        logger.info("Delete book initiated | bookId={}", bookId);

        if (findBookById(bookId) == null) {
            throw new BookNotFoundException("book not found");
        }

        bookRepo.deleteById(bookId);

        logger.info("Delete book successful | bookId={}", bookId);

        return new MessageDto("book has been deleted successfully");
    }

    @Override
    public boolean isBookAvailable(Long bookId) {

        logger.info("Check book availability | bookId={}", bookId);

        boolean exists = bookRepo.existsById(bookId);

        logger.info("Book availability result | bookId={} available={}", bookId, exists);

        return exists;
    }

    @Override
    public Integer getStock(Long bookId) {

        logger.info("Get book stock initiated | bookId={}", bookId);

        Book book = findBookById(bookId);

        logger.info("Get book stock successful | bookId={} stock={}", bookId, book.getStock());

        return book.getStock();
    }

    @Override
    public MessageDto updateBookStock(Long bookId, Integer quantity) {

        logger.info("Update book stock initiated | bookId={} quantity={}", bookId, quantity);

        Book book = findBookById(bookId);

        if (book == null) {
            throw new BookNotFoundException("BOOK NOT FOUND");
        }

        int updatedStock = book.getStock() - quantity;

        if (updatedStock < 0) updatedStock = 0;

        book.setStock(updatedStock);

        bookRepo.save(book);

        logger.info("Update book stock successful | bookId={} updatedStock={}", bookId, updatedStock);

        return new MessageDto("stock has been updated successfully");
    }

    @Override
    public List<Book> getAllBooks() {

        logger.info("Get all books initiated");

        List<Book> books = bookRepo.findAll();

        logger.info("Get all books successful | count={}", books.size());

        return books;
    }

    @Override
    public List<Book> findBooksByCategory(Long categoryId) {

        logger.info("Find books by category initiated | categoryId={}", categoryId);

        categoryRepo.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("category not found"));

        List<Book> books = bookRepo.findByCategoryId(categoryId);

        logger.info("Find books by category successful | categoryId={} count={}", categoryId, books.size());

        return books;
    }

    @Override
    public List<Book> searchBooks(String keyword) {

        logger.info("Search books initiated | keyword={}", keyword);

        List<Book> books = bookRepo.searchByKeyword(keyword);

        logger.info("Search books successful | keyword={} count={}", keyword, books.size());

        return books;
    }

    @Override
    public List<Book> getBooksByAuthor(String author) {

        logger.info("Get books by author initiated | author={}", author);

        List<Book> books = bookRepo.findByAuthorContainingIgnoreCase(author);

        logger.info("Get books by author successful | author={} count={}", author, books.size());

        return books;
    }

    @Override
    public boolean existsByIsbn(String isbn) {

        logger.info("Check ISBN existence | isbn={}", isbn);

        boolean exists = bookRepo.existsByIsbn(isbn);

        logger.info("Check ISBN existence result | isbn={} exists={}", isbn, exists);

        return exists;
    }
}
