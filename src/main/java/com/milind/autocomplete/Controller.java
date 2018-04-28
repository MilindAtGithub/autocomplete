package com.milind.autocomplete;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;


@RestController
public class Controller {
    @Autowired
    AutoComplete autoComplete;

    @RequestMapping(value = "/postjson", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    public String postjson(@RequestBody String json) throws Exception {

        System.out.println("Request: "+json);

        return autoComplete.process(json);
    }
}
