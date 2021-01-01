package com.dermacon.securewebapp.service;

import com.dermacon.securewebapp.data.Course;
import com.dermacon.securewebapp.data.CourseRepository;
import com.dermacon.securewebapp.data.Person;
import com.dermacon.securewebapp.data.formInput.FormCourseInfo;
import com.dermacon.securewebapp.exception.DuplicateCourseException;
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

import java.util.HashSet;
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
        Person currLoggedInPerson = SamplePersonUtils.createSamplePerson(0);
        doReturn(currLoggedInPerson).when(personService).getLoggedInPerson();

        FormCourseInfo formInput = SampleCourseUtils.createSampleFormInput(0);
        assertEquals(0, courseRepository.count());
        courseService.createCourse(formInput);
        assertEquals(1, courseRepository.count());

        Course createdCourse = courseService.allCourses().iterator().next();
        assertEquals(SampleCourseUtils.createSampleCourse_empty(0, 0), createdCourse);
    }


    @Test
    public void test_valid_getCourse() {
        Course course = SampleCourseUtils.createSampleCourse_empty(0, 0);

    }

}