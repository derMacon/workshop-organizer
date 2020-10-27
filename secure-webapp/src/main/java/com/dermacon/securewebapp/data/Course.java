package com.dermacon.securewebapp.data;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long courseId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "host_id")
    private Person host;

    private String courseName;
    private String courseSummary;
    private String courseDescription;
    private int participantCount;

    public Course() {
    }

    public Course(Person host, String courseName, String courseSummary, String courseDescription, int participantCount) {
        this.host = host;
        this.courseName = courseName;
        this.courseSummary = courseSummary;
        this.courseDescription = courseDescription;
        this.participantCount = participantCount;
    }

    public long getCourseId() {
        return courseId;
    }

    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }

    public Person getHost() {
        return host;
    }

    public void setHost(Person host) {
        this.host = host;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseSummary() {
        return courseSummary;
    }

    public void setCourseSummary(String courseSummary) {
        this.courseSummary = courseSummary;
    }

    public String getCourseDescription() {
        return courseDescription;
    }

    public void setCourseDescription(String courseDescription) {
        this.courseDescription = courseDescription;
    }

    public int getParticipantCount() {
        return participantCount;
    }

    public void setParticipantCount(int participantCount) {
        this.participantCount = participantCount;
    }

    @Override
    public String toString() {
        return "Course{" +
                "courseId=" + courseId +
                ", host=" + host +
                ", courseName='" + courseName + '\'' +
                ", summary='" + courseSummary + '\'' +
                ", courseDescription='" + courseDescription + '\'' +
                ", participantCount=" + participantCount +
                '}';
    }
}
