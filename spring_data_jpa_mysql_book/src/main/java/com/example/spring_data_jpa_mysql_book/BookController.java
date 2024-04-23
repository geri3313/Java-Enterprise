package com.example.spring_data_jpa_mysql_book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/books")
public class BookController {


    @Autowired
    private BookService bookService;




    @PostMapping
    public Book addBook(@RequestBody Book book) {
        return bookService.createBook(book);
    }

    @GetMapping
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("/{id}")
    public Optional<Book> getBookWithId(@PathVariable Long id) {
        return bookService.getBookById(id);
    }

    @GetMapping(params = {"author"})
    public List<Book> findBookByAuthor(@RequestParam(value = "author") String author) {
        return bookService.getAllBooksByAuthor(author);
    }

    @PutMapping("/{id}")
    public Book updateBookFromDB(@PathVariable("id") long id, @RequestBody Book book) {

      return bookService.updateBook(id,book);
    }

    @DeleteMapping("/{id}")
    public void deleteBookWithId(@PathVariable Long id) {
         bookService.deleteBook(id);
    }

    @DeleteMapping
    public void deleteAllBooks() {
        bookService.deleteAllBooks();
    }
}

