package com.example.Repositories;


import com.example.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {

    User  findByEmail(String email);

}
