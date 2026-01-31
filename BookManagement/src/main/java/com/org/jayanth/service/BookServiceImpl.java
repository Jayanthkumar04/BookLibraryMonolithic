package com.org.jayanth.service;

import java.util.List;

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
public class BookServiceImpl implements BookService{

	@Autowired
	private BookRepo bookRepo;
	
	@Autowired
	private CategoryRepo categoryRepo;
	
	@Override
	public BookDto addBook(BookDto dto) {
 
		Category c = categoryRepo.findById(dto.getCategory()).orElseThrow(()->new CategoryNotFoundException("category not found"));;
		
		
		    Book b = new Book();
	        b.setTitle(dto.getTitle());
	        b.setDescription(dto.getDescription());
	        b.setAuthor(dto.getAuthor());
	        b.setPrice(dto.getPrice());
	        b.setDiscount(dto.getDiscount() != null ? dto.getDiscount() : 0.0);
	       if(existsByIsbn(dto.getIsbn())) throw new DuplicateBookException("duplicate of book is found");
	        b.setIsbn(dto.getIsbn());
	        b.setStock(dto.getStock() != null ? dto.getStock() : 0);
	        b.setImageUrls(dto.getImageUrls());
	        b.setCategory(c);
	       
		
		
		
		bookRepo.save(b);
		
		return dto;
	}

	
	@Override
	public Book findBookById(Long bookId) {
		
		return bookRepo.findById(bookId).orElseThrow(()->new BookNotFoundException("book not found"));
	}

	@Override
	public Book updateBook(Long bookId, BookDto dto) {
	
		Book book = findBookById(bookId);
		
		System.out.println(book);
		if(book == null)
		{
			throw new BookNotFoundException("book not found");
		}
		
		if (dto.getTitle() != null)  book.setTitle(dto.getTitle());
        if (dto.getAuthor() != null) book.setAuthor(dto.getAuthor());
        if (dto.getPrice() != null)  book.setPrice(dto.getPrice());
        if (dto.getStock() != null)  book.setStock(dto.getStock());
//      if (dto.getCategory() != null) book.setCategory(dto.getCategory());
        if (dto.getDescription() != null) book.setDescription(dto.getDescription());
        
        System.out.println(book);
        return bookRepo.save(book);
		
	}

	@Override
	public MessageDto deleteBook(Long bookId) {
	
		if(findBookById(bookId) == null) throw new BookNotFoundException("book not found");
		
		bookRepo.deleteById(bookId);
		
		return new MessageDto("book has been deleted successfully");
	
	}

	@Override
	public boolean isBookAvailable(Long bookId) {
	
//	  Book book = findBookById(bookId);
//	  
//	  if(book != null) return true;
//	  
//	  else return false;
		
		return bookRepo.existsById(bookId);
	}

	@Override
	public Integer getStock(Long bookId) {
		Book book = findBookById(bookId);
		
		
		return book.getStock();
	}

	@Override
	public void updateBookStock(Long bookId, Integer quantity) {
	
		Book book = findBookById(bookId);
		
		if(book == null) throw new BookNotFoundException("BOOK NOT FOUND");
		
		int updatedStock = book.getStock() - quantity;
		
		if(updatedStock < 0) updatedStock=0;
	
		book.setStock(updatedStock);
		
		bookRepo.save(book);
	}

	@Override
	public List<Book> getAllBooks() {
		// TODO Auto-generated method stub
		return bookRepo.findAll();
	}

	@Override
	public List<Book> findBooksByCategory(Long categoryId) {
		Category c = categoryRepo.findById(categoryId).orElseThrow(()->new CategoryNotFoundException("category not found"));;
		
		
	return bookRepo.findByCategoryId(categoryId);
	}

	@Override
	public List<Book> searchBooks(String keyword) {
	return bookRepo.searchByKeyword(keyword);
	}

	@Override
	public List<Book> getBooksByAuthor(String author) {

		return bookRepo.findByAuthorContainingIgnoreCase(author);
	}


	@Override
	public boolean existsByIsbn(String isbn) {

		if(bookRepo.existsByIsbn(isbn)) return true;
		else return false;
	}

	
}
