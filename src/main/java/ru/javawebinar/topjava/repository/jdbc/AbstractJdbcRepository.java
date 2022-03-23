package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

public abstract class AbstractJdbcRepository<T> {

    protected final JdbcTemplate jdbcTemplate;

    protected final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    protected final Validator validator;

    public AbstractJdbcRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    protected void validate(T entity) {
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(entity);
        if (!constraintViolations.isEmpty()) {
            throw new ConstraintViolationException(constraintViolations);
        }
    }
}
