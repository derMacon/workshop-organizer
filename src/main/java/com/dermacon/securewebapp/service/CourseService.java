package com.dermacon.securewebapp.service;

import com.dermacon.securewebapp.controller.ManagerController;
import com.dermacon.securewebapp.data.Announcement;
import com.dermacon.securewebapp.data.Course;
import com.dermacon.securewebapp.data.CourseRepository;
import com.dermacon.securewebapp.data.form_input.FormAnnouncementInfo;
import com.dermacon.securewebapp.data.form_input.FormCourseInfo;
import com.dermacon.securewebapp.data.Person;
import com.dermacon.securewebapp.data.UserRole;
import com.dermacon.securewebapp.exception.AnnouncementNonExistentException;
import com.dermacon.securewebapp.exception.DuplicateCourseException;
import com.dermacon.securewebapp.exception.ErrorCodeException;
import com.dermacon.securewebapp.exception.HostEnrollOwnCourseException;
import com.dermacon.securewebapp.exception.NonExistentCourseException;
import com.dermacon.securewebapp.exception.UserAlreadyEnrolledException;
import com.dermacon.securewebapp.exception.UserCantCreateCourseException;
import com.dermacon.securewebapp.exception.UserNotEnrolledAtDropoutException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
public class CourseService {

    private static Logger log = Logger.getLogger(ManagerController.class);

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private PersonService personService;

    @Autowired
    private AnnouncementService announcementService;

    @Autowired
    private MailService mailService;


    /* ---------- information for the displaying data ---------- */

    public Course getCourse(long courseId) throws NonExistentCourseException {
        Course course = courseRepository.findByCourseId(courseId);
        if (course == null) {
            throw new NonExistentCourseException();
        }
        return courseRepository.findByCourseId(courseId);
    }

    public Iterable<Course> allCourses() {
        return courseRepository.findAll();
    }

    public Iterable<Course> createdCourses() {
        return courseRepository.findAllByHost(personService.getLoggedInPerson());
    }

    public Iterable<Person> getAllCreators() {
        Stream<Course> stream = StreamSupport.stream(
                courseRepository.findAll().spliterator(),
                false
        );
        return stream.map(Course::getHost).collect(Collectors.toSet());
    }

    public boolean currUserIsEnrolled(Course course) {
        return course.getParticipants().contains(personService.getLoggedInPerson());
    }



    /* ---------- course entities (add / delete) ---------- */

    public void createCourse(FormCourseInfo courseInfo) throws ErrorCodeException {
        if (!loggedInPersonCanCreateCourse()) {
            throw new UserCantCreateCourseException();
        }

        String inputCourseName = courseInfo.getCourseName();
        if (courseRepository.existsByCourseNameIgnoreCase(inputCourseName)) {
            throw new DuplicateCourseException();
        }

        Course newCourse = new Course.Builder()
                .host(personService.getLoggedInPerson())
                .courseName(courseInfo.getCourseName())
                .courseSummary(courseInfo.getCourseSummary())
                .courseDescription(courseInfo.getCourseDescription())
                .maxParticipantCount(courseInfo.getMaxParticipantCount())
                .build();

        courseRepository.save(newCourse);
    }

    public void removeCourse(long id) throws NonExistentCourseException {
        Course course = getCourse(id);
        // first delete all foreign key references
        course.setHost(null);
        course.setParticipants(null);
        Set<Announcement> announcements = course.getAnnouncements();
        announcementService.deleteAnnouncements(announcements);
        courseRepository.delete(course);
    }


    /* ---------- person information ---------- */

    /**
     * Checks if the person created the course or the person is an admin
     * @param course course to check
     * @return true if the person created the course or the person is an admin
     */
    public boolean loggedInPersonCanEditCourse(Course course) {
        UserRole role = personService.getLoggedInUser().getRole();
        Person person = personService.getLoggedInPerson();
        return role == UserRole.ROLE_ADMIN || course.getHost().equals(person);
    }

    public boolean loggedInPersonCanCreateCourse() {
        UserRole role = personService.getLoggedInUser().getRole();
        return role == UserRole.ROLE_ADMIN || role == UserRole.ROLE_MANAGER;
    }


    /* ---------- person information ---------- */

    /**
     * Enroll logged in person in a given course
     *
     * @param courseId id of the course
     * @throws NonExistentCourseException the specified course does not exist
     * @throws UserAlreadyEnrolledException the person is already enrolled in the course
     * @throws HostEnrollOwnCourseException the person has created the course
     */
    public void enrollLoggedInPerson(long courseId) throws NonExistentCourseException, UserAlreadyEnrolledException, HostEnrollOwnCourseException {
        Course course = getCourse(courseId);

        Person newParticipant = personService.getLoggedInPerson();
        if (course.getParticipants().contains(newParticipant)) {
            throw new UserAlreadyEnrolledException();
        }

        if (course.getHost().equals(newParticipant)) {
            throw new HostEnrollOwnCourseException();
        }

        course.addNewParticipant(newParticipant);
        courseRepository.save(course);
        mailService.sendGreeting(newParticipant, course);
    }

    public void dropoutLoggedInPerson(long courseId) throws NonExistentCourseException, UserNotEnrolledAtDropoutException {
        Course course = getCourse(courseId);

        Person participant = personService.getLoggedInPerson();
        if (!currUserIsEnrolled(course)) {
            throw new UserNotEnrolledAtDropoutException();
        }

        course.removeParticipant(participant);
        courseRepository.save(course);
        mailService.sendDropoutConfirmation(participant, course);
    }


    /* ---------- announcements ---------- */

    public void createAnnouncement(FormAnnouncementInfo announcementInfo, long courseId) throws NonExistentCourseException {
        Course course = getCourse(courseId);
        announcementService.createNewAnnouncement(course, announcementInfo);
    }

    public void deleteAnnouncement(long announcementId) throws AnnouncementNonExistentException {
        announcementService.deleteAnnouncement(announcementId);
    }

}
