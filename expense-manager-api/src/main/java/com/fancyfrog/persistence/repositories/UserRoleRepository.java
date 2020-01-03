package com.fancyfrog.persistence.repositories;

import com.fancyfrog.persistence.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Ujjwal Gupta on Jan,2020
 */
public interface UserRoleRepository extends JpaRepository<UserRole,UserRole.Id> {
}
