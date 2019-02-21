package br.com.achimid.notfy.dto;

import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotNull;
import java.net.URI;

@Data
public class CrawlRequest {

    @NotNull
    private String url;
    private CrawlConfig config;

}
