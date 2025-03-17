//package com.example.demo.repository;
//
//import com.example.demo.entity.UserEntity;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.jdbc.core.RowMapper;
//import org.springframework.stereotype.Repository;
//
//import javax.sql.DataSource;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.List;
//
//@Repository
//public class PostgresUserRepository {
//
//    private final JdbcTemplate jdbcTemplate;
//
//    @Autowired
//    public PostgresUserRepository(DataSource dataSource) {
//        this.jdbcTemplate = new JdbcTemplate(dataSource);
//    }
//
//    private final RowMapper<UserEntity> userRowMapper = (ResultSet rs, int rowNum) -> {
//        UserEntity user = new UserEntity();
//        user.setId(rs.getLong("id"));
////        user.setUsername(rs.getString("username"));
//        user.setPassword(rs.getString("password"));
////        user.setDob(rs.getDate("dob").toLocalDate());
//        return user;
//    };
//
////    public List<UserEntity> findAllUsers() {
////        String sql = "SELECT * FROM user_entity_table";
////        return jdbcTemplate.query(sql, userRowMapper);
////    }
//}