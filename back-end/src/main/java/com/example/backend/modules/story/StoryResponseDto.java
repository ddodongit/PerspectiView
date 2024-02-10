package com.example.backend.modules.story;

import com.example.backend.modules.character.Character;
import com.example.backend.modules.character.CharacterResponseDto;
import com.example.backend.modules.foreshadowing.ForeShadowing;
import com.example.backend.modules.foreshadowing.ForeShadowingPreviewResponseDto;
import com.example.backend.modules.foreshadowing.ForeShadowingResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class StoryResponseDto {
    private Long storyId;
    private String storyTitle;
    private List<CharacterResponseDto> characters;
    private List<ForeShadowingPreviewResponseDto> foreShadowings;
    private ContentDto content;
    private int positionX;
    private Double positionY;

    public static StoryResponseDto of(Story story, List<Character> characters, List<ForeShadowing> foreShadowings) {
        return StoryResponseDto.builder()
                .storyId(story.getId())
                .storyTitle(story.getTitle())
                .characters(characters.stream().map(CharacterResponseDto::of).collect(Collectors.toList()))
                .foreShadowings(foreShadowings.stream().map(ForeShadowingPreviewResponseDto::of).collect(Collectors.toList()))
                .content(ContentDto.of(story.getContent()))
                .positionX(story.getPositionX())
                .positionY(story.getPositionY())
                .build();
    }
}
