package com.dermacon.securewebapp.controller;

import com.dermacon.securewebapp.data.Person;
import com.dermacon.securewebapp.data.PersonRepository;
import com.dermacon.securewebapp.data.User;
import com.dermacon.securewebapp.data.UserRepository;
import com.dermacon.securewebapp.data.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class PersonService {

    @Autowired
    PersonRepository personRepository;

    @Autowired
    UserRepository userRepository;

    /**
     * Determines the currently logged in user
     * @return the currently logged in user
     */
    public Person getLoggedInPerson() {
        return personRepository.findByUser(getLoggedInUser());
    }

    public User getLoggedInUser() {
        // for some reason the id is always 0
        String username = ((User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal()).getUsername();

        return userRepository.findByUsername(username);
    }
}
