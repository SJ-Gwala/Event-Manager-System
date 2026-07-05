
package oopcyllo.eventmanagersystem;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import oopcyllo.sjrmtgnr.Event;
import oopcyllo.eventmanagersystem.OnlineEvent;
import oopcyllo.eventmanagersystem.PhysicalEvent;


public class EventManagerSystem extends JFrame {

    private final JTextField titleField;
    private final JTextField dateField;
    private final JTextField categoryField;
    private final JTextField extraField;
    private final JTextField searchField;
    private final JComboBox<String> eventTypeBox;
    private final JComboBox<String> searchCriteriaBox;
    private JLabel extraLabel;
    private final File eventFile = new File("events.txt");

    public EventManagerSystem() {
        setTitle("Event Management System");
        setSize(700, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel inputPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Add / Edit Event"));

        titleField = new JTextField();
        dateField = new JTextField();
        categoryField = new JTextField();
        extraField = new JTextField();
        eventTypeBox = new JComboBox<>(new String[] { "Online", "Physical" });
        extraLabel = new JLabel("Platform:");

        inputPanel.add(new JLabel("Title:"));
        inputPanel.add(titleField);
        inputPanel.add(new JLabel("Date (YYYY-MM-DD):"));
        inputPanel.add(dateField);
        inputPanel.add(new JLabel("Category:"));
        inputPanel.add(categoryField);
        inputPanel.add(new JLabel("Event Type:"));
        inputPanel.add(eventTypeBox);
        inputPanel.add(extraLabel);
        inputPanel.add(extraField);

        eventTypeBox.addActionListener(e -> {
            if ("Online".equals(eventTypeBox.getSelectedItem())) {
                extraLabel.setText("Platform:");
            } else {
                extraLabel.setText("Location:");
            }
        });

        JPanel searchPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Search by Title, Date, or Category"));

        searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(titleField.getPreferredSize().width, searchField.getPreferredSize().height));
        searchPanel.add(searchField);

        searchCriteriaBox = new JComboBox<>(new String[] { "Title", "Date", "Category" });
        searchPanel.add(searchCriteriaBox);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        JButton addBtn = new JButton("Add");
        JButton editBtn = new JButton("Edit");
        JButton delBtn = new JButton("Delete");
        JButton searchBtn = new JButton("Search");

        buttonPanel.add(addBtn);
        buttonPanel.add(editBtn);
        buttonPanel.add(delBtn);
        buttonPanel.add(searchBtn);

        addBtn.addActionListener(e -> addEvent());
        editBtn.addActionListener(e -> editEvent());
        delBtn.addActionListener(e -> deleteEvent());
        searchBtn.addActionListener(e -> searchEvents());

        setLayout(new BorderLayout(10, 10));
        add(inputPanel, BorderLayout.NORTH);
        add(searchPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private Event createEventFromInput() {
        String title = titleField.getText().trim();
        String date = dateField.getText().trim();
        String category = categoryField.getText().trim();
        String extra = extraField.getText().trim();
        String type = (String) eventTypeBox.getSelectedItem();

        if (title.isEmpty() || date.isEmpty() || category.isEmpty() || extra.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.");
            return null;
        }

        return "Online".equals(type)
                ? new OnlineEvent(title, date, category, extra)
                : new PhysicalEvent(title, date, category, extra);
    }

    private void addEvent() {
        Event event = createEventFromInput();
        if (event == null) return;

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(eventFile, true))) {
            bw.write(event.toFileString());
            bw.newLine();
            JOptionPane.showMessageDialog(this, "Event added!");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error occurred while adding event: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void editEvent() {
        String titleToEdit = titleField.getText().trim();
        if (titleToEdit.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter the event title to edit.");
            return;
        }

        Event newEvent = createEventFromInput();
        if (newEvent == null) return;

        try {
            // Read all events and rewrite the file with edited event
            BufferedReader br = new BufferedReader(new FileReader(eventFile));
            StringBuilder updatedContent = new StringBuilder();
            String line;
            boolean eventFound = false;

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 1 && parts[0].equals(titleToEdit)) {
                    updatedContent.append(newEvent.toFileString()).append("\n");
                    eventFound = true;
                } else {
                    updatedContent.append(line).append("\n");
                }
            }

            if (eventFound) {
                BufferedWriter bw = new BufferedWriter(new FileWriter(eventFile));
                bw.write(updatedContent.toString());
                bw.close();
                JOptionPane.showMessageDialog(this, "Event updated.");
            } else {
                JOptionPane.showMessageDialog(this, "Event not found.");
            }

            br.close();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error occurred while editing event: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void deleteEvent() {
        String titleToDelete = JOptionPane.showInputDialog(this, "Enter the title of the event to delete:");

        if (titleToDelete == null || titleToDelete.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a valid event title.");
            return;
        }

        try {
            // Read all events and rewrite the file without the deleted event
            BufferedReader br = new BufferedReader(new FileReader(eventFile));
            StringBuilder updatedContent = new StringBuilder();
            String line;
            boolean eventDeleted = false;

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 1 && parts[0].equals(titleToDelete)) {
                    eventDeleted = true;
                    continue; // Skip the event to delete
                }
                updatedContent.append(line).append("\n");
            }

            if (eventDeleted) {
                BufferedWriter bw = new BufferedWriter(new FileWriter(eventFile));
                bw.write(updatedContent.toString());
                bw.close();
                JOptionPane.showMessageDialog(this, "Event deleted.");
            } else {
                JOptionPane.showMessageDialog(this, "Event not found.");
            }

            br.close();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error occurred while deleting event: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void searchEvents() {
        String keyword = searchField.getText().trim().toLowerCase();
        String searchType = (String) searchCriteriaBox.getSelectedItem();
        StringBuilder resultBuilder = new StringBuilder();

        if (keyword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter a search keyword.");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(eventFile))) {
            String line;
            boolean found = false;

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    String title = parts[0];
                    String date = parts[1];
                    String category = parts[2];
                    String type = parts[3];
                    String extra = parts[4];

                    boolean match = false;
                    if ("Title".equals(searchType) && title.toLowerCase().contains(keyword)) {
                        match = true;
                    } else if ("Date".equals(searchType) && date.toLowerCase().contains(keyword)) {
                        match = true;
                    } else if ("Category".equals(searchType) && category.toLowerCase().contains(keyword)) {
                        match = true;
                    }

                    if (match) {
                        Event e = "Online".equals(type)
                                ? new OnlineEvent(title, date, category, extra)
                                : new PhysicalEvent(title, date, category, extra);

                        resultBuilder.append(e.toString()).append("\n\n");
                        found = true;
                    }
                }
            }

            if (found) {
                JOptionPane.showMessageDialog(this, resultBuilder.toString(), "Search Results", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "No events found for: " + keyword);
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error occurred during search: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new EventManagerSystem().setVisible(true));
    }

    
}

