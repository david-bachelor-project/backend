package com.paperless.controller;

import com.paperless.model.SearchResult;
import com.paperless.service.ElasticSearchQueryService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/documents/search")
public class SearchController {

    private final ElasticSearchQueryService es;

    public SearchController(ElasticSearchQueryService es) {
        this.es = es;
    }

    @GetMapping
    public List<SearchResult> search(@RequestParam String q) {
        return es.search(q);
    }
}
