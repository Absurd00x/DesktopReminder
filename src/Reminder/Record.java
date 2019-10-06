package Reminder;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

public class Record {
    enum Frequency {
        Once, EveryDay, EveryFortnight, EveryMonth, EveryYear
    }
    private Date date;
    private String description;
    private Frequency repeat;
    private final SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");

    public Record(Date date, Frequency repeat, String description) {
        this.date = date;

        this.description = description;
        this.repeat = repeat;
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
