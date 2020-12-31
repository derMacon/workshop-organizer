package com.dermacon.securewebapp.data.formInput;

public class FormSignupInfo {

    private String email;
    private String username;
    private String password;
    private String firstname;
    private String surname;


    public static class Builder {

        private String email;
        private String username;
        private String password;
        private String firstname;
        private String surname;

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder firstname(String firstname) {
            this.firstname = firstname;
            return this;
        }

        public Builder surname(String surname) {
            this.surname = surname;
            return this;
        }

        public FormSignupInfo build() {
            return new FormSignupInfo(this);
        }

    }

    public FormSignupInfo() {}

    private FormSignupInfo(Builder b) {
        this.email = b.email;
        this.username = b.username;
        this.password = b.password;
        this.firstname = b.firstname;
        this.surname = b.surname;
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
