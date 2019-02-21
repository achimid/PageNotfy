package br.com.achimid.notfy.mail;

import java.util.Map;

public interface IMailTemplate {

    String getTemplatePath();

    Map<String, Object> getModel();

}
