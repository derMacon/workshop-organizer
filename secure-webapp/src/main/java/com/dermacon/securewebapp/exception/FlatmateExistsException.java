package com.dermacon.securewebapp.exception;

public class FlatmateExistsException extends Exception {

    private InputPerson person;

    public FlatmateExistsException(InputPerson person, Flatmate fm) {
        super("Person (" + person + ") already exists as flatmate (" + fm + ")");
    }

    public FlatmateExistsException() {}

    public String getGeneralDescription() {
        return "flatmate (" + person.getFirstname() + ") already exists.";
    }

}
