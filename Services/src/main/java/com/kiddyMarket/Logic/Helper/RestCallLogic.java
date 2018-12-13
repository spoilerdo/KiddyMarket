package com.kiddyMarket.Logic.Helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;

import java.net.URI;

import static com.kiddyMarket.Logic.Constants.APIConstants.AUTHHEADER;

@Service
public class RestCallLogic {
    private HttpServletRequest request;

    @Autowired
    public RestCallLogic(HttpServletRequest request){
        this.request = request;
    }

    public <T> ResponseEntity<T> postCall(String url, String data, Class<T> type){
        //build-in spring uri builder to avoid uri-syntax-exception handling
        URI uri = UriComponentsBuilder.fromUriString(url).build().toUri();

        //set the requested headers and fill in the body
        RequestEntity<String> request = RequestEntity
                .post(uri)
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHHEADER, this.request.getHeader(AUTHHEADER))
                .body(data);

        //post call
        RestTemplate restCall = new RestTemplate();
        ResponseEntity<T> response = restCall.exchange(request, type);

        //check if status code is correct
        if(response.getStatusCode() != HttpStatus.OK){
            throw new IllegalArgumentException(response.getBody().toString());
        }

        return response;
    }

    public <T> ResponseEntity<T> getCallWithStatusCheck(String url, Class<T> type){
        ResponseEntity<T> response = getCall(url, type);

        //check if status code is correct
        if(response.getStatusCode() != HttpStatus.OK){
            throw new IllegalArgumentException(response.getBody().toString());
        }

        return response;
    }

    public <T> ResponseEntity<T> getCall(String url, Class<T> type){
        //check if auth header exists
        if(request.getHeader(AUTHHEADER).isEmpty()){
            throw new IllegalArgumentException("No " + AUTHHEADER + " header found");
        }

        //set the requested headers
        HttpHeaders headers = new HttpHeaders();
        headers.set(AUTHHEADER, request.getHeader(AUTHHEADER)); //TODO: als de get call niet werkt dan is dit waarschijnlijk fout

        HttpEntity<?> httpEntity = new HttpEntity<>("", headers);

        //get call
        RestTemplate restCall = new RestTemplate();
        return restCall.exchange(url, HttpMethod.GET, httpEntity, type);
    }
}
