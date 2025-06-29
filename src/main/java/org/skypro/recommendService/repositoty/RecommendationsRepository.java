package org.skypro.recommendService.repositoty;

import org.skypro.recommendService.DTO.Recommendation;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
public class RecommendationsRepository {
    private final JdbcTemplate jdbcTemplate;

    public RecommendationsRepository(@Qualifier("recommendationsJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


//    public boolean recommendTopSaving(UUID userId) {
//        String sql1 = loadQuery("sql/TopSaving/sqlRequest1.sql");
//        String sql2 = loadQuery("sql/TopSaving/sqlRequest2.sql");
//        String sql3 = loadQuery("sql/TopSaving/sqlRequest3.sql");
//
//        boolean exists1 = Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql1, Boolean.class, userId));
//
//        Map<String, Object> result2 = jdbcTemplate.queryForMap(sql2, userId);
//        Boolean debitCondition = (Boolean) result2.get("debit_condition");
//        Boolean savingCondition = (Boolean) result2.get("saving_condition");
//        Boolean exists3 = jdbcTemplate.queryForObject(sql3, Boolean.class, userId);
//        return exists1 || Boolean.TRUE.equals(debitCondition) || Boolean.TRUE.equals(savingCondition) || Boolean.TRUE.equals(exists3);
//    }
//
//    public boolean recommendInvest500(UUID userId) {
//        return false;
//    }
//
//    public boolean recommendJustCredit(UUID userId) {
//        return false;
//    }
//
//    private String loadQuery(String path) {
//        try {
//            Resource resource = new ClassPathResource(path);
//            byte[] bytes = FileCopyUtils.copyToByteArray(resource.getInputStream());
//            return new String(bytes, StandardCharsets.UTF_8).trim();
//        } catch (Exception e) {
//            throw new RuntimeException("Ошибка загрузки SQL-запроса: " + path, e);
//        }
//    }
}




