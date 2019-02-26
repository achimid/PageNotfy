package br.com.achimid.notfy.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class NotificationInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String email;

    private String urlMonitor;

    private String websiteName;

}
