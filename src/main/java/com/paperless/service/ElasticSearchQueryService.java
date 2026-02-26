package com.paperless.service;

import com.paperless.model.SearchResult;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.Map;

@Service
public class ElasticSearchQueryService {

    private final WebClient webClient;

    public ElasticSearchQueryService(WebClient.Builder builder,
            @Value("${paperless.elasticsearch.url}") String elasticUrl
    ) {
        this.webClient = builder
                .baseUrl(elasticUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public List<SearchResult> search(String query) {

        String body = """
        {
          "query": {
            "multi_match": {
              "query": "%s",
              "fields": ["ocrText", "summary"],
              "fuzziness": "AUTO"
            }
          }
        }
        """.formatted(query);

        Map<String, Object> response = webClient.post()
                .uri("/paperless/_search")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        Map<String, Object> hits = (Map<String, Object>) response.get("hits");
        List<Map<String, Object>> hitList = (List<Map<String, Object>>) hits.get("hits");

        return hitList.stream()
                .map(hit -> {
                    Map<String, Object> src = (Map<String, Object>) hit.get("_source");
                    return new SearchResult(
                            (String) hit.get("_id"),
                            (String) src.get("summary")
                    );
                })
                .toList();
    }
}
