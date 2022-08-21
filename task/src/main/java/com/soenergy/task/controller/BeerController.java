package com.soenergy.task.controller;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.soenergy.task.beans.Beer;
import com.soenergy.task.service.BeerService;

@RestController
@RequestMapping("/v1")
public class BeerController {

	private BeerService beerService;
	
	public BeerController(BeerService beerService) {
		super();
		this.beerService = beerService;
	}

	@GetMapping("/beers")
	public ResponseEntity<List<Beer>> findAllBeers() {
		List<Beer> beers=beerService.findAll();
		return ResponseEntity.ok(beers);
		
	}
	@GetMapping("/beers/random")
	public ResponseEntity<Beer> findRandomBeer() {
		Beer beer=beerService.findRandom();
		return ResponseEntity.ok(beer);
		
	}
	@GetMapping("/beers/{id}")
	public ResponseEntity<Beer> findBeerById(@PathVariable(required = true) long id) {
		Beer beer=beerService.findById(id);
		return ResponseEntity.ok(beer);
		
	}
	@PostMapping("/beers")
	public ResponseEntity<Beer> addBeer(@Valid @RequestBody Beer beer){
		Beer savedBeer=beerService.save(beer);
		//Get URI of created resource
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
						.path("/{id}").buildAndExpand(savedBeer.getIdentifier()).toUri();
		return ResponseEntity.created(uri)
					.body(savedBeer);
		
	}
	@GetMapping("/beers/search")
	public ResponseEntity<List<Beer>> findBeerByName(@RequestParam(required = true) String name) {
		List<Beer> beers=beerService.findByName(name);
		return ResponseEntity.ok(beers);
		
	}
}
