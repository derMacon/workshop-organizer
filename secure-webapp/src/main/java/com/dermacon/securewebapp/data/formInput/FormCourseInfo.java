package com.dermacon.securewebapp.data.formInput;

/**
 * Simple POJO that will be filled with the necessary information in the thymeleaf template for
 * creating a new course
 */
public class FormCourseInfo {
    private String courseName;
    private String courseSummary;
    private String courseDescription;
    private int maxParticipantCount;

    public FormCourseInfo() {
    }

    public FormCourseInfo(String courseName, String courseSummary, String courseDescription, int maxParticipantCount) {
        this.courseName = courseName;
        this.courseSummary = courseSummary;
        this.courseDescription = courseDescription;
        this.maxParticipantCount = maxParticipantCount;
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

    @Override
    public String toString() {
        return "FormCourseInfo{" +
                "courseName='" + courseName + '\'' +
                ", courseSummary='" + courseSummary + '\'' +
                ", courseDescription='" + courseDescription + '\'' +
                ", maxParticipantCount=" + maxParticipantCount +
                '}';
    }
}
