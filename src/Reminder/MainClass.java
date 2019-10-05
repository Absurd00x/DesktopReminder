package Reminder;

import javax.swing.*;
import java.awt.event.*;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class MainClass {

    public static JFrame constructMainWindow(int width, int height, String title) {
        JFrame window = new JFrame();
        window.setSize(width, height);
        window.setTitle(title);
        window.setLayout(null);
        window.setResizable(false);
        window.setDefaultCloseOperation(EXIT_ON_CLOSE);
        return window;
    }

    public static JList<Record> constructListbox(int x, int y, int width, int height) {
        JList<Record> listbox = new JList<>();
        listbox.setBounds(20, 20, 500, 420);
        return listbox;
    }

    public static JButton constructButton(String title, Icon icon, Action action) {
        JButton button = new JButton(title, icon);
        button.addActionListener(action);
        return button;
    }

    public static void main(String[] args) {
        JFrame mainWindow = constructMainWindow(640, 480, "Reminder");
        JList<Record> eventsBox = constructListbox(20, 20, 500, 420);
        mainWindow.add(eventsBox);



        mainWindow.setVisible(true);
    }
}
