package org.serbia.covid19.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.serbia.covid19.model.Cases;
import org.serbia.covid19.repository.CasesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

@Service
public class CasesScraper {
	
	@Autowired
	private CasesRepository casesRepository;
	
	private static final String covid19URL = "https://covid19.rs/";
	
	@Scheduled(cron = "*/2 * * * * *") //0 2 8,18 * * *
	public void scrapeCases() {
      WebClient client = new WebClient();
      client.getOptions().setJavaScriptEnabled(false);
      client.getOptions().setCssEnabled(false);
      client.getOptions().setUseInsecureSSL(true);

      try {
          HtmlPage page = client.getPage(covid19URL);
          HtmlElement elementCases = page.getFirstByXPath("//*[@id=\"site-header-inner\"]/div/div/div/section[6]/div[2]/div/div/div/div/div/div/h2");

          String casesText = elementCases.asText();
          String[] elements = casesText.split(" ");
          int numberCases = Integer.parseInt(elements[6]);
          writeValue(numberCases);
          
      } catch (IOException e) {
    	  e.printStackTrace();
      }
	}
	
	public void writeValue(int numberCases) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd");
		Date today = new Date();
		
		Cases casesLast = this.casesRepository.findTopByOrderByIdDesc();
		
		System.out.println(casesLast);
		if(!simpleDateFormat.format(casesLast.getDay()).equals(simpleDateFormat.format(today))) {
			Cases cases = Cases.builder().day(new Date()).numberOfCases(numberCases).build();
			this.casesRepository.save(cases);			
		} else {
			casesLast.setNumberOfCases(numberCases);
			this.casesRepository.save(casesLast);
		}
	}
}
