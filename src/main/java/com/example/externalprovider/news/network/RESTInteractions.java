package com.example.externalprovider.news.network;

import com.example.externalprovider.news.helper.MessageUtil;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.StringJoiner;

@Component
public class RESTInteractions {
    private final String API_KEY;
    private String API_URL = "";
    private RestTemplate restTemplate;

    public RESTInteractions(RestTemplate restTemplate,
                            Environment env) {
        this.restTemplate = restTemplate;
        API_KEY = env.getProperty("API_KEY");
    }

    public <T extends ResponseType> ResponseType restResponse(String endPoint, Map<String, String> params, Class<T> clazz) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Api-Key", API_KEY);

        HttpEntity<String> entity = new HttpEntity<>("body", headers);

        String url = urlMaker(endPoint, params);

        return responseMaker(url,entity,clazz);
    }

    private String urlMaker(String endPoint, Map<String, String> params) {
        API_URL = MessageUtil.getMessage("news.api.url");
        String url = API_URL + endPoint;

        if (params != null && !params.isEmpty()) {
            StringJoiner joiner = new StringJoiner("&");
            for (Map.Entry<String, String> entry : params.entrySet()) {
                joiner.add(entry.getKey() + "=" + entry.getValue());
            }
            url += "?" + joiner.toString() + "&apiKey=" + API_KEY;
        }

        return url;
    }

    private <T extends ResponseType> ResponseType responseMaker(String url, HttpEntity<String> entity, Class<T> clazz) {

        if (ArticleResponse.class.isAssignableFrom(clazz)) {
            ResponseEntity<ArticleResponse> response = restTemplate.exchange(
                    url, HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<ArticleResponse>() {
                    }
            );

            ArticleResponse articleResponse = new ArticleResponse();
            articleResponse.setArticles(response.getBody().getArticles());
            articleResponse.setStatus(response.getBody().getStatus());
            articleResponse.setTotalResults(response.getBody().getTotalResults());

            return articleResponse;
        } else if (SourceResponse.class.isAssignableFrom(clazz)) {
            ResponseEntity<SourceResponse> response = restTemplate.exchange(
                    url, HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<SourceResponse>() {
                    }
            );

            SourceResponse sourceResponse = new SourceResponse();
            sourceResponse.setSources(response.getBody().getSources());
            sourceResponse.setStatus(response.getBody().getStatus());
            sourceResponse.setTotalResults(response.getBody().getTotalResults());

            return sourceResponse;

//            return (List<BaseResponse>) (List<?>) response.getBody().getSources();
        }
        return null;

    }
}
