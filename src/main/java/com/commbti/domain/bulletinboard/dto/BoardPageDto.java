package com.commbti.domain.bulletinboard.dto;

import com.commbti.domain.bulletinboard.entity.Bulletin;
import com.commbti.domain.page.dto.PageInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class BoardPageDto {
    private PageInfo pageInfo;
    private List<BoardResponseDto> boardResponseDtoList;

    private BoardPageDto(PageInfo pageInfo, List<BoardResponseDto> boardResponseDtoList) {
        this.pageInfo = pageInfo;
        this.boardResponseDtoList = boardResponseDtoList;
    }

    public static BoardPageDto toPageDto(Page<Bulletin> boardPage) {
        PageInfo pageInfo = new PageInfo(boardPage);
        List<BoardResponseDto> boardResponseDtoList = boardPage.stream().map(article -> article.toBoardResponseDto())
                .collect(Collectors.toList());

        return new BoardPageDto(pageInfo, boardResponseDtoList);
    }
}
