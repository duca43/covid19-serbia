package org.serbia.covid19.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CasesDto {

    private Long id;
    private Integer confirmedCases;
    private Integer deathCases;
    private Integer recoveredCases;
    private String date;
}