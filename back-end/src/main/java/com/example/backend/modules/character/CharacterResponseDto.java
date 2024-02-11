package com.example.backend.modules.character;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CharacterResponseDto {
    private Long id;
    private String name;
    private String detail;
    private double positionX;
    private double positionY;

    public static CharacterResponseDto of(Character character) {
        return CharacterResponseDto.builder()
                .id(character.getId())
                .name(character.getCharacterName())
                .detail(character.getCharacterDetail())
                .positionX(character.getPositionX())
                .positionY(character.getPositionY())
                .build();
    }
}
