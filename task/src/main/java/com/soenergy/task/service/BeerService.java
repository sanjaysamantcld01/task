package com.soenergy.task.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.soenergy.task.beans.Beer;
import com.soenergy.task.repository.BeerRepository;

@Service
public class BeerService {
	
	private BeerRepository beerRepository;

	public BeerService(BeerRepository beerRepository) {
		super();
		this.beerRepository = beerRepository;
	}
	 /**
	  * Find all the beers from repository
	  * @return
	  */
	public List<Beer> findAll(){
		return beerRepository.findAll();
	}
	/**
	 * return random beer from repository
	 * @return
	 */
	public Beer findRandom() {
		return beerRepository.findRandom();
	}
	
	/**
	 * Find the beer for ID
	 * @return
	 */
	public Beer findById(long id) {
		return beerRepository.findById(id);
	}
	/**
	 * Find the beers by name. Incase sensitive partial search
	 * @param name
	 * @return
	 */
	public List<Beer> findByName(String name){
		return beerRepository.findByName(name);
	}
	/**
	 * Save the beer
	 * @param beer
	 * @return
	 */
	public Beer save(Beer beer) {
		return beerRepository.save(beer);
	}
	
	
}
