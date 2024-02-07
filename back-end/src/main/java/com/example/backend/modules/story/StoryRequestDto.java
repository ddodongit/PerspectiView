package com.example.backend.modules.story;

import com.example.backend.modules.character.Person;
import com.example.backend.modules.foreshadowing.ForeShadowing;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class StoryRequestDto {
    private String storyTitle;
    private String storyContent;
    private List<Person> people;
    private List<ForeShadowing> foreShadowings;
    private int positionX;
    private Double positionY;

    public static Story of(StoryRequestDto storyRequestDto) {
        return Story.builder()
                .title(storyRequestDto.getStoryTitle())
                .positionX(storyRequestDto.getPositionX())
                .positionY(storyRequestDto.getPositionY())
                .build();
    }
}
