package br.com.achimid.notfy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class JobsService {

    @Autowired
    private MonitorService monitorService;

    @Scheduled(fixedDelay = 30000)
    public void startMonitoring(){
        monitorService.notifyAllMonitors();
    }

}
