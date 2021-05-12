package com.game.service;

import com.game.controller.PlayerOrder;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.entity.Player;

import java.util.List;

public interface PlayerService {

    List<Player> getPlayers(
            String name,
            String title,
            Race race,
            Profession profession,
            Long after,
            Long before,
            Boolean banned,
            Integer minExperience,
            Integer maxExperience,
            Integer minLevel,
            Integer maxLevel
    );

    Player save(Player player);

    Player update(Player lastPlayer, Player newPlayer);

    void delete(Player player);

    Player getPlayer(Long id);

    List<Player> sortedPlayers(List<Player> players, PlayerOrder order);

    List<Player> getPage(List<Player> ships, Integer pageNumber, Integer pageSize);

    boolean isPlayerValid(Player player);

    Integer calculateLevel(Integer experience);

    Integer calculateUntilNextLevel(Integer level, Integer experience);
}
