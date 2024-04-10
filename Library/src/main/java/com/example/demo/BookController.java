package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Optional;


@RestController
@RequestMapping("/books")
public class BookController {


    @Autowired
    private BookRepository repository;

    @PostMapping
    public ResponseEntity<?> addBook(@RequestBody Book book) {
        return new ResponseEntity<>(repository.save(book), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Collection<Book>> getAllBooks() {
        return new ResponseEntity<>(repository.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookWithId(@PathVariable Long id) {
        return new ResponseEntity<Book>(repository.findById(id).get(), HttpStatus.OK);
    }

    @GetMapping(params = {"author"})
    public ResponseEntity<Collection<Book>> findBookWithName(@RequestParam(value = "author") String author) {
        return new ResponseEntity<>(repository.findByAuthor(author), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBookFromDB(@PathVariable("id") long id, @RequestBody Book book) {

        Optional<Book> currentBookOpt = repository.findById(id);
        Book currentBook = currentBookOpt.get();
        currentBook.setAuthor(book.getAuthor());
        currentBook.setTitle(book.getTitle());

        return new ResponseEntity<>(repository.save(currentBook), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public void deleteBookWithId(@PathVariable Long id) {
        repository.deleteById(id);
    }

    @DeleteMapping
    public void deleteAllBooks() {
        repository.deleteAll();
    }
}
