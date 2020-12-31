package com.dermacon.securewebapp.service;

import com.dermacon.securewebapp.data.Person;
import com.dermacon.securewebapp.data.PersonRepository;
import com.dermacon.securewebapp.data.User;
import com.dermacon.securewebapp.data.UserRepository;
import com.dermacon.securewebapp.data.UserRole;
import com.dermacon.securewebapp.data.formInput.FormSignupInfo;
import com.dermacon.securewebapp.exception.ErrorCodeException;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
// drop database before each test
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class PersonServiceTest {

    private final String SAMPLE_PASSWORD_PLAIN = "password";
    // the plain pw after putting it through the bcrypt algorithm
    private final String SAMPLE_PASSWORD_ENCODED = "$2a$10$1JT96p9Nge3K7mjkLqKmDO0o5t/wvb2SCGIQGDEApkOIy0MP1vkze";

    @Autowired
    private PersonService personService;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    private <T> List<T> toList(Iterable<T> it) {
        List<T> out = new ArrayList<>();
        it.forEach(out::add);
        return out;
    }

    private FormSignupInfo createFormSingupInput(Person person) {
        return null;
    }


    @Test
    @Transactional
    public void test_registration_valid_emptyDB() throws ErrorCodeException {
        User user = new User.Builder()
                .username("u1")
                .password(SAMPLE_PASSWORD_ENCODED)
                .role(UserRole.ROLE_USER)
                .build();

        Person person = new Person.Builder()
                .email("mail1@mail.com")
                .firstname("fst1")
                .surname("sur1")
                .user(user)
                .build();

        FormSignupInfo signupInfo = new FormSignupInfo.Builder()
                .email(person.getEmail())
                .username(person.getUser().getUsername())
                .password(SAMPLE_PASSWORD_PLAIN)
                .firstname(person.getFirstname())
                .surname(person.getSurname())
                .build();


        assertEquals(0, userRepository.count());
        assertEquals(0, personRepository.count());

        personService.register(signupInfo);

        assertEquals(1, userRepository.count());
        assertEquals(1, personRepository.count());

        assertEquals(user, userRepository.findByUsername(user.getUsername()));
        assertEquals(person, personRepository.findByEmail(person.getEmail()));
    }

    @Test
    public void test_registration_emailAlreadyExists() {



//        FormSignupInfo signupInfo = new FormSignupInfo.Builder()
//                .email()
//                .build();



//        personService.testDelegatedService();
//        verify(testService, times(1)).test();
    }


}