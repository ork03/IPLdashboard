package com.ipl.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.ipl.model.Matches;

public interface MatchRepository extends JpaRepository<Matches, Long>{
	List<Matches> findByTeam1OrTeam2OrderByDateDesc(String teamName1,String teamName2,Pageable page);
	@Query("select m from Matches m where (m.team1=:teamName or m.team2 =:teamName) "
			+ "and m.date between :startDate and :endDate Order By date desc")
	List<Matches> getMatchesByTeamBetweenDates(@Param("teamName")String teamName,
			@Param("startDate") LocalDate startDate,
			@Param("endDate") LocalDate endDate);
//	List<Matches> findByTeam1AndDateBetweenOrTeam2AndDateBetween(
//			String teamName1,LocalDate date1,LocalDate date2,
//			String teamName2,LocalDate date3,LocalDate date4
//);
	default List<Matches> findLatestMatchesbyTeam(String teamName,int count){
		return findByTeam1OrTeam2OrderByDateDesc(teamName,teamName,PageRequest.of(0, count));
	}
	@Query("SELECT m.season FROM Matches m WHERE m.team1 = :teamName1 OR m.team2 = :teamName2 GROUP BY m.season")
	List<String> findByTeam1OrTeam2GroupBySeason(@Param("teamName1") String teamName1, @Param("teamName2") String teamName2);
}
