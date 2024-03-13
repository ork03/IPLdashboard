package com.ipl.data;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.internal.build.AllowSysOut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.ipl.model.Teams;
import com.ipl.repository.TeamRepository;

import ch.qos.logback.core.net.SyslogOutputStream;

@Component
public class JobCompletionNotificationListener implements JobExecutionListener {

    private static final Logger log = LoggerFactory.getLogger(JobCompletionNotificationListener.class);

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JobCompletionNotificationListener(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            Map<String, Teams> teamData = new HashMap<>();

            jdbcTemplate.query("SELECT team1, COUNT(*) FROM matches GROUP BY team1", (rs, rowNum) -> {
                Teams team = new Teams(rs.getString(1), rs.getLong(2));
                teamData.putIfAbsent(team.getTeamName(), team);
                return null;
            });

            jdbcTemplate.query("SELECT team2, COUNT(*) FROM matches GROUP BY team2", (rs, rowNum) -> {
                Teams team = teamData.get(rs.getString(1));
                if (team != null) team.setTotalMatches(team.getTotalMatches() + rs.getLong(2));
                return null;
            });

            jdbcTemplate.query("SELECT winning_team, COUNT(*) FROM matches GROUP BY winning_team", (rs, rowNum) -> {
                Teams team = teamData.get(rs.getString(1));
                if (team != null) team.setTotalWins(rs.getLong(2));
                return null;
            });

            teamData.values().forEach(team -> {
            	jdbcTemplate.update("INSERT INTO teams (team_name, total_matches, total_wins) VALUES (?, ?, ?)", team.getTeamName(), team.getTotalMatches(), team.getTotalWins());

            	jdbcTemplate.update("UPDATE teams SET total_matches = total_matches + ?, total_wins = total_wins + ? WHERE team_name = ?",
            	        team.getTotalMatches(), team.getTotalWins(), team.getTeamName());

            });

            teamData.values().forEach(team -> System.out.println(team));
        } else {
            System.out.println("Not Done");
        }
    }
}
