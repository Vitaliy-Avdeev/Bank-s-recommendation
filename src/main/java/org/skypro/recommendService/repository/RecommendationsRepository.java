package org.skypro.recommendService.repository;

import org.skypro.recommendService.DTO.RecommendationObject;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class RecommendationsRepository {
    final static String INVEST500 = "Откройте свой путь к успеху с индивидуальным инвестиционным счетом (ИИС) от нашего банка! Воспользуйтесь налоговыми льготами и начните инвестировать с умом. Пополните счет до конца года и получите выгоду в виде вычета на взнос в следующем налоговом периоде. Не упустите возможность разнообразить свой портфель, снизить риски и следить за актуальными рыночными тенденциями. Откройте ИИС сегодня и станьте ближе к финансовой независимости!";
    final static String TOP_SAVING = "Откройте свою собственную «Копилку» с нашим банком! «Копилка» — это уникальный банковский инструмент, который поможет вам легко и удобно накапливать деньги на важные цели. Больше никаких забытых чеков и потерянных квитанций — всё под контролем!\nПреимущества «Копилки»:\nНакопление средств на конкретные цели. Установите лимит и срок накопления, и банк будет автоматически переводить определенную сумму на ваш счет.\nПрозрачность и контроль. Отслеживайте свои доходы и расходы, контролируйте процесс накопления и корректируйте стратегию при необходимости.\nБезопасность и надежность. Ваши средства находятся под защитой банка, а доступ к ним возможен только через мобильное приложение или интернет-банкинг.\nНачните использовать «Копилку» уже сегодня и станьте ближе к своим финансовым целям!";
    final static String SIMPLE_CREDIT = "Откройте мир выгодных кредитов с нами!\nИщете способ быстро и без лишних хлопот получить нужную сумму? Тогда наш выгодный кредит — именно то, что вам нужно! Мы предлагаем низкие процентные ставки, гибкие условия и индивидуальный подход к каждому клиенту.\nПочему выбирают нас\nБыстрое рассмотрение заявки. Мы ценим ваше время, поэтому процесс рассмотрения заявки занимает всего несколько часов.\nУдобное оформление. Подать заявку на кредит можно онлайн на нашем сайте или в мобильном приложении.\nШирокий выбор кредитных продуктов. Мы предлагаем кредиты на различные цели: покупку недвижимости, автомобиля, образование, лечение и многое другое.\nНе упустите возможность воспользоваться выгодными условиями кредитования от нашей компании!";

    private final JdbcTemplate jdbcTemplate;

    public RecommendationsRepository(@Qualifier("recommendationsJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<List<RecommendationObject>> checkInvest500(UUID id){
        UUID productId = UUID.fromString("147f6a0f-3b91-413b-ab99-87f081d60d5a");

        Boolean result1 = jdbcTemplate.queryForObject(
                "SELECT count(*) > 0 FROM TRANSACTIONS t " +
                        "join PRODUCTS p on t.PRODUCT_ID = p.ID " +
                        "WHERE t.USER_ID = ? and p.TYPE = 'DEBIT'",
                Boolean.class,
                id);
        Boolean result2 = jdbcTemplate.queryForObject(
                "SELECT count(*) = 0 FROM TRANSACTIONS t " +
                        "join PRODUCTS p on t.PRODUCT_ID = p.ID " +
                        "WHERE t.USER_ID = ? and p.TYPE = 'INVEST'",
                Boolean.class,
                id);
        Boolean result3 = jdbcTemplate.queryForObject(
                "SELECT SUM(AMOUNT) > 1000 FROM TRANSACTIONS t " +
                        "join PRODUCTS p on t.PRODUCT_ID = p.ID " +
                        "WHERE t.USER_ID = ? and p.TYPE = 'SAVING' AND t.TYPE = 'DEPOSIT'",
                Boolean.class,
                id);

        if (Boolean.TRUE.equals(result1) && Boolean.TRUE.equals(result2) && Boolean.TRUE.equals(result3)){
            return Optional.of(List.of(
                    new RecommendationObject(productId, "Invest 500", INVEST500)
                    ));
        }
        return Optional.empty();
    }

    public Optional<List<RecommendationObject>> checkTopSaving(UUID id){
        UUID productId = UUID.fromString("59efc529-2fff-41af-baff-90ccd7402925");

        Boolean result1 = jdbcTemplate.queryForObject(
                "SELECT count(*) > 0 FROM TRANSACTIONS t " +
                        "join PRODUCTS p on t.PRODUCT_ID = p.ID " +
                        "WHERE t.user_id = ? and p.TYPE = 'DEBIT'",
                Boolean.class,
                id);
        Boolean result2_1 = jdbcTemplate.queryForObject(
                "SELECT sum(t.AMOUNT) >= 50000 FROM TRANSACTIONS t " +
                        "join PRODUCTS p on t.PRODUCT_ID = p.ID " +
                        "WHERE t.USER_ID = ? and p.TYPE = 'DEBIT' AND t.TYPE = 'DEPOSIT'",
                Boolean.class,
                id);
        Boolean result2_2 = jdbcTemplate.queryForObject(
                "SELECT sum(t.AMOUNT) >= 50000 FROM TRANSACTIONS t " +
                        "join PRODUCTS p on t.PRODUCT_ID = p.ID " +
                        "WHERE t.USER_ID = ? and p.TYPE = 'SAVING' AND t.TYPE = 'DEPOSIT'",
                Boolean.class,
                id);
        Boolean result3 = jdbcTemplate.queryForObject(
                "SELECT total_deposits > total_withdraws AS is_deposit_greater " +
                        "FROM (SELECT " +
                                "SUM(CASE WHEN t.TYPE = 'DEPOSIT' THEN t.AMOUNT ELSE 0 END) AS total_deposits, " +
                                "SUM(CASE WHEN t.TYPE = 'WITHDRAW' THEN t.AMOUNT ELSE 0 END) AS total_withdraws " +
                                "FROM TRANSACTIONS t " +
                                "JOIN PRODUCTS p ON t.PRODUCT_ID = p.ID " +
                                "WHERE p.TYPE = 'DEBIT' AND t.USER_ID = ? " +
                        ") sub",
                Boolean.class,
                id);

        if (Boolean.TRUE.equals(result1) && (Boolean.TRUE.equals(result2_1) || Boolean.TRUE.equals(result2_2))
                && Boolean.TRUE.equals(result3)){
            return Optional.of(List.of(
                    new RecommendationObject(productId, "Top Saving", TOP_SAVING)
            ));
        }
        return Optional.empty();
    }

    public Optional<List<RecommendationObject>> checkSimpleCredit(UUID id){
        UUID productId = UUID.fromString("ab138afb-f3ba-4a93-b74f-0fcee86d447f");

        Boolean result1 = jdbcTemplate.queryForObject(
                "SELECT count(*) = 0 FROM TRANSACTIONS t " +
                        "join PRODUCTS p on t.PRODUCT_ID = p.ID " +
                        "WHERE t.user_id = ? and p.TYPE = 'CREDIT'",
                Boolean.class,
                id);
        Boolean result2 = jdbcTemplate.queryForObject(
                "SELECT total_deposits > total_withdraws AS is_deposit_greater " +
                        "FROM (SELECT " +
                        "SUM(CASE WHEN t.TYPE = 'DEPOSIT' THEN t.AMOUNT ELSE 0 END) AS total_deposits, " +
                        "SUM(CASE WHEN t.TYPE = 'WITHDRAW' THEN t.AMOUNT ELSE 0 END) AS total_withdraws " +
                        "FROM TRANSACTIONS t " +
                        "JOIN PRODUCTS p ON t.PRODUCT_ID = p.ID " +
                        "WHERE p.TYPE = 'DEBIT' AND t.USER_ID = ? " +
                        ") sub",
                Boolean.class,
                id);
        Boolean result3 = jdbcTemplate.queryForObject(
                "SELECT sum(t.AMOUNT) > 100000 FROM TRANSACTIONS t " +
                        "join PRODUCTS p on t.PRODUCT_ID = p.ID " +
                        "WHERE t.USER_ID = ? and p.TYPE = 'DEBIT' AND t.TYPE = 'WITHDRAW'",
                Boolean.class,
                id);

        if (Boolean.TRUE.equals(result1) && Boolean.TRUE.equals(result2) && Boolean.TRUE.equals(result3)){
            return Optional.of(List.of(
                    new RecommendationObject(id, "Simple credit", SIMPLE_CREDIT)
            ));
        }
        return Optional.empty();
    }
}
