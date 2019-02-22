package br.com.achimid.notfy.dto;

import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Data
public class CrawlRequest {

    @NotNull
    private String url;
    private CrawlConfig config = new CrawlConfig();
    private String cssQuery;
    private List<String> scriptCommandList = new ArrayList<>();

}
