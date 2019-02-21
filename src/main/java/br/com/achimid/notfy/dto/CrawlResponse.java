package br.com.achimid.notfy.dto;

import lombok.Data;

import java.net.URI;
import java.util.List;

@Data
public class CrawlResponse {

    private String url;
    private String htmlFullPage;
    private String cssQuery;
    private String htmlQueryReturn;
    private List<JavascriptResult> javascriptResultList;

}
