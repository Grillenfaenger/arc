package de.uni_koeln.spinfo.arc.editor.server.persistance.config;

import java.net.UnknownHostException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;

import de.uni_koeln.spinfo.arc.editor.server.persistance.repository.PageRangeDaoRepository;
import de.uni_koeln.spinfo.arc.editor.server.persistance.repository.WordDaoRepository;
import de.uni_koeln.spinfo.arc.editor.server.persistance.repository.WordImplDaoRepository;
import de.uni_koeln.spinfo.arc.editor.server.persistance.repository.WorkingUnitDaoRepository;
import de.uni_koeln.spinfo.arc.editor.server.persistance.repository.WorkingUnitImplDaoRepository;

@Configuration
@EnableMongoRepositories(basePackages = "de.spinfo.arc.persistance", includeFilters = @ComponentScan.Filter(value = {
		WorkingUnitDaoRepository.class, WorkingUnitImplDaoRepository.class,
		WordImplDaoRepository.class, WordDaoRepository.class,
		PageRangeDaoRepository.class }, type = FilterType.ASSIGNABLE_TYPE))
public class MongoConfiguration {
	private static String DB_UNDER_TEST = "arc_working_units";

	public @Bean MongoTemplate mongoTemplate(Mongo mongo)
			throws UnknownHostException {
		return new MongoTemplate(mongo, DB_UNDER_TEST);
	}

	public @Bean Mongo mongo() throws UnknownHostException {
		return new MongoClient("localhost");
	}
}