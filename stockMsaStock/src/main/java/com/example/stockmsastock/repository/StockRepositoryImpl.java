package com.example.stockmsastock.repository;

import com.example.stockmsastock.domain.stock.Stock;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.oxm.ValidationFailureException;

import static com.example.stockmsastock.domain.stock.QStock.stock;
import static com.example.stockmsastock.repository.StockSortOrder.*;
import static com.example.stockmsastock.repository.StockSortType.*;

@RequiredArgsConstructor
public class StockRepositoryImpl implements StockRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Stock> getPageOrderBy(StockSortType sortBy, StockSortOrder sortOrder, Pageable pageable) {
        BooleanExpression expression = stock.isNotNull();

        OrderSpecifier<Comparable<?>> orderSpecifier = getOrderSpecifier(sortBy, sortOrder);

        return applyPagination(pageable, expression, orderSpecifier);
    }

    private static Path<?> getPath(StockSortType sortBy) {
        Path<?> field = null;
        if (sortBy.equals(NAME)) {
            field = stock.stockName;
        } else if (sortBy.equals(CODE)) {
            field = stock.itemCode;
        } else if (sortBy.equals(FLUNCRATIO)) {
            field = stock.fluctuationsRatio;
        } else if (sortBy.equals(ACCUMULVOLUME)) {
            field = stock.accumulatedTradingVolume;
        } else if (sortBy.equals(ACCUMULVALUE)) {
            field = stock.accumulatedTradingValue;
        } else {
            throw new ValidationFailureException("잘못된 sortBy");
        }
        return field;
    }

    private OrderSpecifier<Comparable<?>> getOrderSpecifier(StockSortType sortBy, StockSortOrder sortOrder) {
        Order order = sortOrder.equals(DESC) ? Order.DESC : Order.ASC;
        Path<?> field = getPath(sortBy);
        return new OrderSpecifier<>(order, (Path<Comparable<?>>) field);
    }

    private Page<Stock> applyPagination(Pageable pageable, BooleanExpression expression, OrderSpecifier<?>... orders) {
        long total = queryFactory.selectFrom(stock)
                .where(expression)
                .fetchCount();

        var query = queryFactory.selectFrom(stock)
                .where(expression)
                .orderBy(orders)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        return new PageImpl<>(query.fetch(), pageable, total);
    }
}