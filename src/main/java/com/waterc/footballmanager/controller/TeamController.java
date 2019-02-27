package com.waterc.footballmanager.controller;

import com.waterc.footballmanager.model.PlayerDto;
import com.waterc.footballmanager.model.TeamDto;
import com.waterc.footballmanager.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/api/teams")
public class TeamController {
    protected final TeamService teamService;

    @Autowired
    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<TeamDto> getTeam(@PathVariable Long id) {
        return new ResponseEntity<>(teamService.getTeam(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<TeamDto>> geAllTeam() {
        return new ResponseEntity<>(teamService.getAll(), HttpStatus.OK);
    }

    @GetMapping(path = "/{teamId}/captain")
    public ResponseEntity<PlayerDto> getCaptain(@PathVariable Long teamId) {
        return new ResponseEntity<>(teamService.getCaptain(teamId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<TeamDto> createTeam(@RequestBody TeamDto teamDto) {
        return new ResponseEntity<>(teamService.addTeam(teamDto), HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/{teamId}")
    public ResponseEntity deleteTeam(@PathVariable Long teamId) {
        teamService.removeTeam(teamId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PutMapping(path = "/{teamId}/assign/{playerId}/captain")
    public ResponseEntity assignCaptain(@PathVariable Long teamId, @PathVariable Long playerId) {
        teamService.assignCaptain(teamId, playerId);
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

    @PutMapping(path = "/{teamId}/assign/{playerId}")
    public ResponseEntity assignPlayer(@PathVariable Long teamId, @PathVariable Long playerId) {
        teamService.assignPlayer(teamId, playerId);
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

    @PutMapping(path = "/{teamId}/add/player/{playerId}")
    public ResponseEntity addPlayer(@PathVariable Long teamId, @PathVariable Long playerId) {
        teamService.addPlayer(teamId, playerId);
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }
}
