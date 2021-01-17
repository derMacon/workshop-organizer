package com.dermacon.securewebapp.data;

import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface MeetingRepository extends CrudRepository<Meeting, Long> {
    Set<Meeting> findAllByCourse(Course course);
}
