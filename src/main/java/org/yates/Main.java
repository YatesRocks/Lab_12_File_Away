package org.yates;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        JFrame jFrame = new JFrame("File Picker Example");
        JButton openButton = new JButton("Open File");
        JTextArea jTextArea = new JTextArea();
        jTextArea.setColumns(40);
        jTextArea.setRows(20);
        JScrollPane jScrollPane = new JScrollPane(jTextArea);

        JFileChooser jFileChooser = new JFileChooser();

        openButton.addActionListener(createOpenButtonActionListener(jFrame, jFileChooser, jTextArea));

        jFrame.setLayout(new FlowLayout());
        jFrame.getContentPane().add(jScrollPane);
        jFrame.getContentPane().add(openButton);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setSize(500,600);
        jFrame.setVisible(true);
    }

    private static ActionListener createOpenButtonActionListener(JFrame frame, JFileChooser fileChooser, JTextArea jTextArea) {
        return e -> handleOpenButtonAction(frame, fileChooser, jTextArea);
    }

    private static void handleOpenButtonAction(JFrame frame, JFileChooser fileChooser, JTextArea jTextArea) {
        int returnVal = fileChooser.showOpenDialog(frame);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            java.io.File file = fileChooser.getSelectedFile();
            System.out.println("Opening: " + file.getName());

            // Read the contents of the file and display in the JTextArea
            try {
                String fileContent = readFileContents(file);
                displayFileContent(jTextArea, fileContent);

                frame.revalidate();
                frame.repaint();
            } catch (IOException ex) {
                handleIOException(ex);
            }
        } else {
            System.out.println("Open command cancelled by user.");
        }
    }

    private static String readFileContents(java.io.File file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        StringBuilder content = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            content.append(line).append("\n");
        }
        reader.close();
        return content.toString();
    }

    private static void displayFileContent(JTextArea jTextArea, String content) {
        jTextArea.setText(content);
    }

    private static void handleIOException(IOException ex) {
        ex.printStackTrace();
    }
}