package Reminder;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;


import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class MainClass {

    private ArrayList<Record> events = new ArrayList<>();
    private final int windowWidth = 640;
    private final int windowHeight = 480;
    private final String windowName = "Reminder";
    private final int offsetX = 20;
    private final int offsetY = 20;
    private final int eventBoxWidth = 500;
    private final int buttonSize = 50;
    private final String eventFilename = "data";

    private JFrame constructMainWindow(int width, int height, String title) {
        JFrame window = new JFrame();
        window.setSize(width, height);
        window.setTitle(title);
        window.setLayout(null);
        window.setResizable(false);
        window.setLocationRelativeTo(null);
        window.setDefaultCloseOperation(EXIT_ON_CLOSE);
        window.addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                try (FileWriter file = new FileWriter(eventFilename)) {
                    for (Record record : events)
                        file.write(record.toString() + '\n');
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
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
    private JButton constructImageButton(Icon icon, int x, int y, int width, int height, ActionListener action) {
        JButton button = new JButton(icon);
        button.setBounds(x, y, width, height);
        button.addActionListener(action);
        return button;
    }
    private JPanel constructPanel(int x, int y, int width, int height) {
        JPanel panel = new JPanel();
        panel.setBounds(x, y, width, height);
        return panel;
    }
    private JScrollPane constructJScrollPane(int x, int y, int width, int height) {
        String[] columns = {"Date", "Frequency", "Description"};

        DefaultTableModel tableModel = new DefaultTableModel(0, 3) {
            @Override
            public String getColumnName(int index) { return columns[index]; }
        };
        tableModel.setNumRows(events.size());
        for (int i = 0; i < events.size(); i++) {
            tableModel.setValueAt(events.get(i).getDate(), i, 0);
            tableModel.setValueAt(events.get(i).getRepeat(), i, 1);
            tableModel.setValueAt(events.get(i).getDescription(), i, 2);
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
    private void run() {
        readData();
        JFrame mainWindow = constructMainWindow(windowWidth, windowHeight, windowName);

        JScrollPane eventBox = constructJScrollPane(offsetX, offsetY, eventBoxWidth, windowHeight - 3 * offsetY);
        mainWindow.add(eventBox);

        ImageIcon plusIcon = new ImageIcon("./Icons/SmallPlus.png");
        ImageIcon minusIcon = new ImageIcon("./Icons/SmallMinus.png");

        JPanel buttonPanel = constructPanel(eventBoxWidth + 2 * offsetX, offsetY,
                windowWidth - 3 * offsetX - eventBoxWidth, windowHeight - 3 * offsetY);
        JButton addButton = constructImageButton(plusIcon, 0, 0, buttonSize, buttonSize, e -> {
            events.add(new Record("31.01.2000", "EveryFortnight", "My Birthday"));
            events.sort(new Record.SortByDate());
            JViewport viewport = eventBox.getViewport();
            DefaultTableModel table = ((DefaultTableModel) ((JTable) viewport.getView()).getModel());
            table.setNumRows(events.size());
            for (int i = 0; i < events.size(); i++) {
                table.setValueAt(events.get(i).getDate(), i, 0);
                table.setValueAt(events.get(i).getRepeat(), i, 1);
                table.setValueAt(events.get(i).getDescription(), i, 2);
            }
        });
        JButton removeButton = constructImageButton(minusIcon, 0, buttonSize + offsetY,
                buttonSize, buttonSize, e -> {
                    JViewport viewport = eventBox.getViewport();
                    JTable table = (JTable)viewport.getView();
                    int[] selectedRows = table.getSelectedRows();
                    for (int i = selectedRows.length - 1; i > -1; i--) {
                        ((DefaultTableModel) table.getModel()).removeRow(selectedRows[i]);
                        events.remove(selectedRows[i]);
                    }
                });
        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        mainWindow.add(buttonPanel);

        mainWindow.setVisible(true);
    }
    public static void main(String[] args) {
        MainClass mc = new MainClass();
        mc.run();
    }
}
