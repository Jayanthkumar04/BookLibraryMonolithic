package com.org.jayanth.controller;

import java.net.URI;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.org.jayanth.dto.BookDto;
import com.org.jayanth.dtobestprac.MessageDto;
import com.org.jayanth.entity.Book;
import com.org.jayanth.repo.BookRepo;
import com.org.jayanth.service.BookService;

@RestController
@RequestMapping("api/books")
public class BookController {

	   
	  private static final Logger logger = LoggerFactory.getLogger(BookController.class);
	  @Autowired
	  private BookService bookService;

	  @Autowired
	  private BookRepo bookRepo;

	    // ---------------------- CREATE (ADMIN) ----------------------
	    @PostMapping
	    @PreAuthorize("hasRole('ADMIN')")
	    public ResponseEntity<?> createBook(@RequestBody BookDto dto) {
	        logger.info("Admin request - create book: {}", dto.getTitle());


	        
	        // map dto -> entity (simple mapping; use MapStruct if you want)
	        BookDto created = bookService.addBook(dto);
		       
	        

	        return ResponseEntity.created(URI.create("/api/books/" + created.getIsbn()))
	                             .body(created);
	    }
	    
	    @PutMapping("/{id}")
	    @PreAuthorize("hasRole('ADMIN')")
	    public ResponseEntity<?> updateBook(@PathVariable Long id, @RequestBody BookDto dto) {
	        logger.info("Admin request - update book id: {}", id);
	        System.out.println("am i cmng");
	        Book updated = bookService.updateBook(id, dto);
	        
	        if (updated == null) {
	            return ResponseEntity.notFound().build();
	        }
	        System.out.println(updated);
	        return ResponseEntity.ok(updated);
	    }
	    
	    // ---------------------- DELETE (ADMIN) ----------------------
	    @DeleteMapping("/{id}")
	    @PreAuthorize("hasRole('ADMIN')")
	    public MessageDto deleteBook(@PathVariable Long id) {
	        logger.warn("Admin request - delete book id: {}", id);

	        // prefer logical delete: set isActive=false in service or here
	        return bookService.deleteBook(id);
	      
	    }
	    
	 // ---------------------- GET BY ID (USER/ADMIN) ----------------------
	    @GetMapping("/{id}")
	    @PreAuthorize("hasAnyRole('USER','ADMIN')")
	    public ResponseEntity<?> getBookById(@PathVariable Long id) {
	        Book book = bookService.findBookById(id);
	        if (book == null) return ResponseEntity.notFound().build();
	        return ResponseEntity.ok(book);
	    }
	    
	 // ---------------------- SIMPLE LIST (no pagination) ----------------------
	    // Use only for small datasets; prefer /paged endpoint below in frontend
	    @GetMapping("/all")
	    @PreAuthorize("hasAnyRole('USER','ADMIN')")
	    public ResponseEntity<List<Book>> getAllBooks() {
	    	
	        List<Book> list = bookService.getAllBooks();
	        
	        return ResponseEntity.ok(list);
	    }
	    
	 // ---------------------- PAGED LIST (USER/ADMIN) ----------------------
	    // Example: /api/books?page=0&size=10&sort=title,asc
	    @GetMapping
	    @PreAuthorize("hasAnyRole('USER','ADMIN')")
	    public ResponseEntity<?> getBooksPaged(
	            @RequestParam(value = "page", defaultValue = "0") int page,
	            @RequestParam(value = "size", defaultValue = "10") int size,
	            @RequestParam(value = "sort", defaultValue = "id,desc") String sort, // format: field,dir
	            @RequestParam(value = "keyword", required = false) String keyword,
	            @RequestParam(value = "categoryId", required = false) Long categoryId
	    ) {
	        // parse sort param
	        String[] sortParts = sort.split(",");
	        Sort.Direction dir = sortParts.length > 1 && sortParts[1].equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
	        String sortBy = sortParts[0];

	        Pageable pageable = PageRequest.of(page, size, Sort.by(dir, sortBy));

	        Page<Book> result;

	        if (keyword != null && !keyword.isBlank()) {
	            result = bookRepo.searchByKeyword(keyword.toLowerCase(), pageable);
	        } else if (categoryId != null) {
	            result = bookRepo.findByCategoryId(categoryId, pageable);
	        } else {
	            result = bookRepo.findAll(pageable);
	        }

	        // build response with metadata
	        var body = Map.of(
	                "content", result.getContent(),
	                "page", result.getNumber(),
	                "size", result.getSize(),
	                "totalElements", result.getTotalElements(),
	                "totalPages", result.getTotalPages(),
	                "last", result.isLast()
	        );

	        return ResponseEntity.ok(body);
	    }
	 // ---------------------- SEARCH (non-paged) ----------------------
	    @GetMapping("/search")
	    @PreAuthorize("hasAnyRole('USER','ADMIN')")
	    public ResponseEntity<List<Book>> searchBooks(@RequestParam("q") String q) {
	        List<Book> res = bookService.searchBooks(q);
	        return ResponseEntity.ok(res);
	    }
	 // ---------------------- GET BY AUTHOR ----------------------
	    @GetMapping("/author")
	    @PreAuthorize("hasAnyRole('USER','ADMIN')")
	    public ResponseEntity<List<Book>> getByAuthor(@RequestParam("name") String author) {
	        List<Book> res = bookService.getBooksByAuthor(author);
	        return ResponseEntity.ok(res);
	    }

	 // ---------------------- UPDATE STOCK (ADMIN) ----------------------
	    @PatchMapping("/{id}/stock")
	    @PreAuthorize("hasRole('ADMIN')")
	    public ResponseEntity<?> updateStock(@PathVariable Long id, @RequestParam("qty") Integer qty) {
	        logger.info("Admin request - update stock bookId={} qty={}", id, qty);
	        bookService.updateBookStock(id, qty);
	        return ResponseEntity.ok("Stock updated");
	    }
	    
	    @GetMapping("/category/{id}")
	    @PreAuthorize("hasAnyRole('USER','ADMIN')")
	    public ResponseEntity<?> getBooksByCategory(@PathVariable Long id)
	    {
	    	logger.info("books by category request - categoryid {}",id);
	    	
	    	List<Book> books =   bookService.findBooksByCategory(id);
	    	
	    	return ResponseEntity.ok(books);
	    }
	
}
