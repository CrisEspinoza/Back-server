package com.example.Repositories;

import com.example.Entities.Club;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClubRepository extends JpaRepository<Club,Long> {

    Club findClubById(Long id);
}
