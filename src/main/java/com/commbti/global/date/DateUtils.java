package com.commbti.global.date;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * LocalDateTime -> '몇 ?(초,분,일,달,년) 전' 으로 포맷팅
 */
public class DateUtils {

    public static String convertToTimesAgo(LocalDateTime localDateTime) {
        String result;
        long gap = ChronoUnit.MINUTES.between(localDateTime, LocalDateTime.now());

        if (gap == 0) {
            result = "방금 전";
        } else if (gap < 60) {
            result = gap + "분 전";
        } else if (gap < 60 * 24) {
            result = (gap / 60) + "시간 전";
        } else if (gap < 60 * 24 * 30) {
            result = (gap / 60 / 24) + "일 전";
        } else if (gap < 60 * 24 * 30 * 12) {
            result = (gap / 60 / 24 / 30) + "달 전";
        } else {
            result = (gap / 60 / 24 / 30 / 12) + "년 전";
        }
        return result;
    }
}
