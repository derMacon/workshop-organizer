package com.dermacon.securewebapp.data;

import org.springframework.data.repository.CrudRepository;

public interface CourseRepository extends CrudRepository<Course, Long> {
    Course findByCourseId(Long id);
    Course findByCourseName(String courseName);
    Iterable<Course> findAllByHost(Person person);
    void deleteByCourseId(long id);
}
