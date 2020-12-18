package com.dermacon.securewebapp.data;

import org.springframework.data.repository.CrudRepository;

public interface CourseRepository extends CrudRepository<Course, Long> {
    Course findByCourseId(Long id);

}
