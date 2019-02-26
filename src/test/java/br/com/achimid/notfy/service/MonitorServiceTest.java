package br.com.achimid.notfy.service;

import br.com.achimid.notfy.model.CrawlResponse;
import br.com.achimid.notfy.model.MonitorPage;
import br.com.achimid.notfy.util.Stub;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@ActiveProfiles("test")
@SpringBootTest
@RunWith(SpringRunner.class)
public class MonitorServiceTest {

    @Autowired
    protected MonitorService monitorService;

    @Autowired
    protected CrawlerService crawlerService;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateMonitor_Success(){
        CrawlResponse resp = Stub.getCResponse();

        MonitorPage monitorPage = monitorService.createMonitor(Stub.getMonitorPage());
        monitorService.notifyMonitorExecute(monitorPage);
        monitorService.notifyMonitorExecute(monitorPage);
        monitorService.notifyMonitorExecute(monitorPage);
        monitorService.notifyMonitorExecute(monitorPage);
    }

}
