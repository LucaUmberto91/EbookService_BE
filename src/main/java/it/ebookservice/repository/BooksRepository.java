package it.ebookservice.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import it.ebookservice.model.Book;

@Repository
public interface BooksRepository extends CrudRepository<Book, Integer>{

	public Book findByBookName(String name);
}
