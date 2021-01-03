package com.dermacon.securewebapp.service;

import com.dermacon.securewebapp.data.Course;
import com.dermacon.securewebapp.data.CourseRepository;
import com.dermacon.securewebapp.data.Person;
import com.dermacon.securewebapp.data.PersonRepository;
import com.dermacon.securewebapp.data.UserRepository;
import com.dermacon.securewebapp.data.form_input.FormCourseInfo;
import com.dermacon.securewebapp.exception.DuplicateCourseException;
import com.dermacon.securewebapp.exception.ErrorCodeException;
import com.dermacon.securewebapp.exception.HostEnrollOwnCourseException;
import com.dermacon.securewebapp.exception.NonExistentCourseException;
import com.dermacon.securewebapp.exception.UserNotEnrolledAtDropoutException;
import com.dermacon.securewebapp.utils.SampleCourseUtils;
import com.dermacon.securewebapp.utils.SamplePersonUtils;
import com.dermacon.securewebapp.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;

import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@SpringBootTest
// drop database before each test
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Transactional
class CourseServiceTest {

    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private UserRepository userRepository;

    @MockBean
    private MailService mailService;

    @MockBean
    private PersonService personService;

    @Test
    void test_getCourses_valid() {
        assertEquals(0, courseRepository.count());
        Set<Course> compareSet = new HashSet<>();

        for (int i = 1; i < 5; i++) {
            Course course_in = SampleCourseUtils.createSampleCourse_empty(i, i);
            courseRepository.save(course_in);
            compareSet.add(course_in);

            assertEquals(i, courseRepository.count());
            Course course_out = courseRepository.findByCourseName(course_in.getCourseName());
            assertNotNull(course_out);
            assertEquals(course_in, course_out);

            assertTrue(TestUtils.toSet(courseService.allCourses()).containsAll(compareSet));
        }
    }

    @Test
    void test_valid_createCourse() throws ErrorCodeException {
        int hostSeed = 0;
        int fst_courseSeed = 0;
        int snd_courseSeed = 1;
        Person currLoggedInPerson = SamplePersonUtils.createSampleManagerPerson(hostSeed);
        doReturn(currLoggedInPerson).when(personService).getLoggedInPerson();
        doReturn(currLoggedInPerson.getUser()).when(personService).getLoggedInUser();

        // first course
        FormCourseInfo formInput = SampleCourseUtils.createSampleFormInput(fst_courseSeed);
        assertEquals(0, courseRepository.count());
        courseService.createCourse(formInput);
        assertEquals(1, courseRepository.count());

        Course createdCourse = courseService.allCourses().iterator().next();
        Course expCourse = SampleCourseUtils.createSampleCourse_empty(hostSeed, fst_courseSeed);
        assertEquals(expCourse, createdCourse);

        // second course - same information but different course name (valid)
        FormCourseInfo formInput_copy = SampleCourseUtils.createSampleFormInput(fst_courseSeed);
        FormCourseInfo tmp = SampleCourseUtils.createSampleFormInput(snd_courseSeed);
        formInput_copy.setCourseName(tmp.getCourseName());
        assertEquals(1, courseRepository.count());
        courseService.createCourse(formInput_copy);
        assertEquals(2, courseRepository.count());

        Set<Course> actual_courses = TestUtils.toSet(courseService.allCourses());


        Course c1 = SampleCourseUtils.createSampleCourse_empty(hostSeed,fst_courseSeed);
        Course c2 = SampleCourseUtils.createSampleCourse_empty(hostSeed,fst_courseSeed);
        Course ctmp = SampleCourseUtils.createSampleCourse_empty(hostSeed, snd_courseSeed);
        c2.setCourseName(ctmp.getCourseName());

        assertEquals(actual_courses.size(), 2);

        Iterator<Course> it = actual_courses.iterator();

        Course cit1 = it.next();
        boolean b1 = cit1.equals(c1);
        boolean b2 = cit1.equals(c2);
        assertTrue(b1 || b2);

        Course cit2 = it.next();
        boolean b3 = cit2.equals(c1);
        boolean b4 = cit2.equals(c2);
        assertTrue(b3 || b4);

        // for some reason it's not possible to create an expected set of courses
        // and test via .containsAll(), no idea why :(
    }

