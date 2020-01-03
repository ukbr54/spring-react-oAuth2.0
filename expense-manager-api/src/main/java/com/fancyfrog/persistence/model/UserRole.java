package com.fancyfrog.persistence.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Ujjwal Gupta on Jan,2020
 */

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "USER_ROLE")
@Entity(name = "UserRole")
public class UserRole {

    public static class Id implements Serializable {

        @Column(name = "USER_ID")
        protected Long userId;

        @Column(name = "ROLE")
        @Enumerated(EnumType.STRING)
        protected Role role;

        public Id() { }

        public Id(Long userId, Role role) {
            this.userId = userId;
            this.role = role;
        }
    }

    @JsonIgnore
    @EmbeddedId
    Id id = new Id();

    @Enumerated(EnumType.STRING)
    @Column(name = "ROLE",insertable = false,updatable = false)
    protected Role role;
}
