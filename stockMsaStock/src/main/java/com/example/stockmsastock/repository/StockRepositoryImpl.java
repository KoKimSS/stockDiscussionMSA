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

import static com.example.stockmsastock.domain.stock.QStock.stock;

@RequiredArgsConstructor
public class StockRepositoryImpl implements StockRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Stock> getPageOrderBy(String sortBy, String sortOrder, Pageable pageable) {
        BooleanExpression expression = stock.isNotNull();

        OrderSpecifier<Comparable<?>> orderSpecifier = getOrderSpecifier(sortBy, sortOrder);

        return applyPagination(pageable, expression, orderSpecifier);
    }

    private OrderSpecifier<Comparable<?>> getOrderSpecifier(String sortBy, String sortOrder) {
        Order order = "desc".equalsIgnoreCase(sortOrder) ? Order.DESC : Order.ASC;
        Path<?> field = getPath(sortBy);
        return new OrderSpecifier<>(order, (Path<Comparable<?>>) field);
    }

    private static Path<?> getPath(String sortBy) {
        Path<?> field = null;
        if ("name".equalsIgnoreCase(sortBy)) {
            field = stock.stockName;
        } else if ("code".equalsIgnoreCase(sortBy)) {
            field = stock.itemCode;
        } else if ("fluctuationsRatio".equalsIgnoreCase(sortBy)) {
            field = stock.fluctuationsRatio;
        } else if ("accumulatedTradingVolume".equalsIgnoreCase(sortBy)) {
            field = stock.accumulatedTradingVolume;
        } else if ("accumulatedTradingValue".equalsIgnoreCase(sortBy)) {
            field = stock.accumulatedTradingValue;
        }
        return field;
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