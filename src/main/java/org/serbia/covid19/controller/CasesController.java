package org.serbia.covid19.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.serbia.covid19.exceptions.ResourceNotFoundException;
import org.serbia.covid19.model.Cases;
import org.serbia.covid19.repository.CasesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class CasesController {
	
	@Autowired
	private CasesRepository casesRepository;
	
	@GetMapping("/cases")
	public List<Cases> getAllCases() {
		return this.casesRepository.findAll();
	}
	
	@GetMapping("/cases/{id}")
	public ResponseEntity<Cases> getCasesById(@PathVariable(value = "id") Long casesId) throws ResourceNotFoundException {
		Cases cases = casesRepository.findById(casesId).orElseThrow(() -> new ResourceNotFoundException("Cases not found, id: " + casesId));
		return ResponseEntity.ok().body(cases);
	}

	@PostMapping("/cases")
	public Cases createCases(@RequestBody Cases cases) {
		return this.casesRepository.save(cases);
	}
	
	@PutMapping("cases/{id}")
	public ResponseEntity<Cases> updateCases(@PathVariable(value = "id") Long casesId, @Valid @RequestBody Cases casesValues) throws ResourceNotFoundException {
		Cases cases = casesRepository.findById(casesId).orElseThrow(() -> new ResourceNotFoundException("Cases not found, id: " + casesId));
		
		Cases.builder().numberOfCases(casesValues.getNumberOfCases()).build();
		Cases.builder().day(casesValues.getDay()).build();
		
		return ResponseEntity.ok(this.casesRepository.save(cases));
	}
	
	@DeleteMapping("cases/{id}")
	public Map<String, Boolean> deleteCases(@PathVariable(value = "id") Long casesId) throws ResourceNotFoundException {
		Cases cases = casesRepository.findById(casesId).orElseThrow(() -> new ResourceNotFoundException("Cases not found, id: " + casesId));
		
		this.casesRepository.delete(cases);
		
		Map<String, Boolean> response = new HashMap<>();
		response.put("Deleted", Boolean.TRUE);
		
		return response;
	}
}
