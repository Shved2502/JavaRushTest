package com.game.service;

import com.game.controller.PlayerOrder;
import com.game.model.Player;

import java.util.List;

public interface PlayerService {
    List<Player> getPlayers();

    void create(Player player);

    boolean update(Player player);

    boolean delete(Player player);

    Player getPlayer(Long id);

    List<Player> sortedPlayers(List<Player> players, PlayerOrder order);

    // Under consideration
    Long countPlayers(List<Player> players, PlayerOrder order);
}
