package com.example.backend.modules.character;

import com.example.backend.modules.api.ApiResult;
import com.example.backend.modules.auth.principal.PrincipalDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/team/{teamId}/product/{productId}/character")
public class CharacterController {
    private final PersonService personService;

    @GetMapping
    public ApiResult<List<PersonResponseDto>> getCharacters(@PathVariable Long productId, @PathVariable Long teamId, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        List<Person> people = personService.getCharacters(productId, teamId, principalDetails.getUser());
        return ApiResult.OK(people.stream().map(PersonResponseDto::of)
                .collect(Collectors.toList()));
    }

    @GetMapping("/{characterId}")
    public ApiResult<PersonResponseDto> getCharacter(@PathVariable Long productId, @PathVariable Long teamId,
                                                     @AuthenticationPrincipal PrincipalDetails principalDetails, @PathVariable Long characterId) {
        Person person = personService.getCharacter(characterId, productId, teamId, principalDetails.getUser());
        return ApiResult.OK(PersonResponseDto.of(person));
    }

    @DeleteMapping("/{characterId}")
    public ApiResult<PersonResponseDto> deleteCharacter(@PathVariable Long productId, @PathVariable Long teamId,
                                                        @AuthenticationPrincipal PrincipalDetails principalDetails,
                                                        @PathVariable Long characterId) {
        personService.deleteCharacter(characterId, productId, teamId, principalDetails.getUser());
        return ApiResult.OK(null);
    }

    @PatchMapping("/{characterId}")
    public ApiResult<PersonResponseDto> updateCharacter(@PathVariable Long productId, @PathVariable Long teamId, @AuthenticationPrincipal PrincipalDetails principalDetails,
                                                        @PathVariable Long characterId, @RequestBody @Valid PersonRequestDto personRequestDto) {
        Person person = personService.updateCharacter(PersonRequestDto.from(personRequestDto), characterId, productId, teamId, principalDetails.getUser());
        return ApiResult.OK(PersonResponseDto.of(person));
    }

    @PostMapping
    public ApiResult<PersonResponseDto> createCharacter(@PathVariable Long productId, @PathVariable Long teamId,
                                                        @RequestBody @Valid PersonRequestDto personRequestDto,
                                                        @AuthenticationPrincipal PrincipalDetails principalDetails) {
        Person person = personService.createCharacter(PersonRequestDto.from(personRequestDto), productId, teamId, principalDetails.getUser());
        return ApiResult.OK(PersonResponseDto.of(person));
    }
}