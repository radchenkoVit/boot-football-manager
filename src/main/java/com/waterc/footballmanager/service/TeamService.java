package com.waterc.footballmanager.service;

import com.waterc.footballmanager.entity.Player;
import com.waterc.footballmanager.entity.Team;
import com.waterc.footballmanager.exceptions.EntityNotFoundException;
import com.waterc.footballmanager.model.PlayerDto;
import com.waterc.footballmanager.model.TeamDto;
import com.waterc.footballmanager.repository.PlayerRepository;
import com.waterc.footballmanager.repository.TeamRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Service
public class TeamService {
    private final TeamRepository teamRepository;
    private final PlayerRepository playerRepository;
    private final ModelMapper mapper;

    @Autowired
    public TeamService(TeamRepository teamRepository, ModelMapper mapper, PlayerRepository playerRepository) {
        this.teamRepository = teamRepository;
        this.mapper = mapper;
        this.playerRepository = playerRepository;
    }

    @Transactional(readOnly = true)
    public List<TeamDto> getAll() {
        return teamRepository.findAll()
                .stream()
                .map(team -> mapper.map(team, TeamDto.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TeamDto getTeam(Long id) {
        return mapper.map(findTeam(id), TeamDto.class);
    }

    @Transactional(readOnly = true)
    public PlayerDto getCaptain(Long teamId) {
        Team team = findTeam(teamId);
        Player captain = playerRepository
                                    .findById(team.getCaptainId())
                                    .orElseThrow(() -> new EntityNotFoundException(format("No captain for team with id: %s", teamId)));
        return mapper.map(captain, PlayerDto.class);
    }

    @Transactional
    public TeamDto addTeam(TeamDto teamDto) {
        Team team = mapper.map(teamDto, Team.class);
        teamRepository.save(team);

        return mapper.map(team, TeamDto.class);
    }

    @Transactional
    public void assignCaptain(Long teamId, Long captainId) {
        Player captainPlayer = findPlayer(captainId);
        Team newTeam = findTeam(teamId);

        if (captainPlayer.getTeam() != null && !Objects.equals(captainPlayer.getTeam().getId(), teamId)) {
            Team oldTeam = captainPlayer.getTeam();
            oldTeam.setCaptainId(null);

            captainPlayer.setTeam(newTeam);
            newTeam.addPlayer(captainPlayer);
        }

        newTeam.setCaptainId(captainPlayer.getId());
    }

    @Transactional
    public void assignPlayer(Long teamId, Long playerId) {
        Player player = findPlayer(playerId);
        Team team = findTeam(teamId);

        player.setTeam(team);
        team.addPlayer(player);
    }

    @Transactional
    public void removeTeam(Long id) {
        if (teamRepository.existsById(id)) {
            Team team = findTeam(id);
            team.getPlayers().forEach(player -> player.setTeam(null));
            teamRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException(format("Team with id:%s not found", id));
        }
    }

    @Transactional
    public void addPlayer(Long teamId, Long playerId) {
        Team team = findTeam(teamId);
        Player player = findPlayer(playerId);

        team.addPlayer(player);
    }

    private Team findTeam(Long id) {
        return teamRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(format("Team with id:%s not found", id)));
    }

    private Player findPlayer(Long id) {
        return playerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(format("Player with id:%s not found", id)));
    }
}
