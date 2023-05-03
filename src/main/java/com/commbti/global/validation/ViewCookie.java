package com.commbti.global.validation;

import com.commbti.controller.ViewController;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.Cookie;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Slf4j
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ViewCookie {
    private boolean increaseNumberViews;
    private Cookie cookie;

    public static ViewCookie validateViewNumberCookie(Long bulletinId, Cookie cookie) {
        log.debug("validateViewNumberCookie 호출");
        boolean increaseNumberViews;
        if (cookie == null || cookie.getValue() == null) {
            log.debug("전달받은 cookie 또는 cookie의 값이 널!");
            cookie = new Cookie("VIEWNUMBER", "[" + bulletinId + "]");
            cookie.setPath("/");
            cookie.setMaxAge(calculateRemainingEndDay());
            increaseNumberViews = true;
        } else {
            if (cookie.getValue().contains("[" + bulletinId + "]")) {
                increaseNumberViews = false;
            } else {
                cookie.setValue(cookie.getValue() + "[" + bulletinId + "]");
                increaseNumberViews = true;
            }
        }
        return new ViewCookie(increaseNumberViews, cookie);
    }

    private ViewCookie(boolean increaseNumberViews, Cookie cookie) {
        this.increaseNumberViews = increaseNumberViews;
        this.cookie = cookie;
    }

    private static int calculateRemainingEndDay() {
        return LocalDate.now().atTime(LocalTime.MAX).getSecond() - LocalDateTime.now().getSecond();
    }

    public boolean isIncreaseNumberViews() {
        return increaseNumberViews;
    }
}
