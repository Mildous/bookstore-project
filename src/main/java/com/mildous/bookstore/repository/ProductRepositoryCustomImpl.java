package com.mildous.bookstore.repository;

import com.mildous.bookstore.constant.ProductSellStatus;
import com.mildous.bookstore.dto.MainProductDto;
import com.mildous.bookstore.dto.ProductSearchDto;
import com.mildous.bookstore.dto.QMainProductDto;
import com.mildous.bookstore.entity.Product;
import com.mildous.bookstore.entity.QProduct;
import com.mildous.bookstore.entity.QProductImg;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

public class ProductRepositoryCustomImpl implements ProductRepositoryCustom {

    private JPAQueryFactory queryFactory;

    private ProductRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    private BooleanExpression searchSellStatusEq(ProductSellStatus searchSellStatus) {
        return searchSellStatus == null ? null : QProduct.product.productSellStatus.eq(searchSellStatus);
    }

    private BooleanExpression regDateAfter(String searchDateType) {
        LocalDateTime dt = LocalDateTime.now();

        if(StringUtils.equals("all", searchDateType) || searchDateType == null) {
            return null;
        } else if(StringUtils.equals("1d", searchDateType)) {
            dt = dt.minusDays(1);
        } else if(StringUtils.equals("1w", searchDateType)) {
            dt = dt.minusWeeks(1);
        } else if(StringUtils.equals("1m", searchDateType)) {
            dt = dt.minusMonths(1);
        } else if(StringUtils.equals("6m", searchDateType)) {
            dt = dt.minusMonths(6);
        }

        return QProduct.product.regDate.after(dt);
    }

    private BooleanExpression searchByLike(String searchBy, String searchQuery) {
        if(StringUtils.equals("productName", searchBy)) {
            return QProduct.product.productName.like("%" + searchQuery + "%");
        } else if(StringUtils.equals("createdBy", searchBy)) {
            return QProduct.product.createdBy.like("%" + searchQuery + "%");
        }

        return null;
    }

    @Override
    public Page<Product> getProductAdminPage(ProductSearchDto productSearchDto, Pageable pageable) {
        List<Product> content = queryFactory
                .selectFrom(QProduct.product)
                .where(regDateAfter(productSearchDto.getDate()),
                        searchSellStatusEq(productSearchDto.getStatus()),
                        searchByLike(productSearchDto.getBy(),
                                productSearchDto.getQuery()))
                .orderBy(QProduct.product.productCode.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory.select(Wildcard.count).from(QProduct.product)
                .where(regDateAfter(productSearchDto.getDate()),
                        searchSellStatusEq(productSearchDto.getStatus()),
                        searchByLike(productSearchDto.getBy(), productSearchDto.getQuery()))
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }

    private BooleanExpression productNameLike(String searchQuery){
        return StringUtils.isEmpty(searchQuery) ? null : QProduct.product.productName.like("%" + searchQuery + "%");
    }

    @Override
    public Page<MainProductDto> getMainProductPage(ProductSearchDto productSearchDto, Pageable pageable) {
        QProduct product = QProduct.product;
        QProductImg productImg = QProductImg.productImg;

        List<MainProductDto> content = queryFactory
                .select(
                        new QMainProductDto(
                                product.productCode,
                                product.productName,
                                product.productSubName,
                                product.category,
                                product.author,
                                product.publisher,
                                productImg.imgUrl,
                                product.productPrice
                        )
                )
                .from(productImg)
                .join(productImg.product, product)
                .where(productImg.repImgYn.eq("Y"))
                .where(productNameLike(productSearchDto.getQuery()))
                .orderBy(product.productCode.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .select(Wildcard.count)
                .from(productImg)
                .join(productImg.product, product)
                .where(productImg.repImgYn.eq("Y"))
                .where(productNameLike(productSearchDto.getQuery()))
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }

}
