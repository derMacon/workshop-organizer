package com.dermacon.securewebapp.utils;

import com.dermacon.securewebapp.data.Announcement;
import com.dermacon.securewebapp.data.Course;
import com.dermacon.securewebapp.data.Person;

import java.util.Set;

public class SampleCourseUtils {

    private final static int SAMPLE_MAX_PARTICIPANT_CNT = 5;

    public static Course createSampleCourse_empty(int hostId, int courseId) {
        Person host = SamplePersonUtils.createSamplePerson(hostId);
        return new Course.Builder()
                .host(host)
                .courseName("course" + courseId)
                .courseSummary("summary" + courseId)
                .courseDescription("description" + courseId)
                .maxParticipantCount(SAMPLE_MAX_PARTICIPANT_CNT)
                .build();
    }

}
