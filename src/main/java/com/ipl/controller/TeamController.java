package com.ipl.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ipl.model.Matches;
import com.ipl.model.Teams;
import com.ipl.repository.MatchRepository;
import com.ipl.repository.TeamRepository;
@CrossOrigin("*") 
@RestController
@RequestMapping("/team")
public class TeamController {
	@Autowired
	private TeamRepository teamRepository;
	@Autowired
    private MatchRepository matchRepository;
	 
    @GetMapping
    public ResponseEntity<List<Teams>> getAllTeam() {
    	List<Teams> team = teamRepository.findAll();
        return ResponseEntity.ok(team);
    }

    @GetMapping("{teamName}")
    public ResponseEntity<Teams> getTeam(@PathVariable String teamName) {
    	
        Teams team = teamRepository.findByTeamName(teamName);
        team.setMatches(matchRepository.findLatestMatchesbyTeam(teamName,4));
        team.setAllYearsPlayed(matchRepository.findByTeam1OrTeam2GroupBySeason(teamName, teamName));
        return ResponseEntity.ok(team);
    }	
    @GetMapping("{teamName}/Matches")
    public ResponseEntity<List<Matches>> getAllMatches(@PathVariable String teamName,@RequestParam int year) {
    	LocalDate date1 =LocalDate.of(year, 1, 1);
    	LocalDate date2 =LocalDate.of(year+1, 1, 1);
    	List<Matches> team = matchRepository.getMatchesByTeamBetweenDates(
    			teamName,date1,date2);
        return ResponseEntity.ok(team);
    }
}
