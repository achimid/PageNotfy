package br.com.achimid.notfy.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.thymeleaf.util.StringUtils;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Entity
public class CrawlResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Integer id;

    @NotNull
    private String url;

    @Lob
    private String htmlFullPage;

    @Lob
    private String htmlQueryReturn;

    @Transient
    private List<JavascriptResult> javascriptResultList;

    public String getHtml(){
        return StringUtils.isEmpty(htmlQueryReturn) ? htmlFullPage : htmlQueryReturn;
    }

}
