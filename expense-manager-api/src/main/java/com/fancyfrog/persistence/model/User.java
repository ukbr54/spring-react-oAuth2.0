package com.fancyfrog.persistence.model;

import com.fancyfrog.persistence.AuthProvider;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

/**
 * Created by Ujjwal Gupta on Dec,2019
 */

@Data
@Entity(name = "User")
@Table(name = "users",uniqueConstraints = {
        @UniqueConstraint(columnNames = "email")
})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Email
    @Column(nullable = false)
    private String email;

    private String imageUrl;

    @JsonIgnore
    private String password;

    @NotNull
    @Enumerated(EnumType.STRING)
    private AuthProvider provider;

    private String providerId;

    public User() { }

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "USER_ID",referencedColumnName = "ID")
    private List<UserRole> roles;

    public User(String name, @Email String email, String password, @NotNull AuthProvider provider) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.provider = provider;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }
}
