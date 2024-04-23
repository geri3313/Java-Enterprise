package com.example.spring_data_jpa_mysql_book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import javax.ws.rs.NotFoundException;

@Service
public class BookService {

    @Autowired
    private  BookRepository bookRepository;

  

    public List<Book> getAllBooks() {
        return bookRepository.findAll();

    }

    public List<Book> getAllBooksByAuthor(String author) {
        return bookRepository.findByAuthor(author);
    }
    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    public Book createBook(Book book) {
        return bookRepository.save(book);
    }

    public Book updateBook(Long id, Book updatedBook) {
        Optional<Book> existingBookOptional = bookRepository.findById(id);
        if (existingBookOptional.isPresent()) {
            Book existingBook = existingBookOptional.get();
            existingBook.setTitle(updatedBook.getTitle());
            existingBook.setAuthor(updatedBook.getAuthor());
            return bookRepository.save(existingBook);
        } else {
            throw new NotFoundException("Book with id " + id + " not found");
        }
    }

    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    public void deleteAllBooks(){
         bookRepository.deleteAll();
    }
}
