package br.com.achimid.notfy.dto;

import com.gargoylesoftware.htmlunit.ScriptResult;
import lombok.Data;

@Data
public class JavascriptResult {

    private String scriptCommand;
    private ScriptResult scriptResult;
    private String htmlResult;

}