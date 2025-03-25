package com.devteria.post.service;

import com.devteria.post.exception.AppException;
import com.devteria.post.exception.ErrorCode;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DateTimeFormatter {

  Map<Long, Function<Instant, String>> strategyMap = new LinkedHashMap<>();

  public DateTimeFormatter() {
    strategyMap.put(60L, this::formatInSeconds);
    strategyMap.put(3600L, this::formatInMinutes);
    strategyMap.put(86400L, this::formatInHours);
    strategyMap.put(Long.MAX_VALUE, this::formatInDate);
  }

  private String formatInSeconds(Instant instant) {
    long elapsedSeconds = ChronoUnit.SECONDS.between(instant, Instant.now());
    return elapsedSeconds + " seconds";
  }

  private String formatInMinutes(Instant instant) {
    long elapsedMinutes = ChronoUnit.MINUTES.between(instant, Instant.now());
    return elapsedMinutes + " minutes";
  }

  private String formatInHours(Instant instant) {
    long elapsedHours = ChronoUnit.HOURS.between(instant, Instant.now());
    return elapsedHours + " hours";
  }

  private String formatInDate(Instant instant) {
    LocalDate localDate = LocalDate.ofInstant(instant, ZoneId.systemDefault());
    return localDate.toString();
  }

  public String format(Instant instant) {
    long elapsedSeconds = ChronoUnit.SECONDS.between(instant, Instant.now());

    Entry<Long, Function<Instant, String>> strategy = strategyMap.entrySet().stream()
        .filter(longFunctionEntry -> {
          log.info(String.valueOf(longFunctionEntry.getKey()));
          return elapsedSeconds < longFunctionEntry.getKey();
        })
        .findFirst().orElseThrow(() -> new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION));

    return strategy.getValue().apply(instant);
  }
}
