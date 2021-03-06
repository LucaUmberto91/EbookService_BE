package it.ebookservice.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
	public Object putBook(@RequestParam(value = "fileBook") MultipartFile file,
			@RequestParam(value = "username") String username, @RequestParam(value = "password") String pswd,
			@RequestParam(value = "bookName") String bookName,@RequestParam(value = "typeBook") String typeBook,
			@RequestParam(value = "pathBook") String pathBook,
			@RequestParam(value = "bucketBook") String bucketBook) throws IllegalStateException, IOException {
		Users user = usersRepo.findByUsernameAndPassword("admin", "admin");
		if (pswd.equals("admin") && user != null) {
			Book book = new Book(bookName,typeBook,bucketBook,pathBook);
			Book bookSaved = s3Service.uploadFileS3(file, book);
			if (bookSaved != null) {
				bookRepo.save(bookSaved);
				return new Messages(0, "Book saved!");
			} else
				return new Messages(8, "Error!Book is not present!");
		} else
			return new Messages(7, "Error!You are not admin!");
	}
}
