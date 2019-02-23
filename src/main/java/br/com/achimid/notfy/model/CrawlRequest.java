package br.com.achimid.notfy.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class CrawlRequest {

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    private String url;

    @OneToOne(cascade = CascadeType.ALL)
    private CrawlConfig config = new CrawlConfig();

    private String cssQuery;

    @Lob
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> scriptCommandList = new ArrayList<>();

}
