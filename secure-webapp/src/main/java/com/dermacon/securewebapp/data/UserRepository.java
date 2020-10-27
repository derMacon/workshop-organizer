package com.dermacon.securewebapp.data;

import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsername(String username);
    Set<User> findAllByRole(UserRole role);
}
