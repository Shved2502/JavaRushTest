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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class PlayerServiceImpl implements PlayerService {

    // Checked
    private PlayerRepository playerRepository;

    // Checked
    public PlayerServiceImpl() {
    }

    // Checked
    @Autowired
    public PlayerServiceImpl(PlayerRepository playerRepository) {
        super();
        this.playerRepository = playerRepository;
    }

    // Passed
    @Override
    public List<Player> getPlayers(
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
    ) {
        final Date afterDate = after == null ? null : new Date(after);
        final Date beforeDate = before == null ? null : new Date(before);
        List<Player> players = new ArrayList<>();
        playerRepository.findAll().forEach((player) -> {
            if (name != null && !player.getName().contains(name)) return;
            if (title != null && !player.getTitle().contains(title)) return;
            if (race != null && player.getRace() != race) return;
            if (profession != null && player.getProfession() != profession) return;
            if (afterDate != null && player.getBirthday().before(afterDate)) return;
            if (beforeDate != null && player.getBirthday().after(beforeDate)) return;
            if (banned != null && player.getBanned().booleanValue() != banned.booleanValue()) return;
            if (minExperience != null && player.getExperience().compareTo(minExperience) < 0) return;
            if (maxExperience != null && player.getExperience().compareTo(maxExperience) > 0) return;
            if (minLevel != null && player.getLevel().compareTo(minLevel) < 0) return;
            if (maxLevel != null && player.getLevel().compareTo(maxLevel) > 0) return;

            players.add(player);
        });
        return players;
    }

    @Override
    public Player update(Player lastPlayer, Player newPlayer) {
        boolean shouldChangeLevel = false;

        final String name = newPlayer.getName();
        if (name != null) {
            if (isNameValid(name)) {
                lastPlayer.setName(name);
            } else {
                throw new IllegalArgumentException();
            }
        }
        final String title = newPlayer.getTitle();
        if (title != null) {
            if (isTitleValid(title)) {
                lastPlayer.setTitle(title);
            } else {
                throw new IllegalArgumentException();
            }
        }
        if (newPlayer.getRace() != null) {
            lastPlayer.setRace(newPlayer.getRace());
        }
        if (newPlayer.getProfession() != null) {
            lastPlayer.setProfession(newPlayer.getProfession());
        }
        final Date birthday = newPlayer.getBirthday();
        if (birthday != null) {
            if (isBirthdayValid(birthday)) {
                lastPlayer.setBirthday(birthday);
                shouldChangeLevel = true;
            } else {
                throw new IllegalArgumentException();
            }
        }
        if (newPlayer.getBanned() != null) {
            lastPlayer.setBanned(newPlayer.getBanned());
            shouldChangeLevel = true;
        }
        final Integer experience = newPlayer.getExperience();
        if (experience != null) {
            if (isExperienceValid(experience)) {
                lastPlayer.setExperience(experience);
                shouldChangeLevel = true;
            } else {
                throw new IllegalArgumentException();
            }
        }
        if (shouldChangeLevel) {
            final Integer level = calculateLevel(lastPlayer.getExperience());
            lastPlayer.setLevel(level);
            final Integer untilNextLevel = calculateUntilNextLevel(level, lastPlayer.getExperience());
            lastPlayer.setUntilNextLevel(untilNextLevel);
        }
        playerRepository.save(lastPlayer);
        return lastPlayer;
    }

    // Passed
    @Override
    public Player save(Player player) {
        return playerRepository.save(player);
    }

    //Passed
    @Override
    public void delete(Player player) {
        playerRepository.delete(player);
    }

    //Passed
    @Override
    public Player getPlayer(Long id) {
        return playerRepository.findById(id).orElse(null);
    }

    @Override
    public List<Player> sortedPlayers(List<Player> players, PlayerOrder order) {
        if (order != null) {
            players.sort((player1, player2) -> {
                switch (order) {
                    case ID: return player1.getId().compareTo(player2.getId());
                    case NAME: return player1.getName().compareTo(player2.getName());
                    case EXPERIENCE: return player1.getExperience().compareTo(player2.getExperience());
                    case BIRTHDAY: return player1.getBirthday().compareTo(player2.getBirthday());
                    case LEVEL: return player1.getLevel().compareTo(player2.getLevel());
                    default: return 0;
                }
            });
        }
        return players;
    }

    // Checked
    @Override
    public List<Player> getPage(List<Player> players, Integer pageNumber, Integer pageSize) {
        final Integer page = pageNumber == null ? 0 : pageNumber;
        final Integer size = pageSize == null ? 3 : pageSize;
        final int from = page * size;
        int to = from + size;
        if (to > players.size()) to = players.size();
        return players.subList(from, to);
    }

    // Checked
    @Override
    public boolean isPlayerValid(Player player) {
        return player != null && isNameValid(player.getName())
                && isTitleValid(player.getTitle())
                && isExperienceValid(player.getExperience())
                && isBirthdayValid(player.getBirthday());
    }

    // Passed
    @Override
    public Integer calculateLevel(Integer experience) {
        final double result = (Math.sqrt(2500 + 200 * experience) - 50 ) / 100;
        return (int) result;
    }

    // Passed
    @Override
    public Integer calculateUntilNextLevel(Integer level, Integer experience) {
        return 50 * (level + 1) * (level + 2) - experience;
    }

    // Checked
    public boolean isNameValid(String name) {
        final int maxNameLength = 12;
        return name != null && !name.isEmpty() && name.length() <= maxNameLength;
    }

    // Checked
    public boolean isTitleValid(String title) {
        final int maxTitleLength = 30;
        return title != null && !title.isEmpty() && title.length() <= maxTitleLength;
    }

    // Checked
    public boolean isExperienceValid(Integer experience) {
        final Integer minExperienceValue = 0;
        final Integer maxExperienceValue = 10_000_000;
        return experience != null
                && experience.compareTo(minExperienceValue) >= 0
                && experience.compareTo(maxExperienceValue) <=0;
    }

    // Checked
    private boolean isBirthdayValid(Date birthday) {
        final Date minBirthday = getDateForYear(2000);
        final Date maxBirthday = getDateForYear(3000);
        return birthday != null && birthday.after(minBirthday) && birthday.before(maxBirthday);
    }

    // Checked
    private Date getDateForYear(int year) {
        final Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        return calendar.getTime();
    }
}
