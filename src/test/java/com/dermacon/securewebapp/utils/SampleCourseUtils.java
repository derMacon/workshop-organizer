package com.dermacon.securewebapp.utils;

import com.dermacon.securewebapp.data.Course;
import com.dermacon.securewebapp.data.Person;
import com.dermacon.securewebapp.data.form_input.FormCourseInfo;

public class SampleCourseUtils {

    private final static int SAMPLE_MAX_PARTICIPANT_CNT = 5;

    public static Course createSampleCourse_empty(int hostSeed, int courseSeed) {
        Person host = SamplePersonUtils.createSampleManagerPerson(hostSeed);
        return new Course.Builder()
                .host(host)
                .courseName("course" + courseSeed)
                .courseSummary("summary" + courseSeed)
                .courseDescription("description" + courseSeed)
                .maxParticipantCount(SAMPLE_MAX_PARTICIPANT_CNT)
                .build();
    }

    public static FormCourseInfo createSampleFormInput(int seed) {
        return new FormCourseInfo.Builder()
                .courseName("course" + seed)
                .courseSummary("summary" + seed)
                .courseDescription("description" + seed)
                .maxParticipantCount(SAMPLE_MAX_PARTICIPANT_CNT)
                .build();
    }

}
