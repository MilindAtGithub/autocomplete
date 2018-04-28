package com.milind.autocomplete;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriTemplate;

import java.util.*;

@Component
public class AutoComplete {



    public String process(String str) throws  Exception{

        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:9200/tagindex/tag_type/_search?pretty";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String string1="{\n" +
                "\"from\" : 0, \"size\" : 10,"+
                "\"query\": {\n" +
                "        \"query_string\" : {\n" +
                "            \"default_field\" : \"tag\",\n" +
                "            \"query\" : \"(#1) \"\n" +
                "        }\n" +
                "    }\n" +
                "}";

        String string2="{\n" +
                "\"from\" : 0, \"size\" : 10,"+
                "\"query\": {\n" +
                "        \"query_string\" : {\n" +
                "            \"default_field\" : \"tag\",\n" +
                "            \"query\" : \"(#1) AND (#2) \"\n" +
                "        }\n" +
                "    }\n" +
                "}";
        String string3="{\n" +
                "\"from\" : 0, \"size\" : 10,"+
                "\"query\": {\n" +
                "        \"query_string\" : {\n" +
                "            \"default_field\" : \"tag\",\n" +
                "            \"query\" : \"(#1) AND (#2) AND (#3)\"\n" +
                "        }\n" +
                "    }\n" +
                "}";

        String dummy = null;
        str = str.replace("=","");
        str = str.replace("+"," ");
        if(str.length()<2){
            return "Enter More Character";
        }else {
            String split[] = str.split(" ");
            if(split.length>3){
                return "Enter three words only";
            }else if(split.length==1){
                dummy = string1.replace("#1",str);
            }else if(split.length==2){
                dummy = string2.replace("#1",split[0]);
                dummy = dummy.replace("#2",split[1]);
            }else if(split.length==3){
                dummy = string3.replace("#1",split[0]);
                dummy = dummy.replace("#2",split[1]);
                dummy = dummy.replace("#3",split[2]);
            }
        }
        System.out.println(dummy);
        HttpEntity<String> entity = new HttpEntity<>(dummy, headers);
        ResponseEntity<String> s  = restTemplate.postForEntity(url, entity, String.class);
        String json = s.getBody();
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = new HashMap<String, Object>();
        map = mapper.readValue(json, new TypeReference<Map<String, Object>>(){});
        List<Map<String, Object>> valmap = (List<Map<String, Object>>) ((Map<String, Object>)map.get("hits")).get("hits");
        StringBuilder sb = new StringBuilder();
        List<String> l = new ArrayList<>();
        for(Map m :valmap){

            String val = ((Map<String, String>)m.get("_source")).get("tag");
            if(! l.contains(val)){
                System.out.println(val);

                sb.append(val);
                sb.append("<br>");
            }
            l.add(val);


        }
        return sb.toString();
    }
}
