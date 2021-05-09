package com.game.controller;

import com.game.model.Player;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MyRestController {

    public static class RestResponse {

    }

    @RequestMapping(path = "/rest/players", method = RequestMethod.GET)
    public List<Player> getAllPlayers(String name) {
        return null;
    }
}
