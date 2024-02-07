package com.example.backend.modules.character;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PersonResponseDto {
    private Long id;
    private String name;
    private String detail;
    private double positionX;
    private double positionY;

    public static PersonResponseDto of(Person person) {
        return PersonResponseDto.builder()
                .id(person.getId())
                .name(person.getCharacterName())
                .detail(person.getCharacterDetail())
                .positionX(person.getPositionX())
                .positionY(person.getPositionY())
                .build();
    }
}
