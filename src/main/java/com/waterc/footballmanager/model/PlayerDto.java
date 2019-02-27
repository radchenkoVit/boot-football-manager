package com.waterc.footballmanager.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties()
public class PlayerDto {
    private Long id;
    private String firstName;
    private String position;

    private long teamId;
}