    @Test
    void test_invalid_createCourse_duplicate() throws ErrorCodeException {
        Person currLoggedInPerson = SamplePersonUtils.createSampleManagerPerson(0);
        doReturn(currLoggedInPerson).when(personService).getLoggedInPerson();
        doReturn(currLoggedInPerson.getUser()).when(personService).getLoggedInUser();

        // complete copy
        FormCourseInfo formInput = SampleCourseUtils.createSampleFormInput(0);
        courseService.createCourse(formInput);

        ErrorCodeException thrown = assertThrows(
                DuplicateCourseException.class,
                () -> courseService.createCourse(formInput),
                "Expected to throw DuplicateCourseException, but didn't"
        );

        assertEquals(1, courseRepository.count());

        // completely new course - but same name
        FormCourseInfo tmp = SampleCourseUtils.createSampleFormInput(0);
        FormCourseInfo formInput2 = SampleCourseUtils.createSampleFormInput(1);
        formInput2.setCourseName(tmp.getCourseName());

        ErrorCodeException thrown2 = assertThrows(
                DuplicateCourseException.class,
                () -> courseService.createCourse(formInput2),
                "Expected to throw DuplicateCourseException, but didn't"
        );
    }

    @Test
    void test_valid_remove_single() throws ErrorCodeException {
        int host_seed = 0;
        Person currLoggedInPerson = SamplePersonUtils.createSampleManagerPerson(host_seed);
        doReturn(currLoggedInPerson).when(personService).getLoggedInPerson();
        doReturn(currLoggedInPerson.getUser()).when(personService).getLoggedInUser();

        FormCourseInfo formInput = SampleCourseUtils.createSampleFormInput(0);
        courseService.createCourse(formInput);

        assertEquals(1, courseRepository.count());
        Course course = courseRepository.findByCourseName(formInput.getCourseName());
        courseService.removeCourse(course.getCourseId());
        assertEquals(0, courseRepository.count());
        assertNull(courseRepository.findByCourseName(formInput.getCourseName()));
    }

    @Test
    void test_valid_remove_multiple() throws ErrorCodeException {
        int host_seed = 0;
        Person currLoggedInPerson = SamplePersonUtils.createSampleManagerPerson(host_seed);
        doReturn(currLoggedInPerson).when(personService).getLoggedInPerson();
        doReturn(currLoggedInPerson.getUser()).when(personService).getLoggedInUser();

        List<FormCourseInfo> formInputs = new ArrayList<>();
        for (int i = 1; i < 5; i++) {
            FormCourseInfo formInput = SampleCourseUtils.createSampleFormInput(i);
            formInputs.add(formInput);
            courseService.createCourse(formInput);
        }

        Iterator<FormCourseInfo> it = formInputs.iterator();
        for (int i = 1; i < 5; i++) {
            assertEquals(5 - i, courseRepository.count());
            FormCourseInfo currInfoObj = it.next();
            Course course = courseRepository.findByCourseName(currInfoObj.getCourseName());
            courseService.removeCourse(course.getCourseId());
            assertEquals(5 - i - 1, courseRepository.count());
            assertNull(courseRepository.findByCourseName(currInfoObj.getCourseName()));
        }
    }

    @Test
    void test_invalid_remove() throws ErrorCodeException {
        Person currLoggedInPerson = SamplePersonUtils.createSampleManagerPerson(0);
        doReturn(currLoggedInPerson).when(personService).getLoggedInPerson();
        doReturn(currLoggedInPerson.getUser()).when(personService).getLoggedInUser();

        // empty db
        long wrongId = 42;
        assertNull(courseRepository.findByCourseId(wrongId));

        ErrorCodeException thrown = assertThrows(
                NonExistentCourseException.class,
                () -> courseService.removeCourse(wrongId),
                "Expected to throw NonExistentCourseException, but didn't"
        );

        // courses in db
        for (int i = 1; i < 5; i++) {
            FormCourseInfo formInput = SampleCourseUtils.createSampleFormInput(i);
            courseService.createCourse(formInput);
        }

        assertNull(courseRepository.findByCourseId(wrongId));

        ErrorCodeException thrown2 = assertThrows(
                NonExistentCourseException.class,
                () -> courseService.removeCourse(wrongId),
                "Expected to throw NonExistentCourseException, but didn't"
        );
    }

    @Test
    void test_loggedInPersonCanEditCourse_validManager() {
        int host_seed = 0;
        int course_seed = 0;

        Person currLoggedInPerson = SamplePersonUtils.createSampleManagerPerson(host_seed);
        doReturn(currLoggedInPerson).when(personService).getLoggedInPerson();
        doReturn(currLoggedInPerson.getUser()).when(personService).getLoggedInUser();

        Course course = SampleCourseUtils.createSampleCourse_empty(host_seed, course_seed);

        assertTrue(courseService.loggedInPersonCanEditCourse(course));
    }

