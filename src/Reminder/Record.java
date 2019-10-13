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
    private String note;
    String getNote() { return note; }
    private Frequency frequency;
    Frequency getFrequency() { return frequency; }

    Record(String date, String frequency, String note) {
        String[] buff = date.split("\\.");
        int day = Integer.parseInt(buff[0]);
        int month = Integer.parseInt(buff[1]);
        int year = Integer.parseInt(buff[2]);
        this.date = LocalDate.of(year, month, day);
        this.note = note;
        this.frequency = Frequency.valueOf(frequency);
    }

    @Override
    public String toString() {
        return String.format("%s,%s,%s", getStringDate(), frequency, note);
    }

    public static class SortByDate implements Comparator<Record> {
        public int compare(Record a, Record b) {
            return a.date.compareTo(b.date);
        }
    }
}
