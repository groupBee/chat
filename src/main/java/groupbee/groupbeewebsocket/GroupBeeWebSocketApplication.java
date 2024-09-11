package groupbee.groupbeewebsocket;

import org.jetbrains.annotations.NotNull;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@SpringBootApplication
public class GroupBeeWebSocketApplication {

    public static void main(String[] args) {
        SpringApplication.run(GroupBeeWebSocketApplication.class, args);
    }

    @Component
    public static class LongToLocalDateTimeConverter implements Converter<Long, LocalDateTime> {
        @Override
        public LocalDateTime convert(@NotNull Long source) {
            return LocalDateTime.ofInstant(Instant.ofEpochMilli(source), ZoneId.systemDefault());
        }
    }

    @Component
    public static class LocalDateTimeToLongConverter implements Converter<LocalDateTime, Long> {
        @Override
        public Long convert(LocalDateTime source) {
            return source.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        }
    }


}
