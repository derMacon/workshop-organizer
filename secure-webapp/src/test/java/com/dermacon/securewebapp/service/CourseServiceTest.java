package com.dermacon.securewebapp.service;

import com.dermacon.securewebapp.data.Course;
import com.dermacon.securewebapp.data.CourseRepository;
import com.dermacon.securewebapp.utils.SampleCourseUtils;
import com.dermacon.securewebapp.utils.SamplePersonUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

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


    @Test
    public void test_getCourses_valid() {
        assertEquals(0, courseRepository.count());

        Course course_in = SampleCourseUtils.createSampleCourse_empty(0, 0);
        courseRepository.save(course_in);

        assertEquals(1, courseRepository.count());
        Course course_out = courseRepository.findByCourseName(course_in.getCourseName());
        assertNotNull(course_out);
        assertEquals(course_in, course_out);
    }


}