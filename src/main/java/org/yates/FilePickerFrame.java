package org.yates;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class FilePickerFrame extends JFrame {
    // CONSTANTS
    private static final int TEXT_COLUMNS = 45;
    private static final int TEXT_ROWS = 20;
    private static final int WINDOW_WIDTH = 525;
    private static final int WINDOW_HEIGHT = 600;
    private static final String WINDOW_TITLE = "Lab 12 File Away";

    // UI Components
    private final JTextArea jTextArea = new JTextArea();
    private final JFileChooser jFileChooser = new JFileChooser();
    private final JLabel statisticsLabel = new JLabel();

    private final Logger logger = Logger.getLogger(FilePickerFrame.class.getName());

    public FilePickerFrame() {
        // Main window UI setup
        setTitle(WINDOW_TITLE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setLayout(new FlowLayout());

        configureJTextArea();
        configureFileChooser();

        // Make it scrollable for text that doesn't fit
        JScrollPane jScrollPane = new JScrollPane(jTextArea);

        JButton openButton = new JButton("Open File");
        openButton.addActionListener(e -> handleOpenButtonAction());

        getContentPane().add(jScrollPane);
        getContentPane().add(openButton);
        getContentPane().add(statisticsLabel);
    }

    private void configureFileChooser() {
        // Default to only show .txt files
        FileNameExtensionFilter nameExtensionFilter = new FileNameExtensionFilter("Text Files", "txt");
        jFileChooser.setFileFilter(nameExtensionFilter);
    }

    private void configureJTextArea() {
        jTextArea.setColumns(TEXT_COLUMNS);
        jTextArea.setRows(TEXT_ROWS);
        // If I turned this to "True" and added a save button I'd have Notepad.exe :)
        jTextArea.setEditable(false);
    }

    private void handleOpenButtonAction() {
        int returnVal = jFileChooser.showOpenDialog(this);
        // True when user clicks "open" on the popup
        // False when cancel is clicked or window is closed
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = jFileChooser.getSelectedFile();
            logger.log(Level.INFO, "Opening file: {0}", file.getAbsolutePath());
            try {
                String fileContent = readFileContents(file);

                jTextArea.setText(fileContent);

                displayFileStatistics(fileContent);

                // Updates UI to include file content & statistics label
                revalidate();
                repaint();
            } catch (IOException e) {
                handleIOException(e);
            }
        }
    }

    private String readFileContents(java.io.File file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String content;
        // Read: For every line in reader add a newline. Then, collect all lines into
        // one long string.
        content = reader.lines().map(line -> line + "\n").collect(Collectors.joining());
        reader.close(); // ALWAYS CLOSE YOUR IO!!!
        return content;
    }

    // don't even know what would trigger this tbh
    private void handleIOException(IOException ex) {
        logger.log(Level.SEVERE, "Error while reading file.", ex);
    }

    private void displayFileStatistics(String fileContent) {
        int wordCount = getWordCount(fileContent);
        // Removes all newline characters, but spaces are included in char count
        int charCount = fileContent.replaceAll("\\r\\n|\\r|\\n", "").length();
        // -1 because a newline is appended to the text stream
        int lineCount = jTextArea.getLineCount() - 1;

        // Display statistics in the JLabel
        String statisticsText = String.format(
                "File Name: %s, Line Count: %d, Word Count: %d, Character Count: %d",
                jFileChooser.getSelectedFile().getName(),
                lineCount,
                wordCount,
                charCount
        );

        statisticsLabel.setText(statisticsText);
    }

    private int getWordCount(String content) {
        // Splits on whitespace
        String[] words = content.split("\\s+");
        return words.length;
    }
}
