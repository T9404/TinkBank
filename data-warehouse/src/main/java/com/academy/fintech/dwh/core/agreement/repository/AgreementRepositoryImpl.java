package com.academy.fintech.dwh.core.agreement.repository;

import com.academy.fintech.dwh.core.agreement.repository.entity.AgreementEntity;
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
public class AgreementRepositoryImpl implements AgreementRepository {
    private final DataClassRowMapper<AgreementEntity> rowMapper = DataClassRowMapper.newInstance(AgreementEntity.class);
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public void save(AgreementEntity entity) {
        var sql = """
                INSERT INTO data_agreement(data_agreement_id, agreement_number, status, business_date, amount)
                VALUES (:dataAgreementId, :agreementNumber, :status, :businessDate, :amount)
                """;

        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("dataAgreementId", entity.dataAgreementId());
        parameterSource.addValue("agreementNumber", entity.agreementNumber());
        parameterSource.addValue("status", entity.status());
        parameterSource.addValue("businessDate", entity.businessDate());
        parameterSource.addValue("amount", entity.amount());

        jdbcTemplate.update(sql, parameterSource);
    }

    @Override
    public Optional<AgreementEntity> findByDataAgreementId(String dataAgreementId) {
        var sql = """
                SELECT data_agreement_id, agreement_number, status, business_date, amount
                FROM data_agreement
                WHERE data_agreement_id = :dataAgreementId
                """;

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("dataAgreementId", dataAgreementId);

        try {
            var dataApplication = jdbcTemplate.query(sql, parameters, rowMapper).get(0);
            return Optional.of(dataApplication);
        } catch (EmptyResultDataAccessException | IndexOutOfBoundsException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<AgreementEntity> findAllByAgreementNumber(String agreementNumber) {
        var sql = """
                SELECT data_agreement_id, agreement_number, status, business_date, amount
                FROM data_agreement
                WHERE agreement_number = :agreementNumber
                """;

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("agreementNumber", agreementNumber);

        return jdbcTemplate.query(sql, parameters, rowMapper);
    }

    @Override
    public void deleteAll() {
        var sql = "DELETE FROM data_agreement";
        jdbcTemplate.update(sql, new MapSqlParameterSource());
    }
}
