package com.game.service;

import com.game.controller.PlayerOrder;
import com.game.model.Player;
import com.game.repository.PlayerRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class PlayerServiceImpl implements PlayerService {
    private PlayerRepository repository;

    @Override
    public List<Player> getPlayers() {
        List<Player> players = new ArrayList<>();
        players.addAll((Collection<? extends Player>) repository.findAll());
        return players;
    }

    @Override
    public void create(Player player) {

    }

    @Override
    public boolean update(Player player) {
        return false;
    }

    @Override
    public boolean delete(Player player) {
        return false;
    }

    @Override
    public Player getPlayer(Long id) {
        return null;
    }

    @Override
    public List<Player> sortedPlayers(List<Player> players, PlayerOrder order) {
        return null;
    }

    @Override
    public Long countPlayers(List<Player> players, PlayerOrder order) {
        return null;
    }
}
