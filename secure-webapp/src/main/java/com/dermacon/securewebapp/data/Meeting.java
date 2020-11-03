package com.dermacon.securewebapp.data;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Entity
public class Meeting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long meetingId;

    @Temporal(TemporalType.DATE)
    private Date meetingDate;

    private String address;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "course_id")
    private Course course;

    public Meeting() {
    }

    public Meeting(Date meetingDate, String address, Course course) {
        this.meetingDate = meetingDate;
        this.address = address;
        this.course = course;
    }

    public long getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(long meetingId) {
        this.meetingId = meetingId;
    }

    public Date getMeetingDate() {
        return meetingDate;
    }

    public void setMeetingDate(Date meetingDate) {
        this.meetingDate = meetingDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    @Override
    public String toString() {
        return "Meeting{" +
                "meetingId=" + meetingId +
                ", meetingDate=" + meetingDate +
                ", address='" + address + '\'' +
                ", course=" + course +
                '}';
    }
}
