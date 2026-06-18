package com.example.check.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.TimeZone;
import java.util.stream.Stream;

@Configuration
public class JacksonConfig {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer customizer() {
        return builder -> {
            builder.locale(Locale.CHINA);
            builder.timeZone(TimeZone.getTimeZone(ZoneId.systemDefault()));
            builder.simpleDateFormat("yyyy-MM-dd HH:mm:ss");

            JavaTimeModule javaTimeModule = new JavaTimeModule();
            javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateEnum.COMMON_DATE_TIME.getDateTimeFormatter()));
            javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateEnum.COMMON_MONTH_DAY.getDateTimeFormatter()));
            javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(DateEnum.COLON_DELIMITED_TIME.getDateTimeFormatter()));
            javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateEnum.COMMON_DATE_TIME.getDateTimeFormatter()));
            javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateEnum.COMMON_MONTH_DAY.getDateTimeFormatter()));
            javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateEnum.COLON_DELIMITED_TIME.getDateTimeFormatter()));

            builder.modules(javaTimeModule);
        };
    }
}
@Getter
@AllArgsConstructor
enum DateEnum {

    /**
     * 时间格式
     */
    COMMON_DATE_TIME(0, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.systemDefault())),
    COMMON_SHORT_DATE(1, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").withZone(ZoneId.systemDefault())),
    COMMON_MONTH_DAY(2, DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(ZoneId.systemDefault())),
    COMMON_MONTH(3, DateTimeFormatter.ofPattern("yyyy-MM").withZone(ZoneId.systemDefault())),
    YEAR(4, DateTimeFormatter.ofPattern("yyyy").withZone(ZoneId.systemDefault())),
    COLON_DELIMITED_TIME(5, DateTimeFormatter.ofPattern("HH:mm:ss").withZone(ZoneId.systemDefault())),
    COLON_DELIMITED_SHORT_TIME(6, DateTimeFormatter.ofPattern("HH:mm").withZone(ZoneId.systemDefault())),
    CHINESE_DATE_TIME(7, DateTimeFormatter.ofPattern("yyyy年MM月dd日HH时mm分ss秒").withZone(ZoneId.systemDefault())),
    CHINESE_SHORT_DATE(8, DateTimeFormatter.ofPattern("yyyy年MM月dd日HH时mm分").withZone(ZoneId.systemDefault())),
    CHINESE_DATE(9, DateTimeFormatter.ofPattern("yyyy年MM月dd日").withZone(ZoneId.systemDefault())),
    CHINESE_MONTH(10, DateTimeFormatter.ofPattern("yyyy年MM月").withZone(ZoneId.systemDefault())),
    CHINESE_YEAR(11, DateTimeFormatter.ofPattern("yyyy年").withZone(ZoneId.systemDefault())),
    CHINESE_TIME(12, DateTimeFormatter.ofPattern("HH时mm分ss秒").withZone(ZoneId.systemDefault())),
    CHINESE_SHORT_TIME(13, DateTimeFormatter.ofPattern("HH时mm分").withZone(ZoneId.systemDefault()));

    private final Integer code;
    private final DateTimeFormatter dateTimeFormatter;


    public static DateEnum findByCode(int code){
        return Stream.of(values())
                .filter(e -> e.getCode().equals(code))
                .findAny()
                .orElse(null);
    }

}