package com.dermacon.securewebapp.data;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * Class used by the form or api input.
 * This class will be used to get the user input, it will be converted to a
 * complete flatmate instance that will be saved in the database
 */
public class InputPerson {

    private String firstname;

    private String surname;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthday;

    private long livingSpaceId;

    public InputPerson() {}

    public InputPerson(String firstname, String surname, Date birthday, long livingSpaceId) {
        this.firstname = firstname;
        this.surname = surname;
        this.birthday = birthday;
        this.livingSpaceId = livingSpaceId;
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

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public long getLivingSpaceId() {
        return livingSpaceId;
    }

    public void setLivingSpaceId(long livingSpaceId) {
        this.livingSpaceId = livingSpaceId;
    }

    @Override
    public String toString() {
        return "InputPerson{" +
                "firstname='" + firstname + '\'' +
                ", surname='" + surname + '\'' +
                ", birthday=" + birthday +
                ", livingSpaceId=" + livingSpaceId +
                '}';
    }
}

