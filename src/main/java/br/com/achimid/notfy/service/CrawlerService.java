package br.com.achimid.notfy.service;

import br.com.achimid.notfy.dto.CrawlConfig;
import br.com.achimid.notfy.dto.CrawlRequest;
import br.com.achimid.notfy.dto.CrawlResponse;
import br.com.achimid.notfy.dto.JavascriptResult;
import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@Service
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CrawlerService {

    private final int javaScriptDeplay = 3000; //3segundos

    @Autowired
    private WebClient browser;

    public CrawlResponse crawlPage(@NonNull CrawlRequest cRequest) throws IOException{

        final CrawlConfig config = cRequest.getConfig();

        // Execute the initial request to the page
        final HtmlPage page = browser.getPage(cRequest.getUrl().toString());

        // Waiting javascript background
        if(config.isWaitForJavascript()) browser.waitForBackgroundJavaScript(config.getJavascriptWaitTime());

        // Injecting jQuery lib
        this.injectJqueryLib(page, config);

        // Execute javascriptCommands if Exists
        final List<JavascriptResult> javascriptResults = this.executeJavascripCommandList(page, config);

        // Build response object
        CrawlResponse response = new CrawlResponse();
        response.setHtml(page.asXml());
        response.setJavascriptResultList(javascriptResults);
        response.setUrl(cRequest.getUrl());

        return response;
    }

    private List<JavascriptResult> executeJavascripCommandList(final HtmlPage page, CrawlConfig config){
        final List<JavascriptResult> results = new ArrayList<>();
        for(String command : config.getScriptCommandList()){
            JavascriptResult result;

            if(config.isEnableJqueryLib()){
                result = executeJqueryCommand(page, command);
            }else{
                final ScriptResult res = page.executeJavaScript(command);
                result = new JavascriptResult();
                result.setScriptResult(res);
                result.setScriptCommand(command);
            }

            results.add(result);
        }
        return results;
    }

    private JavascriptResult executeJqueryCommand(final HtmlPage page, String command){
        JavascriptResult result = new JavascriptResult();

        // Create to store jQuery scripts results
        this.createResponseContent(page);

        // Execute jQuery command and store result into spacific tag
        final ScriptResult res = page.executeJavaScript("$('#idJqueryReturnTag').html($(" + command + ").clone())");

        // Create to store jQuery scripts results
        this.removeResponseContent(page);

        // Retrieve return content from page and setting return object
        result.setHtmlResult(page.getElementById("idJqueryReturnTag").asXml());
        result.setScriptCommand(command);
        result.setScriptResult(res);

        return result;
    }


    private void injectJqueryLib(final HtmlPage page, CrawlConfig config){
        if(config.isEnableJqueryLib()){
            // TODO: melhorar e criar config
            String injectScriptJquery = "var jq = document.createElement('script');\n" +
                    "jq.src = \"https://ajax.googleapis.com/ajax/libs/jquery/2.1.4/jquery.min.js\";\n" +
                    "document.getElementsByTagName('head')[0].appendChild(jq);\n" +
                    "jQuery.noConflict();";
            page.executeJavaScript(injectScriptJquery);
        }
    }

    private void createResponseContent(final HtmlPage page){
        this.removeResponseContent(page);
        page.executeJavaScript("$('html').append('<return id=\"idJqueryReturnTag\"></return>')");
    }

    private void removeResponseContent(final HtmlPage page){
        page.executeJavaScript("$('return').remove()");
    }


}
