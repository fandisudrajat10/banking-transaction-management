package com.fandi.bankingtransaction.util;

import com.fandi.bankingtransaction.dto.SearchDto;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class SearchUtils {

    private static final int DEFAULT_PAGE_SIZE = 10;

    private SearchUtils() {
    }

    public static Pageable toPageable(SearchDto searchDto) {
        int page = searchDto.getPage() > 0 ? searchDto.getPage() - 1 : 0;
        int size = searchDto.getSize() > 0 ? searchDto.getSize() : DEFAULT_PAGE_SIZE;
        if (!StringUtils.hasText(searchDto.getSort())) {
            return PageRequest.of(page, size);
        } else {
            String[] sortValues = searchDto.getSort().split(",");
            List<Sort.Order> sortOrderList = new ArrayList();
            String[] var5 = sortValues;
            int var6 = sortValues.length;

            for (int var7 = 0; var7 < var6; ++var7) {
                String sortValue = var5[var7];
                String[] sorts = sortValue.split("-");
                String sortDirection = sorts[1];
                String sortField = sorts[0];
                sortOrderList.add(new Sort.Order(getSortDirection(sortDirection), sortField));
            }

            return PageRequest.of(page, size, Sort.by(sortOrderList));
        }
    }

    private static Sort.Direction getSortDirection(String sortDirection) {
        if (sortDirection.equalsIgnoreCase("asc")) {
            return Sort.Direction.ASC;
        } else if (sortDirection.equalsIgnoreCase("desc")) {
            return Sort.Direction.DESC;
        } else {
            throw new IllegalArgumentException("Allowed sort direction only (asc,desc)");
        }
    }

}
