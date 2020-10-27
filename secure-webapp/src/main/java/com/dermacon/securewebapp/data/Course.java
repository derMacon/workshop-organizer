package com.dermacon.securewebapp.data;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import java.util.Set;

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

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "Course_Person",
            joinColumns = { @JoinColumn(name = "course_id")},
            inverseJoinColumns = { @JoinColumn(name = "person_id")}
    )
    private Set<Person> participants;

    public Course() {
    }

    public Course(Person host, String courseName, String courseSummary, String courseDescription, int participantCount, Set<Person> participants) {
        this.host = host;
        this.courseName = courseName;
        this.courseSummary = courseSummary;
        this.courseDescription = courseDescription;
        this.participantCount = participantCount;
        this.participants = participants;
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

    public Set<Person> getParticipants() {
        return participants;
    }

    public void setParticipants(Set<Person> participants) {
        this.participants = participants;
    }

    @Override
    public String toString() {
        return "Course{" +
                "courseId=" + courseId +
                ", host=" + host +
                ", courseName='" + courseName + '\'' +
                ", courseSummary='" + courseSummary + '\'' +
                ", courseDescription='" + courseDescription + '\'' +
                ", participantCount=" + participantCount +
                ", participants=" + participants +
                '}';
    }
}
