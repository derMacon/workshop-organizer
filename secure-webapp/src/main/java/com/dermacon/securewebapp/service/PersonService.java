package com.dermacon.securewebapp.service;

import com.dermacon.securewebapp.data.Person;
import com.dermacon.securewebapp.data.PersonRepository;
import com.dermacon.securewebapp.data.User;
import com.dermacon.securewebapp.data.UserRepository;
import com.dermacon.securewebapp.data.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

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


    // todo check if needed
    /**
     * list of hosts to pick from while creating new course
     * @return list of hosts to pick from while creating new course
     */
    public Iterable<Person> getPossibleHosts() {
        Set<User> users = userRepository.findAllByRole(UserRole.ROLE_ADMIN);
        users.addAll(userRepository.findAllByRole(UserRole.ROLE_MANAGER));

        // list of hosts to pick from while creating new course
        Iterable<Person> possible_hosts = users.stream()
                .map(personRepository::findByUser)
                .collect(Collectors.toList());

        return possible_hosts;
    }
}
