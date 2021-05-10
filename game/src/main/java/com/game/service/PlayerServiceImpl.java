package com.game.service;

import com.game.controller.PlayerOrder;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.entity.Player;
import com.game.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class PlayerServiceImpl implements PlayerService {

    private PlayerRepository playerRepository;

    public PlayerServiceImpl() {
    }

    @Autowired
    public PlayerServiceImpl(PlayerRepository playerRepository) {
        super();
        this.playerRepository = playerRepository;
    }

    @Override
    public List<Player> getPlayers(
            String name,
            String title,
            Race race,
            Profession profession,
            Integer experience,
            Integer level,
            Integer untilNextLevel,
            Boolean banned
    ) {
        List<Player> players = new ArrayList<>();
        playerRepository.findAll().forEach((player) -> {
            if (name != null && !player.getName().contains(name)) return;
            if (title != null && !player.getTitle().contains(title)) return;
            if (race != null && player.getRace() != race) return;
            if (profession != null && player.getProfession() != profession) return;
            if (banned != null && player.getBanned().booleanValue() != banned.booleanValue()) return;
            if (experience != null && player.getExperience().compareTo(experience) < 0) return;
            if (level != null && player.getLevel().compareTo(level) > 0) return;
            if (untilNextLevel != null && player.getUntilNextLevel().compareTo(untilNextLevel) < 0) return;

            players.add(player);
        });
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

    // Under consideration
    @Override
    public List<Player> getPage(List<Player> players, Integer pageNumber, Integer pageSize) {
        final Integer page = pageNumber == null ? 0 : pageNumber;
        final Integer size = pageSize == null ? 3 : pageSize;
        final int from = page * size;
        int to = from + size;
        if (to > players.size()) to = players.size();
        return players.subList(from, to);
    }
}
