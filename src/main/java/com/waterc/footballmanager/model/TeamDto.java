package com.waterc.footballmanager.model;

import lombok.Data;

import java.util.List;

@Data
public class TeamDto {
    private Long id;
    private String name;
    private Long captainId;
    private List<PlayerDto> players;
}
