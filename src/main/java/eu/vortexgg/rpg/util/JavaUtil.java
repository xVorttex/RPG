package eu.vortexgg.rpg.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;

import com.eatthepath.uuid.FastUUID;
import com.google.common.base.CharMatcher;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

public class JavaUtil {
    public static final CharMatcher CHAR_MATCHER_ASCII = CharMatcher.inRange('0', '9').or(CharMatcher.inRange('a', 'z'))
	    .or(CharMatcher.inRange('A', 'Z')).or(CharMatcher.WHITESPACE).precomputed();
    public static final Pattern UUID_PATTERN = Pattern
	    .compile("[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[34][0-9a-fA-F]{3}-[89ab][0-9a-fA-F]{3}-[0-9a-fA-F]{12}");

    public static boolean isUUID(String string) {
	return UUID_PATTERN.matcher(string).find();
    }

    public static boolean isAlphanumeric(String string) {
	return CHAR_MATCHER_ASCII.matchesAllOf((CharSequence) string);
    }

    public static boolean containsIgnoreCase(Iterable<? extends String> elements, String string) {
	for (String element : elements) {
	    if (!StringUtils.containsIgnoreCase(element, string)) {
		continue;
	    }
	    return true;
	}
	return false;
    }
    
    public static List<String> uuidsToStrings(Collection<UUID> set) {
	return set.stream().map(uuid -> FastUUID.toString(uuid)).collect(Collectors.toList());
    }
    
    public static String listToString(Collection<String> set) {
	StringBuilder builder = new StringBuilder();
	set.forEach(str -> builder.append(str + " "));
	return builder.toString();
    }

    public static String format(Number number) {
	return format(number, 5);
    }

    public static String toDecimal(double number) {
	return TimeUtil.REMAINING_SECONDS_TRAILING.get().format(number).replace(',', '.');
    }

    public static HashMap<String, Double> calculatePercents(HashMap<String, Integer> attackers, int totalDamage) {
	HashMap<String, Double> damagePercents = Maps.newHashMap();
	double totalDamagePercents = totalDamage / 100;
	for (String key : attackers.keySet()) {
	    damagePercents.put(key, attackers.get(key) / totalDamagePercents);
	}
	return damagePercents;
    }

    public static boolean isInteger(String value) {
	try {
	    Integer.parseInt(value);
	} catch (NumberFormatException e) {
	    return false;
	}
	return true;
    }

    public static String format(Number number, int decimalPlaces) {
	return format(number, decimalPlaces, RoundingMode.HALF_DOWN);
    }

    public static String format(Number number, int decimalPlaces, RoundingMode roundingMode) {
	Preconditions.checkNotNull(number, "The number cannot be null");
	return new BigDecimal(number.toString()).setScale(decimalPlaces, roundingMode).stripTrailingZeros()
		.toPlainString();
    }

    public static String andJoin(Collection<String> collection, boolean delimiterBeforeAnd) {
	return andJoin(collection, delimiterBeforeAnd, ", ");
    }

    public static String andJoin(Collection<String> collection, boolean delimiterBeforeAnd, String delimiter) {
	if (collection == null || collection.isEmpty())
	    return "";
	ArrayList<String> contents = new ArrayList<String>(collection);
	String last = contents.remove(contents.size() - 1);
	StringBuilder builder = new StringBuilder(Joiner.on(delimiter).join(contents));
	if (delimiterBeforeAnd) {
	    builder.append(delimiter);
	}
	return builder.append(" and ").append(last).toString();
    }

    public static Integer tryParseInt(String string) {
	try {
	    return Integer.parseInt(string);
	} catch (NumberFormatException ex) {
	    return 0;
	}
    }

    public static Double tryParseDouble(String string) {
	try {
	    return Double.parseDouble(string);
	} catch (NumberFormatException ex) {
	    return 0.0;
	}
    }
}
