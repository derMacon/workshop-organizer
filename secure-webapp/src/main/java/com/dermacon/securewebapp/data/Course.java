package com.dermacon.securewebapp.data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long courseId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "host_id")
    private Person host;


    @OneToMany(mappedBy="course")
    private Set<Announcement> announcements;

    private String courseName;
    private String courseSummary;
    private String courseDescription;
    private int maxParticipantCount;

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "Course_Person",
            joinColumns = { @JoinColumn(name = "course_id")},
            inverseJoinColumns = { @JoinColumn(name = "person_id")}
    )
    private Set<Person> participants;

    public Course() {
        this.participants = new HashSet<>();
    }

    public Course(Person host, Set<Announcement> announcements, String courseName, String courseSummary, String courseDescription, int maxParticipantCount, Set<Person> participants) {
        this.host = host;
        this.announcements = announcements;
        this.courseName = courseName;
        this.courseSummary = courseSummary;
        this.courseDescription = courseDescription;
        this.maxParticipantCount = maxParticipantCount;
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

    public Set<Announcement> getAnnouncements() {
        return announcements;
    }

    public void setAnnouncements(Set<Announcement> announcements) {
        this.announcements = announcements;
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

    public int getMaxParticipantCount() {
        return maxParticipantCount;
    }

    public void setMaxParticipantCount(int maxParticipantCount) {
        this.maxParticipantCount = maxParticipantCount;
    }

    public Set<Person> getParticipants() {
        return participants;
    }

    public void setParticipants(Set<Person> participants) {
        this.participants = participants;
    }

    public void addNewParticipant(Person person) {
        this.participants.add(person);
    }

    public void removeParticipant(Person person) {
        this.participants.remove(person);
    }

    @Override
    public String toString() {
        return "Course{" +
                "courseId=" + courseId +
                ", host=" + host +
                ", announcements=" + announcements +
                ", courseName='" + courseName + '\'' +
                ", courseSummary='" + courseSummary + '\'' +
                ", courseDescription='" + courseDescription + '\'' +
                ", maxParticipantCount=" + maxParticipantCount +
                ", participants=" + participants +
                '}';
    }
}