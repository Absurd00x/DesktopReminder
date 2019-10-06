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
    public String getDate() {
        return String.format("%02d.%02d.%04d", date.getDayOfMonth(),
                date.getMonth().getValue(),date.getYear());
    }
    private String description;
    public String getDescription() { return description; }
    private Frequency repeat;
    public Frequency getRepeat() { return repeat; }
    private final SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
//
    public Record(String date, String repeat, String description) {
        String[] buff = date.split("\\.");
        int day = Integer.parseInt(buff[0]);
        int month = Integer.parseInt(buff[1]);
        int year = Integer.parseInt(buff[2]);
        this.date = LocalDate.of(year, month, day);
        this.description = description;
        this.repeat = Frequency.valueOf(repeat);
    }

    @Override
    public String toString() {
        return String.format("%s,%s,%s", getDate(), repeat, description);
    }

    public static class SortByDate implements Comparator<Record> {
        public int compare(Record a, Record b) {
            return a.date.compareTo(b.date);
        }
    }
}
