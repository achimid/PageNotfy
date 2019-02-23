package br.com.achimid.notfy.controller;

import br.com.achimid.notfy.model.CrawlRequest;
import br.com.achimid.notfy.model.CrawlResponse;
import br.com.achimid.notfy.mail.MailModel;
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
    private CrawlerService crawlerService;

    @Autowired
    private MonitorService monitorService;

    @PostMapping
    public CrawlResponse monitorRequest(CrawlRequest request) {
        // TODO: Validation here
        return crawlerService.crawlPage(request);
    }

    @GetMapping("/teste")
    public CrawlResponse teste() {
        CrawlRequest c = new CrawlRequest();
        c.setUrl("https://horriblesubs.info/");
        c.setCssQuery(".latest-releases");
        return this.monitorRequest(c);
    }

    String script = "function removeParentSlb($element){\n" +
            "    if($element.prop(\"tagName\") == \"BODY\") return;\n" +
            "    $element.siblings().each(function(){\n" +
            "\tvar tag = $(this).prop(\"tagName\");\n" +
            "\tif(tag != \"SCRIPT\" && tag != \"HEAD\" && tag != \"STYLE\"){\n" +
            "\t  console.log($(this))\n" +
            "\t  $(this).remove();\n" +
            "\t}\n" +
            "    })\n" +
            "    $element = $element.parent();\n" +
            "    removeParentSlb($element);\n" +
            "}\n" +
            "removeParentSlb($('.latest-releases'))";

    @GetMapping("/createMonitor")
    public String teste2() {
        /*CrawlRequest c = new CrawlRequest();
        c.setUrl("https://horriblesubs.info/");
        c.getConfig().setEnableJqueryLib(true);
        c.getScriptCommandList().add(script)*/;

        CrawlRequest c = new CrawlRequest();
        c.setUrl("https://horriblesubs.info/");
        c.setCssQuery(".latest-releases");

        monitorService.createMonitor(c);

        return "ok";
    }

}
