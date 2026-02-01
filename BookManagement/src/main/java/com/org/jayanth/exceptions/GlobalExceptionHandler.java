package com.org.jayanth.exceptions;

import java.time.LocalDateTime;

import org.apache.catalina.connector.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties.Http;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	
	
	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<ErrorDetails> handleUserNotFound(UserNotFoundException ex)
	{
	     logger.error("user not found");
		ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(),ex.getMessage(),"user is not found");
		return new ResponseEntity<>(errorDetails,HttpStatus.NOT_FOUND);
		
	}
	
	@ExceptionHandler
	public ResponseEntity<ErrorDetails> handleUserUnauthorizedPageException(UserUnauthorizedPageException ex)
	{
	     logger.error("user is not allowed to access this page");
		ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(),ex.getMessage(),"user is not authorized to use this page");
		return new ResponseEntity<>(errorDetails,HttpStatus.UNAUTHORIZED);
		
		
	}
	
	@ExceptionHandler
	
	
	public ResponseEntity<ErrorDetails> handleWrongUserCredentialsException(WrongUserCredentialsException ex)
	{
		 	logger.error("bad credentials");
			ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(),ex.getMessage(),"credentails are wrong");
			return new ResponseEntity<>(errorDetails,HttpStatus.NOT_FOUND);
			
		
	}
	
	@ExceptionHandler
	public ResponseEntity<ErrorDetails> handleEmailAlreadyExistsException(EmailAlreadyExistsException ex)
	{
		
	 	logger.error("email already exists!!!!!!");
		ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(),ex.getMessage(),"Email Already Exists with the email id!!");
		return new ResponseEntity<>(errorDetails,HttpStatus.CONFLICT);
	
		
	}
	
	@ExceptionHandler
	public ResponseEntity<ErrorDetails> handleInvalidRoleException(InvalidRoleException ex)
	{
	 	logger.error("invalid role exception");
			ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(),ex.getMessage(),"Role selected is not either admin or user!!");
			return new ResponseEntity<>(errorDetails,HttpStatus.NOT_ACCEPTABLE);
			
	}
	
	@ExceptionHandler
	public ResponseEntity<ErrorDetails> handleAddressNotFoundException(AddressNotFoundException ex)
	{
		logger.error("address not found");
		ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(),ex.getMessage(),"Address not found");
	   return new ResponseEntity<>(errorDetails,HttpStatus.NOT_FOUND);
		
	}
	
	@ExceptionHandler
	public ResponseEntity<ErrorDetails> handleReviewNotFoundException(ReviewNotFoundException ex)
	{
		logger.error("review not found");
		ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(),ex.getMessage(),"Reviews Not found");
		return new ResponseEntity<>(errorDetails,HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler
	public ResponseEntity<ErrorDetails> handleCartEmptyException(CartEmptyException ex)
	{
		logger.error("empty cart");
		ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(),ex.getMessage(),"Cart is empty");
		return new ResponseEntity<>(errorDetails,HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler
	public ResponseEntity<ErrorDetails> handleCategoryAlreadyExistsException(CategoryAlreadyExistsException ex)
	{
		logger.error("category already exists");
		ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(),ex.getMessage(),"category already exists");
		return new ResponseEntity<>(errorDetails,HttpStatus.CONFLICT);
	
	}
	
	@ExceptionHandler
	public ResponseEntity<ErrorDetails> handleCategoryNotFoundException(CategoryNotFoundException ex)
	{
		logger.error("category not found");
		ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(),ex.getMessage(),"category not found");
		return new ResponseEntity<>(errorDetails,HttpStatus.NOT_FOUND);
	
	}
	
	@ExceptionHandler
	public ResponseEntity<ErrorDetails> handleOrderNotFoundException(OrderNotFoundException ex)
	{
	
		logger.error("order not found");
		ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(),ex.getMessage(),"order not found");
		return new ResponseEntity<>(errorDetails,HttpStatus.NOT_FOUND);
	
	}
	
	@ExceptionHandler
	public ResponseEntity<ErrorDetails> cartItemNotFoundException(CartItemNotFoundException ex)
	{
	
		logger.error("cart item not found");
		ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(),ex.getMessage(),"cart item not found");
		return new ResponseEntity<>(errorDetails,HttpStatus.NOT_FOUND);
	
	}
	
	@ExceptionHandler
	public ResponseEntity<ErrorDetails> handleDuplicateBookException(DuplicateBookException ex)
	{
	
		logger.error("duplicate book found");
		ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(),ex.getMessage(),"duplicate entry of book found please check");
		return new ResponseEntity<>(errorDetails,HttpStatus.CONFLICT);
	
	}
	
	@ExceptionHandler
	public ResponseEntity<ErrorDetails> handleStockNotAvailable(StockNotAvailableException ex)
	{
		logger.error("stock not available");
		ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(),ex.getMessage(),"stock not available");
		return new ResponseEntity<>(errorDetails,HttpStatus.NOT_FOUND);
		
	}
	
	@ExceptionHandler
	public ResponseEntity<ErrorDetails> handleBookNotFoundException(BookNotFoundException ex)
	{
		logger.error("book not found");
		ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(),ex.getMessage(),"book not found");
		return new ResponseEntity<>(errorDetails,HttpStatus.NOT_FOUND);
		
	}
	
	@ExceptionHandler
	public ResponseEntity<ErrorDetails> handleIncorrectTokenException(IncorrectTokenException ex)
	{
		logger.error("incorrect token");
		ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(),ex.getMessage(),"Incorrect token entered");
		return new ResponseEntity<>(errorDetails,HttpStatus.NOT_FOUND);
		
	}
	
	
}
