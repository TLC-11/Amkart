package com.amkart.estore.repositiries;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.amkart.estore.entities.User;

public interface UserRepository extends JpaRepository<User, String> {

	Optional<User> findByemailid(String emailid);

	Optional<User> findByEmailidAndPassword(String emailid, String password);

	Optional<List<User>> findBynameContaining(String pattern);

}
