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
import org.springframework.transaction.annotation.Transactional;

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

    public MonitorPage createMonitor(@NonNull MonitorPage monitorPage) {
        CrawlRequest request = monitorPage.getRequest();
        if(request == null) {
            request = new CrawlRequest();
            request.setUrl(monitorPage.getNotificationInfo().getUrlMonitor());
        }

        CrawlResponse response = crawlerService.crawlPage(request);

        monitorPage.setRequest(request);
        monitorPage.addResponse(response);

        this.save(monitorPage);

        return monitorPage;
    }

    public void notifyAllMonitors(){
        List<MonitorPage> pages = repository.findAll();
        pages.forEach(m -> notifyMonitorExecute(m));
    }

    @Transactional
    public void notifyMonitorExecute(MonitorPage monitorPage){
        log.info("Iniciando monitoramento... {}", monitorPage.getRequest().getUrl());

        CrawlResponse response = crawlerService.crawlPage(monitorPage.getRequest());

        if(!monitorPage.compareHash(response.getHtml())){ // Response html is different from current hmlt
            monitorPage.addResponse(response);
            this.sendEmailReturnContentUpdated(response);
            this.save(monitorPage); // Update hash with new html response
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

        log.info("Enviando notificação para {} ....................................", m.getTo());

        /*try {
            emailService.sendSimpleHtmlMail(m);
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

}
