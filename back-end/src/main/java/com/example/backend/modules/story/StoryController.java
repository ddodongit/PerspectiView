package com.example.backend.modules.story;

import com.example.backend.modules.api.ApiResult;
import com.example.backend.modules.character.CharacterRequestDto;
import com.example.backend.modules.character.CharacterResponseDto;
import com.example.backend.modules.foreshadowing.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/team/{teamId}/product/{productId}/plot/{plotId}/story")
public class StoryController {
    private final StoryService storyService;
    private final ForeShadowingService foreShadowingService;

    @PostMapping
    public ApiResult<StoryResponseDto> createStory(@RequestBody StoryRequestDto storyRequestDto,
                                                   @PathVariable("plotId") Long plotId) {
        //story 등록
        Story story = storyService.createStory(
                StoryRequestDto.of(storyRequestDto, null, null),
                plotId,
                storyRequestDto.getStoryContent(),
                storyRequestDto.getCharacters().stream().map(CharacterRequestDto::from).collect(Collectors.toList()),
                storyRequestDto.getForeShadowings().stream().map(ForeShadowingRequestDto::from).collect(Collectors.toList()));

        StoryResponseDto storyResponseDto = storyService.findByStoryId(story.getId());
        return ApiResult.OK(storyResponseDto);
    }

    @PutMapping("/{storyId}")
    public ApiResult<StoryResponseDto> updateStory(@RequestBody StoryRequestDto storyRequestDto) {
        Story story = storyService.updateStory(
                StoryRequestDto.of(storyRequestDto, null, null),
                storyRequestDto.getCharacters().stream().map(CharacterRequestDto::from).collect(Collectors.toList()),
                storyRequestDto.getForeShadowings().stream().map(ForeShadowingRequestDto::from).collect(Collectors.toList()));

        StoryResponseDto storyResponseDto = storyService.findByStoryId(story.getId());
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
        storyService.updatePositionY(StoryRequestDto.of(storyRequestDto, null, null));
        StoryResponseDto storyResponseDto = storyService.findByStoryId(storyId);
        return ApiResult.OK(storyResponseDto);
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

    @PutMapping("/{storyId}/fsStatus/{foreshadowingId}")
    public ApiResult<ForeShadowingResponseDto> addForeShadowing(@PathVariable("storyId") Long storyId,
                                                                @PathVariable("foreshadowingId") Long foreshadowingId) {
        ForeShadowing result = storyService.createStoryFshadow(foreshadowingId, storyId);
        if (result != null) {
            List<FshadowStoryIdDto> storyIds = foreShadowingService.findStories(result);
            String columnId = setColumn(storyIds, result);
            return ApiResult.OK(ForeShadowingResponseDto.of(result, storyIds, columnId));
        }
        log.info("========중복 데이터입니다.==========");
        return ApiResult.OK(null);
    }


    @DeleteMapping("/{storyId}/fsStatus/{foreshadowingId}")
    public ApiResult<ForeShadowingResponseDto> delForeShadowing(@PathVariable("storyId") Long storyId,
                                                                @PathVariable("foreshadowingId") Long foreshadowingId) {

        ForeShadowing result = storyService.deleteStoryFshadow(foreshadowingId, storyId);
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