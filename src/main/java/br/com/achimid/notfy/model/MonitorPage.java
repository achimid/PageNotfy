package br.com.achimid.notfy.model;

import lombok.Data;
import org.apache.commons.codec.digest.DigestUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.util.*;

@Data
@Entity
@Table(indexes = { @Index(name = "IDX_DOCPAGE", columnList = "id,hashHtml") })
public class MonitorPage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(optional = false, cascade = {CascadeType.ALL})
    private CrawlRequest request;

    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    private Set<CrawlResponse> responseList;

    @Lob
    @Column(updatable = false)
    private String pageHtml;

    @Column(updatable = false)
    private String hashHtml;

    @Temporal(value = TemporalType.TIMESTAMP)
    private Calendar dateInsert;

    @Transient
    private Document pageDoc;
    public Document getDocument(){
        if(pageDoc == null && pageHtml == null) return null;
        if(pageDoc != null) return pageDoc;
        if(pageHtml != null) pageDoc = Jsoup.parse(pageHtml);
        return pageDoc;
    }

    public void setPageHtml(String pageHtml){
        this.pageHtml = pageHtml;
        if(pageHtml != null) this.pageDoc = Jsoup.parse(pageHtml);
    }

    public void generateHashHtml(){
        if(this.pageHtml != null){
            this.hashHtml = DigestUtils.md5Hex(this.pageHtml);
        }
    }

    public boolean compareHash(String htmlCompare){
        return this.hashHtml.equals(DigestUtils.md5Hex(htmlCompare));
    }

    public void addResponse(CrawlResponse response){
        if(CollectionUtils.isEmpty(responseList)) this.responseList = new HashSet<>();
        this.responseList.add(response);
        this.pageHtml = StringUtils.isEmpty(response.getCssQuery()) ? response.getHtmlFullPage() : response.getHtmlQueryReturn();
    }


}
