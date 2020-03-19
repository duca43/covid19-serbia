package org.serbia.covid19;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class Covid19Application {

//        private static final String covid19URL = "https://covid19.rs/";

    public static void main(String[] args) {
        SpringApplication.run(Covid19Application.class, args);

//            WebClient client = new WebClient();
//            client.getOptions().setJavaScriptEnabled(false);
//            client.getOptions().setCssEnabled(false);
//            client.getOptions().setUseInsecureSSL(true);
//
//            try {
//                HtmlPage page = client.getPage(covid19URL);
//                HtmlElement elementCases = page.getFirstByXPath("//*[@id=\"site-header-inner\"]/div/div/div/section[6]/div[2]/div/div/div/div/div/div/h2");
//
//                String casesText = elementCases.asText();
//                String[] elements = casesText.split(" ");
//                int numberCases = Integer.parseInt(elements[6]);
//                System.out.println(numberCases);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
    }
}
