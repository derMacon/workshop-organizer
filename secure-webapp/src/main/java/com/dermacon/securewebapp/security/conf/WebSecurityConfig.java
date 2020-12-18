package com.dermacon.securewebapp.security.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;
import java.util.concurrent.TimeUnit;

@EnableWebSecurity
public class WebSecurityConfig {

    /**
     * Https endpoint for api requests.
     * Uses basic authentication.
     */
    @Configuration
    @Order(1)
    public static class ApiWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {
        protected void configure(HttpSecurity http) throws Exception {
            // todo permit /api/admin/ only for admin users
            http
                    .csrf().disable()
                    .antMatcher("/api/**")
                        .authorizeRequests()
                        .anyRequest()
                        .hasAnyRole("USER", "ADMIN")
                    .and()
                    .httpBasic();
        }
    }


    /**
     * Http endpoint for standard get requests by the browser to display the website.
     * Uses form login for authentication
     */
    @Configuration
    public static class FormLoginWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {


        @Autowired
        private UserDetailsService userDetailsService;

        @Autowired
        @Qualifier("dataSource")
        private DataSource dataSource;

        /**
         * source: http://cristianruizblog.com/spring-security-persistent-token-2/
         *
         * @return
         */
        @Bean
        public PersistentTokenRepository persistentTokenRepository() {
            JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
            tokenRepository.setDataSource(dataSource);
            return tokenRepository;
        }


        @Override
        public void configure(HttpSecurity http) throws Exception {
            http
                    .authorizeRequests()
                    .antMatchers("/noSecurity", "/css/**").permitAll()
                    .antMatchers("/test").hasRole("ADMIN")
                    .anyRequest()
                    .authenticated()
                    .and()
                    .formLogin()
                    .permitAll()
                    .and()
                    .rememberMe()
                    .tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(100))
//                    .tokenValiditySeconds((int)60)
                    .tokenRepository(persistentTokenRepository())
                    .userDetailsService(userDetailsService)
                    // random key, todo put into application.properties
                    .key("45lk432j5;lj435;l432j");
        }

        @Autowired
        public void globalSecurityConfiguration(AuthenticationManagerBuilder auth) throws Exception {
            auth.userDetailsService(userDetailsService);
        }

        @Bean
        public UserDetailsService userDetailsService() {
            return super.userDetailsService();
        }


        // ---------- hashing passwords -----------

        @Bean
        public static PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }

        @Bean
        public DaoAuthenticationProvider authProvider() {
            DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
            authProvider.setUserDetailsService(userDetailsService);
            authProvider.setPasswordEncoder(passwordEncoder());
            return authProvider;
        }

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.authenticationProvider(authProvider());
        }
    }
}
