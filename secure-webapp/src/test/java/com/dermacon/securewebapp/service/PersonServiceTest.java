package com.dermacon.securewebapp.service;

import com.dermacon.securewebapp.utils.SamplePersonUtils;
import com.dermacon.securewebapp.utils.TestUtils;
import com.dermacon.securewebapp.data.Person;
import com.dermacon.securewebapp.data.PersonRepository;
import com.dermacon.securewebapp.data.User;
import com.dermacon.securewebapp.data.UserRepository;
import com.dermacon.securewebapp.data.form_input.FormSignupInfo;
import com.dermacon.securewebapp.exception.EmailAlreadyExistsException;
import com.dermacon.securewebapp.exception.ErrorCodeException;
import com.dermacon.securewebapp.exception.UsernameAlreadyExistsException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
// drop database before each test
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Transactional
class PersonServiceTest {

    @Autowired
    private PersonService personService;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private UserRepository userRepository;

    @MockBean
    private MailService mailService;


    @Test
    void test_registration_valid_emptyDB() throws ErrorCodeException {
        User user = SamplePersonUtils.createSampleUser(0);
        Person person = SamplePersonUtils.createSampleUserPerson(0);
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
    void test_registration_valid_filledDB() throws ErrorCodeException {
        assertEquals(0, userRepository.count());
        assertEquals(0, personRepository.count());

        Set<Person> compareSet_person = new HashSet<>();
        Set<User> compareSet_user = new HashSet<>();

        for (int i = 1; i < 5; i++) {
            compareSet_person.add(SamplePersonUtils.createSampleUserPerson(i));
            compareSet_user.add(SamplePersonUtils.createSampleUser(i));

            FormSignupInfo signupInfo = SamplePersonUtils.createSampleFormSignupInfo(i);
            personService.register(signupInfo);

            assertEquals(i, userRepository.count());
            assertEquals(i, personRepository.count());

            assertTrue(TestUtils.toSet(userRepository.findAll()).containsAll(compareSet_user));
            assertTrue(TestUtils.toSet(personRepository.findAll()).containsAll(compareSet_person));
        }

    }

    @Test
    void test_registration_invalid_emailInUse() throws ErrorCodeException {
        assertEquals(0, userRepository.count());
        assertEquals(0, personRepository.count());

        FormSignupInfo signupInfo_valid = SamplePersonUtils.createSampleFormSignupInfo(0);
        FormSignupInfo signupInfo_invalid = SamplePersonUtils.createSampleFormSignupInfo(1);
        signupInfo_invalid.setEmail(signupInfo_valid.getEmail());

        personService.register(signupInfo_valid);

        assertEquals(1, userRepository.count());
        assertEquals(1, personRepository.count());

        assertNotNull(personRepository.findByEmail(signupInfo_valid.getEmail()));

        ErrorCodeException thrown = assertThrows(
                EmailAlreadyExistsException.class,
                () -> personService.register(signupInfo_invalid),
                "Expected to throw EmailAlreadyExistsException, but didn't"
        );
    }

    @Test
    void test_registration_invalid_usernameInUse() throws ErrorCodeException {
        assertEquals(0, userRepository.count());
        assertEquals(0, personRepository.count());

        FormSignupInfo signupInfo_valid = SamplePersonUtils.createSampleFormSignupInfo(0);
        FormSignupInfo signupInfo_invalid = SamplePersonUtils.createSampleFormSignupInfo(1);
        signupInfo_invalid.setUsername(signupInfo_valid.getUsername());

        personService.register(signupInfo_valid);

        assertEquals(1, userRepository.count());
        assertEquals(1, personRepository.count());

        assertNotNull(personRepository.findByEmail(signupInfo_valid.getEmail()));


        ErrorCodeException thrown = assertThrows(
                UsernameAlreadyExistsException.class,
                () -> personService.register(signupInfo_invalid),
                "Expected to throw EmailAlreadyExistsException, but didn't"
        );
    }

    @Test
    void test_registration_mailService_userGetsNotified() throws ErrorCodeException {
        doNothing().when(mailService).sendAccountConfirmation(isA(Person.class));

        FormSignupInfo signupInfo = SamplePersonUtils.createSampleFormSignupInfo(0);
        personService.register(signupInfo);

        verify(mailService, times(1)).sendAccountConfirmation(isA(Person.class));
    }

//    @Test
//    public void test_loggedInPerson_noPersonForUser() {
//        // todo find out how to inject personRepository without an Annotation
//        PersonService personService = mock(PersonService.class);
//        int seed = 0;
//        User currLoggedInUser = SamplePersonUtils.createSampleUser(seed);
//        doReturn(currLoggedInUser).when(personService).getLoggedInUser();
//        when(personService.getLoggedInPerson()).thenCallRealMethod();
//
//        assertNull(personService.getLoggedInPerson());
//    }

}
