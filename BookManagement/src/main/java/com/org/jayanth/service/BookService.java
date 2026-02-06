package com.org.jayanth.service;

import java.util.List;

import com.org.jayanth.dto.BookDto;
import com.org.jayanth.dtobestprac.MessageDto;
import com.org.jayanth.entity.Book;

public interface BookService {

	BookDto addBook(BookDto bookDto);
	
	Book findBookById(Long bookId);
	
	BookDto updateBook(Long bookId,BookDto dto);
	
	MessageDto deleteBook(Long bookId);
	
	boolean existsByIsbn(String isbn);
	boolean isBookAvailable(Long bookId);
	Integer getStock(Long bookId);
	MessageDto updateBookStock(Long bookId,Integer quantity);
	
	List<Book> getAllBooks();
    List<Book> findBooksByCategory(Long categoryId);
    List<Book> searchBooks(String keyword);
    List<Book> getBooksByAuthor(String author);
	
}
