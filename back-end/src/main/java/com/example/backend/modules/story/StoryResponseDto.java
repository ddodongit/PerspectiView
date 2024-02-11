package com.example.backend.modules.story;

import com.example.backend.modules.character.Character;
import com.example.backend.modules.character.CharacterResponseDto;
import com.example.backend.modules.foreshadowing.ForeShadowing;
import com.example.backend.modules.foreshadowing.ForeShadowingPreviewResponseDto;
import com.example.backend.modules.foreshadowing.ForeShadowingResponseDto;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@RedisHash("storyId")
public class StoryResponseDto implements Serializable {
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
                .content(com.example.backend.modules.story.ContentDto.of(story.getContent()))
                .positionX(story.getPositionX())
                .positionY(story.getPositionY())
                .build();
    }

    @JsonCreator
    public StoryResponseDto(@JsonProperty("storyId") Long storyId,
                            @JsonProperty("storyTitle") String storyTitle,
                            @JsonProperty("characters") List<CharacterResponseDto> characters,
                            @JsonProperty("foreShadowings") List<ForeShadowingPreviewResponseDto> foreShadowings,
                            @JsonProperty("content") ContentDto content,
                            @JsonProperty("positionX") int positionX,
                            @JsonProperty("positionY") Double positionY) {
        this.storyId = storyId;
        this.storyTitle = storyTitle;
        this.characters = characters;
        this.foreShadowings = foreShadowings;
        this.content = content;
        this.positionX = positionX;
        this.positionY = positionY;
    }
}
