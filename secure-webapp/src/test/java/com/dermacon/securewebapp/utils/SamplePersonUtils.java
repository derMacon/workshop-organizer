package com.dermacon.securewebapp.utils;

import com.dermacon.securewebapp.data.Person;
import com.dermacon.securewebapp.data.User;
import com.dermacon.securewebapp.data.UserRole;
import com.dermacon.securewebapp.data.formInput.FormSignupInfo;

public class SamplePersonUtils {

    private static final String SAMPLE_PASSWORD_PLAIN = "password";
    // the plain pw after putting it through the bcrypt algorithm
    private static final String SAMPLE_PASSWORD_ENCODED =
            "$2a$10$1JT96p9Nge3K7mjkLqKmDO0o5t/wvb2SCGIQGDEApkOIy0MP1vkze";

    public static User createSampleUser(int seed) {
        return new User.Builder()
                .username("user" + seed)
                .password(SAMPLE_PASSWORD_ENCODED)
                .role(UserRole.ROLE_USER)
                .build();
    }

    public static Person createSamplePerson(int id) {
        User user = createSampleUser(id);
        return new Person.Builder()
                .email("mail" + id + "@mail.com")
                .firstname("firstname" + id)
                .surname("surname" + id)
                .user(user)
                .build();
    }

    public static FormSignupInfo createSampleFormSignupInfo(int id) {
        return createSampleFormSignupInfo(createSamplePerson(id));
    }

    public static FormSignupInfo createSampleFormSignupInfo(Person person) {
        return new FormSignupInfo.Builder()
                .email(person.getEmail())
                .username(person.getUser().getUsername())
                .password(SAMPLE_PASSWORD_PLAIN)
                .firstname(person.getFirstname())
                .surname(person.getSurname())
                .build();
    }

}
