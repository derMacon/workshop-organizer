package com.dermacon.securewebapp.data;

import com.dermacon.securewebapp.data.Course;
import com.dermacon.securewebapp.data.User;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@Entity
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long personId;

    private String firstname;
    private String surname;
    private String email;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany(mappedBy = "participants")
    private Set<Course> courses;

    public static class Builder {
        private String firstname;
        private String surname;
        private String email;
        private User user;
        private Set<Course> courses;

        public Builder firstname(String firstname) {
            this.firstname = firstname;
            return this;
        }

        public Builder surname(String surname) {
            this.surname = surname;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder user(User user) {
            this.user = user;
            return this;
        }

        public Builder courses(Set<Course> courses) {
            this.courses = courses;
            return this;
        }

        public Person build() {
            return new Person(this);
        }

    }

    public Person(Builder b) {
        this.firstname = b.firstname;
        this.surname = b.surname;
        this.email = b.email;
        this.user = b.user;
    }

    public Person() {
    }

    public Person(String firstname, String surname, String email, User user) {
        this.firstname = firstname;
        this.surname = surname;
        this.email = email;
        this.user = user;
    }

    public long getPersonId() {
        return personId;
    }

    public void setPersonId(long personId) {
        this.personId = personId;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Course> getCourses() {
        return courses;
    }

    public void setCourses(Set<Course> courses) {
        this.courses = courses;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Person other = (Person) o;

        return this.firstname.equals(other.firstname)
                && this.surname.equals(other.surname)
                && this.email.equals(other.email)
                && this.user.equals(other.user)
                && coursesEqual(this.courses, other.courses);
    }

    private boolean coursesEqual(Set<Course> thisCourse, Set<Course> otherCourse) {
        if (thisCourse == null) {
            return otherCourse == null;
        }
        if (otherCourse == null) {
            return thisCourse == null;
        }

        boolean out = thisCourse.size() == otherCourse.size();
        Iterator<Course> this_iterator = thisCourse.iterator();

        while (out && this_iterator.hasNext()) {
            out = otherCourse.contains(this_iterator.next());
        }

        return out;
    }



    @Override
    public int hashCode() {
        int result = firstname != null ? firstname.hashCode() : 0;
        result = 31 * result + (surname != null ? surname.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (user != null ? user.hashCode() : 0);
        result = 31 * result + (courses != null ? courses.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Person{" +
                "personId=" + personId +
                ", firstname='" + firstname + '\'' +
                ", surname='" + surname + '\'' +
                ", email='" + email + '\'' +
                ", user=" + user +
                '}';
    }
}
