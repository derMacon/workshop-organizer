package com.dermacon.securewebapp.data;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsername(String username);
    void deleteByUserId(long id);
}
