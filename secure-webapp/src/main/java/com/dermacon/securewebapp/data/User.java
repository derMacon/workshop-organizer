package com.dermacon.securewebapp.data;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.Set;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userId;

    @Column(unique = true)
    private String username;

    private String password;

//    @ElementCollection(targetClass=AppRole.class, fetch = FetchType.EAGER)
//    @Enumerated(EnumType.STRING)
////    @Column(name = "user_role")
//private Set<AppRole> roles;

    @OneToMany(fetch = FetchType.EAGER)
    @Column(name="role_id")
    private Set<AppRole> roleId;

    protected User() {
    }

    public User(String username, String password, Set<AppRole> roleId) {
        this.username = username;
        this.password = password;
        this.roleId = roleId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
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

    public Set<AppRole> getRoleId() {
        return roleId;
    }

    public void setRoleId(Set<AppRole> roleId) {
        this.roleId = roleId;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", roleId=" + roleId +
                '}';
    }
}
