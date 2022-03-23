package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.sql.*;
import java.sql.Date;
import java.util.*;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepository extends AbstractJdbcRepository<User> implements UserRepository {

    private static final ResultSetExtractor<List<User>> RESULT_EXTRACTOR = new UserResultExtractor();

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(jdbcTemplate, namedParameterJdbcTemplate);
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Transactional
    @Override
    public User save(User user) {
        validate(user);

        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);

        jdbcTemplate.update("DELETE FROM user_roles WHERE user_id=?", user.getId());
        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
        } else if (namedParameterJdbcTemplate.update("""
                   UPDATE users SET name=:name, email=:email, password=:password, 
                   registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id
                """, parameterSource) == 0) {
            return null;
        }
        ArrayList<Role> roles = new ArrayList<>(user.getRoles());
        jdbcTemplate.batchUpdate(
                "insert into user_roles(user_id, role) values(?, ?)",
                new BatchPreparedStatementSetter() {
                    public void setValues(PreparedStatement statement, int statementIndex) throws SQLException {
                        statement.setInt(1, user.getId());
                        statement.setString(2, roles.get(statementIndex).name());
                    }

                    public int getBatchSize() {
                        return roles.size();
                    }
                });
        return user;
    }

    @Transactional
    @Override
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        List<User> users = jdbcTemplate.query("SELECT u.*, r.role FROM users u LEFT JOIN user_roles r ON r.user_id = u.id WHERE id=?", RESULT_EXTRACTOR, id);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public User getByEmail(String email) {
//        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        List<User> users = jdbcTemplate.query("SELECT u.*, r.role FROM users u LEFT JOIN user_roles r ON r.user_id = u.id WHERE email=?", RESULT_EXTRACTOR, email);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query("SELECT u.*, r.role FROM users u LEFT JOIN user_roles r ON r.user_id = u.id ORDER BY name, email", RESULT_EXTRACTOR);
    }

    private static class UserResultExtractor implements ResultSetExtractor<List<User>> {
        @Override
        public List<User> extractData(ResultSet rs) throws SQLException, DataAccessException {
            Map<Integer, User> users = new LinkedHashMap<>();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                String password = rs.getString("password");
                Date registered = new Date(rs.getTimestamp("registered").getTime());
                boolean enabled = rs.getBoolean("enabled");
                int caloriesPerDay = rs.getInt("calories_per_day");
                String roleAsString = rs.getString("role");
                Role role = roleAsString == null ? null : Role.valueOf(roleAsString);
                List<Role> roles = role == null ? Collections.emptyList() : List.of(role);
                users.compute(id, (key, oldUser) -> {
                    if (oldUser == null) {
                        return new User(id, name, email, password, caloriesPerDay, enabled, registered, roles);
                    } else {
                        Set<Role> oldRoles = oldUser.getRoles();
                        oldRoles.addAll(roles);
                        oldUser.setRoles(oldRoles);
                        return oldUser;
                    }
                });
            }
            return new ArrayList<>(users.values());
        }
    }
}
