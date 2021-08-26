package eu.vortexgg.rpg.util;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.apache.commons.lang.time.FastDateFormat;

import java.text.DecimalFormat;
import java.time.ZoneId;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class TimeUtil {
    public static final TimeZone SERVER_TIME_ZONE = TimeZone.getTimeZone("EST");
    public static final ZoneId SERVER_ZONE_ID = SERVER_TIME_ZONE.toZoneId();
    public static final FastDateFormat DAY_MTH_HR_MIN = FastDateFormat.getInstance("dd/MM HH:mm", SERVER_TIME_ZONE,
            Locale.ENGLISH);
    public static final FastDateFormat DAY_MTH_HR_MIN_SECS = FastDateFormat.getInstance("dd/MM HH:mm:ss",
            SERVER_TIME_ZONE, Locale.ENGLISH);
    public static final FastDateFormat DAY_MTH_YR_HR_MIN_AMPM = FastDateFormat.getInstance("dd/MM/yy hh:mma",
            SERVER_TIME_ZONE, Locale.ENGLISH);
    public static final FastDateFormat DAY_MTH_HR_MIN_AMPM = FastDateFormat.getInstance("dd/MM hh:mma",
            SERVER_TIME_ZONE, Locale.ENGLISH);
    public static final FastDateFormat HR_MIN_AMPM = FastDateFormat.getInstance("hh:mma", SERVER_TIME_ZONE,
            Locale.ENGLISH);
    public static final FastDateFormat HR_MIN_AMPM_TIMEZONE = FastDateFormat.getInstance("hh:mma z", SERVER_TIME_ZONE,
            Locale.ENGLISH);
    public static final FastDateFormat HR_MIN = FastDateFormat.getInstance("hh:mm", SERVER_TIME_ZONE, Locale.ENGLISH);
    public static final FastDateFormat MIN_SECS = FastDateFormat.getInstance("mm:ss", SERVER_TIME_ZONE, Locale.ENGLISH);
    public static final FastDateFormat KOTH_FORMAT = FastDateFormat.getInstance("m:ss", SERVER_TIME_ZONE,
            Locale.ENGLISH);
    public static final FastDateFormat PALACE_FORMAT = FastDateFormat.getInstance("m:ss", SERVER_TIME_ZONE,
            Locale.ENGLISH);
    public static final ThreadLocal<DecimalFormat> SECONDS = ThreadLocal.withInitial(() -> new DecimalFormat("0"));
    public static final ThreadLocal<DecimalFormat> REMAINING_SECONDS_TRAILING = ThreadLocal
            .withInitial(() -> new DecimalFormat("0.0"));
    public static final ThreadLocal<DecimalFormat> REMAINING_SECONDS = new ThreadLocal<DecimalFormat>() {
        @Override
        protected DecimalFormat initialValue() {
            return new DecimalFormat("0.#");
        }
    };

    public static String getRemaining(long millis, boolean milliseconds) {
        return getRemaining(millis, milliseconds, true);
    }

    public static String getRemaining(long duration, boolean milliseconds, boolean trail) {
        if (milliseconds && duration < TimeUnit.MINUTES.toMillis(1)) {
            return (trail ? REMAINING_SECONDS_TRAILING : REMAINING_SECONDS).get().format(duration * 0.001).replace(',',
                    '.') + 's';
        }
        return DurationFormatUtils.formatDuration(duration,
                ((duration >= TimeUnit.HOURS.toMillis(1)) ? "HH:" : "") + "mm:ss");
    }

    public static String getCollonRemaining(long duration, boolean milliseconds) {
        return DurationFormatUtils.formatDuration(duration,
                ((duration >= TimeUnit.HOURS.toMillis(1)) ? "HH:" : "") + "mm:ss");
    }

    public static String getCleanRemaining(long duration, boolean milliseconds, boolean trail) {
        if (milliseconds && duration < TimeUnit.MINUTES.toMillis(1)) {
            return (trail ? REMAINING_SECONDS_TRAILING : REMAINING_SECONDS).get().format(duration * 0.001).replace(',',
                    '.');
        }
        return DurationFormatUtils.formatDuration(duration,
                ((duration >= TimeUnit.HOURS.toMillis(1)) ? "HH:" : "") + "mm:ss");
    }

    public static String convertShort(long millis) {
        long days = TimeUnit.MILLISECONDS.toDays(millis);
        millis -= TimeUnit.DAYS.toMillis(days);
        long hours = TimeUnit.MILLISECONDS.toHours(millis);
        millis -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        millis -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);
        StringBuilder sb = new StringBuilder(64);
        if (days != 0L) {
            sb.append(days + "d");
        }
        if (hours != 0L) {
            sb.append(" " + hours + "h");
        }
        if (minutes != 0L) {
            sb.append(" " + minutes + "m");
        }
        if (seconds != 0L) {
            sb.append(" " + seconds + "s");
        }
        return sb.toString();
    }

    public static long getTimeByArg(String arg) {
        long time = 0;
        if (arg.equalsIgnoreCase("perm")) {
            return Long.MAX_VALUE;
        }
        if (StringUtils.containsIgnoreCase(arg, "s")) {
            int num = JavaUtil.tryParseInt(arg.replace("s", ""));
            time += num;
        }
        if (StringUtils.containsIgnoreCase(arg, "m")) {
            int num = JavaUtil.tryParseInt(arg.replace("m", ""));
            time += num * 60;
        }
        if (StringUtils.containsIgnoreCase(arg, "h")) {
            int num = JavaUtil.tryParseInt(arg.replace("h", ""));
            time += num * 60 * 60;
        }
        if (StringUtils.containsIgnoreCase(arg, "d")) {
            int num = JavaUtil.tryParseInt(arg.replace("d", ""));
            time += num * 60 * 60 * 24;
        }
        return time * 1000;
    }

    public static String convertShortWithoutSpace(long millis) {
        long days = TimeUnit.MILLISECONDS.toDays(millis);
        millis -= TimeUnit.DAYS.toMillis(days);
        long hours = TimeUnit.MILLISECONDS.toHours(millis);
        millis -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        millis -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);
        StringBuilder sb = new StringBuilder(64);
        if (days != 0L) {
            sb.append(days + "d");
        }
        if (hours != 0L) {
            sb.append(hours + "h");
        }
        if (minutes != 0L) {
            sb.append(minutes + "m");
        }
        if (seconds != 0L) {
            sb.append(seconds + "s");
        }
        return sb.toString();
    }

}