    @Test
    void test_loggedInPersonCanEditCourse_validAdmin() {
        int admin_seed = 0;
        int host_seed = 1;
        int course_seed = 0;

        Person currLoggedInPerson = SamplePersonUtils.createSampleAdminPerson(admin_seed);
        doReturn(currLoggedInPerson).when(personService).getLoggedInPerson();
        doReturn(currLoggedInPerson.getUser()).when(personService).getLoggedInUser();

        Course course = SampleCourseUtils.createSampleCourse_empty(host_seed, course_seed);

        assertTrue(courseService.loggedInPersonCanEditCourse(course));
    }

    @Test
    void test_loggedInPersonCanEditCourse_invalidUser() {
        int user_seed = 0;
        int host_seed = 1;
        int course_seed = 0;

        Person currLoggedInPerson = SamplePersonUtils.createSampleUserPerson(user_seed);
        doReturn(currLoggedInPerson).when(personService).getLoggedInPerson();
        doReturn(currLoggedInPerson.getUser()).when(personService).getLoggedInUser();

        Course course = SampleCourseUtils.createSampleCourse_empty(host_seed, course_seed);

        assertFalse(courseService.loggedInPersonCanEditCourse(course));
    }

    @Test
    void test_loggedInPersonCanEditCourse_invalidHost() {
        int host_seed = 0;
        int otherHost_seed = 1;
        int course_seed = 0;

        Person currLoggedInPerson = SamplePersonUtils.createSampleManagerPerson(host_seed);
        doReturn(currLoggedInPerson).when(personService).getLoggedInPerson();
        doReturn(currLoggedInPerson.getUser()).when(personService).getLoggedInUser();

        Course course = SampleCourseUtils.createSampleCourse_empty(otherHost_seed, course_seed);

        assertFalse(courseService.loggedInPersonCanEditCourse(course));
    }

    @Test
    void test_loggedInPersonCanCreateCourse_validManager() {
        int host_seed = 0;
        Person currLoggedInPerson = SamplePersonUtils.createSampleManagerPerson(host_seed);
        doReturn(currLoggedInPerson).when(personService).getLoggedInPerson();
        doReturn(currLoggedInPerson.getUser()).when(personService).getLoggedInUser();

        assertTrue(courseService.loggedInPersonCanCreateCourse());
    }

    @Test
    void test_loggedInPersonCanCreateCourse_validAdmin() {
        int host_seed = 0;
        Person currLoggedInPerson = SamplePersonUtils.createSampleManagerPerson(host_seed);
        doReturn(currLoggedInPerson).when(personService).getLoggedInPerson();
        doReturn(currLoggedInPerson.getUser()).when(personService).getLoggedInUser();

        assertTrue(courseService.loggedInPersonCanCreateCourse());
    }

    @Test
    void test_loggedInPersonCanCreateCourse_invalidUser() {
        int user_seed = 0;
        Person currLoggedInPerson = SamplePersonUtils.createSampleUserPerson(user_seed);
        doReturn(currLoggedInPerson).when(personService).getLoggedInPerson();
        doReturn(currLoggedInPerson.getUser()).when(personService).getLoggedInUser();

        assertFalse(courseService.loggedInPersonCanCreateCourse());
    }


    // ---------- enrollLoggedInPerson ---------- //

    @Test
    void test_enrollLoggedInPerson_validUser() throws ErrorCodeException {
        int user_seed = 0;
        int host_seed = 1;
        int course_seed = 2;

        Person currLoggedInPerson = SamplePersonUtils.createSampleUserPerson(user_seed);
        doReturn(currLoggedInPerson).when(personService).getLoggedInPerson();
        doReturn(currLoggedInPerson.getUser()).when(personService).getLoggedInUser();

        Course course = SampleCourseUtils.createSampleCourse_empty(host_seed, course_seed);
        courseRepository.save(course);

        assertTrue(course.getParticipants().isEmpty());
        courseService.enrollLoggedInPerson(course.getCourseId());

        assertEquals(1, course.getParticipants().size());
        assertTrue(course.getParticipants().contains(currLoggedInPerson));

        assertEquals(1, courseRepository.count());
        Course db_course = courseRepository.findAll().iterator().next();
        assertEquals(1, db_course.getParticipants().size());
        assertTrue(course.getParticipants().contains(currLoggedInPerson));
    }


