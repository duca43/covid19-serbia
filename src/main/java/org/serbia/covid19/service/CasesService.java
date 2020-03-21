package org.serbia.covid19.service;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.serbia.covid19.dto.CasesDto;
import org.serbia.covid19.model.Cases;
import org.serbia.covid19.repository.CasesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CasesService {

    private static final String covid19URL = "https://covid19.rs/";
    private static final DateTimeFormatter SERBIAN_DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy.");
    private final CasesRepository casesRepository;
    private final ModelMapper modelMapper;

    @Autowired

    public CasesService(final CasesRepository casesRepository, final ModelMapper modelMapper) {
        this.casesRepository = casesRepository;
        this.modelMapper = modelMapper;
    }

    public List<CasesDto> findAll() {
        return this.casesRepository.findAll()
                .stream()
                .map(cases -> {
                    final CasesDto casesDto = this.modelMapper.map(cases, CasesDto.class);
                    casesDto.setDate(cases.getDate().format(SERBIAN_DATE_FORMATTER));
                    return casesDto;
                })
                .collect(Collectors.toList());
    }

    @Scheduled(cron = "*/2 * * * * *") //0 2 8,18 * * *
    public void scrapeCases() {
        final WebClient client = new WebClient();
        client.getOptions().setJavaScriptEnabled(false);
        client.getOptions().setCssEnabled(false);
        client.getOptions().setUseInsecureSSL(true);

        try {
            final HtmlPage page = client.getPage(covid19URL);
            final HtmlElement elementCases = page.getFirstByXPath("//*[@id=\"site-header-inner\"]/div/div/div/section[6]/div[2]/div/div/div/div/div/div/h2");

            final String casesText = elementCases.asText();
            final String[] elements = casesText.split(" ");
            final int numberCases = Integer.parseInt(elements[6]);
            this.writeValue(numberCases);

        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    private void writeValue(final int numberCases) {
        final LocalDate today = LocalDate.now();
        log.info("Writing number of cases for following date: {}", today);

        Cases cases = this.casesRepository.findTopByOrderByIdDesc();

        if (cases == null || !today.isEqual(cases.getDate())) {
            log.info("First recording of cases at date {}. Number of cases is {}", today, numberCases);
            cases = Cases.builder().date(today).numberOfCases(numberCases).build();
        } else {
            log.info("Cases at date {} are already recorded. Number of cases is updated from {} to {}", today, cases.getNumberOfCases(), numberCases);
            cases.setNumberOfCases(numberCases);
        }

        this.casesRepository.save(cases);
    }
}