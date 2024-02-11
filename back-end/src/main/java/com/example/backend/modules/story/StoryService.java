package com.example.backend.modules.story;

import com.example.backend.modules.character.Character;
import com.example.backend.modules.character.CharacterRepository;
import com.example.backend.modules.character.CharacterRequestDto;
import com.example.backend.modules.exception.NotFoundException;
import com.example.backend.modules.foreshadowing.ForeShadowing;
import com.example.backend.modules.foreshadowing.ForeShadowingPreviewResponseDto;
import com.example.backend.modules.foreshadowing.ForeShadowingRepository;
import com.example.backend.modules.foreshadowing.ForeShadowingRequestDto;
import com.example.backend.modules.plot.Plot;
import com.example.backend.modules.plot.PlotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
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
    private final CharacterRepository characterRepository;

    /**
     * 스토리 생성
     */
    @Transactional
    @CachePut(value = "StoryResponseDto", key = "#story.id", cacheManager = "testCacheManager")
    public StoryResponseDto createStory(Story story, Long plotId, String storycontent, List<Character> characters, List<ForeShadowing> foreShadowings) {
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
            Character character = characterRepository.findById(c.getId()).orElseThrow(()->new NotFoundException());
            StoryRelation storyRelation = StoryRelation.builder()
                    .story(madeStory)
                    .character(character).build();
            StoryRelation makeStoryRelation = storyRelationRepository.save(storyRelation);
            madeStory.addStoryRelation(makeStoryRelation);
        }

        //복선스토리 저장
        for(ForeShadowing fs : foreShadowings){
            ForeShadowing foreShadowing = foreShadowingRepository.findById(fs.getId()).orElseThrow(()->new NotFoundException());
            createStoryFshadow(foreShadowing.getId(), madeStory.getId());
            StoryForeShadowing storyForeShadowing = StoryForeShadowing.builder()
                    .foreShadowing(foreShadowing)
                    .story(madeStory).build();
            StoryForeShadowing makeStoryForeShadowing = storyForeShadowingRepository.save(storyForeShadowing);
            madeStory.addStoryForeShadowing(makeStoryForeShadowing);
        }

        return StoryResponseDto.of(madeStory,characters,foreShadowings);
    }

    /**
     * story content 생성
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
     */
    @Transactional
    @CachePut(value = "StoryResponseDto", key = "#storyId", cacheManager = "testCacheManager")
    public StoryResponseDto updateStory(Long storyId, StoryRequestDto storyRequestDto) {
        Story story = storyRepository.findById(storyId).orElseThrow(NotFoundException::new);
        clearExistingRelationsAndForeShadowings(story);

        Story findStory = storyRepository.findWithPlotById(story.getId()).orElseThrow(RuntimeException::new);

        List<Character> characters = createCharacters(storyRequestDto);
        List<ForeShadowing> foreShadowings = createForeShadowings(storyRequestDto);

        Set<StoryRelation> storyRelations = saveStoryRelations(findStory, characters);
        Set<StoryForeShadowing> storyForeShadowings = saveStoryForeShadowings(findStory, foreShadowings);

        //Content를 가져와서 수정
        Content content = contentRepository.findById(story.getContent().getId()).orElseThrow(() -> new RuntimeException());
        content.updateContent(storyRequestDto.getStoryContent());

        findStory.updateStory(storyRequestDto.getStoryTitle(), content, storyRelations, storyForeShadowings, storyRequestDto.getPositionY());
        return StoryResponseDto.of(findStory, characters, foreShadowings);
    }

    private void clearExistingRelationsAndForeShadowings(Story story) {
        storyRelationRepository.deleteAll(story.getStoryRelations());
        storyForeShadowingRepository.deleteAll(story.getStoryForeShadowings());
    }

    private List<Character> createCharacters(StoryRequestDto storyRequestDto) {
        return storyRequestDto.getCharacters().stream()
                .map(CharacterRequestDto::from)
                .collect(Collectors.toList());
    }

    private List<ForeShadowing> createForeShadowings(StoryRequestDto storyRequestDto) {
        return storyRequestDto.getForeShadowings().stream()
                .map(ForeShadowingRequestDto::from)
                .collect(Collectors.toList());
    }

    private Set<StoryRelation> saveStoryRelations(Story findStory, List<Character> characters) {
        return characters.stream()
                .map(c -> StoryRelation.builder().story(findStory).character(c).build())
                .map(storyRelationRepository::save)
                .collect(Collectors.toSet());
    }

    private Set<StoryForeShadowing> saveStoryForeShadowings(Story findStory, List<ForeShadowing> foreShadowings) {
        return foreShadowings.stream()
                .map(fs -> StoryForeShadowing.builder().story(findStory).foreShadowing(fs).build())
                .map(storyForeShadowingRepository::save)
                .collect(Collectors.toSet());
    }

    private Content getContent(Story story) {
        return contentRepository.findById(story.getContent().getId()).orElseThrow(RuntimeException::new);
    }


    /**
     * 스토리 삭제
     *
     * @param storyId
     */
    @Transactional
    @CacheEvict(value = "StoryResponseDto", key = "#storyId", cacheManager = "testCacheManager")
    public void deleteStory(Long storyId) {
        storyRepository.deleteById(storyId);
    }

    /**
     * 스토리 아이디로 조회
     */
    @Cacheable(value = "StoryResponseDto", key = "#storyId", cacheManager = "testCacheManager")
    public StoryResponseDto findByStoryId(Long storyId) {
        Story story = storyRepository.findWithPlotById(storyId).orElseThrow(() -> new RuntimeException());
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
    @CachePut(value = "StoryResponseDto", key = "#story.id", cacheManager = "testCacheManager")
    public StoryResponseDto updatePositionY(Story story) {
        Story findStory = storyRepository.findById(story.getId()).orElseThrow(() -> new RuntimeException());
        findStory.updatePositionY(story.getPositionY());
        return findByStoryId(story.getId());
    }


    /**
     * 복선 스토리에 추가
     */
    @Transactional
    @CachePut(value = "StoryResponseDto", key = "#storyId", cacheManager = "testCacheManager")
    public StoryResponseDto createStoryFshadow(Long foreshadowingId, Long storyId) {
        ForeShadowing fshadow = foreShadowingRepository.findById(foreshadowingId).orElseThrow(()->new NotFoundException());

        Story story = storyRepository.findById(storyId).orElseThrow(() -> new NotFoundException());

        StoryForeShadowing storyForeShadowing = storyForeShadowingRepository.save(StoryForeShadowing.builder()
                .foreShadowing(fshadow)
                .story(story).build());
        fshadow.addStoryFshadow(storyForeShadowing);

        StoryResponseDto storyResponseDto = findByStoryId(storyId);
        storyResponseDto.getForeShadowings().add(ForeShadowingPreviewResponseDto.of(fshadow));
        return storyResponseDto;
    }


    /**
     * 복선 스토리에서 삭제
     */
    @Transactional
    @CachePut(value = "StoryResponseDto", key = "#storyId", cacheManager = "testCacheManager")
    public StoryResponseDto deleteStoryFshadow(Long foreshadowingId, Long storyId) {
        ForeShadowing fshadow = foreShadowingRepository.findById(foreshadowingId).orElseThrow(()->new NotFoundException());

        //복선리스트에서 복선스토리 삭제
        StoryForeShadowing sfs = storyForeShadowingRepository.findByForeShadowing(fshadow);
        storyForeShadowingRepository.delete(sfs);

        StoryResponseDto storyResponseDto = findByStoryId(storyId);
//        storyResponseDto.getForeShadowings().add(ForeShadowingPreviewResponseDto.of(fshadow));
        return storyResponseDto;
    }

    /**
     * 복선 회수
     */
    @Transactional
    public ForeShadowing updateFshadowClose(Long fshadowId, Long closeStoryId){
        ForeShadowing foreShadowing = foreShadowingRepository.findById(fshadowId).orElseThrow(()->new NotFoundException());
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
        return foreShadowing;
    }

    /**
     * 스토리 등장인물이랑 같이 조회
     */
    public List<Story> findWithStoryRelation(Plot plot){
        List<Story> stories = storyRepository.findWithStoryRelationByPlot(plot);
        for(Story s:stories){
            List<StoryRelation> storyRelations = findWithCharacterByStory(s);
            s.updateStoryRelation(storyRelations);
        }
        return stories;
    }

    /**
     * 스토리관계 인물이랑 같이 조회
     */
    public List<StoryRelation> findWithCharacterByStory(Story story){
        return storyRelationRepository.findWithCharacterByStory(story);
    }


}
