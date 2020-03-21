package org.serbia.covid19.controller;

import org.serbia.covid19.dto.CasesDto;
import org.serbia.covid19.service.CasesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/cases")
public class CasesController {

    private final CasesService casesService;

    @Autowired
    public CasesController(final CasesService casesService) {
        this.casesService = casesService;
    }

    @GetMapping
    public ResponseEntity<List<CasesDto>> getAllCases() {
        return ResponseEntity.ok(this.casesService.findAll());
    }
}
