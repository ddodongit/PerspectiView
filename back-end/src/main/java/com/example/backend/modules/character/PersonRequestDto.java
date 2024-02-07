package com.example.backend.modules.character;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PersonRequestDto {
    @NotNull
    private String name;

    private String detail;

    private double positionX;

    private double positionY;

    public static Person from(PersonRequestDto personRequestDto){
        return Person.positionBuilder()
                .characterName(personRequestDto.getName())
                .characterDetail(personRequestDto.getDetail())
                .positionX(personRequestDto.getPositionX())
                .positionY(personRequestDto.getPositionY())
                .build();
    }
}
