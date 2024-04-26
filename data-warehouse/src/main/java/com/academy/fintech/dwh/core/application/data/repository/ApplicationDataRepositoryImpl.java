package com.academy.fintech.dwh.core.application.data.repository;

import com.academy.fintech.dwh.core.application.data.repository.entity.ApplicationDataEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ApplicationDataRepositoryImpl implements ApplicationDataRepository {
    private final DataClassRowMapper<ApplicationDataEntity> rowMapper = DataClassRowMapper.newInstance(ApplicationDataEntity.class);
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public void save(ApplicationDataEntity entity) {
        var sql = """
                INSERT INTO data_application(id, content, business_date)
                VALUES (:id, :content, :businessDate)
                """;

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("id", entity.id());
        parameters.addValue("content", entity.content());
        parameters.addValue("businessDate", entity.businessDate());

        jdbcTemplate.update(sql, parameters);
    }

    @Override
    public Optional<ApplicationDataEntity> findById(String id) {
        var sql = """
                SELECT id, content, business_date
                FROM data_application
                WHERE id = :id
                """;

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("id", id);

        try {
            var dataApplication = jdbcTemplate.query(sql, parameters, rowMapper).get(0);
            return Optional.of(dataApplication);
        } catch (EmptyResultDataAccessException | IndexOutOfBoundsException e) {
            return Optional.empty();
        }
    }

    @Override
    public void deleteAll() {
        var sql = "DELETE FROM data_application";
        jdbcTemplate.update(sql, new MapSqlParameterSource());
    }
}
