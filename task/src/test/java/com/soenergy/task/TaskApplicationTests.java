package com.soenergy.task;



import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import com.soenergy.task.beans.Beer;

@SpringBootTest(classes=TaskApplication.class, 
		webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TaskApplicationTests {
	@LocalServerPort
	private int port;
	
	private String baseURL="http://localhost:%d/v1/beers";

	
	@Test
	void testGetAllBeers() {
		String url=String.format(baseURL,port);
		//create TestRestTemplate
		TestRestTemplate restTemplate= new TestRestTemplate();
		ResponseEntity<Beer[]> response=restTemplate.getForEntity(url, Beer[].class);
		//Check if number of beers returned are greater than or equal to 27
		assertTrue(response.getBody().length>=27);
	}	
	@Test
	void testGetRandomBeer() {
		String url=String.format(baseURL.concat("/random"),port);
		//create TestRestTemplate
		TestRestTemplate restTemplate= new TestRestTemplate();
		ResponseEntity<Beer> response=restTemplate.getForEntity(url, Beer.class);
		//Check if beer returned is not null
		assertNotNull(response.getBody());
	}	
	@Test
	void testGetBeerById() throws JSONException {
		String url=String.format(baseURL.concat("/%d"),port,1);
		//create TestRestTemplate
		TestRestTemplate restTemplate= new TestRestTemplate();
		ResponseEntity<String> response=restTemplate.getForEntity(url, String.class);
		//compare returned beer 
		String expected="{\n"
				+ "beer_identifier: 1,\n"
				+ "beer_name: \"So Energy Supply\",\n"
				+ "beer_description: \"This beer is as green as the energy we supply.\"\n"
				+ "}";
		JSONAssert.assertEquals(expected,response.getBody() , false);
		assertNotNull(response.getBody());
	}	
	@Test
	void testGetBeersByName() {
		String url=String.format(baseURL.concat("/search/?name=%s"),port,"india");
		//create TestRestTemplate
		TestRestTemplate restTemplate= new TestRestTemplate();
		ResponseEntity<Beer[]> response=restTemplate.getForEntity(url, Beer[].class);
		//Check if number of beers returned are equal to 2
		assertTrue(response.getBody().length==2);
	}	
	@Test
	void createBeer() {
		String url=String.format(baseURL,port);
		//create TestRestTemplate
		TestRestTemplate restTemplate= new TestRestTemplate();
		//create Beer
		Beer beer=new Beer();
		beer.setName("Test");
		beer.setDescription("Test Beer");
		ResponseEntity<Beer> response=restTemplate.postForEntity(url, beer, Beer.class);
		//Check if number of beers returned are greater than or equal to 27
		assertTrue(response.getBody().getIdentifier()>=0);
	}	

}
