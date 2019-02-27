package com.waterc.footballmanager.service;

import com.waterc.footballmanager.entity.Player;
import com.waterc.footballmanager.exceptions.BrokenRequestDataException;
import com.waterc.footballmanager.exceptions.EntityNotFoundException;
import com.waterc.footballmanager.model.PlayerDto;
import com.waterc.footballmanager.repository.PlayerRepository;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Service
public class PlayerService {
    private final PlayerRepository playerRepository;
    private final ModelMapper mapper;

    @Autowired
    public PlayerService(PlayerRepository playerRepository, ModelMapper mapper) {
        this.playerRepository = playerRepository;
        this.mapper = mapper;

        mapper.addConverter((Converter<Player, PlayerDto>) mappingContext -> {
            Player playerEntity = mappingContext.getSource();
            BeanUtils.copyProperties(playerEntity, mappingContext.getDestination());
            return mappingContext.getDestination();
        });
    }

    @Transactional(readOnly = true)
    public List<PlayerDto> getAll() {
        return playerRepository.findAll()
                .stream()
                .map(player -> mapper.map(player, PlayerDto.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PlayerDto findOne(Long id) {
        return playerRepository.findById(id)
                .map(player -> mapper.map(player, PlayerDto.class))
                .orElseThrow(() -> new EntityNotFoundException(format("No Player with id: %s", id)));
    }

    @Transactional
    public PlayerDto addPlayer(PlayerDto playerDto) {
        if (playerDto.getId() != null) {
            throw new BrokenRequestDataException("Player json id should be null");
        }
        Player player = mapper.map(playerDto, Player.class);
        playerRepository.save(player);

        return  mapper.map(player, PlayerDto.class);
    }

    @Transactional
    public PlayerDto updatePlayer(PlayerDto playerDto) {
        return playerRepository.findById(playerDto.getId())
                .map(p -> {
                    Player player = mapper.map(playerDto, Player.class);
                    playerRepository.save(player);

                    return mapper.map(player, PlayerDto.class);
                })
                .orElseThrow(() -> new BrokenRequestDataException(format("No Player with id: %s", playerDto.getId())));
    }

    @Transactional
    public void removePlayer(Long playerId) {
        playerRepository.deleteById(playerId);
    }
}
