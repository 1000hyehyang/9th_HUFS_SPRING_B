package com.spring_b.thousandhyehyang.store.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.spring_b.thousandhyehyang.store.entity.QStore;
import com.spring_b.thousandhyehyang.store.entity.Store;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class StoreRepositoryImpl implements StoreRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public StoreRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Page<Store> searchStores(List<String> regions, String searchKeyword, Pageable pageable) {
        QStore store = QStore.store;

        BooleanBuilder builder = new BooleanBuilder();

        // 삭제되지 않은 가게만 조회
        builder.and(store.deletedAt.isNull());

        // 지역 필터링 (다중 선택 가능)
        if (regions != null && !regions.isEmpty()) {
            // 공백 제거 및 빈 문자열 필터링
            List<String> validRegions = regions.stream()
                    .filter(region -> region != null && !region.trim().isEmpty())
                    .map(String::trim)
                    .toList();

            if (!validRegions.isEmpty()) {
                BooleanBuilder regionBuilder = new BooleanBuilder();
                for (String region : validRegions) {
                    regionBuilder.or(store.sigungu.eq(region));
                }
                builder.and(regionBuilder);
            }
        }

        // 이름 검색
        if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
            String trimmedKeyword = searchKeyword.trim();
            
            // 공백 포함 여부 확인
            if (trimmedKeyword.contains(" ")) {
                // 공백 포함: 각 단어가 포함된 가게의 합집합 (OR)
                BooleanBuilder keywordBuilder = new BooleanBuilder();
                String[] keywords = trimmedKeyword.split("\\s+");
                for (String keyword : keywords) {
                    if (!keyword.isEmpty()) {
                        keywordBuilder.or(store.name.containsIgnoreCase(keyword));
                    }
                }
                builder.and(keywordBuilder);
            } else {
                // 공백 없음: 검색어 전체가 포함된 가게만 조회
                builder.and(store.name.containsIgnoreCase(trimmedKeyword));
            }
        }

        JPAQuery<Store> query = queryFactory
                .selectFrom(store)
                .where(builder);

        // 정렬 적용
        List<OrderSpecifier<?>> orderSpecifiers = convertToQueryDslOrder(pageable.getSort(), store);
        if (!orderSpecifiers.isEmpty()) {
            query.orderBy(orderSpecifiers.toArray(new OrderSpecifier[0]));
        } else {
            // 기본 정렬: 최신순
            query.orderBy(store.createdAt.desc());
        }

        // 페이징 적용
        List<Store> stores = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 전체 개수 조회
        Long totalCount = queryFactory
                .select(store.count())
                .from(store)
                .where(builder)
                .fetchOne();

        long total = totalCount != null ? totalCount : 0L;

        return new PageImpl<>(stores, pageable, total);
    }

    /**
     * Spring Data Sort를 QueryDSL OrderSpecifier로 변환
     *
     * @param sort Spring Data Sort 객체
     * @param store QStore 객체
     * @return QueryDSL OrderSpecifier 리스트
     */
    private List<OrderSpecifier<?>> convertToQueryDslOrder(Sort sort, QStore store) {
        List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();

        if (sort == null || !sort.isSorted()) {
            return orderSpecifiers;
        }

        // 여러 정렬 조건을 모두 처리
        for (Sort.Order order : sort) {
            String property = order.getProperty();
            com.querydsl.core.types.Order direction = order.isAscending()
                    ? com.querydsl.core.types.Order.ASC
                    : com.querydsl.core.types.Order.DESC;

            OrderSpecifier<?> orderSpecifier = null;
            switch (property) {
                case "createdAt":
                    orderSpecifier = new OrderSpecifier<>(direction, store.createdAt);
                    break;
                case "name":
                    orderSpecifier = new OrderSpecifier<>(direction, store.name);
                    break;
                default:
                    break;
            }

            if (orderSpecifier != null) {
                orderSpecifiers.add(orderSpecifier);
            }
        }

        return orderSpecifiers;
    }
}


