package com.appcenter.wnt.domain.enums;

import java.time.LocalTime;

public enum NailReservationTime {
    NINE_AM(LocalTime.of(9, 0)),
    TEN_AM(LocalTime.of(10, 0)),
    ELEVEN_AM(LocalTime.of(11, 0)),
    NOON(LocalTime.of(12, 0)),
    ONE_PM(LocalTime.of(13, 0)),
    TWO_PM(LocalTime.of(14, 0)),
    THREE_PM(LocalTime.of(15, 0)),
    FOUR_PM(LocalTime.of(16, 0)),
    FIVE_PM(LocalTime.of(17, 0)),
    SIX_PM(LocalTime.of(18, 0));

    private final LocalTime time;

    NailReservationTime(LocalTime time) {
        this.time = time;
    }

    public LocalTime getTime() {
        return time;
    }
}
