package Reminder;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Date;

public class Record {
    enum Frequency {
        Once, EveryDay, EveryWeek, EveryFortnight, EveryMonth, EveryYear
    }
    private LocalDate date;
    LocalDate getDate() { return date; }
    String getStringDate() {
        return String.format("%02d.%02d.%04d", date.getDayOfMonth(),
                date.getMonth().getValue(),date.getYear());
    }
    private String description;
    String getDescription() { return description; }
    private Frequency frequency;
    Frequency getfrequency() { return frequency; }

    Record(String date, String frequency, String description) {
        String[] buff = date.split("\\.");
        int day = Integer.parseInt(buff[0]);
        int month = Integer.parseInt(buff[1]);
        int year = Integer.parseInt(buff[2]);
        this.date = LocalDate.of(year, month, day);
        this.description = description;
        this.frequency = Frequency.valueOf(frequency);
    }

    @Override
    public String toString() {
        return String.format("%s,%s,%s", getStringDate(), frequency, description);
    }

    public static class SortByDate implements Comparator<Record> {
        public int compare(Record a, Record b) {
            return a.date.compareTo(b.date);
        }
    }
}
