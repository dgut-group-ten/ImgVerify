package com.example.ai.domain;

import javax.persistence.*;

@Entity
@Table(name = "img")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(nullable = false)
    String url;

    public Image() {
    }

    public Image(Long id, String url) {
        this.id = id;
        this.url = url;
    }
    public Image(String url) {
        this.url = url;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
