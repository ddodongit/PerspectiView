package com.example.backend.modules.foreshadowing;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;

import java.util.List;

@Data
@Builder
@DynamicInsert
public class ForeShadowingPreviewResponseDto {
    Long fShadowId;
    String fshadowName;
    String fshadowContent;
    Long fshadowClose;

    public static ForeShadowingPreviewResponseDto of(ForeShadowing foreShadowing){
        return ForeShadowingPreviewResponseDto.builder()
                .fShadowId(foreShadowing.getId())
                .fshadowName(foreShadowing.getFShadowName())
                .fshadowContent(foreShadowing.getFShadowContent())
                .fshadowClose(foreShadowing.getFShadowClose())
                .build();
    }
}
