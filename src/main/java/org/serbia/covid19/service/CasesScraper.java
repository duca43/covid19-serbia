package org.serbia.covid19.service;

import java.io.IOException;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

@Controller
public class CasesScraper {

	private static final String covid19URL = "https://covid19.rs/";
	
	@Scheduled(cron = "0 2 8,18 * * *")
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
          System.out.println(numberCases);
      } catch (IOException e) {
    	  e.printStackTrace();
      }
	}
}
