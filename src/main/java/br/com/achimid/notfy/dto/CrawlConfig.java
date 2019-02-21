package br.com.achimid.notfy.dto;

import lombok.Data;

import java.util.List;

@Data
public class CrawlConfig {

    private boolean waitForJavascript = true;
    private int javascriptWaitTime = 3000;
    private boolean enableJqueryLib = false;

}
