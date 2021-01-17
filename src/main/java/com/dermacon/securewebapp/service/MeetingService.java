package com.dermacon.securewebapp.service;

import com.dermacon.securewebapp.data.Course;
import com.dermacon.securewebapp.data.Meeting;
import com.dermacon.securewebapp.data.MeetingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MeetingService {

    @Autowired
    private MeetingRepository meetingRepository;

    public Iterable<Meeting> getAllMeetings(Course course) {
        return meetingRepository.findAllByCourse(course);
    }

}
