package com.academy.fintech.dwh.core.application.repository;

import com.academy.fintech.dwh.core.application.repository.entity.ApplicationEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ApplicationRepositoryImpl implements ApplicationRepository {
    private final DataClassRowMapper<ApplicationEntity> rowMapper = DataClassRowMapper.newInstance(ApplicationEntity.class);
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public void save(ApplicationEntity entity) {
        var sql = """
                INSERT INTO data_application(data_application_id, application_id, status, business_date)
                VALUES (:dataApplicationId, :applicationId, :status, :businessDate)
                """;

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("dataApplicationId", entity.dataApplicationId());
        parameters.addValue("applicationId", entity.applicationId());
        parameters.addValue("status", entity.status());
        parameters.addValue("businessDate", entity.businessDate());

        jdbcTemplate.update(sql, parameters);
    }

    @Override
    public Optional<ApplicationEntity> findByDataApplicationId(String dataApplicationId) {
        var sql = """
                SELECT data_application_id, application_id, status, business_date
                FROM data_application
                WHERE data_application_id = :dataApplicationId
                """;

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("dataApplicationId", dataApplicationId);

        try {
            var dataApplication = jdbcTemplate.query(sql, parameters, rowMapper).get(0);
            return Optional.of(dataApplication);
        } catch (EmptyResultDataAccessException | IndexOutOfBoundsException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<ApplicationEntity> findAllByApplicationId(String applicationId) {
        var sql = """
                SELECT data_application_id, application_id, status, business_date
                FROM data_application
                WHERE application_id = :applicationId
                """;

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("applicationId", applicationId);

        return jdbcTemplate.query(sql, parameters, rowMapper);
    }

    @Override
    public void deleteAll() {
        var sql = "DELETE FROM data_application";
        jdbcTemplate.update(sql, new MapSqlParameterSource());
    }
}
