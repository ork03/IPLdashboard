package com.ipl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ipl.model.Teams;
public interface TeamRepository extends JpaRepository<Teams, Long>  {
    Teams findByTeamName(String teamName1);
}
