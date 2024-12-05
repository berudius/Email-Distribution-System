package com.InternShip.Repository;


import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import com.InternShip.Models.User;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    boolean existsByEmailAndIdNot(String email, Long id);
    User findFirstByIdGreaterThanOrderByCreatedOnAsc(Long id);

    Page<User> findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(String name, String email, Pageable pageable);
    Page<User> findByNameContainingIgnoreCaseAndEmailContainingIgnoreCase(String name, String email, Pageable pageable);
} 
