package com.example.java.xslt.uriresolver.resolver;

import com.example.java.xslt.uriresolver.entities.Xslt;
import com.example.java.xslt.uriresolver.repo.XsltRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.IOException;

@Component
public class DBResolver implements URIResolver {

    @Autowired
    XsltRepository repository;

    @Override
    public Source resolve(String href, String base) throws TransformerException {
        if(href != null && !href.trim().isEmpty() && isDigit(href)){
            DocumentBuilderFactory dFactory = DocumentBuilderFactory
                    .newInstance();

            dFactory.setNamespaceAware(true);

            DocumentBuilder dBuilder = null;

            try {
                dBuilder = dFactory.newDocumentBuilder();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            }

            Xslt xslt = repository.getXsltById(Integer.parseInt(href));
            if(xslt == null)
                throw new TransformerException(href + " not found");
            Document xslDoc = null;
            try {
                xslDoc = dBuilder.parse(new ByteArrayInputStream(xslt.xsl.getBytes()));
                DOMSource xslDomSource = new DOMSource(xslDoc);
                xslDomSource.setSystemId(href);
                return xslDomSource;
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        throw new TransformerException(href + " is not a digit");
    }

    private boolean isDigit(String href){
        int id = -1;
        try {
            id = Integer.parseInt(href);
        } catch (NumberFormatException e) {
            return false;
        }
        return (id >= 0);
    }
}
