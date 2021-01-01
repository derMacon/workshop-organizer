package com.dermacon.securewebapp.service;

import com.dermacon.securewebapp.data.Course;
import com.dermacon.securewebapp.data.Person;
import com.dermacon.securewebapp.data.PersonRepository;
import com.dermacon.securewebapp.data.User;
import com.dermacon.securewebapp.data.UserRepository;
import com.dermacon.securewebapp.data.UserRole;
import com.dermacon.securewebapp.data.formInput.FormSignupInfo;
import com.dermacon.securewebapp.exception.EmailAlreadyExistsException;
import com.dermacon.securewebapp.exception.ErrorCodeException;
import com.dermacon.securewebapp.exception.UsernameAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;

import static com.dermacon.securewebapp.data.UserRole.ROLE_ANONYMOUS;
import static com.dermacon.securewebapp.data.UserRole.ROLE_USER;

@Service
public class PersonService {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MailService mailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Checks if the currently logged in user has at least one of
     * the required permissions
     * @param role roles to check
     * @return true if the currently logged in user has at least one of
     * the required permissions
     */
    public boolean matchesAtLeastOneRole(UserRole... role) {
        UserRole currUser_role = getLoggedInUser().getRole();
        return Arrays.asList(role).stream()
                .filter(e -> e == currUser_role)
                .findAny().isPresent();
    }

    /**
     * Determines the currently logged in user
     * @return the currently logged in user
     */
    public Person getLoggedInPerson() {
        try {
            return personRepository.findByUser(getLoggedInUser());
        } catch (ClassCastException e) {
            return null;
        }
    }

    public User getLoggedInUser() {
            // for some reason the id is always 0
            String username = ((User) SecurityContextHolder
                    .getContext()
                    .getAuthentication()
                    .getPrincipal()).getUsername();

            return userRepository.findByUsername(username);
    }

    public Iterable<Person> getAllPersons() {
        return personRepository.findAll();
    }

    public void register(FormSignupInfo signupInfo) throws ErrorCodeException {
        if (personRepository.findByEmail(signupInfo.getEmail()) != null) {
            throw new EmailAlreadyExistsException();
        }

        if (userRepository.findByUsername(signupInfo.getUsername()) != null) {
            throw new UsernameAlreadyExistsException();
        }

        User user = new User.Builder()
                .username(signupInfo.getUsername())
                .password(passwordEncoder.encode(signupInfo.getPassword()))
                .role(ROLE_USER)
                .build();

        Person person = new Person.Builder()
                .email(signupInfo.getEmail())
                .firstname(signupInfo.getFirstname())
                .surname(signupInfo.getSurname())
                .user(user)
                .build();


        personRepository.save(person);
        mailService.sendAccountConfirmation(person);
    }

    // todo check if needed
    /**
     * list of hosts to pick from while creating new course
     * @return list of hosts to pick from while creating new course
     */
//    public Iterable<Person> getPossibleHosts() {
//        Set<User> users = userRepository.findAllByRole(UserRole.ROLE_ADMIN);
//        users.addAll(userRepository.findAllByRole(UserRole.ROLE_MANAGER));
//
//        // list of hosts to pick from while creating new course
//        Iterable<Person> possible_hosts = users.stream()
//                .map(personRepository::findByUser)
//                .collect(Collectors.toList());
//
//        return possible_hosts;
//    }

}
