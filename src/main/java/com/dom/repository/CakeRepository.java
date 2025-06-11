package com.dom.repository;

import com.dom.model.CakeEntity;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;

import java.util.List;

@JdbcRepository(dialect = Dialect.H2)
public interface CakeRepository extends CrudRepository<CakeEntity, Long> {
    List<CakeEntity> findByTitle(String title);
}
