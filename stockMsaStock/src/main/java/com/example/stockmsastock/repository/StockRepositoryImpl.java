package com.example.stockmsastock.repository;

import com.example.stockmsastock.domain.stock.Stock;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
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
    public Page<Stock> findAllOrderedBy(String sortBy, String sortOrder, Pageable pageable) {
        BooleanExpression expression = stock.isNotNull();

        OrderSpecifier<String> orderSpecifier;
        if ("name".equalsIgnoreCase(sortBy)) {
            orderSpecifier = getOrderSpecifier(stock.stockName, sortOrder);
        } else {
            orderSpecifier = getOrderSpecifier(stock.itemCode, sortOrder);
        }

        return applyPagination(pageable, expression, orderSpecifier);
    }

    private OrderSpecifier<String> getOrderSpecifier(com.querydsl.core.types.Path<String> field, String sortOrder) {
        Order order = "desc".equalsIgnoreCase(sortOrder) ? Order.DESC : Order.ASC;
        return new OrderSpecifier<>(order, field);
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