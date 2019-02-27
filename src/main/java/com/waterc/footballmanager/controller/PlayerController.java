package com.waterc.footballmanager.controller;

import com.waterc.footballmanager.model.PlayerDto;
import com.waterc.footballmanager.service.PlayerService;
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
@RequestMapping(path = "/api/players")
public class PlayerController {

    private final PlayerService playerService;

    @Autowired
    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping
    public ResponseEntity<List<PlayerDto>> geAll() {
        return new ResponseEntity<>(playerService.getAll(), HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<PlayerDto> getOne(@PathVariable Long id) {
        return new ResponseEntity<>(playerService.findOne(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<PlayerDto> createPlayer(@RequestBody PlayerDto playerDto) {
        return new ResponseEntity<>(playerService.addPlayer(playerDto), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<PlayerDto> updatePlayer(@RequestBody PlayerDto playerDto) {
        return new ResponseEntity<>(playerService.updatePlayer(playerDto), HttpStatus.RESET_CONTENT);
    }

    @DeleteMapping
    public ResponseEntity removePlayer(@PathVariable Long playerId) {
        playerService.removePlayer(playerId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
