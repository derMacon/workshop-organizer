package com.dermacon.securewebapp.data;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Entity
public class Announcement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long announcementId;

    private String title;

    private String content;

    @Temporal(TemporalType.DATE)
    private Date publishingDate;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "course_id")
    private Course course;


    public Announcement() {}

    public Announcement(String title, String content, Date publishingDate, Course course) {
        this.title = title;
        this.content = content;
        this.publishingDate = publishingDate;
        this.course = course;
    }

    public long getAnnouncementId() {
        return announcementId;
    }

    public void setAnnouncementId(long announcementId) {
        this.announcementId = announcementId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getPublishingDate() {
        return publishingDate;
    }

    public void setPublishingDate(Date publishingDate) {
        this.publishingDate = publishingDate;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Announcement other = (Announcement) o;
        return this.title.equalsIgnoreCase(other.title)
                && this.content.equalsIgnoreCase(other.content)
                && this.publishingDate.equals(other.publishingDate)
                && this.course.equals(other.course);
    }

    @Override
    public int hashCode() {
        return title.hashCode()
                * content.hashCode()
                * publishingDate.hashCode()
                * course.hashCode();
    }

    @Override
    public String toString() {
        return "Announcement{" +
                "announcementId=" + announcementId +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", publishingDate=" + publishingDate +
                '}';
    }
}
