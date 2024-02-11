package com.example.backend.modules.story;

import com.example.backend.modules.character.Character;
import com.example.backend.modules.exception.NotFoundException;
import com.example.backend.modules.foreshadowing.ForeShadowing;
import com.example.backend.modules.foreshadowing.ForeShadowingRepository;
import com.example.backend.modules.foreshadowing.ForeShadowingResponseDto;
import com.example.backend.modules.plot.Plot;
import com.example.backend.modules.plot.PlotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoryService {
    private final StoryRepository storyRepository;
    private final ContentRepository contentRepository;
    private final StoryRelationRepository storyRelationRepository;
    private final StoryForeShadowingRepository storyForeShadowingRepository;
    private final ForeShadowingRepository foreShadowingRepository;
    private final PlotRepository plotRepository;

    /**
     * 스토리 생성
     */
    @Transactional
    public Story createStory(Story story, Long plotId, String storycontent, List<Character> characters) {
        /**
         * 순서는 플롯 안에서의 순서는 따진다.
         * update story s left join plot p
         * on p.plot_id = s.plot_id
         *  set position_x = position_x + 1
         * where position_x>={현재값}
         */
        //content 등록
        Content content = createContent(storycontent);
        story.updateContent(content);

        //먼저 index뒤로 미루기
        storyRepository.updatePositionX(plotId, story.getPositionX());

        //plotId로 plot 찾아오기
        Plot plot = plotRepository.findById(plotId).orElseThrow(()-> new NotFoundException());
        story.updatePlot(plot);

        //content와 plot 저장 후 넣기
        Story madeStory = storyRepository.save(story);

        for (Character c : characters) {
            StoryRelation storyRelation = StoryRelation.builder()
                    .story(madeStory)
                    .character(c).build();
            madeStory.addStoryRelation(storyRelationRepository.save(storyRelation));
        }

        return madeStory;
    }

    /**
     * story content 생성
     *
     * @param content
     * @return
     */
    @Transactional
    public Content createContent(String content) {
        Content makeContent = Content.builder()
                .content(content)
                .build();
        return contentRepository.save(makeContent);
    }

    /**
     * 스토리 수정
     *
     * @param story
     * @param characters
     * @param foreShadowings
     * @return
     */
    @Transactional
    public Story updateStory(Story story, List<Character> characters, List<ForeShadowing> foreShadowings) {
        //먼저 있던 리스트를 없애고 새로운 리스트 넣기
        storyRelationRepository.deleteAll(story.getStoryRelations());
        storyForeShadowingRepository.deleteAll(story.getStoryForeShadowings());

        Story findStory = storyRepository.findWithPlotContentById(story.getId()).orElseThrow(() -> new NotFoundException());

        Set<StoryRelation> storyRelations;
        Set<StoryForeShadowing> storyForeShadowings;

        storyRelations = characters.stream()
                .map(c -> StoryRelation.builder().story(findStory).character(c).build())
                .map(storyRelationRepository::save)
                .collect(Collectors.toSet());

        storyForeShadowings = foreShadowings.stream()
                .map(fs -> StoryForeShadowing.builder().story(findStory).foreShadowing(fs).build())
                .map(storyForeShadowingRepository::save)
                .collect(Collectors.toSet());

        //Content를 가져와서 수정
        Content content = contentRepository.findById(story.getContent().getId()).orElseThrow(() -> new RuntimeException());

        findStory.updateStory(story.getTitle(), content, storyRelations, storyForeShadowings, story.getPositionY());
        return findStory;
    }

    /**
     * 스토리 삭제
     *
     * @param storyId
     */
    @Transactional
    public void deleteStory(Long storyId) {
        storyRepository.deleteById(storyId);
    }

    /**
     * 스토리 아이디로 조회
     *
     * @param storyId
     * @return
     */
    public StoryResponseDto findByStoryId(Long storyId) {
        Story story = storyRepository.findWithPlotContentById(storyId).orElseThrow(() -> new RuntimeException());
        List<Character> characterList = story.getStoryRelations().stream()
                .map(StoryRelation::getCharacter)
                .collect(Collectors.toList());

        List<ForeShadowing> foreShadowingList = story.getStoryForeShadowings().stream()
                .map(StoryForeShadowing::getForeShadowing)
                .collect(Collectors.toList());

        return StoryResponseDto.of(story, characterList, foreShadowingList);
    }


    /**
     * 스토리 y축
     */
    public Story updatePositionY(Story story) {
        Story findStory = storyRepository.findById(story.getId()).orElseThrow(() -> new RuntimeException());
        findStory.updatePositionY(story.getPositionY());
        return findStory;
    }


    /**
     * 복선 스토리에 추가
     */
    @Transactional
    public ForeShadowing createStoryFshadow(Long foreshadowingId, Long storyId) {
        ForeShadowing fshadow = foreShadowingRepository.findById(foreshadowingId).orElseThrow(()->new NotFoundException());

        Story story = storyRepository.findById(storyId).orElseThrow(() -> new NotFoundException());

        StoryForeShadowing storyForeShadowing = storyForeShadowingRepository.save(StoryForeShadowing.builder()
                .foreShadowing(fshadow)
                .story(story).build());


        fshadow.addStoryFshadow(storyForeShadowing);

        return fshadow;
    }


    /**
     * 복선 스토리에서 삭제
     */
    @Transactional
    public ForeShadowing deleteStoryFshadow(Long foreshadowingId, Long storyId) {
        ForeShadowing fshadow = foreShadowingRepository.findById(foreshadowingId).orElseThrow(()->new NotFoundException());

        //복선리스트에서 복선스토리 삭제
        StoryForeShadowing sfs = storyForeShadowingRepository.findByForeShadowing(fshadow);
        fshadow.deleteStoryFshadow(sfs);
        storyForeShadowingRepository.delete(sfs);

        return fshadow;
    }

    /**
     * 복선 회수
     */
    @Transactional
    public ForeShadowing updateFshadowClose(Long fshadowId, Long closeStoryId){
        ForeShadowing foreShadowing = foreShadowingRepository.findById(fshadowId).orElseThrow(()->new NotFoundException());

//        StoryForeShadowing delStoryForeShadowing = storyForeShadowingRepository.findByForeShadowing(foreShadowing);
//        storyForeShadowingRepository.delete(delStoryForeShadowing);

        foreShadowing.updateFshadowClose(closeStoryId);

        return foreShadowing;
    }

    /**
     * 복선 회수 취소
     */
    @Transactional
    public ForeShadowing deleteFshadowClose(Long fshadowId, Long closeStoryId){
        ForeShadowing foreShadowing = foreShadowingRepository.findById(fshadowId).orElseThrow(()->new NotFoundException());
        foreShadowing.updateFshadowClose(null);
//        Story story = storyRepository.findById(closeStoryId).orElseThrow(()->new NotFoundException());

//        StoryForeShadowing storyForeShadowing = storyForeShadowingRepository.save(StoryForeShadowing.builder()
//                .foreShadowing(foreShadowing)
//                .story(story).build());

        return foreShadowing;
    }

    /**
     * 스토리 등장인물이랑 같이 조회
     */
    public List<Story> findWithStoryRelation(Plot plot){
        return storyRepository.findWithStoryRelationByPlot(plot);
    }


}
