package br.com.achimid.notfy.service;

import br.com.achimid.notfy.model.CrawlConfig;
import br.com.achimid.notfy.model.CrawlRequest;
import br.com.achimid.notfy.model.CrawlResponse;
import br.com.achimid.notfy.model.JavascriptResult;
import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.host.Window;
import com.gargoylesoftware.htmlunit.javascript.host.css.ComputedCSSStyleDeclaration;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLElement;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@Service
//@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CrawlerService {

    @Autowired
    private WebClient browser;

    public CrawlResponse crawlPage(@NonNull CrawlRequest cRequest){

        final CrawlConfig config = cRequest.getConfig();

        // Execute the initial request to the page
        HtmlPage page = null;
        try {
            page = browser.getPage(cRequest.getUrl().toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Waiting javascript background
        if(config.isWaitForJavascript()) browser.waitForBackgroundJavaScript(config.getJavascriptWaitTime());

        // Injecting jQuery lib
        this.injectJqueryLib(page, config);

        // Execute javascriptCommands if Exists
        final List<JavascriptResult> javascriptResults = this.executeJavascripCommandList(page, cRequest);

        // Not Work
        //String css = getCss(browser, page.getDocumentElement());

        // Inject Css files inline
        // this.injectCssInline(page);

        // Find Using Css Selectors/Query
        final String fullHtmlPage = page.asXml();
        String htmlCssQuery = null;
        if(cRequest.getCssQuery() != null && !cRequest.getCssQuery().isEmpty()){
            Document htmlDocument = Jsoup.parse(fullHtmlPage);
            Elements elements = htmlDocument.select(cRequest.getCssQuery());
            htmlCssQuery = elements.html();
        }


        // Build response object
        CrawlResponse response = new CrawlResponse();
        response.setHtmlFullPage(fullHtmlPage);
        response.setJavascriptResultList(javascriptResults);
        response.setUrl(cRequest.getUrl());
        response.setHtmlQueryReturn(htmlCssQuery);

        return response;
    }

    private void injectCssInline(final HtmlPage page){
        for(DomElement el : page.getElementsByTagName("link")){
            final String urlCss = el.getAttribute("href");
            try {
                log.info("Consulting css url to inject inline {}", urlCss);
                Document doc = Jsoup.connect(urlCss).get();
                DomElement novo = page.createElement("style");
                novo.setAttribute("type", "text/css");
                novo.setNodeValue(doc.body().text());
                el.getParentNode().appendChild(novo);
            }catch (Exception e){
                log.error("Ignored css style inline url: {}", urlCss);
            }
        }
    }

    private List<JavascriptResult> executeJavascripCommandList(final HtmlPage page, CrawlRequest cRequest){
        if(CollectionUtils.isEmpty(cRequest.getScriptCommandList())) return null;

        final List<JavascriptResult> results = new ArrayList<>();
        for(String command : cRequest.getScriptCommandList()){
            JavascriptResult result;

            if(cRequest.getConfig().isEnableJqueryLib()){
                result = executeJqueryCommand(page, command);
            }else{
                final ScriptResult res = page.executeJavaScript(command);
                result = new JavascriptResult();
                result.setScriptResult(res);
                result.setScriptCommand(command);
            }

            results.add(result);
            // https://stackoverflow.com/questions/20245829/how-can-i-request-a-css-attribut-in-htmlunit
        }
        return results;
    }

    private String getCss(WebClient browser, HtmlElement element){
        WebWindow window = browser.getCurrentWindow();
        ScriptableObject sco = window.getScriptableObject();
        Window jscript = (Window) sco;
        HTMLElement htmlElement = (HTMLElement) jscript.makeScriptableFor(element);
        ComputedCSSStyleDeclaration style = jscript.getComputedStyle(htmlElement, null);
        System.out.println(style);
        return style.toString();
    }

    private JavascriptResult executeJqueryCommand(final HtmlPage page, String command){
        JavascriptResult result = new JavascriptResult();

        // Create to store jQuery scripts results
        this.createResponseContent(page);

        // Execute jQuery command and store result into spacific tag
        ScriptResult res = page.executeJavaScript("$('#idJqueryReturnTag').html($(" + command + ").clone())");
        // retry
        if(res.getJavaScriptResult() == null) res = page.executeJavaScript(command);


        // Retrieve return content from page and setting return object
        result.setHtmlResult(page.getElementById("idJqueryReturnTag").asXml());
        result.setScriptCommand(command);
        result.setScriptResult(res);

        // Create to store jQuery scripts results
        this.removeResponseContent(page);

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
