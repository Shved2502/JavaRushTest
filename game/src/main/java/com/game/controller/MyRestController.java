package com.game.controller;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MyRestController {

    // Checked
    private PlayerService playerService;

    // Checked
    public MyRestController() {
    }

    // Checked
    @Autowired
    public MyRestController(PlayerService playerService) {
        this.playerService = playerService;
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
        final List<Player> players = playerService.getPlayers(name, title, race, profession, experience, level, untilNextLevel, banned);

        return playerService.getPage(players, pageNumber, pageSize);
    }

    @RequestMapping(path = "/rest/players", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Player> createPlayer(@RequestBody Player player) {
        if (!playerService.isPlayerValid(player)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (player.getBanned() == null) player.setBanned(false);
        final Player savedPlayer = playerService.create(player);

        return new ResponseEntity<>(savedPlayer, HttpStatus.OK);
    }
}
