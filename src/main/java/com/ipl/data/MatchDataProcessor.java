package com.ipl.data;

import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.batch.item.ItemProcessor;

import com.ipl.model.Matches;

public class MatchDataProcessor implements ItemProcessor<MatchData, Matches>{


	  private static final Logger log = LoggerFactory.getLogger(MatchDataProcessor.class);

	  @Override
	  public Matches process(final MatchData matchData) {
		  Matches mdt = new Matches();
		  mdt.setId(Long.parseLong(matchData.getID()));
		  mdt.setCity(matchData.getCity());
		  mdt.setDate(LocalDate.parse(matchData.getDate()));
		  mdt.setSeason(matchData.getSeason());
		  mdt.setMatchNumber(matchData.getMatchNumber());
		  String firstInningsTeam,secondInningsTeam;
		  if("bat".equals(matchData.getTossDecision())) {
			  firstInningsTeam = matchData.getTossWinner();
			  secondInningsTeam = matchData.getTossWinner().equals(matchData.getTeam1()) ? matchData.getTeam2() :matchData.getTeam1();
		  }
		  else {
			  secondInningsTeam = matchData.getTossWinner();
			  firstInningsTeam = matchData.getTossWinner().equals(matchData.getTeam1()) ? matchData.getTeam2() :matchData.getTeam1();
		  }
		  mdt.setTeam1(firstInningsTeam);
		  mdt.setTeam2(secondInningsTeam);
		  mdt.setVenue(matchData.getVenue());
		  mdt.setTossWinner(matchData.getTossWinner());
		  mdt.setTossDecision(matchData.getTossDecision());
		  mdt.setSuperOver(matchData.getSuperOver());
		  mdt.setWinningTeam(matchData.getWinningTeam());
		  mdt.setWonBy(matchData.getWonBy());
		  mdt.setMargin(matchData.getMargin());
		  mdt.setMethod(matchData.getMethod());
		  mdt.setPlayerOfMatch(matchData.getPlayer_of_Match());
		  mdt.setUmpire1(matchData.getUmpire1());
		  mdt.setUmpire2(matchData.getUmpire2());
		  
		  
		  return mdt;
	  }

}

