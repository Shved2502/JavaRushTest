package com.game.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyRestController {

    public static class RestResponse {

    }

    @RequestMapping(value = "/rest/players", method = RequestMethod.GET)
    public RestResponse restMethod(String name) {
        return null;
    }
}
