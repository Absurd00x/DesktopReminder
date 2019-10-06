package Reminder;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Date;

public class Record {
    enum Frequency {
        Once, EveryDay, EveryFortnight, EveryMonth, EveryYear
    }
    private LocalDate date;
    public LocalDate getDate() { return date; }
    private String description;
    public String getDescription() { return description; }
    private Frequency repeat;
    public Frequency getRepeat() { return repeat; }
    private final SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");

    public Record(LocalDate date, Frequency repeat, String description) {
        this.date = date;
        this.description = description;
        this.repeat = repeat;
    }
    public Record(LocalDate date, String repeat, String description) {
        this.date = date;
        this.description = description;
        this.repeat = Frequency.valueOf(repeat);
    }

    @Override
    public String toString() {
        return String.format("%s | %14s | %s", format.format(date), repeat, description);
    }

    public static class SortByDate implements Comparator<Record> {
        public int compare(Record a, Record b) {
            return a.date.compareTo(b.date);
        }
    }
}
