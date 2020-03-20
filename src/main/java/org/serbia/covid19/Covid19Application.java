package org.serbia.covid19;

import java.io.IOException;

import org.serbia.covid19.service.CasesScraper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

@Controller
@SpringBootApplication
public class Covid19Application {


    public static void main(final String[] args) {
        SpringApplication.run(Covid19Application.class, args);
    
    }
    
}
