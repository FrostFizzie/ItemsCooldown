package me.FrostFizzie.itemscooldown.client;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class SingleTimeDuration {
    enum TimeUnit {
        YEARS(31449600,"y"),
        WEEKS(604800, "w"),
        DAYS   (86400, "d"),
        HOURS  (3600,  "h"),
        MINUTES(60,    "m"),
        SECONDS(1,     "s"),
        MILLISECOND(0.001f, "ms");

        final float seconds;
        final String symbol;

        TimeUnit(float seconds, String symbol) {
            this.seconds = seconds;
            this.symbol = symbol;
        }
    }
    public static String formatDuration(float seconds) {
        for (TimeUnit unit : TimeUnit.values()) {
            if (seconds >= unit.seconds) {
                double value = (double) seconds / unit.seconds;
                value = unit == TimeUnit.MILLISECOND ? Math.round(value) : Math.round(value * 100.0) / 100.0;
                return new BigDecimal(value).setScale(2, RoundingMode.HALF_UP).stripTrailingZeros().stripTrailingZeros().toPlainString() + unit.symbol;
            }
        }
        return "0s";
    }

}
