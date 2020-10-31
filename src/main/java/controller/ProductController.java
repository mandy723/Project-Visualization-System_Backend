package controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.xml.ws.Response;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class ProductController{

    @GetMapping("/products/{id}")
    public String getProduct(@PathVariable("id") String id) throws IOException {
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.setErrorHandler(new CustomErrorHandler());

            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer 036075e2e957735ed448e88f3d700dbc50564fe6");
//            headers.add("content-type", "application/json"); // just modified graphql into json

        Map<String, Object> params = new HashMap<>();
//        params.put("query", "{repository(owner: \"facebook\", name: \"react\") { description }}");

        params.put("query", "{repository(owner: \"facebook\", name:\"react\") {" +
                                "defaultBranchRef {" +
                                    "target {" +
                                        "... on Commit {" +
                                            "history (first:100) {" +
                                                "nodes {" +
                                                    "committedDate" +
                                                "}" +
                                            "}" +
                                        "}" +
                                    "}" +
                                "}" +
                            "}}");

//        String query = "{\"query\": \"{"+
//            "repository(owner: \"facebook\", name:\"react\") {"+
//                "description"+
//            "}"+
//        "}}";

//            String query = "query: query{" +
//                                "repository(owner: \"facebook\", name:\"react\") {" +
//                                    "defaultBranchRef {" +
//                                        "target {" +
//                                            "... on Commit {" +
//                                                "history (first:100) {" +
//                                                    "nodes {" +
//                                                        "committedDate" +
//                                                    "}" +
//                                                "}" +
//                                            "}" +
//                                        "}" +
//                                    "}" +
//                                "}" +
//                            "}";


            String URL = "https://api.github.com/graphql";


            ResponseEntity<String> response = restTemplate.postForEntity(URL, new HttpEntity<>(params, headers), String.class);
            System.out.println("The response=================" + response);

        System.out.println(response.getBody());
        return response.getBody();
    }

}

class CustomErrorHandler implements ResponseErrorHandler {

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return true;
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {

    }
}
