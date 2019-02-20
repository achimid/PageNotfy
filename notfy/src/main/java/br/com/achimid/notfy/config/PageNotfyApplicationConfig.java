package br.com.achimid.notfy.config;

import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Slf4j
@Configuration
@EnableScheduling
@EnableCaching
public class PageNotfyApplicationConfig {

    @Value("${webclient.config.timeout}") private int timeout;
    @Value("${webclient.config.javascript.timeout}") private int javascriptTimeout;

    @Bean
    public WebClient getWebClient(){
        WebClient browser = new WebClient();

        log.info("Configurando WebCliente");

        browser.getOptions().setCssEnabled(false);
        browser.getOptions().setUseInsecureSSL(true);
        browser.getOptions().setJavaScriptEnabled(true);
        browser.getOptions().setThrowExceptionOnScriptError(false);
        browser.getCookieManager().setCookiesEnabled(true);
        browser.setAjaxController(new NicelyResynchronizingAjaxController());
        browser.getOptions().setTimeout(timeout);
        browser.setJavaScriptTimeout(javascriptTimeout);

        log.info("WebCliente configurado e inicializado");

        return browser;
    }


}
