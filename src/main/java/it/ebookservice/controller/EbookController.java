package it.ebookservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.ebookservice.aws.s3.S3Service;
import it.ebookservice.model.Book;
import it.ebookservice.model.Messages;
import it.ebookservice.model.Users;
import it.ebookservice.repository.BooksRepository;
import it.ebookservice.repository.UsersRepository;

@RestController
@RequestMapping("/archive")
@CrossOrigin(origins = "http://localhost:3000")
public class EbookController {

	@Autowired
	private BooksRepository bookRepo;
	@Autowired
	private S3Service s3Service;
	@Autowired
	private UsersRepository usersRepo;

	@PostMapping(path = "/newBook")
	public Object putBook(@RequestParam(value = "username") String username,
			@RequestParam(value = "password") String pswd, @RequestBody Book book) {
		Users user = usersRepo.findByUsernameAndPassword("admin", "admin");
		if (pswd.equals("admin") && user != null) {
			//Ss3Service.uploadFile(book.getPath(),book.getBucket(), filePath);
			bookRepo.save(book);
			return new Messages(0, "Book saved!");
		} else
			return new Messages(7, "Error!You are not admin!");
	}
}
