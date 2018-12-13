package com.kiddyMarket.Logic.Helper;

import com.google.gson.Gson;
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

    public <T> ResponseEntity<T> callWithStatusCheck(String url, Object data, Class<T> type, HttpMethod method, boolean checkHeader){
        ResponseEntity<T> response = call(url, data, type, method, checkHeader);

        //check if status code is correct
        if(response.getStatusCode() != HttpStatus.OK){
            throw new IllegalArgumentException("something went wrong with the API call");
        }

        return response;
    }

    public <T> ResponseEntity<T> call(String url, Object data, Class<T> type, HttpMethod method, boolean checkHeader){
        HttpHeaders headers = new HttpHeaders();
        Gson gson = new Gson();

        headers.setContentType(MediaType.APPLICATION_JSON);

        if(checkHeader){
            //check if auth header exists
            if(request.getHeader(AUTHHEADER).isEmpty()){
                throw new IllegalArgumentException("No " + AUTHHEADER + " header found");
            }

            //set the requested headers
            headers.set(AUTHHEADER, request.getHeader(AUTHHEADER));
        }

        //if the call needs a body it will be constructed here
        HttpEntity<?> httpEntity;
        if(data == null){
            httpEntity = new HttpEntity<>("", headers);
        }else{
            httpEntity = new HttpEntity<>(gson.toJson(data), headers);
        }

        //make the rest call
        RestTemplate restCall = new RestTemplate();
        return restCall.exchange(url, method, httpEntity, type);
    }
}
