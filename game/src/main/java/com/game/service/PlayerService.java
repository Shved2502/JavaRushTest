package com.game.service;

import com.game.controller.PlayerOrder;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.model.Player;

import java.util.List;

public interface PlayerService {

    List<Player> getPlayers(
            String name,
            String title,
            Race race,
            Profession profession,
            Integer experience,
            Integer level,
            Integer untilNextLevel,
            Boolean banned
    );

    void create(Player player);

    boolean update(Player player);

    boolean delete(Player player);

    Player getPlayer(Long id);

    List<Player> sortedPlayers(List<Player> players, PlayerOrder order);

    // Under consideration
    Long countPlayers(List<Player> players, PlayerOrder order);

    List<Player> getPage(List<Player> ships, Integer pageNumber, Integer pageSize);
}
