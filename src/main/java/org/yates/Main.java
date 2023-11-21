package org.yates;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Invokes the constructor, and after that completes makes the window visible
        SwingUtilities.invokeLater(() -> new FilePickerFrame().setVisible(true));
    }
}