package br.com.achimid.notfy.model;

import lombok.Data;
import org.apache.commons.codec.digest.DigestUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.persistence.*;
import java.util.Calendar;

@Data
@Entity
@Table(indexes = { @Index(name = "IDX_DOCPAGE", columnList = "id,hashHtml") })
public class DocPage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

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



}
