package com.example.backend.modules.character;

import com.example.backend.modules.user.User;
import com.example.backend.modules.product.Product;
import com.example.backend.modules.product.ProductService;
import com.example.backend.modules.team.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PersonService {
    private final PersonRepository personRepository;
    private final TeamService teamService;
    private final ProductService productService;

    /**
     * 등장인물 생성
     *
     * @param person 등장인물
     * @param productId 작품
     * @param teamId    팀 아이디
     * @param user      유저
     * @return 등장인물
     */
    @Transactional
    public Person createCharacter(Person person, Long productId, Long teamId, User user) {
        teamService.checkIfManager(user, teamService.getTeam(teamId));

        return personRepository.save(person);
    }


    /**
     * 전체 등장인물 조회
     *
     * @param productId 작품 아이디
     * @param teamId    팀 아이디
     * @param user      유저
     * @return 등장인물 리스트
     */
    public List<Person> getCharacters(Long productId, Long teamId, User user) {
        Product product = productService.findByProductId(user, teamId, productId);
        teamService.checkIfMember(user, teamService.getTeam(teamId));

        // 해당 작품에 포함된 인물인지도 검사해야함.
        return personRepository.findAllByProduct(product);
    }

    /**
     * 단일 등장인물 조회
     *
     * @param charterId 등장인물 아이디
     * @param productId 작품 아이디
     * @param teamId    팀 아이디
     * @param user      유저
     * @return 등장인물
     */
    public Person getCharacter(Long charterId, Long productId, Long teamId, User user) {
        teamService.checkIfMember(user, teamService.getTeam(teamId));

        // 해당 작품에 포함된 인물인지도 검사해야함.

        return personRepository.findById(charterId).orElseThrow(() -> new RuntimeException());
    }

    /**
     * 등장인물 정보 수정
     *
     * @param person   등장인물
     * @param characterId 등장인물 아이디
     * @param productId   작품 아이디
     * @param teamId      팀 아이디
     * @param user        유저
     * @return 등장인물
     */
    @Transactional
    public Person updateCharacter(Person person, Long characterId, Long productId, Long teamId, User user) {
        teamService.checkIfManager(user, teamService.getTeam(teamId));

        Person newPerson = personRepository.findById(characterId).orElseThrow(() -> new RuntimeException());
        newPerson.changeCharacter(person);

        return newPerson;
    }

    /**
     * 등장인물 삭제
     *
     * @param charterId 등장인물 아이디
     * @param productId 작품 아이디
     * @param teamId    팀 아이디
     * @param user      유저
     */
    @Transactional
    public void deleteCharacter(Long charterId, Long productId, Long teamId, User user) {
        teamService.checkIfManager(user, teamService.getTeam(teamId));

        Person findChar = personRepository.findById(charterId).orElseThrow(() -> new RuntimeException());

        personRepository.delete(findChar);
    }
}
