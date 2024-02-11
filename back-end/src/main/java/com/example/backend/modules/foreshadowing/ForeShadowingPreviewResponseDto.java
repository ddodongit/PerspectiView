package com.example.backend.modules.foreshadowing;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonCreator
    public ForeShadowingPreviewResponseDto(@JsonProperty("fShadowId") Long fShadowId, @JsonProperty("fshadowName") String fshadowName, @JsonProperty("fshadowContent") String fshadowContent, @JsonProperty("fshadowClose") Long fshadowClose) {
        this.fShadowId = fShadowId;
        this.fshadowName = fshadowName;
        this.fshadowContent = fshadowContent;
        this.fshadowClose = fshadowClose;
    }
}
