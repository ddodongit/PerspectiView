package com.example.backend.modules.foreshadowing;

import com.example.backend.modules.api.ApiResult;
import com.example.backend.modules.auth.principal.PrincipalDetails;
import com.example.backend.modules.product.ProductService;
import com.example.backend.modules.story.Story;
import com.example.backend.modules.story.StoryService;
import com.example.backend.modules.team.TeamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/team/{teamId}/product/{productId}/foreshadowingId")
public class foreShadowingController {
    private final ForeShadowingService foreShadowingService;
    private final TeamService teamService;
    private final ProductService productService;


    @PostMapping("/")
    public ApiResult<ForeShadowingResponseDto> createForeShadowing(@RequestBody @Valid ForeShadowingRequestDto foreShadowingRequestDto,
                                                 @PathVariable("teamId") Long teamId,
                                                 @PathVariable("productId") Long productId,
                                                 @AuthenticationPrincipal PrincipalDetails principalDetails) {
        ForeShadowing foreShadowing = foreShadowingService.createForeShadowing(principalDetails.getUser(), teamId, productId, foreShadowingRequestDto.of(foreShadowingRequestDto));
        List<FshadowStoryIdDto> storyids = foreShadowingService.findStories(foreShadowing);
        return ApiResult.OK(ForeShadowingResponseDto.from(foreShadowing,storyids));
    }

    @PatchMapping("/{foreshadowingId}")
    public ApiResult<ForeShadowingResponseDto> updateForeShadowing(@RequestBody @Valid ForeShadowingRequestDto foreShadowingRequestDto,
                                                 @PathVariable("teamId") Long teamId,
                                                 @PathVariable("productId") Long productId,
                                                 @AuthenticationPrincipal PrincipalDetails principalDetails){
        ForeShadowing foreShadowing = foreShadowingService.updateForeShadowing(principalDetails.getUser(), teamId, productId, ForeShadowingRequestDto.of(foreShadowingRequestDto));
        List<FshadowStoryIdDto> storyids = foreShadowingService.findStories(foreShadowing);
        return ApiResult.OK(ForeShadowingResponseDto.from(foreShadowing,storyids));
    }

    @DeleteMapping("/{foreshadowingId}")
    public ApiResult<ForeShadowingResponseDto> deleteForeShadowing(@PathVariable("foreshadowingId") Long foreShadowingId,
                                                 @PathVariable("teamId") Long teamId,
                                                 @PathVariable("productId") Long productId,
                                                 @AuthenticationPrincipal PrincipalDetails principalDetails){
        foreShadowingService.deleteForeShadowing(principalDetails.getUser(),teamId,productId,foreShadowingId);
        return ApiResult.OK(null);
    }

    @GetMapping
    public ApiResult<Map<Long,ForeShadowingResponseDto>> findAllFshadow(@PathVariable("teamId") Long teamId,
                                                                    @PathVariable("productId") Long productId,
                                                                    @AuthenticationPrincipal PrincipalDetails principalDetails){
        List<ForeShadowing> foreShadowings = foreShadowingService.findByProductId(principalDetails.getUser(),teamId,productId);
        Map<Long, ForeShadowingResponseDto> result = new HashMap<>();
        for(ForeShadowing fs : foreShadowings){
            List<FshadowStoryIdDto> storyids = foreShadowingService.findStories(fs);
            result.put(fs.getId(),ForeShadowingResponseDto.from(fs,storyids));
        }
        return ApiResult.OK(result);
    }

}
