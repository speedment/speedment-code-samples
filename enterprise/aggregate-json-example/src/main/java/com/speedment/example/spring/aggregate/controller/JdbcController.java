package com.speedment.example.spring.aggregate.controller;

import com.speedment.example.spring.aggregate.util.Utils;
import lombok.Data;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Emil Forslund
 * @since  1.0.0
 */
@RestController
@RequestMapping("/jdbc")
public class JdbcController {

    private final JdbcTemplate template;

    JdbcController(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
    }

    @GetMapping
    Result getEmployeeSalaries(@RequestParam String from,
                               @RequestParam String to) {
        return template.queryForObject(
            "select count(emp_no),min(from_date),max(to_date),avg(salary) " +
            "from salaries where from_date < ? and to_date >= ?;",
            (rs, n) -> new Result(rs),
            to, from
        );
    }

    @Data
    static class Result {
        private final long count;
        private final String from, to, average;

        Result(ResultSet rs) throws SQLException {
            count   = rs.getLong(1);
            from    = rs.getString(2);
            to      = rs.getString(3);
            average = Utils.CASH.format(rs.getDouble(4));
        }
    }
}
