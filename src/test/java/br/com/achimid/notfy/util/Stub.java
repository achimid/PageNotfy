package br.com.achimid.notfy.util;

import br.com.achimid.notfy.mail.GenericMailTemplate;
import br.com.achimid.notfy.mail.MailModel;
import br.com.achimid.notfy.model.*;

import javax.management.monitor.Monitor;

public class Stub {

    public static final String url = "https://horriblesubs.info/";
    public static final String cssSelector = ".latest-releases";

    public static String getScriptTest(){
        StringBuilder sb = new StringBuilder();

        sb.append("function removeParentSlb($element){");
        sb.append("    if($element.prop(\"tagName\") == \"BODY\") return;");
        sb.append("    $element.siblings().each(function(){");
        sb.append("         var tag = $(this).prop(\"tagName\");");
        sb.append("         if(tag != \"SCRIPT\" && tag != \"HEAD\" && tag != \"STYLE\"){");
        sb.append("             $(this).remove();");
        sb.append("         }");
        sb.append("    })");
        sb.append("    $element = $element.parent();");
        sb.append("    removeParentSlb($element);");
        sb.append("}");
        sb.append("removeParentSlb($('.latest-releases'))");

        return sb.toString();
    }

    public static CrawlConfig getSimpleConfig(){
        CrawlConfig config = new CrawlConfig();

        config.setEnableJqueryLib(true);
        config.setJavascriptWaitTime(3000);
        config.setWaitForJavascript(true);

        return config;
    }

    public static NotificationInfo getNotificationInfo(){
        NotificationInfo n = new NotificationInfo();

        n.setEmail("achimid@hotmail.com");
        n.setUrlMonitor(url);
        n.setWebsiteName("HorribleSubs");

        return n;
    }

    public static CrawlRequest getCRequest(){
        return getCRequest(null);
    }

    public static CrawlRequest getCRequest(CrawlConfig config){
        CrawlRequest r = new CrawlRequest();

        r.setConfig(config == null ? getSimpleConfig() : config);
        r.setUrl(url);
        r.setCssQuery(cssSelector);

        return r;
    }

    public static CrawlResponse getCResponse() {
        CrawlResponse res = new CrawlResponse();

        res.setHtmlFullPage("html full page");
        res.setHtmlQueryReturn("html query return");
        res.setUrl(url);

        return res;
    }

    public static MonitorPage getMonitorPage(){
        MonitorPage m = new MonitorPage();

        m.setNotificationInfo(getNotificationInfo());
        m.setRequest(getCRequest());

        return m;
    }

}
