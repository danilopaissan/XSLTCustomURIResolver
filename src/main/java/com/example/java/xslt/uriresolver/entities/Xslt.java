package com.example.java.xslt.uriresolver.entities;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Xslt {
    @Id
    public int id;
    @Column(length = 2000)
    public String xsl;
}
