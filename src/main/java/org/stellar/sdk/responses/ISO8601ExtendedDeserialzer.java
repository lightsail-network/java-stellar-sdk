package org.stellar.sdk.responses;

import org.threeten.bp.LocalDate;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ISO8601ExtendedDeserialzer implements FormattedDateStringDeserializer {

    @Override
    public long unixEpochSeconds(String is8601DateString, long defaultEpoch) {

        Pattern pattern = Pattern.compile(
                "^(?<epochsign>[-+]?)" +
                        "(?<year>\\d{4,})" +
                        "-(?<month>\\d{2})" +
                        "-(?<day>\\d{2})T" +
                        "(?<hour>2[0-3]|[01][0-9]):" +
                        "(?<minute>[0-5][0-9]):" +
                        "(?<second>[0-5][0-9])" +
                        ".*",
                Pattern.CASE_INSENSITIVE);

        Matcher matcher = pattern.matcher(is8601DateString);
        if (!matcher.find()) {
            return defaultEpoch;
        }
        int epochNegative = matcher.group(1) != null && matcher.group(1).equals("-") ? -1 : 1;
        long gregorianYear = epochNegative * Long.parseLong(matcher.group(2));
        int leapDaysEndYear = getLeapDaysModulo(gregorianYear, Integer.parseInt(matcher.group(3)), Integer.parseInt(matcher.group(4)));

        return totalLeapSeconds(Integer.parseInt(matcher.group(7)),
                Integer.parseInt(matcher.group(6)),
                Integer.parseInt(matcher.group(5)),
                leapDaysEndYear, gregorianYear);

    }

    private long totalLeapSeconds(int seconds, int minutes, int hours, int daysInYear, long gregorianYear) {
        // condensed unix epoch seconds including leap year calcs,
        long allSeconds = (seconds + (minutes*60L) +
                (hours*3600L)) +
                (daysInYear*86400L) +
                ((gregorianYear-1970)*31536000);

        if (gregorianYear > 1969) {
            return allSeconds + ((gregorianYear - 1969) / 4) * 86400
                    - ((gregorianYear - 1901) / 100) * 86400
                    + ((gregorianYear - 1601) / 400) * 86400;
        } else {
            return allSeconds + ((gregorianYear - 1971) / 4) * 86400
                    - ((gregorianYear - 1999) / 100) * 86400
                    + ((gregorianYear - 1999) / 400) * 86400;
        }
    }

    private int getLeapDaysModulo(long gregorianYear, int month, int day) {
        // this applies extra leap year day if current gregorianYear is leap
        // and offset days of year has crossed over the 2/29 date
        int yearDays = LocalDate.of(2006, month, day).getDayOfYear() - 1;

        if (gregorianYear > 1969 && isLeapYear(gregorianYear) && yearDays > 59) {
            yearDays+=1;
        }
        if (gregorianYear < 1970 && isLeapYear(gregorianYear) && yearDays < 60) {
            yearDays+=1;
        }

        return yearDays;
    }

    private boolean isLeapYear(long gregorianYear) {
        return gregorianYear % 400 == 0 || ((gregorianYear % 4) == 0 && (gregorianYear % 100) > 0);
    }
}
