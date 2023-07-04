package com.github.bitsapling.sapling.mbp;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.function.Function;

@AllArgsConstructor
@Getter
public class PagedResult<T> {
    private final List<OrderItem> orders;
    private final long offset;
    private final Long maxLimit;
    private final long pages;
    private final long total;
    private final long size;
    private final long current;
    private final String countId;
    private List<T> records;

    public static <T, R> PagedResult<R> from(IPage<T> iPage, Function<T, R> recordsMapper) {
        return new PagedResult<>(
                ImmutableList.copyOf(iPage.orders()),
                iPage.offset(),
                iPage.maxLimit(),
                iPage.getPages(),
                iPage.getTotal(),
                iPage.getSize(),
                iPage.getCurrent(),
                iPage.countId(),
                iPage.getRecords().stream().map(recordsMapper).toList()
        );
    }
}
