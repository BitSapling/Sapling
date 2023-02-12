package com.github.bitsapling.sapling.controller.torrent.dto.request;

import com.github.bitsapling.sapling.controller.dto.response.UserTinyResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Data
@AllArgsConstructor
@Validated
public class ThanksResponseDTO {
    private long thanks;
    private List<UserTinyResponseDTO> users;
}
