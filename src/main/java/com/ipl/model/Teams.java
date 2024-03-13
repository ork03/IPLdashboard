package com.ipl.model;

import java.util.List;

import jakarta.persistence.*;



@Entity
public class Teams {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String teamName;
    private long totalMatches;
    private long totalWins;
    
    @Transient
    private List<Matches> matches;
    @Transient
    private List<String> allYearsPlayed;

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getTeamName() {
        return teamName;
    }
    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }
    public long getTotalMatches() {
        return totalMatches;
    }
    public void setTotalMatches(long totalMatches) {
        this.totalMatches = totalMatches;
    }
    public long getTotalWins() {
        return totalWins;
    }
    public void setTotalWins(long totalWins) {
        this.totalWins = totalWins;
    }
    public Teams(String teamName, long totalMatches) {
        this.teamName = teamName;
        this.totalMatches = totalMatches;
    }
    @Override
    public String toString() {
        return "Team [teamName=" + teamName + ", totalMatches=" + totalMatches + ", totalWins=" + totalWins + "]";
    }
    public Teams(String teamName) {
        this.teamName =teamName;
    }
    public Teams() {
    }
    public List<Matches> getMatches() {
        return matches;
    }
    public void setMatches(List<Matches> matches) {
        this.matches = matches;
    }
	public List<String> getAllYearsPlayed() {
		return allYearsPlayed;
	}
	public void setAllYearsPlayed(List<String> allYearsPlayed) {
		this.allYearsPlayed = allYearsPlayed;
	}
	
    
    
    
    
    

    
}
