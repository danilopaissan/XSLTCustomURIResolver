package com.example.java.xslt.uriresolver.controller;

import com.example.java.xslt.uriresolver.repo.XsltRepository;
import com.example.java.xslt.uriresolver.resolver.DBResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;
import java.io.StringWriter;

@Controller
public class XSLTController {

    @Autowired
    XsltRepository repository;

    @Autowired
    DBResolver dbResolver;

    @RequestMapping(
            value = "/xslt/{id}",
            produces = { MediaType.APPLICATION_JSON_VALUE },
            method = RequestMethod.GET)
    public ResponseEntity<String> getGreeting(@PathVariable String id){
        Source xsltOne=new StreamSource(new StringReader(repository.getXsltById(Integer.parseInt(id)).xsl));
        Source xml =new StreamSource(new StringReader("<?xml version=\"1.0\" encoding=\"UTF-8\"?><catalog><cd><title>titolo</title><artist>artista</artist></cd></catalog>"));

        TransformerFactory factory = TransformerFactory.newInstance();
        factory.setURIResolver(dbResolver);
        Transformer transformer = null;
        try {
            transformer = factory.newTransformer(xsltOne);
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        }

        StringWriter writer = new StringWriter();
        try {
            transformer.transform(xml, new StreamResult(writer));
        } catch (TransformerException e) {
            e.printStackTrace();
        }

        return new ResponseEntity(writer.toString(),HttpStatus.OK);
    }
}
