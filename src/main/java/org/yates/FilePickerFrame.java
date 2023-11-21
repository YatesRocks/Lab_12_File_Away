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
    private final JButton openButton = new JButton("Open File");
    private final JTextArea jTextArea = new JTextArea();
    private final JFileChooser jFileChooser = new JFileChooser();

    private static final int TEXT_COLUMNS = 40;
    private static final int TEXT_ROWS = 20;
    private static final int WINDOW_WIDTH = 500;
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

        openButton.addActionListener(e -> handleOpenButtonAction());

        getContentPane().add(jScrollPane);
        getContentPane().add(openButton);
    }

    private void handleOpenButtonAction() {
        int returnVal = jFileChooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = jFileChooser.getSelectedFile();
            logger.log(Level.INFO, "Opening file: {0}", file.getAbsolutePath());
            try {
                String fileContent = readFileContents(file);

                jTextArea.setText(fileContent);
                revalidate();
                repaint();
            } catch (IOException e) {
                handleIOException(e);
            }
        }
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
