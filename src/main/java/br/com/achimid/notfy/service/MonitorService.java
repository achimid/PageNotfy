package br.com.achimid.notfy.service;

import br.com.achimid.notfy.mail.GenericMailTemplate;
import br.com.achimid.notfy.mail.MailModel;
import br.com.achimid.notfy.model.CrawlRequest;
import br.com.achimid.notfy.model.CrawlResponse;
import br.com.achimid.notfy.model.MonitorPage;
import br.com.achimid.notfy.repository.MonitorPageRepository;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

@Slf4j
@Service
public class MonitorService {

    @Autowired
    private CrawlerService crawlerService;

    @Autowired
    private MonitorPageRepository repository;

    @Autowired
    private EmailService emailService;

    private MonitorPage save(@NonNull MonitorPage monitorPage) {
        monitorPage.generateHashHtml();
        monitorPage.setDateInsert(Calendar.getInstance());
        return repository.save(monitorPage);
    }

    public void createMonitor(CrawlRequest request) {
        CrawlResponse response = crawlerService.crawlPage(request);

        MonitorPage monitorPage = new MonitorPage();
        monitorPage.setRequest(request);
        monitorPage.setResponse(response);

        monitorPage.setPageHtml(StringUtils.isEmpty(response.getCssQuery()) ? response.getHtmlFullPage() : response.getHtmlQueryReturn());

        this.save(monitorPage);
    }

    public void notifyAllMonitors(){
        List<MonitorPage> pages = repository.findAll();
        pages.forEach(m -> notifyMonitorExecute(m));
    }

    public void notifyMonitorExecute(MonitorPage monitorPage){
        log.info("Iniciando monitoramento... {}", monitorPage.getRequest().getUrl());

        CrawlResponse response = crawlerService.crawlPage(monitorPage.getRequest());

        if(!monitorPage.compareHash(response.getHtml())){
            this.sendEmailReturnContentUpdated(response);
        }
    }

    private void sendEmailReturnContentUpdated(CrawlResponse response){
        MailModel m = new MailModel();

        m.setFrom("achimid@hotmail.com");
        m.setTo("achimid@hotmail.com");
        m.setSubject("Nova Atualização no Site!!");

        GenericMailTemplate template = new GenericMailTemplate();
        template.getModel().put("site", "HorribleSubs");
        template.getModel().put("urlSite", response.getUrl());
        template.getModel().put("newContent", response.getHtml());
        template.setTemplatePath("avisoTemplate");

        m.setTemplate(template);

        try {
            emailService.sendSimpleHtmlMail(m);
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
