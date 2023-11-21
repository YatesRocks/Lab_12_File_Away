package org.yates;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Logger;
import java.util.logging.Level;

public class FilePickerFrame extends JFrame {
    private final JTextArea jTextArea = new JTextArea();
    private final JFileChooser jFileChooser = new JFileChooser();
    private final JLabel statistics = new JLabel();

    private static final int TEXT_COLUMNS = 45;
    private static final int TEXT_ROWS = 20;
    private static final int WINDOW_WIDTH = 525;
    private static final int WINDOW_HEIGHT = 600;
    private final Logger logger = Logger.getLogger(FilePickerFrame.class.getName());

    public FilePickerFrame() {
        setTitle("Lab 12 File Away");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setLayout(new FlowLayout());

        configureJTextArea();
        configureFileChooser();

        JScrollPane jScrollPane = new JScrollPane(jTextArea);

        JButton openButton = new JButton("Open File");
        openButton.addActionListener(e -> handleOpenButtonAction());

        getContentPane().add(jScrollPane);
        getContentPane().add(openButton);
        getContentPane().add(statistics);
    }

    private void handleOpenButtonAction() {
        int returnVal = jFileChooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = jFileChooser.getSelectedFile();
            logger.log(Level.INFO, "Opening file: {0}", file.getAbsolutePath());
            try {
                String fileContent = readFileContents(file);

                jTextArea.setText(fileContent);

                displayFileStatistics(fileContent);

                revalidate();
                repaint();
            } catch (IOException e) {
                handleIOException(e);
            }
        }
    }

    private void displayFileStatistics(String fileContent) {
        int wordCount = getWordCount(fileContent);
        int charCount = fileContent.length();
        int lineCount = jTextArea.getLineCount();

        // Display statistics in the JLabel
        String statisticsText = String.format(
                "File Name: %s, Line Count: %d, Word Count: %d, Character Count: %d",
                jFileChooser.getSelectedFile().getName(),
                lineCount,
                wordCount,
                charCount
        );
        statistics.setText(statisticsText);
    }

    private int getWordCount(String content) {
        String[] words = content.split("\\s+");
        return words.length;
    }

    private String readFileContents(java.io.File file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        StringBuilder content = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            content.append(line).append("\n");
        }
        reader.close();
        return content.toString();
    }

    private void handleIOException(IOException ex) {
        logger.log(Level.SEVERE, "An IO exception occurred", ex);
    }

    private void configureFileChooser() {
        FileNameExtensionFilter nameExtensionFilter = new FileNameExtensionFilter("Text Files", "txt");
        jFileChooser.setFileFilter(nameExtensionFilter);
    }

    private void configureJTextArea() {
        jTextArea.setColumns(TEXT_COLUMNS);
        jTextArea.setRows(TEXT_ROWS);
        jTextArea.setEditable(false);
    }
}
