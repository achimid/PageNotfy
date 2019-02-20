package br.com.achimid.notfy.service;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;


@Slf4j
@Scope
@Service
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
