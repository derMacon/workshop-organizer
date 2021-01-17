package com.dermacon.securewebapp.data;


import org.springframework.data.repository.CrudRepository;

public interface PersonRepository extends CrudRepository<Person, Long> {
    Person findByUser(User user);
    Person findByEmail(String email);
}
