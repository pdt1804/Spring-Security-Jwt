package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entities.UserApp;

public interface UserRepository extends JpaRepository<UserApp, String>{

}
