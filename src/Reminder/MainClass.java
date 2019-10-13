package Reminder;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;

import static java.time.temporal.ChronoUnit.DAYS;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class MainClass {

    private ArrayList<Record> events = new ArrayList<>();
    private final int windowWidth = 640;
    private final int windowHeight = 480;
    private final int offsetX = 20;
    private final int offsetY = 20;
    private final int eventBoxWidth = 500;
    private final int buttonSize = 60;
    private final String eventFilename = "data.csv";
    private final int popupWidth = 450;
    private final int popupHeight = 200;


    private JFrame constructWindow(int width, int height, String title) {
        JFrame window = new JFrame();
        window.setSize(width, height);
        window.setTitle(title);
        window.setLayout(null);
        window.setResizable(false);
        window.setLocationRelativeTo(null);
        return window;
    }
    private void readData() {
        try {
            Files.createFile(Path.of(eventFilename));
        } catch (FileAlreadyExistsException ignored) {
            try (BufferedReader file = new BufferedReader(new FileReader(new File(eventFilename)))) {
                String line;
                while ((line=file.readLine())!=null) {
                    String[] buff = line.split(",");
                    events.add(new Record(buff[0], buff[1], buff[2]));
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    private JPanel constructPanel(int x, int y, int width, int height) {
        JPanel panel = new JPanel();
        panel.setBounds(x, y, width, height);
        return panel;
    }
    private JScrollPane constructJScrollPane(int x, int y, int width, int height) {
        String[] columns = {"Date", "Frequency", "Note"};

        DefaultTableModel tableModel = new DefaultTableModel(0, 3) {
            @Override
            public String getColumnName(int index) { return columns[index]; }
        };
        tableModel.setNumRows(events.size());
        for (int i = 0; i < events.size(); i++) {
            tableModel.setValueAt(events.get(i).getStringDate(), i, 0);
            tableModel.setValueAt(events.get(i).getFrequency(), i, 1);
            tableModel.setValueAt(events.get(i).getNote(), i, 2);
        }
        JTable table = new JTable(tableModel);
        table.getColumnModel().getColumn(0).setMaxWidth(100);
        table.getColumnModel().getColumn(1).setMinWidth(100);
        table.getColumnModel().getColumn(1).setMaxWidth(100);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        table.setCellSelectionEnabled(true);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(x, y, width, height);
        return scrollPane;
    }
    private void changeDays(JComboBox<Integer> days, JComboBox<String> months, JSpinner years) {
        YearMonth buff = YearMonth.of((Integer) years.getValue(), months.getSelectedIndex() + 1);
        int daysInMonth = buff.lengthOfMonth();
        while (days.getItemCount() > daysInMonth) {
            if (days.getSelectedIndex() == days.getItemCount() - 1)
                days.setSelectedIndex(days.getSelectedIndex() - 1);
            days.removeItemAt(days.getItemCount() - 1);
        }
        while (days.getItemCount() < daysInMonth)
            days.addItem(days.getItemCount() + 1);
    }
    private void callPopupWindow(JFrame mainWindow, JScrollPane eventBox) {
        JDialog popup = new JDialog(mainWindow, "Add new event", true);
        popup.setSize(popupWidth, popupHeight);

        JLabel dateLabel = new JLabel("Date:");
        JLabel frequencyLabel = new JLabel("Frequency:");
        JLabel noteLabel = new JLabel("Note:");

        popup.add(dateLabel);
        popup.add(frequencyLabel);
        popup.add(noteLabel);

        String[] frequencies = {"Once", "Every day", "Every week", "Every fortnight", "Every month", "Every year"};
        JComboBox<String> selectFrequency = new JComboBox<>(frequencies);
        SpinnerModel spinnermModel = new SpinnerNumberModel(2000, 0, 3000, 1);
        JSpinner selectYear = new JSpinner(spinnermModel);
        String[] months = {"January", "February", "March", "April", "May", "June", "July",
                "August", "September", "October", "November", "December"};
        JComboBox<String> selectMonth = new JComboBox<>(months);
        Integer[] days = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20,
        21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31};
        JComboBox<Integer> selectDay = new JComboBox<>(days);
        selectMonth.addActionListener(e -> changeDays(selectDay, selectMonth, selectYear));
        selectYear.addChangeListener(e -> changeDays(selectDay, selectMonth, selectYear));
        JTextField noteText = new JTextField("Add note");

        JButton okButton = new JButton("OK");
        okButton.addActionListener( e -> {
            String date = String.format(
                    "%s.%s.%s",
                    selectDay.getSelectedItem(),
                    selectMonth.getSelectedIndex() + 1,
                    selectYear.getValue());
            String frequency = Record.Frequency.values()[selectFrequency.getSelectedIndex()].toString();
            String note = noteText.getText();
            events.add(new Record(date, frequency, note));
            events.sort(new Record.SortByDate());
            JViewport viewport = eventBox.getViewport();
            DefaultTableModel table = ((DefaultTableModel) ((JTable) viewport.getView()).getModel());
            table.setNumRows(events.size());
            for (int i = 0; i < events.size(); i++) {
                table.setValueAt(events.get(i).getStringDate(), i, 0);
                table.setValueAt(events.get(i).getFrequency(), i, 1);
                table.setValueAt(events.get(i).getNote(), i, 2);
            }
            popup.dispose();
        });
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener( e -> popup.dispose());
        popup.add(okButton);
        popup.add(cancelButton);

        GroupLayout layout = new GroupLayout(popup.getContentPane());
        popup.getContentPane().setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                    .addComponent(dateLabel)
                    .addComponent(frequencyLabel)
                    .addComponent(noteLabel))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                    .addComponent(noteText)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup()
                            .addComponent(selectDay)
                            .addComponent(selectFrequency)
                            .addComponent(okButton))
                        .addGroup(layout.createParallelGroup()
                            .addComponent(selectMonth)
                            .addComponent(cancelButton))
                    .addComponent(selectYear)))
        );
        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                            .addComponent(dateLabel)
                            .addComponent(selectDay)
                            .addComponent(selectMonth)
                            .addComponent(selectYear))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                            .addComponent(frequencyLabel)
                            .addComponent(selectFrequency))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                            .addComponent(noteLabel)
                            .addComponent(noteText))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                            .addComponent(okButton)
                            .addComponent(cancelButton))
        );
        popup.setLocationRelativeTo(null);
        popup.setVisible(true);
    }
    private void saveFile() {
        try (FileWriter file = new FileWriter(eventFilename)) {
            for (Record record : events)
                file.write(record.toString() + '\n');
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    private void run() {
        readData();
        JFrame mainWindow = constructWindow(windowWidth, windowHeight, "Reminder");
        mainWindow.setDefaultCloseOperation(EXIT_ON_CLOSE);
        mainWindow.addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                saveFile();
            }
        });

        JScrollPane eventBox = constructJScrollPane(offsetX, offsetY, eventBoxWidth, windowHeight - 3 * offsetY);
        mainWindow.add(eventBox);

        JButton removeButton = new JButton("Remove");

        JButton addButton = new JButton("Add");
        addButton.addActionListener(e -> callPopupWindow(mainWindow, eventBox));
        addButton.setMinimumSize(removeButton.getPreferredSize());
        addButton.setMaximumSize(removeButton.getPreferredSize());


        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(e -> {
            saveFile();
            mainWindow.dispose();
        });
        exitButton.setMinimumSize(removeButton.getPreferredSize());
        exitButton.setMaximumSize(removeButton.getPreferredSize());

        removeButton.addActionListener(e -> {
            JViewport viewport = eventBox.getViewport();
            JTable table = (JTable)viewport.getView();
            int[] selectedRows = table.getSelectedRows();
            for (int i = selectedRows.length - 1; i > -1; i--) {
                ((DefaultTableModel) table.getModel()).removeRow(selectedRows[i]);
                events.remove(selectedRows[i]);
            }
        });

        JPanel buttonPanel = constructPanel(eventBoxWidth + offsetX + offsetX / 2, offsetY,
                windowWidth - 2 * offsetX - eventBoxWidth, windowHeight - 3 * offsetY);

        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.PAGE_AXIS));
        buttonPanel.add(addButton);
        buttonPanel.add(Box.createVerticalStrut(offsetY));
        buttonPanel.add(removeButton);
        buttonPanel.add(Box.createVerticalStrut(16 * offsetY));
        buttonPanel.add(exitButton);
        mainWindow.add(buttonPanel);

        mainWindow.setVisible(true);
    }
    private void checkDates() {
        readData();
        LocalDate currentDate = LocalDate.now();
        ArrayList<Record> datesToDisplay = new ArrayList<>();
        for (Record event : events) {
            switch (event.getFrequency()) {
                case Once:
                    if (event.getDate().equals(currentDate))
                        datesToDisplay.add(event);
                    break;
                case EveryDay:
                    datesToDisplay.add(event);
                    break;
                case EveryWeek:
                    if (Math.abs(DAYS.between(currentDate, event.getDate())) % 7 == 0)
                        datesToDisplay.add(event);
                    break;
                case EveryFortnight:
                    if (Math.abs(DAYS.between(currentDate, event.getDate())) % 14 == 0)
                        datesToDisplay.add(event);
                    break;
                case EveryMonth:
                    int daysInCurrentMonth = YearMonth.now().lengthOfMonth();
                    if (daysInCurrentMonth < event.getDate().getDayOfMonth()
                    && currentDate.getDayOfMonth() == daysInCurrentMonth)
                        datesToDisplay.add(event);
                    else if (currentDate.getDayOfMonth() == event.getDate().getDayOfMonth())
                        datesToDisplay.add(event);
                    break;
                case EveryYear:
                    if (currentDate.getMonth() == event.getDate().getMonth()
                    && currentDate.getDayOfMonth() == event.getDate().getDayOfMonth())
                        datesToDisplay.add(event);
                    break;
            }
        }
        if (!datesToDisplay.isEmpty()) {
            JFrame mainWindow = constructWindow(windowWidth / 2, windowHeight / 2, "Notes today");
            mainWindow.setDefaultCloseOperation(EXIT_ON_CLOSE);
            mainWindow.getContentPane().setBackground(Color.LIGHT_GRAY);

            String[] notes = new String[datesToDisplay.size()];
            for (int i = 0; i < notes.length; i++)
                notes[i] = datesToDisplay.get(i).getNote();
            JList<String> notesList = new JList<>(notes);
            notesList.setBorder(new EmptyBorder(5, 5, 5, 5));
            notesList.setBounds(offsetX, offsetY, windowWidth / 2 - offsetX * 2,
                    windowHeight / 2 - offsetY * 5);
            JButton okButton = new JButton("Ok");
            okButton.setBounds((windowWidth / 2 - buttonSize) / 2, windowHeight / 2 - 3 * offsetY,
                    buttonSize, offsetY);
            okButton.addActionListener(e -> mainWindow.dispose());

            mainWindow.add(notesList);
            mainWindow.add(okButton);

            mainWindow.setVisible(true);
        }
    }
    public static void main(String[] args) {
        MainClass mc = new MainClass();
        boolean noEdit = false;
        for (String arg : args)
            if (arg.equals("--noedit")) {
                noEdit = true;
                break;
            }
        if(!noEdit)
            mc.run();
        else
            mc.checkDates();
    }
}
