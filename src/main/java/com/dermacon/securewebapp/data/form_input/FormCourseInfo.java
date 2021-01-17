package com.dermacon.securewebapp.data.form_input;

/**
 * Simple POJO that will be filled with the necessary information in the thymeleaf template for
 * creating a new course
 */
public class FormCourseInfo {
    private String courseName;
    private String courseSummary;
    private String courseDescription;
    private int maxParticipantCount;

    public static class Builder {
        private String courseName;
        private String courseSummary;
        private String courseDescription;
        private int maxParticipantCount;

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

        public FormCourseInfo build() {
            return new FormCourseInfo(this);
        }
    }

    private FormCourseInfo(Builder b) {
        this.courseName = b.courseName;
        this.courseSummary = b.courseSummary;
        this.courseDescription = b.courseDescription;
        this.maxParticipantCount = b.maxParticipantCount;
    }

    public FormCourseInfo() {}

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
