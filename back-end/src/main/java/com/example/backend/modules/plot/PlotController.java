package com.example.backend.modules.plot;

import com.example.backend.modules.api.ApiResult;
import com.example.backend.modules.auth.principal.PrincipalDetails;
import com.example.backend.modules.product.ProductService;
import com.example.backend.modules.story.Story;
import com.example.backend.modules.team.TeamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/team/{teamId}/product/{productId}/plot")
@Slf4j
public class PlotController {
    private final PlotService plotService;

    /**
     * 작품으로 플롯 조회
     *
     * @param productId
     * @return
     */
    @GetMapping
    public ApiResult<List<PlotResponseDto>> findByProduct(@PathVariable("productId") Long productId) {
        List<Plot> plots = plotService.findByProductId( productId);
        //plot List 정렬
        plots.sort(Comparator.comparing(Plot::getId));
        //story정렬
        for (Plot p : plots) {
            if (p != null) {
                p.getStories().sort(Comparator.comparing(Story::getPositionX));
            }
        }
        return ApiResult.OK(plots.stream()
                .map(PlotResponseDto::of)
                .collect(Collectors.toList()));
    }

    @PostMapping
    public ApiResult<PlotResponseDto> createPlot(@RequestBody @Valid PlotRequestDto plotRequestDto,
                                                 @PathVariable("productId") Long productId) {
        log.info("== plot create controller===");
        Plot plot = plotService.createPlot(productId, PlotRequestDto.from(plotRequestDto));
        return ApiResult.OK(PlotResponseDto.of(plot));
    }

    @PatchMapping("/{plotId}")
    public ApiResult<PlotResponseDto> updatePlot(@RequestBody @Valid PlotRequestDto plotRequestDto,
                                                 @PathVariable("productId") Long productId) {
        Plot plot = plotService.updatePlot(productId, PlotRequestDto.from(plotRequestDto));
        return ApiResult.OK(PlotResponseDto.of(plot));
    }

    @DeleteMapping("/{plotId}")
    public ApiResult<PlotResponseDto> deletePlot(@PathVariable("plotId") Long plotId,
                                                 @PathVariable("productId") Long productId) {
        plotService.deletePlot(productId, plotId);
        return ApiResult.OK(null);
    }


}
