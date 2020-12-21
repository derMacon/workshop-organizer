package com.dermacon.securewebapp.data.formInput;

public class FormSignupInfo {

    private String email;
    private String username;
    private String password;
    private String firstname;
    private String surname;

    public FormSignupInfo() {
    }

    public FormSignupInfo(String email, String username, String password, String firstname, String surname) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.firstname = firstname;
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    @Override
    public String toString() {
        return "FormSignupInfo{" +
                "email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", firstname='" + firstname + '\'' +
                ", surname='" + surname + '\'' +
                '}';
    }
}
