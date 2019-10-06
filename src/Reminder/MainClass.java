package Reminder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;


import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class MainClass {

    private ArrayList<Record> events;
    private final int windowWidth = 640;
    private final int windowHeight = 480;
    private final String windowName = "Reminder";
    private final int offsetX = 20;
    private final int offsetY = 20;
    private final int eventBoxWidth = 500;
    private final int buttonSize = 50;

    private JFrame constructMainWindow(int width, int height, String title) {
        JFrame window = new JFrame();
        window.setSize(width, height);
        window.setTitle(title);
        window.setLayout(null);
        window.setResizable(false);
        window.setDefaultCloseOperation(EXIT_ON_CLOSE);
        return window;
    }
    private JButton constructImageButton(Icon icon, int x, int y, int width, int height, ActionListener action) {
        JButton button = new JButton(icon);
        button.setBounds(x, y, width, height);
        button.addActionListener(action);
        return button;
    }
    private JPanel constructPanel(int x, int y, int width, int height) {
        JPanel panel = new JPanel() {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(width, height);
            }
        };
        panel.setLayout(new FlowLayout(FlowLayout.CENTER));
        return panel;
    }
    private JScrollPane constructJScrollPane(int x, int y, int width, int height) {
        String[][] data = { {"1970.01.01", "Once", "Unix BirthDay"} , {"1234.56.78", "lol", "kek"}};
        String[] column = {"Date", "Frequency", "Description"};
        JTable table = new JTable(data, column);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(x, y, width, height);
        return scrollPane;
    }
    private void run() {
        JFrame mainWindow = constructMainWindow(windowWidth, windowHeight, windowName);

        JScrollPane eventBox = constructJScrollPane(offsetX, offsetY, eventBoxWidth, windowHeight - 3 * offsetY);
        mainWindow.add(eventBox);

        ImageIcon plusIcon = new ImageIcon("./Icons/SmallPlus.png");
        ImageIcon minusIcon = new ImageIcon("./Icons/SmallMinus.png");

        JPanel buttonPanel = constructPanel(eventBoxWidth + 2 * offsetX, offsetY,
                windowWidth - 3 * offsetX - eventBoxWidth, windowHeight - 3 * offsetY);
        JButton addButton = constructImageButton(plusIcon, 0, 0, buttonSize, buttonSize, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Plus clicked");
            }
        });
        JButton removeButton = constructImageButton(minusIcon, 0, buttonSize + offsetY,
                buttonSize, buttonSize, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Minus clicked");
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
