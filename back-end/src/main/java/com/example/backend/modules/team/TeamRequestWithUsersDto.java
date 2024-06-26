package com.example.backend.modules.team;

import com.example.backend.modules.user.UserRequestDto;
import com.example.backend.modules.user.UserRequestOnlyEmailDto;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class TeamRequestWithUsersDto {
    @Length(max = 20) @NotNull
    private String title;
    private String info;

    private List<UserRequestOnlyEmailDto> users;
    //TODO Multipart Image

    public static Team from(TeamRequestWithUsersDto teamRequestDto){
        return Team.builder().title(teamRequestDto.getTitle())
                .info(teamRequestDto.getInfo())
                .personal(false)
                .title(teamRequestDto.getTitle())
                .build();
    }
}
