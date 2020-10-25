package com.dermacon.securewebapp.security.user;

import com.dermacon.securewebapp.data.AppRole;
import com.dermacon.securewebapp.data.User;
import com.dermacon.securewebapp.data.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * Source:
 * - https://github.com/ajkr195/springbootrocks
 * - https://stackoverflow.com/questions/37615034/spring-security-spring-boot-how-to-set-roles-for-users
 */
@Service
public class AuthenticatedUserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("The user " + username + " does not exist.");
        }

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        for (AppRole role : user.getRoleId()) {
            grantedAuthorities.add(new SimpleGrantedAuthority(role.getRoleName()));
        }

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                grantedAuthorities
        );
    }
}
