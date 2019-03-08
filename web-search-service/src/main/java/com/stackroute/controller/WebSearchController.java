package com.stackroute.controller;


import com.stackroute.Service.WebSearchService;
import com.stackroute.domain.SearchDocument;
import com.stackroute.domain.UIDocument;
import com.stackroute.exception.DomainNotFoundException;
import com.stackroute.listener.KafkaProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;


@Controller
public class WebSearchController {


    private WebSearchService webSearchService;
    private UIDocument uiDocument;
    private KafkaProducer kafkaProducer;

    @Autowired
    public WebSearchController (WebSearchService webSearchService)
    {
        this.webSearchService=webSearchService;
    }

    @PostMapping("/domain")
    public ResponseEntity<String> domainUpload(@RequestBody UIDocument uiDocument1){

        String message="";
        uiDocument= new UIDocument();
        try
        {
            uiDocument.setDomain(uiDocument1.getDomain());
            uiDocument.setConceptName(uiDocument1.getConceptName());
            kafkaProducer.postservice(uiDocument);
            message="Successfully upload";
            return ResponseEntity.status(HttpStatus.OK).body(message);

        }
        catch(Exception e)
        {
            message="Failed to uplaod";
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(message);
        }
    }

    @GetMapping("/results")
    public ResponseEntity<List<SearchDocument>> searchResults() throws IOException
    {
        ResponseEntity responseEntity;
        try
        {
            List<SearchDocument> searchDocumentList;
            searchDocumentList=webSearchService.getUrls(uiDocument);
            responseEntity = new ResponseEntity<>(searchDocumentList, HttpStatus.OK);
        }
        catch(DomainNotFoundException e)
        {
            responseEntity =new ResponseEntity<>("No results found.", HttpStatus.EXPECTATION_FAILED);
        }
        return responseEntity;

    }

}