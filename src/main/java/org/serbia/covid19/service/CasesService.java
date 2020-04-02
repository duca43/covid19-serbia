package org.serbia.covid19.service;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableCell;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.serbia.covid19.dto.CasesDto;
import org.serbia.covid19.model.Cases;
import org.serbia.covid19.repository.CasesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
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

    private static final String scrapingUrl = "https://www.worldometers.info/coronavirus/";
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

    @Scheduled(cron = "0 2,5,15,20,30 14 * * *")
    @EventListener(ApplicationReadyEvent.class)
    public void scrapeCases() {
        final WebClient client = new WebClient();
        client.getOptions().setJavaScriptEnabled(false);
        client.getOptions().setCssEnabled(false);
        client.getOptions().setUseInsecureSSL(true);

        try {
            final HtmlPage page = client.getPage(scrapingUrl);
            final HtmlTable casesTableElement = page.getFirstByXPath("//*[@id=\"main_table_countries_today\"]");

            final HtmlTableRow serbiaCasesRow = casesTableElement.getRows()
                    .stream()
                    .filter(htmlTableRow -> htmlTableRow.getCells().get(0).getVisibleText().equals("Serbia"))
                    .findFirst()
                    .orElse(null);

            if (serbiaCasesRow != null) {
                final List<HtmlTableCell> serbiaCasesRowCells = serbiaCasesRow.getCells();
                final String confirmedCasesString = serbiaCasesRowCells.get(1).getVisibleText().replaceAll(",", "");
                final int confirmedCases = Integer.parseInt(confirmedCasesString);
                final String deathCasesString = serbiaCasesRowCells.get(3).getVisibleText().replaceAll(",", "");
                final int deathCases = Integer.parseInt(deathCasesString);
                final String recoveredCasesString = serbiaCasesRowCells.get(5).getVisibleText().replaceAll(",", "");
                final int recoveredCases = Integer.parseInt(recoveredCasesString);
                this.writeValue(confirmedCases, deathCases, recoveredCases);
            }
        } catch (final IOException e) {
            //TODO do sth with exception
            e.printStackTrace();
        }
    }

    private void writeValue(final int confirmedCases, final int deathCases, final int recoveredCases) {
        final LocalDate today = LocalDate.now();
        log.info("Writing number of cases for following date: {}", today);

        Cases cases = this.casesRepository.findByDate(today);

        if (cases == null || !today.isEqual(cases.getDate())) {
            log.info("First recording of cases at date {}. Confirmed cases: {}. Death cases: {}. Recovered cases: {}.",
                    today,
                    confirmedCases,
                    deathCases,
                    recoveredCases);

            cases = Cases.builder()
                    .date(today)
                    .confirmedCases(confirmedCases)
                    .deathCases(deathCases)
                    .recoveredCases(recoveredCases)
                    .build();
        } else {
            log.info("Cases at date {} are already recorded. Number of cases is updated from {} to {}",
                    today,
                    cases.getConfirmedCases(),
                    confirmedCases);

            cases.setConfirmedCases(confirmedCases);
            cases.setDeathCases(deathCases);
            cases.setRecoveredCases(recoveredCases);
        }

        this.casesRepository.save(cases);
    }
}