    @Test
    void test_enrollLoggedInPerson_validManager() throws ErrorCodeException {
        int user_seed = 0;
        int host_seed = 1;
        int course_seed = 2;

        Person currLoggedInPerson = SamplePersonUtils.createSampleManagerPerson(user_seed);
        doReturn(currLoggedInPerson).when(personService).getLoggedInPerson();
        doReturn(currLoggedInPerson.getUser()).when(personService).getLoggedInUser();

        Course course = SampleCourseUtils.createSampleCourse_empty(host_seed, course_seed);
        courseRepository.save(course);

        assertTrue(course.getParticipants().isEmpty());
        courseService.enrollLoggedInPerson(course.getCourseId());

        assertEquals(1, course.getParticipants().size());
        assertTrue(course.getParticipants().contains(currLoggedInPerson));

        assertEquals(1, courseRepository.count());
        Course db_course = courseRepository.findAll().iterator().next();
        assertEquals(1, db_course.getParticipants().size());
        assertTrue(course.getParticipants().contains(currLoggedInPerson));
    }

    @Test
    void test_enrollLoggedInPerson_validAdmin() throws ErrorCodeException {
        int user_seed = 0;
        int host_seed = 1;
        int course_seed = 2;

        Person currLoggedInPerson = SamplePersonUtils.createSampleAdminPerson(user_seed);
        doReturn(currLoggedInPerson).when(personService).getLoggedInPerson();
        doReturn(currLoggedInPerson.getUser()).when(personService).getLoggedInUser();

        Course course = SampleCourseUtils.createSampleCourse_empty(host_seed, course_seed);
        courseRepository.save(course);

        assertTrue(course.getParticipants().isEmpty());
        courseService.enrollLoggedInPerson(course.getCourseId());

        assertEquals(1, course.getParticipants().size());
        assertTrue(course.getParticipants().contains(currLoggedInPerson));

        assertEquals(1, courseRepository.count());
        Course db_course = courseRepository.findAll().iterator().next();
        assertEquals(1, db_course.getParticipants().size());
        assertTrue(course.getParticipants().contains(currLoggedInPerson));
    }

    @Test
    void test_enrollLoggedInPerson_invalidCourse() {
        int user_seed = 0;
        int host_seed = 1;
        int course_seed = 2;

        Person currLoggedInPerson = SamplePersonUtils.createSampleUserPerson(user_seed);
        doReturn(currLoggedInPerson).when(personService).getLoggedInPerson();
        doReturn(currLoggedInPerson.getUser()).when(personService).getLoggedInUser();

        assertEquals(0, courseRepository.count());

        int wrongId = 42;
        ErrorCodeException thrown = assertThrows(
                NonExistentCourseException.class,
                () -> courseService.enrollLoggedInPerson(wrongId),
                "Expected to throw NonExistentCourseException, but didn't"
        );

        assertEquals(0, courseRepository.count());
        Course course = SampleCourseUtils.createSampleCourse_empty(host_seed, course_seed);
        courseRepository.save(course);
        assertEquals(1, courseRepository.count());

        thrown = assertThrows(
                NonExistentCourseException.class,
                () -> courseService.enrollLoggedInPerson(wrongId),
                "Expected to throw NonExistentCourseException, but didn't"
        );

        assertEquals(1, courseRepository.count());
    }

    @Test
    void test_enrollLoggedInPerson_invalidHostEnrollInOwnCourse() {
        int host_seed = 1;
        int course_seed = 2;

        Person currLoggedInPerson = SamplePersonUtils.createSampleManagerPerson(host_seed);
        doReturn(currLoggedInPerson).when(personService).getLoggedInPerson();
        doReturn(currLoggedInPerson.getUser()).when(personService).getLoggedInUser();

        assertEquals(0, courseRepository.count());

        Course course = SampleCourseUtils.createSampleCourse_empty(host_seed, course_seed);
        courseRepository.save(course);

        assertEquals(currLoggedInPerson, course.getHost());

        ErrorCodeException thrown = assertThrows(
                HostEnrollOwnCourseException.class,
                () -> courseService.enrollLoggedInPerson(course.getCourseId()),
                "Expected to throw HostEnrollOwnCourseException, but didn't"
        );

    }

