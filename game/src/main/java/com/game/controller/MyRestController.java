package com.game.controller;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
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
            @RequestParam(value = "pageSize", required = false) Integer pageSize,
            @RequestParam(value = "birthday", required = false) Date birthday
            ) {
        final List<Player> players = playerService.getPlayers(name, title, race, profession,
                experience, level, untilNextLevel, birthday,  banned);

        return playerService.getPage(players, pageNumber, pageSize);
    }

    @RequestMapping(path = "/rest/players", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Player> createPlayer(@RequestBody Player player) {
        if (!playerService.isPlayerValid(player)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (player.getBanned() == null) player.setBanned(false);
        player.setRace(player.getRace());
        player.setProfession(player.getProfession());
        player.setLevel(playerService.calculateLevel(player.getExperience()));
        player.setUntilNextLevel(playerService.calculateUntilNextLevel(player.getLevel(), player.getExperience()));

        final Player savedPlayer = playerService.create(player);
        return new ResponseEntity<>(savedPlayer, HttpStatus.OK);
    }


//--------------------------PASSED CODE--------------------------

    //Passed
    @RequestMapping(path = "/rest/players/{id}", method = RequestMethod.GET)
    public ResponseEntity<Player> getPlayer(@PathVariable(value = "id") String pathId) {
        final Long id = convertIdToLong(pathId);
        if (id == null || id <= 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        final Player player = playerService.getPlayer(id);
        if (player == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(player, HttpStatus.OK);
    }

    //Final version
    private Long convertIdToLong(String pathId) {
        if (pathId == null) {
            return null;
        } else try {
            return Long.parseLong(pathId);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
