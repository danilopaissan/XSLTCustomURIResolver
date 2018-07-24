package com.example.java.xslt.uriresolver.repo;

import com.example.java.xslt.uriresolver.entities.Xslt;
import org.springframework.data.repository.CrudRepository;

public interface XsltRepository extends CrudRepository<Xslt,Integer> {
    Xslt getXsltById(Integer id);
}
