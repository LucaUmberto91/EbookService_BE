package it.ebookservice.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
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
import it.ebookservice.service.SendEmail;


/**
 * Controller: insieme delle API utilizzate per l'archivio utenti registrati
 * 
 * @author lucamiraglia
 * 
 *
 */

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:3000")
public class UsersController {
	
	@Autowired
	private UsersRepository usersRepo;
	@Autowired
	private SendEmail sendMailService;
	@Autowired
	private S3Service s3Service;
	@Autowired
	private BooksRepository bookRepo;
	
/**
 * Funzione che registra gli utenti nel database a partire dalla info POST del JSON
 * 
 * @param utente
 * 
 */
@PostMapping(path = "/useRegister")	
	public Messages setUsers(@RequestBody Users user) {
		Messages msg = new Messages();
		if (!user.getName().equals("") && !user.getSurname().equals("")){
			sendMailService.sendEmailRegister(user);
			usersRepo.save(user);
			msg.setCode(0);
			msg.setMessage("Insertion successful!");
		} else if (user.getName().equals("")) {
			msg.setCode(1);
			msg.setMessage("ERROR! Please enter a name");
		} else if (user.getSurname().equals("")) {
			msg.setCode(2);
			msg.setMessage("ERROR! Please enter a surname");
		} else {
			msg.setCode(4);
			msg.setMessage("Error!");
		}
		return msg;
}


/**
 * Prende una stringa email e tramite una query trova le info sull'utente
 * 	
 * @param email
 * @return
 * 
 */
@GetMapping(path = "/userLogin")	
public Object getUsers(@RequestParam(value = "username") String username,@RequestParam(value = "password") String pswd) {
	Users user = usersRepo.findByUsernameAndPassword(username,pswd);
	if(user!=null) {
		return user; 
	}
	else return new Messages(5,"Not found user");
}

/**
 * Metodo per permettere di cambiare la password
 * @param email
 * @param pswd
 * @param newPswd
 * @return User
 */
@PostMapping(path = "/changePass")
public Object changePswd(@RequestParam(value = "username") String username,
		@RequestParam(value = "oldPassword") String pswd,@RequestParam(value = "newPassword") String newPswd) {
	Users user = usersRepo.findByUsernameAndPassword(username,pswd);
	if(user!=null) {
		user.setPassword(newPswd);
		usersRepo.save(user);
		return user;
	}
	else return new Messages(5,"Not found user");
}
@GetMapping(path = "/downloadBook")
	public Object downloadBook(@RequestParam(value = "username") String username,
			@RequestParam(value = "password") String pswd, @RequestParam String bookName) {
		Users user = usersRepo.findByUsernameAndPassword(username, pswd);
		if (user != null) {
			Book book = bookRepo.findByBookName(bookName);
			if (book != null) {
				book.setUrlDownload(s3Service.stringDownloadFile(book.getPath(), book.getBucket()));
				return book;
			} else
				return new Messages(8, "Sorry, book not present");
		} else
			return new Messages(5, "Not found user");
	}

@GetMapping(path = "/downloadBook2")
public Object downloadBook2(@RequestParam(value = "username") String username,
		@RequestParam(value = "password") String pswd, @RequestParam String bookName) {
	String urlBook = "";
	Users user = usersRepo.findByUsernameAndPassword(username, pswd);
	if (user != null) {
		Book book = bookRepo.findByBookName(bookName);
		if (book != null) {
			urlBook = "https://github.com/LucaUmberto91/EbookService_BE/raw/main/src/main/resources/Books/"+bookName+".pdf";
			return urlBook;
		} else
			return new Messages(8, "Sorry, book not present");
	} else
		return new Messages(5, "Not found user");
}

	
}
