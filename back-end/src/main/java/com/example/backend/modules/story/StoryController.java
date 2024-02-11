package com.example.backend.modules.story;

import com.example.backend.modules.api.ApiResult;
import com.example.backend.modules.character.CharacterRequestDto;
import com.example.backend.modules.character.CharacterResponseDto;
import com.example.backend.modules.foreshadowing.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/team/{teamId}/product/{productId}/plot/{plotId}/story")
public class StoryController {
    private final StoryService storyService;
    private final ForeShadowingService foreShadowingService;

    @PostMapping
    public ApiResult<StoryResponseDto> createStory(@RequestBody StoryRequestDto storyRequestDto,
                                                   @PathVariable("plotId") Long plotId) {
        //story 등록
        StoryResponseDto storyResponseDto = storyService.createStory(
                StoryRequestDto.from(storyRequestDto,null,null),
                plotId,
                storyRequestDto.getStoryContent(),
                storyRequestDto.getCharacters().stream().map(CharacterRequestDto::from).collect(Collectors.toList()),
                storyRequestDto.getForeShadowings().stream().map(ForeShadowingRequestDto::from).collect(Collectors.toList()));

        return ApiResult.OK(storyResponseDto);
    }

    @PutMapping("/{storyId}")
    public ApiResult<StoryResponseDto> updateStory(@PathVariable("storyId") Long storyId, @RequestBody StoryRequestDto storyRequestDto) {
        StoryResponseDto storyResponseDto = storyService.updateStory(storyId, storyRequestDto);
        return ApiResult.OK(storyResponseDto);
    }

    @DeleteMapping("/{storyId}")
    public ApiResult<StoryResponseDto> deleteStory(@PathVariable("storyId") Long storyId) {
        storyService.deleteStory(storyId);
        return ApiResult.OK(null);
    }

    @GetMapping("/{storyId}")
    public ApiResult<StoryResponseDto> getStory(@PathVariable("storyId") Long storyId) {
        return ApiResult.OK(storyService.findByStoryId(storyId));
    }

    @PostMapping("/{storyId}/vertical")
    public ApiResult<StoryResponseDto> updatePositionY(@PathVariable("storyId") Long storyId,
                                                       @RequestBody StoryRequestDto storyRequestDto) {
        StoryResponseDto storyResponseDto = storyService.updatePositionY(StoryRequestDto.from(storyRequestDto,null,null));
        return ApiResult.OK(storyResponseDto);
    }

    @PutMapping("/{storyId}/fsStatus/{foreshadowingId}")
    public ApiResult<ForeShadowingResponseDto> addForeShadowing(@PathVariable("storyId") Long storyId,
                                                                @PathVariable("foreshadowingId") Long foreshadowingId) {
        storyService.createStoryFshadow(foreshadowingId, storyId);
        ForeShadowing result = foreShadowingService.findById(foreshadowingId);
        List<FshadowStoryIdDto> storyIds = foreShadowingService.findStories(result);
        String columnId = setColumn(storyIds, result);

        return ApiResult.OK(ForeShadowingResponseDto.of(result, storyIds, columnId));
    }


    private String setColumn(List<FshadowStoryIdDto> storyIds, ForeShadowing foreShadowing) {
        String columnId;
        if (storyIds.isEmpty() && foreShadowing.getFShadowClose() == null) {
            columnId = "column-1";
        } else if (!storyIds.isEmpty() && foreShadowing.getFShadowClose() == null) {
            columnId = "column-2";
        } else if (!storyIds.isEmpty() && foreShadowing.getFShadowClose() != null) {
            columnId = "column-3";
        } else {
            columnId = "column-1";
        }
        return columnId;
    }

    @DeleteMapping("/{storyId}/fsStatus/{foreshadowingId}")
    public ApiResult<ForeShadowingResponseDto> delForeShadowing(@PathVariable("storyId") Long storyId,
                                                                @PathVariable("foreshadowingId") Long foreshadowingId) {

        storyService.deleteStoryFshadow(foreshadowingId, storyId);
        ForeShadowing result = foreShadowingService.findById(foreshadowingId);
                List<FshadowStoryIdDto> storyIds = foreShadowingService.findStories(result);
        String columnId = setColumn(storyIds, result);

        return ApiResult.OK(ForeShadowingResponseDto.of(result, storyIds, columnId));
    }

    @PutMapping("/{storyId}/fsClose/{foreshadowingId}")
    public ApiResult<ForeShadowingResponseDto> closeForeShadowing(@PathVariable("storyId") Long storyId,
                                                                  @PathVariable("foreshadowingId") Long foreshadowingId) {

        ForeShadowing result = storyService.updateFshadowClose(foreshadowingId, storyId);
        List<FshadowStoryIdDto> storyIds = foreShadowingService.findStories(result);

        String columnId = setColumn(storyIds, result);

        return ApiResult.OK(ForeShadowingResponseDto.of(result, storyIds, columnId));
    }

    @DeleteMapping("/{storyId}/fsClose/{foreshadowingId}")
    public ApiResult<ForeShadowingResponseDto> closeCancleForeShadowing(@PathVariable("storyId") Long storyId,
                                                                        @PathVariable("foreshadowingId") Long foreshadowingId) {

        ForeShadowing result = storyService.deleteFshadowClose(foreshadowingId, storyId);
        List<FshadowStoryIdDto> storyIds = foreShadowingService.findStories(result);

        String columnId = setColumn(storyIds, result);

        return ApiResult.OK(ForeShadowingResponseDto.of(result, storyIds, columnId));
    }
}