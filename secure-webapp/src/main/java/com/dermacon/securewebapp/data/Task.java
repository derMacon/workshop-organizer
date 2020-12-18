package com.dermacon.securewebapp.data;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long task_id;

    private String taskTitle;

    private String description;

    private Boolean taskStatus;

    @Temporal(TemporalType.DATE)
    private Date publishingDate;

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "Task_Flatmate",
            joinColumns = { @JoinColumn(name = "task_id")},
            inverseJoinColumns = { @JoinColumn(name = "flatmate_id")}
    )
    Set<Flatmate> responsibleFlatmates = new HashSet<>();

    public Task() {
        this.taskStatus = null;
        this.publishingDate = new Date(System.currentTimeMillis());
    }

    public Task(String taskTitle, String description, Boolean taskStatus, Date publishingDate, Set<Flatmate> responsibleFlatmates) {
        this.taskTitle = taskTitle;
        this.description = description;
        this.taskStatus = taskStatus;
        this.publishingDate = publishingDate;
        this.responsibleFlatmates = responsibleFlatmates;
    }

    public long getTask_id() {
        return task_id;
    }

    public void setTask_id(long task_id) {
        this.task_id = task_id;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(Boolean taskStatus) {
        this.taskStatus = taskStatus;
    }

    public Date getPublishingDate() {
        return publishingDate;
    }

    public void setPublishingDate(Date publishingDate) {
        this.publishingDate = publishingDate;
    }

    public Set<Flatmate> getResponsibleFlatmates() {
        return responsibleFlatmates;
    }

    public void setResponsibleFlatmates(Set<Flatmate> responsibleFlatmates) {
        this.responsibleFlatmates = responsibleFlatmates;
    }

    @Override
    public String toString() {
        return "Task{" +
                "task_id=" + task_id +
                ", task_title='" + taskTitle + '\'' +
                ", description='" + description + '\'' +
                ", taskStatus=" + taskStatus +
                ", publishingDate=" + publishingDate +
                ", responsibleFlatmates=" + responsibleFlatmates +
                '}';
    }

}
