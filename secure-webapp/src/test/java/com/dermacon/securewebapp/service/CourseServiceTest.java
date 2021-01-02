package com.dermacon.securewebapp.service;

import com.dermacon.securewebapp.data.Course;
import com.dermacon.securewebapp.data.CourseRepository;
import com.dermacon.securewebapp.data.Person;
import com.dermacon.securewebapp.data.formInput.FormCourseInfo;
import com.dermacon.securewebapp.exception.DuplicateCourseException;
import com.dermacon.securewebapp.exception.EmailAlreadyExistsException;
import com.dermacon.securewebapp.exception.ErrorCodeException;
import com.dermacon.securewebapp.utils.SampleCourseUtils;
import com.dermacon.securewebapp.utils.SamplePersonUtils;
import com.dermacon.securewebapp.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;

import javax.transaction.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

@SpringBootTest
// drop database before each test
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Transactional
class CourseServiceTest {

    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseRepository courseRepository;

    @MockBean
    private MailService mailService;

    @MockBean
    private PersonService personService;

    @Test
    public void test_getCourses_valid() {
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
    public void test_valid_createCourse() throws DuplicateCourseException {
        int hostSeed = 0;
        int fst_courseSeed = 0;
        int snd_courseSeed = 1;
        Person currLoggedInPerson = SamplePersonUtils.createSamplePerson(hostSeed);
        doReturn(currLoggedInPerson).when(personService).getLoggedInPerson();

        // first course
        FormCourseInfo formInput = SampleCourseUtils.createSampleFormInput(fst_courseSeed);
        assertEquals(0, courseRepository.count());
        courseService.createCourse(formInput);
        assertEquals(1, courseRepository.count());

        Course createdCourse = courseService.allCourses().iterator().next();
        assertEquals(SampleCourseUtils.createSampleCourse_empty(0, 0), createdCourse);

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
    public void test_invalid_createCourse_duplicate() throws DuplicateCourseException {
        Person currLoggedInPerson = SamplePersonUtils.createSamplePerson(0);
        doReturn(currLoggedInPerson).when(personService).getLoggedInPerson();

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
        courseService.createCourse(formInput2);

        ErrorCodeException thrown2 = assertThrows(
                DuplicateCourseException.class,
                () -> courseService.createCourse(formInput2),
                "Expected to throw DuplicateCourseException, but didn't"
        );

    }


    @Test
    public void test_valid_getCourse() {
        Course course = SampleCourseUtils.createSampleCourse_empty(0, 0);

    }

}