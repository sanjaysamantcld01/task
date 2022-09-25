package com.soenergy.task.repository;


import java.io.File;
import java.util.List;
import java.util.OptionalLong;
import java.util.Random;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter.Indenter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.soenergy.task.beans.Beer;
import com.soenergy.task.exception.DataNotFoundException;
import com.soenergy.task.exception.RepositoryException;
import com.soenergy.task.util.TaskExceptionHandler;

@Repository
public class BeerRepository {


	private final RestTemplateBuilder restTemplateBuilder;
	@Value("${punk.api.getbeers.url}")
	private String punkApiUrl;
	@Value("${local.beer.repository.path}")
	private String localBeerRepositoryPath;
	@Value("${punk.beers.start.index}")
	private long punkBeersStartIndex;
	
	private static final Logger logger=LoggerFactory.getLogger(TaskExceptionHandler.class);
	
	
	public BeerRepository(RestTemplateBuilder restTemplateBuilder) {
		this.restTemplateBuilder = restTemplateBuilder;
	}

	private List<Beer> getBeersFromPunkAPI() {
		try {
			
			RestTemplate restTemplate=restTemplateBuilder.build();
			ResponseEntity<List<Beer>> responseEntity= restTemplate.exchange(punkApiUrl, 
												HttpMethod.GET, null,
												new ParameterizedTypeReference<List<Beer>>() {});
				
			List<Beer> beers = responseEntity.getBody();
			//test
			logger.debug(String.format("getBeersFromPunkAPI - %d beers retrieved ",beers.size()));
			return beers.stream()
					.map(beer ->new Beer((beer.getIdentifier()+punkBeersStartIndex), 
										beer.getName(), beer.getDescription()))
					.collect(Collectors.toList());
		}
		catch(Exception e) {
			logger.error("getBeersFromPunkAPI", e);
			throw new RepositoryException(String.format("Error while fetching data from repository -"
												,e.getMessage()), e);
		}
	}
		
	private List<Beer> getBeersFromLocalRepository() {
		
		try {
			TypeReference<List<Beer>> typeReference = new TypeReference<List<Beer>>() {};
			ObjectMapper mapper=new ObjectMapper();
			List<Beer> beers =mapper.readValue(
									new File(localBeerRepositoryPath),
									typeReference);
			logger.debug(String.format("getBeersFromLocalRepository - %d beers retrieved ",beers.size()));
			return beers;
		}	
		catch(Exception e) {
			logger.error("getBeersFromLocalRepository", e);
			throw new RepositoryException(String.format("Error while fetching data from repository -%s"
					,e.getMessage()), e);
			
		}
		
		
	}
	private void saveBeerToLocalRepository(List<Beer> beers) {
		try {
			
			ObjectMapper mapper=new ObjectMapper();
			DefaultPrettyPrinter printer= new DefaultPrettyPrinter();
			Indenter indenter = new DefaultIndenter();
			printer.indentObjectsWith(indenter); 
			printer.indentArraysWith(indenter); 
			mapper.writer(printer)
					.writeValue(new File(localBeerRepositoryPath), 
								beers);
		}	
		catch(Exception e) {
			throw new RepositoryException(String.format("Error while fetching data from repository - %s"
					,e.getMessage()), e);
		}
	}
	
	/**
	 * Find all beers
	 * @return
	 */
	public List<Beer> findAll(){
		List<Beer> allBeers= getBeersFromPunkAPI();
		allBeers.addAll(getBeersFromLocalRepository());
		if(allBeers.isEmpty()) {
			logger.debug("findAll - Beers not found in the repository");
			throw new DataNotFoundException("Beers not Found in the repository");
		} else {
			List<Beer> sortedBeers= allBeers.stream().sorted().collect(Collectors.toList());
			logger.debug(String.format("findAll - %d beers found ",sortedBeers.size()));
			return sortedBeers;
		}
		
		
	}
	/**
	 * Find random beer
	 * @return
	 */
	public Beer findRandom() {
		List<Beer> allBeers=findAll();
		Beer beer= allBeers.get(new Random().nextInt(allBeers.size()));
		logger.debug("findRandom - Beer found {}", beer);
		return beer;
	}
	/**
	 * Find beer by ID
	 * @param id
	 * @return
	 */
	public Beer findById(long id) {
		logger.debug("findById");
		List<Beer> allBeers=findAll();
		List<Beer> beers=allBeers.stream().filter(beer ->(beer.getIdentifier()==id))
				.collect(Collectors.toList());
		if(beers.isEmpty()) {
			logger.debug(String.format("FindById - Beer not found for Id - %d ",id));	
			throw new DataNotFoundException(String.format("Beer not Found for identifer -%d", 
										id, " not found"));
		}
		logger.debug("findById - Beer found {}", beers.get(0));
		return beers.get(0);	
	}
	/**
	 * Save the beer to local repository
	 * @param beer
	 * @return
	 */
	public Beer save(Beer beer) {
		if(beer==null)
			throw new RepositoryException("Beer can't be null ");
		
		logger.debug("SaveBeer");
		List<Beer> beers=getBeersFromLocalRepository();
		OptionalLong id=beers.stream().mapToLong(beer1 -> beer1.getIdentifier()).max();
		long identifier=id.isPresent()?(id.getAsLong()+1L):1L;
		logger.debug(String.format("SaveBeer -New identifier is - %d",identifier));
		beer.setIdentifier(identifier);
		beers.add(beer);
		saveBeerToLocalRepository(beers);
		logger.debug("SaveBeer - Beer added to repository");
		return beer;	
	}
	/**
	 * Case insensitive search for the name
	 * @param name
	 * @return
	 */
	public List<Beer> findByName(String name){
		
		if((name !=null)&&(name.trim().length()==0)){
			throw new DataNotFoundException("Name should not be empty or null");
		};
		logger.debug(String.format("findByName - Search beers with  name - %s", name));
		List<Beer> allBeers=findAll();
		Predicate<Beer> predicate= beer -> Pattern.compile(Pattern.quote(name), Pattern.CASE_INSENSITIVE)
				.matcher(beer.getName())
				.find();
		List<Beer> matchedBeers= allBeers.stream()
				.filter(predicate)
				.sorted()
				.collect(Collectors.toList());
		if (matchedBeers.isEmpty()) {
			logger.debug(String.format("findByName - Beer(s) not found for name - %s ",name));
			throw new DataNotFoundException(String.format("Beers not found for name - %s ",name));
		}	
		else	{
			logger.debug(String.format("findByName - %d beer(s) found ",matchedBeers.size()));
			return matchedBeers;
		}
			
	}
	
}
