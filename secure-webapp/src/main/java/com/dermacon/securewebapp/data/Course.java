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
import javax.persistence.OneToMany;
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


    @OneToMany(mappedBy = "course")
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


    public static class Builder {

        private Person host;
        private String courseName;
        private String courseSummary;
        private String courseDescription;
        private int maxParticipantCount;
        private Set<Announcement> announcements = new HashSet<>();
        private Set<Person> participants = new HashSet<>();

        public Builder host(Person host) {
            this.host = host;
            return this;
        }

        public Builder announcements(Set<Announcement> announcements) {
            this.announcements = announcements;
            return this;
        }

        public Builder courseName(String courseName) {
            this.courseName = courseName;
            return this;
        }

        public Builder courseSummary(String courseSummary) {
            this.courseSummary = courseSummary;
            return this;
        }

        public Builder courseDescription(String courseDescription) {
            this.courseDescription = courseDescription;
            return this;
        }

        public Builder maxParticipantCount(int maxParticipantCount) {
            this.maxParticipantCount = maxParticipantCount;
            return this;
        }

        public Builder participants(Set<Person> participants) {
            this.participants = participants;
            return this;
        }

        public Course build() {
            return new Course(this);
        }

    }

    private Course(Builder b) {
        this.host = b.host;
        this.announcements = b.announcements;
        this.courseName = b.courseName;
        this.courseSummary = b.courseSummary;
        this.courseDescription = b.courseDescription;
        this.maxParticipantCount = b.maxParticipantCount;
        this.participants = b.participants;
    }

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Course other = (Course) o;
        return this.host.equals(other.host)
                && setsEqual(this.announcements, other.announcements)
                && this.courseName.equalsIgnoreCase(other.courseName)
                && this.courseSummary.equalsIgnoreCase(other.courseSummary)
                && this.courseDescription.equalsIgnoreCase(other.courseDescription)
                && this.maxParticipantCount == other.maxParticipantCount
                && setsEqual(this.participants, other.participants);
    }

    @Override
    public int hashCode() {
        return host.hashCode()
                * announcements.hashCode()
                * courseName.hashCode()
                * courseSummary.hashCode()
                * courseDescription.hashCode()
                * maxParticipantCount
                * participants.hashCode();
    }

    /**
     * Nullpointer safe check if the sets are equal
     * @param fstSet first set to check
     * @param sndSet second set to check
     * @param <T> Type of the set elements
     * @return true the sets carry the same amount and instances of elements
     */
    private <T> boolean setsEqual(Set<T> fstSet, Set<T> sndSet) {
        if (fstSet == null) {
            return sndSet == null;
        }
        if (sndSet == null) {
            return fstSet == null;
        }

        return fstSet.size() == sndSet.size()
                && fstSet.containsAll(sndSet);
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
