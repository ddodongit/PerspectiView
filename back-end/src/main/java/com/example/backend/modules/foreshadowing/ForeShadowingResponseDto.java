package com.example.backend.modules.foreshadowing;

import com.example.backend.modules.story.Story;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class ForeShadowingResponseDto {
    Long foreShadowingId;
    String foreShadowingName;
    String foreShadowingContent;
    boolean foreShadowingClose;
    Map<String,Long> storyIdList;

//    public static ForeShadowingResponseDto from(ForeShadowing foreShadowing){
        public static ForeShadowingResponseDto from(ForeShadowing foreShadowing,Map<String,Long> storyList){
        return ForeShadowingResponseDto.builder()
                .foreShadowingId(foreShadowing.getId())
                .foreShadowingName(foreShadowing.getFShadowName())
                .foreShadowingContent(foreShadowing.getFShadowContent())
                .foreShadowingClose(foreShadowing.isFShadowClose())
                .storyIdList(storyList)
                .build();
    }
}
