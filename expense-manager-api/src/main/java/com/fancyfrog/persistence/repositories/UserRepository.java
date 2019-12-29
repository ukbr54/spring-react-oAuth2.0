package com.fancyfrog.persistence.repositories;

import com.fancyfrog.persistence.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Created by Ujjwal Gupta on Dec,2019
 */

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);
}
