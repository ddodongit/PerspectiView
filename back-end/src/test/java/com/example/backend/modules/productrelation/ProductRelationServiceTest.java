package com.example.backend.modules.productrelation;

import com.example.backend.modules.character.Person;
import com.example.backend.modules.character.PersonService;
import com.example.backend.modules.foreshadowing.ForeShadowing;
import com.example.backend.modules.foreshadowing.ForeShadowingRepository;
import com.example.backend.modules.plot.Plot;
import com.example.backend.modules.plot.PlotRepository;
import com.example.backend.modules.product.Product;
import com.example.backend.modules.product.ProductRepository;
import com.example.backend.modules.story.*;
import com.example.backend.modules.team.Team;
import com.example.backend.modules.team.TeamRepository;
import com.example.backend.modules.team.TeamService;
import com.example.backend.modules.user.User;
import com.example.backend.modules.user.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@Slf4j
class ProductRelationServiceTest {
    @Autowired
    UserRepository userRepository;

    @Autowired
    TeamRepository teamRepository;

    @Autowired
    TeamService teamService;

    @Autowired
    ProductRelationRepository productRelationRepository;

    @Autowired
    ProductRelationService productRelationService;

    @Autowired
    StoryRepository storyRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    PersonService personService;

    @Autowired
    PlotRepository plotRepository;

    @Autowired
    ContentRepository contentRepository;

    @Autowired
    StoryService storyService;

    @Autowired
    StoryForeShadowingRepository storyForeShadowingRepository;

    @Autowired
    ForeShadowingRepository foreShadowingRepository;

    private Team team;

    private User user;

    private Product product;

    private Plot plot;

    private ForeShadowing foreShadowing;

    private List<ForeShadowing> foreShadowings;

    List<Person> people;

    private Person fromPerson;

    private Person toPerson;

    private Story story;

    private Content content;

    @PersistenceContext
    EntityManager em;

    @BeforeEach
    public void setup() {
        user = User.builder().userNickname("nickname")
                .userImageUrl("https://s3")
                .username("username")
                .email("kangkun@naver.com")
                .provider("kakao")
                .providerId("kakao_1234")
                .userInfo("bio")
                .build();

        userRepository.save(user);

        team = Team.builder().title("team1")
                .info("team info")
                .profileImageUrl("https://s3")
                .personal(false)
                .build();
        teamService.createTeam(team, user);

        product = Product.builder()
                .title("productTitle")
                .productImageuRL("image")
                .info("info")
                .build();
        productRepository.save(product);

        plot = Plot.builder()
                .name("name")
                .color("red")
                .product(product)
                .build();
        plotRepository.save(plot);

        content = Content.builder()
                .content("SibalContents: StartContents")
                .build();
        contentRepository.save(content);

        story = Story.builder()
                .title("storyTitle")
                .positionX(1)
                .positionY(1.0)
                .plot(plot)
                .storyForeShadowings(new HashSet<>())
                .storyRelations(new HashSet<>())
                .build();

        people = new ArrayList<>();
        foreShadowings = new ArrayList<>();
        storyService.createStory(story, "", people);

        fromPerson = Person.builder()
                .product(product)
                .characterName("fromPerson")
                .build();

        toPerson = Person.builder()
                .product(product)
                .characterName("toPerson")
                .build();
        personService.createCharacter(fromPerson, product.getId(), team.getId(), user);
        personService.createCharacter(toPerson, product.getId(), team.getId(), user);

        foreShadowing = ForeShadowing.builder()
                .product(product)
                .fShadowClose(null)
                .fShadowName("fShadowName")
                .fShadowContent("fShadowContent")
                .build();
        foreShadowingRepository.save(foreShadowing);

        StoryForeShadowing storyForeShadowing = StoryForeShadowing.builder()
                .story(story)
                .foreShadowing(foreShadowing)
                .build();
        storyForeShadowingRepository.save(storyForeShadowing);

        log.info(storyForeShadowing.getStory().getTitle());
        log.info(String.valueOf(storyForeShadowing.getStory().getPositionX()));

        story.addStoryForeShadowing(storyForeShadowing);
    }


    @Test
    void 인물관계생성() throws Exception {
        //given
        ProductRelation productRelation = ProductRelation.builder()
                .product(product)
                .productRelationInfo("info")
                .fromPerson(fromPerson)
                .toPerson(toPerson)
                .build();

        //when
        productRelationService.createProductRelation(productRelation);

        //then
        assertEquals(productRelationRepository.count(), 1);

    }

    @Test
    void 단일인물관계조회() throws Exception {
        //given
        ProductRelation productRelation = ProductRelation.builder()
                .product(product)
                .productRelationInfo("info")
                .fromPerson(fromPerson)
                .toPerson(toPerson)
                .build();
        productRelationService.createProductRelation(productRelation);

        //when
        ProductRelation findProductRelation = productRelationService.findProductRelation(user, team.getId(), productRelation.getId());

        //then
        assertEquals(findProductRelation.getProductRelationInfo(), productRelation.getProductRelationInfo());
    }

    @Test
    void 전체인물관계조회() throws Exception {
        //given
        ProductRelation productRelation = ProductRelation.builder()
                .product(product)
                .productRelationInfo("info")
                .fromPerson(fromPerson)
                .toPerson(toPerson)
                .build();
        productRelationService.createProductRelation(productRelation);
        //when

        List<ProductRelation> productRelations = productRelationService.findAllProductRelation(user, team.getId(), product.getId());

        //then
        assertEquals(productRelations.size(), 1);
    }

    @Test
    void 인물관계수정() throws Exception {
        //given
        ProductRelation productRelation = ProductRelation.builder()
                .product(product)
                .productRelationInfo("info")
                .fromPerson(fromPerson)
                .toPerson(toPerson)
                .build();
        productRelationService.createProductRelation(productRelation);

        ProductRelation beforeUpdate = ProductRelation.builder()
                .product(product)
                .productRelationInfo("updateinfo")
                .fromPerson(fromPerson)
                .toPerson(toPerson)
                .build();
        //when
        ProductRelation afterUpdate = productRelationService.updateProductRelation(productRelation.getId(), beforeUpdate);

        //then
        assertEquals(afterUpdate.getProductRelationInfo(), beforeUpdate.getProductRelationInfo());
    }

    @Test
    void 인물관계삭제() throws Exception {
        //given
        ProductRelation productRelation = ProductRelation.builder()
                .product(product)
                .productRelationInfo("info")
                .fromPerson(fromPerson)
                .toPerson(toPerson)
                .build();
        productRelationService.createProductRelation(productRelation);

        //when
        productRelationService.deleteProductRelation(productRelation.getId());

        //then
        assertEquals(productRelationService.findAllProductRelation(user, team.getId(), product.getId()).size(), 0);
    }
}