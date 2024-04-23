package com.example.spring_data_jpa_mysql_book;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book,Long>
{
    List<Book> findByAuthor(String author);
}

