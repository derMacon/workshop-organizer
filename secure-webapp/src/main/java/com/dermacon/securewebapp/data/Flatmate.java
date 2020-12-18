package com.dermacon.securewebapp.data;

import org.hibernate.annotations.Cascade;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Flatmate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long flatmateId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    private String firstname;

    @Cascade(value=org.hibernate.annotations.CascadeType.ALL)
    private String surname;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthday;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "living_space_id")
    private LivingSpace livingSpace;

    @ManyToMany (mappedBy = "responsibleFlatmates")
    private Set<Task> tasks = new HashSet<>();

    public Flatmate() {}

    public Flatmate(User user, String firstname, String surname, Date birthday, LivingSpace livingSpace) {
        this.user = user;
        this.firstname = firstname;
        this.surname = surname;
        this.birthday = birthday;
        this.livingSpace = livingSpace;
    }

    public long getFlatmateId() {
        return flatmateId;
    }

    public void setFlatmateId(long flatmateId) {
        this.flatmateId = flatmateId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public LivingSpace getLivingSpace() {
        return livingSpace;
    }

    public void setLivingSpace(LivingSpace livingSpace) {
        this.livingSpace = livingSpace;
    }

    @Override
    public String toString() {
        return "Flatmate{" +
                "flatmateId=" + flatmateId +
                ", user=" + user +
                ", firstname='" + firstname + '\'' +
                ", surname='" + surname + '\'' +
                ", birthday=" + birthday +
                ", livingSpace=" + livingSpace +
                '}';
    }
}
