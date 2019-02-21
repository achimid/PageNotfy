package br.com.achimid.notfy.mail;

import lombok.Data;

import java.util.Map;

@Data
public class GenericMailTemplate implements IMailTemplate{

    private String templatePath;
    private Map<String, Object> model;

}
