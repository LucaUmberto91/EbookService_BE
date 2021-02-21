package it.ebookservice.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import it.ebookservice.model.Users;

@Repository
public interface UsersRepository extends CrudRepository< Users, Integer>{

	public Optional<Users> findById(Integer id);
	public Users findByEmailAndPassword(String email,String password);
	public Users findByUsernameAndPassword(String email,String password);
}
