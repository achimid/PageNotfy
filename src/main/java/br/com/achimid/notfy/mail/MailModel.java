package br.com.achimid.notfy.mail;

import lombok.Data;

@Data
public class MailModel{

    private String from;
    private String to;
    private String subject;
    private String content;

    private IMailTemplate template;
}
