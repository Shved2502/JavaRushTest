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

    @RequestMapping(path = "/rest/players/{id}", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Player> updateShip(
            @PathVariable(value = "id") String pathId,
            @RequestBody Player player
    ) {
        final ResponseEntity<Player> entity = getPlayer(pathId);
        final Player savedPlayer = entity.getBody();
        if (savedPlayer == null) {
            return entity;
        }

        final Player result;
        try {
            result = playerService.update(savedPlayer, player);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


//--------------------------PASSED CODE--------------------------

    // Passed
    @RequestMapping(path = "/rest/players", method = RequestMethod.GET)
    public List<Player> getAllPlayers(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "race", required = false) Race race,
            @RequestParam(value = "profession", required = false) Profession profession,
            @RequestParam(value = "after", required = false) Long after,
            @RequestParam(value = "before", required = false) Long before,
            @RequestParam(value = "banned", required = false) Boolean banned,
            @RequestParam(value = "minExperience", required = false) Integer minExperience,
            @RequestParam(value = "maxExperience", required = false) Integer maxExperience,
            @RequestParam(value = "minLevel", required = false) Integer minLevel,
            @RequestParam(value = "maxLevel", required = false) Integer maxLevel,
            @RequestParam(value = "order", required = false) PlayerOrder order,
            @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", required = false) Integer pageSize
    ) {
        final List<Player> players = playerService.getPlayers(name, title, race, profession, after, before, banned,
                minExperience, maxExperience, minLevel, maxLevel);

        final List<Player> sortedPlayers = playerService.sortedPlayers(players, order);

        return playerService.getPage(sortedPlayers, pageNumber, pageSize);
    }

    // Passed
    @RequestMapping(path = "/rest/players/count", method = RequestMethod.GET)
    public Integer getShipsCount(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "race", required = false) Race race,
            @RequestParam(value = "profession", required = false) Profession profession,
            @RequestParam(value = "after", required = false) Long after,
            @RequestParam(value = "before", required = false) Long before,
            @RequestParam(value = "banned", required = false) Boolean banned,
            @RequestParam(value = "minExperience", required = false) Integer minExperience,
            @RequestParam(value = "maxExperience", required = false) Integer maxExperience,
            @RequestParam(value = "minLevel", required = false) Integer minLevel,
            @RequestParam(value = "maxLevel", required = false) Integer maxLevel
    ) {
        return playerService.getPlayers(name, title, race, profession, after, before, banned,
                minExperience, maxExperience, minLevel, maxLevel).size();
    }

    // Passed
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

        final Player savedPlayer = playerService.save(player);
        return new ResponseEntity<>(savedPlayer, HttpStatus.OK);
    }

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

    //Passed
    @RequestMapping(path = "/rest/players/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Player> deletePlayer(@PathVariable(value = "id") String pathId) {
        final ResponseEntity<Player> entity = getPlayer(pathId);
        final Player savedShip = entity.getBody();
        if (savedShip == null) {
            return entity;
        }
        playerService.delete(savedShip);
        return new ResponseEntity<>(HttpStatus.OK);
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
