package com.game.controller;

import com.game.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyRestController {

    private PlayerService service;

    public MyRestController() {
    }

    @Autowired
    public MyRestController(PlayerService service) {
        this.service = service;
    }

    @RequestMapping(path = "/rest/players", method = RequestMethod.GET)
    public List<Player> getAllPlayers(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "race", required = false) Race race,
            @RequestParam(value = "profession", required = false) Profession profession,
            @RequestParam(value = "experience", required = false) Integer experience,
            @RequestParam(value = "level", required = false) Integer level,
            @RequestParam(value = "untilNextLevel", required = false) Integer untilNextLevel,
            @RequestParam(value = "banned", required = false) Boolean banned,
            @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", required = false) Integer pageSize
            ) {
        final List<Player> players = service.getPlayers(name, title, race, profession, experience, level, untilNextLevel, banned);

        return service.getPage(players, pageNumber, pageSize);
    }
}
