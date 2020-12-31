package com.dermacon.securewebapp.service;

import com.dermacon.securewebapp.SamplePersonUtils;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
// drop database before each test
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class PersonServiceTest {

    @Autowired
    private PersonService personService;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private UserRepository userRepository;



    @Test
    @Transactional
    public void test_registration_valid_emptyDB() throws ErrorCodeException {
        User user = SamplePersonUtils.createSampleUser(0);
        Person person = SamplePersonUtils.createSamplePerson(0);
        FormSignupInfo signupInfo = SamplePersonUtils.createSampleFormSignupInfo(person);

        assertEquals(0, userRepository.count());
        assertEquals(0, personRepository.count());

        personService.register(signupInfo);

        assertEquals(1, userRepository.count());
        assertEquals(1, personRepository.count());

        assertEquals(user, userRepository.findByUsername(user.getUsername()));
        assertEquals(person, personRepository.findByEmail(person.getEmail()));
    }

    @Test
    public void test_registration_valid_filledDB() throws ErrorCodeException {
        // empty db
        int newUserId = 1;

        assertEquals(0, userRepository.count());
        assertEquals(0, personRepository.count());

        Set<Person> compareSet = new HashSet<>();

        for (int i = 1; i < 5; i++) {
            compareSet.add(SamplePersonUtils.createSamplePerson(i));

            FormSignupInfo signupInfo = SamplePersonUtils.createSampleFormSignupInfo(i);
            personService.register(signupInfo);

            assertEquals(i, userRepository.count());
            assertEquals(i, personRepository.count());

            // todo compare sets
        }

    }

    @Test
    public void test() {
//        FormSignupInfo signupInfo = new FormSignupInfo.Builder()
//                .email()
//                .build();

//        personService.testDelegatedService();
//        verify(testService, times(1)).test();
    }


}