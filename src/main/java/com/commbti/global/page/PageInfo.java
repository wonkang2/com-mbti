package com.commbti.global.page;

import com.commbti.domain.bulletinboard.entity.Bulletin;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@Getter
@NoArgsConstructor
public class PageInfo {
    private int pageSize;
    private Long total;
    private int currentPage;
    private int totalPage;
    private int startNumber;
    private int endNumber;
    private boolean hasNextPage;
    private boolean hasPreviousPage;



    public PageInfo(Page page) {
        this.pageSize = page.getSize();
        this.total = page.getTotalElements();
        this.currentPage = page.getNumber() + 1;
        this.totalPage = page.getTotalPages();
        this.hasNextPage = page.hasNext();
        this.hasPreviousPage = page.hasPrevious();
        calculateStartPageNumberAndEndPageNumber();
    }

    public void calculateStartPageNumberAndEndPageNumber() {
        // 1 2 3 4 5 6 7 8 9 10
        // 11 12 13 14 15 16 17 18 19 20
        this.endNumber = Math.ceil(currentPage / 10.0) * 10 < totalPage ? (int)(Math.ceil(currentPage / 10.0) * 10) : totalPage;
        this.startNumber = (endNumber - 9) > 0 ? endNumber - 9 : 1;
    }
}
