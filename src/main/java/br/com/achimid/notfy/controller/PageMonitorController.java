package br.com.achimid.notfy.controller;

import br.com.achimid.notfy.dto.CrawlRequest;
import br.com.achimid.notfy.dto.CrawlResponse;
import br.com.achimid.notfy.mail.GenericMailTemplate;
import br.com.achimid.notfy.mail.IMailTemplate;
import br.com.achimid.notfy.mail.MailModel;
import br.com.achimid.notfy.service.CrawlerService;
import br.com.achimid.notfy.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.IOException;
import java.net.URISyntaxException;

@Slf4j
@RestController
@RequestMapping("/api/v1/monitor")
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

    @GetMapping("/teste")
    public CrawlResponse teste(){
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

    @Autowired
    EmailService emailService;

    @GetMapping("/teste2")
    public String teste2(){
        CrawlRequest c = new CrawlRequest();
        c.setUrl("https://horriblesubs.info/");
        c.getConfig().setEnableJqueryLib(true);
        c.getScriptCommandList().add(script);
        String html = this.monitorRequest(c).getHtmlFullPage();

        MailModel m = new MailModel();

        m.setContent("teste");
        m.setFrom("achimid@hotmail.com");
        m.setSubject("Teste");
        m.setTo("achimid@hotmail.com");

        try {
            emailService.sendSimpleHtmlMail(m, html);
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "ok";
    }

}
