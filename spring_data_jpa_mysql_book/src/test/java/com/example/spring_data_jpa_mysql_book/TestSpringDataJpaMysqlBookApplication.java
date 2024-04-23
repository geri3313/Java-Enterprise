package com.example.spring_data_jpa_mysql_book;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.junit.jupiter.api.Assertions.assertFalse;



@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
public class TestSpringDataJpaMysqlBookApplication {

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private BookRepository bookRepository;

	@LocalServerPort
	private int port;

	@Container
	private static final MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:8.1")
			.withDatabaseName("testdb")
			.withUsername("testuser")
			.withPassword("testpass");

	@BeforeAll
	static void setUp() {
		mySQLContainer.start();
	}

	@AfterAll
	static void tearDown() {
		mySQLContainer.stop();
	}

	@BeforeEach
	void init() {
		restTemplate
				= new TestRestTemplate("user", "passwd");
	}

	@Test
	public void testAddBook() {
		Book book = new Book("Test Book", "Test Author");
		ResponseEntity<Book> responseEntity = restTemplate.postForEntity(getBaseUrl(), book, Book.class);
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

		Book savedBook = responseEntity.getBody();
		assertNotNull(savedBook);
		assertEquals(book.getTitle(), savedBook.getTitle());
		assertEquals(book.getAuthor(), savedBook.getAuthor());

		bookRepository.deleteById(savedBook.getId());
	}

	@Test
	public void testGetAllBooks() {

		Book book1 = new Book("Book 1", "Author 1");
    	Book book2 = new Book("Book 2", "Author 2");
    	bookRepository.save(book1);
    	bookRepository.save(book2);

    	ResponseEntity<Book[]> responseEntity = restTemplate.getForEntity(getBaseUrl(), Book[].class);

 		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

    	Book[] books = responseEntity.getBody();

    	assertNotNull(books);

    	assertTrue(books.length >= 2);
	    bookRepository.deleteAll();
	}
	@Test
	public void testGetBookWithId() {
    
		Book book = new Book("Test Book", "Test Author");
    	book = bookRepository.save(book);
	    ResponseEntity<Book> responseEntity = restTemplate.getForEntity(getBaseUrl() + "/" + book.getId(), Book.class);
    	assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
 		Book retrievedBook = responseEntity.getBody();
    	assertEquals(book.getId(), retrievedBook.getId());
    	assertEquals(book.getTitle(), retrievedBook.getTitle());
    	assertEquals(book.getAuthor(), retrievedBook.getAuthor());
 		bookRepository.deleteById(book.getId());
	}
	
	
	
	// Test updating a book in the database
	@Test
	public void testUpdateBookFromDB() {
		// Create a sample book and save it to the database
		Book book = new Book("Test Book", "Test Author");
		book = bookRepository.save(book);

		// Modify the book's title
		book.setTitle("Updated Title");

		// Send a PUT request to update the book
		restTemplate.put(getBaseUrl() + "/" + book.getId(), book);

		// Retrieve the updated book from the database
		Book updatedBook = bookRepository.findById(book.getId()).orElse(null);

		// Assert that the updated book's title matches the modified title
		assertNotNull(updatedBook);
		assertEquals(book.getTitle(), updatedBook.getTitle());

		// Clean up: delete the sample book from the database
		bookRepository.deleteById(book.getId());
	}

	// Test deleting a book by its ID
	@Test
	public void testDeleteBookWithId() {
		// Create a sample book and save it to the database
		Book book = new Book("Test Book", "Test Author");
		book = bookRepository.save(book);

		// Send a DELETE request to delete the book by its ID
		restTemplate.delete(getBaseUrl() + "/" + book.getId());

		// Assert that the book is no longer present in the database
		assertFalse(bookRepository.existsById(book.getId()));
	}

	// Test deleting all books from the database
	@Test
	public void testDeleteAllBooks() {
		// Create some sample books and save them to the database
		Book book1 = new Book("Book 1", "Author 1");
		Book book2 = new Book("Book 2", "Author 2");
		book1 = bookRepository.save(book1);
		book2 = bookRepository.save(book2);

		// Send a DELETE request to delete all books
		restTemplate.delete(getBaseUrl());

		// Assert that there are no books left in the database
		assertEquals(0, bookRepository.count());
	}


		private String getBaseUrl() {
			return "http://localhost:" + port + "/books";
		}
}
