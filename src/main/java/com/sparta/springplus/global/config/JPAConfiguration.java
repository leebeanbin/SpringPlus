package com.sparta.springplus.global.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JPAConfiguration {

    @PersistenceContext
    private EntityManager entityManager;


    @PersistenceContext
    private EntityManager entityManager2;


    /**
     * Jpa query factory jpa query factory.
     *
     * @param entityManager the entity manager
     * @return the jpa query factory
     */
    @Bean
    public JPAQueryFactory jpaQueryFactory(EntityManager entityManager) {
        return new JPAQueryFactory(entityManager);
    }

    /**
     * Jpa query factory 2 jpa query factory.
     *
     * @return the jpa query factory
     */
    @Bean
    public JPAQueryFactory jpaQueryFactory2() {
        return new JPAQueryFactory(entityManager2);
    }
}