    @Test
    void test_enrollLoggedInPerson_validMailNotification() throws ErrorCodeException {
        doNothing().when(mailService).sendGreeting(isA(Person.class), isA(Course.class));

        int user_seed = 0;
        int host_seed = 1;
        int course_seed = 2;

        Person currLoggedInPerson = SamplePersonUtils.createSampleUserPerson(user_seed);
        doReturn(currLoggedInPerson).when(personService).getLoggedInPerson();
        doReturn(currLoggedInPerson.getUser()).when(personService).getLoggedInUser();

        Course course = SampleCourseUtils.createSampleCourse_empty(host_seed, course_seed);
        courseRepository.save(course);

        courseService.enrollLoggedInPerson(course.getCourseId());

        verify(mailService, times(1))
                .sendGreeting(isA(Person.class), isA(Course.class));
    }


    // ---------- dropoutLoggedInPerson ---------- //

    @Test
    void test_dropoutLoggedInPerson_validUser() throws ErrorCodeException {
        int user_seed = 0;
        int host_seed = 1;
        int course_seed = 2;

        Person currLoggedInPerson = SamplePersonUtils.createSampleUserPerson(user_seed);
        doReturn(currLoggedInPerson).when(personService).getLoggedInPerson();
        doReturn(currLoggedInPerson.getUser()).when(personService).getLoggedInUser();

        Course course = SampleCourseUtils.createSampleCourse_empty(host_seed, course_seed);
        course.getParticipants().add(currLoggedInPerson);
        courseRepository.save(course);

        assertEquals(1, course.getParticipants().size());
        assertTrue(course.getParticipants().contains(currLoggedInPerson));

        courseService.dropoutLoggedInPerson(course.getCourseId());

        assertEquals(0, course.getParticipants().size());
        assertFalse(course.getParticipants().contains(currLoggedInPerson));
        assertEquals(1, courseRepository.count());
    }

    @Test
    void test_dropoutLoggedInPerson_invalidCourse() {
        int user_seed = 0;
        Person currLoggedInPerson = SamplePersonUtils.createSampleUserPerson(user_seed);
        doReturn(currLoggedInPerson).when(personService).getLoggedInPerson();
        doReturn(currLoggedInPerson.getUser()).when(personService).getLoggedInUser();

        assertEquals(0, courseRepository.count());

        int wrongId = 42;
        assertThrows(
                NonExistentCourseException.class,
                () -> courseService.dropoutLoggedInPerson(wrongId),
                "Expected to throw NonExistentCourseException, but didn't"
        );

        assertEquals(0, courseRepository.count());
    }

    @Test
    void test_dropoutLoggedInPerson_invalid_UserNotEnrolled() {
        int user_seed = 0;
        int host_seed = 1;
        int course_seed = 2;

        Person currLoggedInPerson = SamplePersonUtils.createSampleUserPerson(user_seed);
        doReturn(currLoggedInPerson).when(personService).getLoggedInPerson();
        doReturn(currLoggedInPerson.getUser()).when(personService).getLoggedInUser();

        Course course = SampleCourseUtils.createSampleCourse_empty(host_seed, course_seed);
        courseRepository.save(course);

        assertEquals(0, course.getParticipants().size());
        assertFalse(course.getParticipants().contains(currLoggedInPerson));

        assertThrows(
                UserNotEnrolledAtDropoutException.class,
                () -> courseService.dropoutLoggedInPerson(course.getCourseId()),
                "Expected to throw UserNotEnrolledAtDropoutException, but didn't"
        );

        assertEquals(0, course.getParticipants().size());
        assertFalse(course.getParticipants().contains(currLoggedInPerson));
    }

    @Test
    void test_dropoutLoggedInPerson_invalid_HostNotEnrolled() {
        int host_seed = 1;
        int course_seed = 2;

        Person currLoggedInPerson = SamplePersonUtils.createSampleManagerPerson(host_seed);
        doReturn(currLoggedInPerson).when(personService).getLoggedInPerson();
        doReturn(currLoggedInPerson.getUser()).when(personService).getLoggedInUser();

        Course course = SampleCourseUtils.createSampleCourse_empty(host_seed, course_seed);
        courseRepository.save(course);

        assertEquals(0, course.getParticipants().size());
        assertFalse(course.getParticipants().contains(currLoggedInPerson));

        assertThrows(
                UserNotEnrolledAtDropoutException.class,
                () -> courseService.dropoutLoggedInPerson(course.getCourseId()),
                "Expected to throw UserNotEnrolledAtDropoutException, but didn't"
        );

        assertEquals(0, course.getParticipants().size());
        assertFalse(course.getParticipants().contains(currLoggedInPerson));
    }
}