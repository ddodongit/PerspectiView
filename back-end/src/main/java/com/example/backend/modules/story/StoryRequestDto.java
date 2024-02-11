package com.example.backend.modules.story;

import com.example.backend.modules.character.Character;
import com.example.backend.modules.character.CharacterRequestDto;
import com.example.backend.modules.foreshadowing.ForeShadowing;
import com.example.backend.modules.foreshadowing.ForeShadowingRequestDto;
import com.example.backend.modules.plot.Plot;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
@Builder
public class StoryRequestDto {
    private String storyTitle;
    private String storyContent;
    private List<CharacterRequestDto> characters;
    private List<ForeShadowingRequestDto> foreShadowings;
    private int positionX;
    private Double positionY;

//    public static Story of(StoryRequestDto storyRequestDto) {
    public static Story from(StoryRequestDto storyRequestDto, Set<StoryRelation> storyRelations, Set<StoryForeShadowing> storyForeShadowings) {
        return Story.builder()
                .title(storyRequestDto.getStoryTitle())
                .storyRelations(storyRelations)
                .storyForeShadowings(storyForeShadowings)
                .positionX(storyRequestDto.getPositionX())
                .positionY(storyRequestDto.getPositionY())
                .build();
    }
}
