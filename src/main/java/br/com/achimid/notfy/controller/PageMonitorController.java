package br.com.achimid.notfy.controller;

import br.com.achimid.notfy.model.CrawlRequest;
import br.com.achimid.notfy.model.CrawlResponse;
import br.com.achimid.notfy.mail.MailModel;
import br.com.achimid.notfy.model.MonitorPage;
import br.com.achimid.notfy.service.CrawlerService;
import br.com.achimid.notfy.service.EmailService;
import br.com.achimid.notfy.service.MonitorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/api/v1/monitor")
public class PageMonitorController {

    @Autowired
    private MonitorService monitorService;

    /*@PostMapping
    public CrawlResponse monitorRequest(CrawlRequest request) {
        // TODO: Validation here
        return crawlerService.crawlPage(request);
    }*/

    @PostMapping
    public MonitorPage monitorRequest(@RequestBody MonitorPage monitorPage) {
        // TODO: Validation here
        return monitorService.createMonitor(monitorPage);
    }

}
