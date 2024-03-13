  package com.ipl.data;

import java.time.LocalDate;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;


import com.ipl.model.Matches;




@Configuration
public class BatchProcessor {
	private final String names[]=new String[]{
		"ID","City","Date","Season","MatchNumber","Team1","Team2","Venue","TossWinner","TossDecision",
		"SuperOver","WinningTeam","WonBy","Margin","method","Player_of_Match","Team1Players","Team2Players","Umpire1","Umpire2"
	};
	@Bean
    public DataSourceTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
	@Bean
	public MatchDataProcessor processor() {
		return new MatchDataProcessor();
	}
	@Bean
	public FlatFileItemReader<MatchData> reader() {
	  return new FlatFileItemReaderBuilder<MatchData>()
	    .name("matchInputReader")
	    .resource(new ClassPathResource("ipl-data.csv"))
	    .delimited()
	    .names(names)
	    .targetType(MatchData.class)
	    .build();
	}
	@Bean
	public JdbcBatchItemWriter<Matches> writer(DataSource dataSource) {
	  return new JdbcBatchItemWriterBuilder<Matches>()
	    .sql("INSERT INTO matches (id,city,date,season,match_number,team1,team2,venue,"
	    		+ "toss_winner,toss_decision,super_over,winning_team,won_by,margin,method,player_of_match,umpire1,umpire2"
	    		+ ") VALUES (:id,:city,:date,:season,:matchNumber,:team1,:team2,:venue,:tossWinner,:tossDecision,:superOver,:winningTeam,"
	    		+ ":wonBy,:margin,:method,:playerOfMatch,:umpire1,:umpire2)")
	    .dataSource(dataSource)
	    .beanMapped()
	    .build();
	}
	@Bean
	public Job importUserJob(JobRepository jobRepository,Step step1, JobCompletionNotificationListener listener) {
	  return new JobBuilder("importUserJob", jobRepository)
	    .listener(listener)
	    .start(step1)
	    .build();
	}

	@Bean
	public Step step1(JobRepository jobRepository, DataSourceTransactionManager transactionManager,
	          FlatFileItemReader<MatchData> reader, MatchDataProcessor processor, JdbcBatchItemWriter<Matches> writer) {
	  return new StepBuilder("step1", jobRepository)
	    .<MatchData, Matches> chunk(3, transactionManager)
	    .reader(reader)
	    .processor(processor)
	    .writer(writer)
	    .build();
	}
}
