package com.trackerapp.repository;

import com.trackerapp.model.User;
import com.trackerapp.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByEmpId(String empId);
    boolean existsByEmail(String email);
    boolean existsByEmpId(String empId);
    Optional<User> findByEmailAndRole(String email, Role role);
}