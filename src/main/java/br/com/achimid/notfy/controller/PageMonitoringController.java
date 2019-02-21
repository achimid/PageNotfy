package br.com.achimid.notfy.controller;

import br.com.achimid.notfy.service.CrawlerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URISyntaxException;

@Slf4j
@RestController
public class PageMonitoringController {

    @Autowired
    private CrawlerService crawlerService;


    @GetMapping("/")
    @Cacheable("getHtml")
    public String getHtml(@RequestParam(value = "url") String url){
        try {
            return crawlerService.getHtmlContent(url);
        } catch (IOException e) {
            log.error("Erro ao extrair html de " + url, e);
            return null;
        } catch (URISyntaxException e) {
            return null;
        }
    }

}
