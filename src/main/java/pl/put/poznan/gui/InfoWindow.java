package pl.put.poznan.gui;

import javax.swing.*;
import java.awt.*;

/**
 * Class responsible for creating an info window upon clicking the info button in the main application
 */
public class InfoWindow extends JFrame {
    private JTextArea infoTextArea;
    public InfoWindow(String title) {
        super(title);

        this.setResizable(false);
        this.setVisible(true);
        this.setSize(800, 400);
        this.setLayout(null);

        infoTextArea = new JTextArea();
        infoTextArea.setBounds(0, 0, 800, 400);
        infoTextArea.setEditable(false);
        infoTextArea.setFont(new Font("calibri", Font.PLAIN, 22));
        infoTextArea.setText("\n\t        Scenario Quality Checker GUI info\n");
        infoTextArea.append("\n    - Press the \"Choose a file\" button to import .txt or .JSON input");
        infoTextArea.append("\n    Alternatively drag and drop a file to the \"Input\" area");
        infoTextArea.append("\n    - Click on the \"Input\" area to start editing the scenario");
        infoTextArea.append("\n    - Use the selector to choose a function to be executed on the scenario");
        infoTextArea.append("\n    - Press the \"Execute function\" button to execute the chosen function on a scenario");
        infoTextArea.append("\n    - Press the \"Clear input\" button to clear \"Input\" area");
        infoTextArea.append("\n    - Press the \"Clear output\" button to clear \"Output\" area");
        infoTextArea.append("\n    - Press the \"Download\" button to save \"Output\" area result as a .txt or .JSON file");

        this.add(infoTextArea);
    }
}
