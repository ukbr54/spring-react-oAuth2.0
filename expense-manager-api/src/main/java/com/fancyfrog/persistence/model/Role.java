package com.fancyfrog.persistence.model;

/**
 * Created by Ujjwal Gupta on Jan,2020
 */
public enum Role {

    ADMIN,USER,MANAGER;

    public String authority(){
        return "ROLE_" + this.name();
    }
}
