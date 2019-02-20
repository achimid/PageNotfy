package br.com.achimid.notfy.service;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;


@Slf4j
@Service
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ScraulerService {

    @Autowired
    private WebClient browser;

    public String getHtmlContent(String url) throws IOException, URISyntaxException {
        URI uri = new URI(url);

        HtmlPage page = browser.getPage(uri.toString());
        browser.waitForBackgroundJavaScript(5000);

        String html = page.asXml();
        log.info(html);

        html = StringUtils.replace(html,"=\"/",  "=\"" + uri.getScheme() + "://" + uri.getHost() + "/");

        return html;
    }
}
