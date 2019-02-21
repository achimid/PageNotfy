package br.com.achimid.notfy.controller;

import br.com.achimid.notfy.dto.CrawlRequest;
import br.com.achimid.notfy.dto.CrawlResponse;
import br.com.achimid.notfy.service.CrawlerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URISyntaxException;

@Slf4j
@RestController("/api/v1/monitor")
public class PageMonitorController {

    @Autowired
    private CrawlerService crawlerService;

    @PostMapping
    public CrawlResponse monitorRequest(CrawlRequest request){
        try {
            // TODO: Validation here
            return crawlerService.crawlPage(request);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping
    public CrawlResponse teste(){
        CrawlRequest c = new CrawlRequest();
        c.setUrl("https://horriblesubs.info/");
        c.setCssQuery(".latest-releases");
        return this.monitorRequest(c);
    }

}
