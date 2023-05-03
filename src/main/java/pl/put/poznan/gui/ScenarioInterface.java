package pl.put.poznan.gui;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.json.JSONException;
import org.json.JSONObject;
import pl.put.poznan.checker.logic.*;
import pl.put.poznan.checker.model.Scenario;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.List;

public class ScenarioInterface extends JFrame {
    private JPanel mainPanel;
    private JPanel titlePanel;
    private JLabel titleLabel;
    private JPanel optionsPanel;
    private JComboBox functionBox;
    private JButton clearInputButton;
    private JButton clearOutputButton;
    private JButton chooseFileButton;
    private JButton infoButton;
    private JScrollPane inputScrollPane;
    private JScrollPane outputScrollPane;
    private JTextArea inputTextArea;
    private JTextArea outputTextArea;
    private JButton executeFunctionButton;
    private JPanel outputResizePanel;
    private JButton downloadResultButton;
    private int cuttingLevel;

    public static void main(String[] args){
        JFrame frame = new ScenarioInterface("Scenario Quality Checker");
        frame.setVisible(true);
    }

    public ScenarioInterface(String title){
        super(title);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(mainPanel);
        this.pack();
        this.setResizable(true);
        this.setMinimumSize(new Dimension(700, 700));

        /**
         * Choosing and opening a file
         */
        chooseFileButton.addActionListener(new ActionListener() {
            /**
             * @param e the event to be processed
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("*.JSON", "JSON"));
                fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("*.txt", "txt"));
                int response = fileChooser.showOpenDialog(null);
                if (response == JFileChooser.APPROVE_OPTION){
                    File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
                    try {
                        FileReader reader = new FileReader(file);
                        BufferedReader br = new BufferedReader(reader);
                        inputTextArea.read(br, null);
                        br.close();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });

        /**
         * Drag and drop file
         * might work only on Windows OS
         */
        inputTextArea.setDropTarget(new DropTarget() {
            public synchronized void drop(DropTargetDropEvent evt) {
                try {
                    evt.acceptDrop(DnDConstants.ACTION_COPY);
                    List<File> droppedFiles = (List<File>)
                            evt.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
                    for (File file : droppedFiles) {
                        try {
                            FileReader reader = new FileReader(file);
                            BufferedReader br = new BufferedReader(reader);
                            inputTextArea.read(br, null);
                            br.close();
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        /**
         * Execute chosen function on input
         */
        executeFunctionButton.addActionListener(new ActionListener() {
            /**
             * @param e the event to be processed
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                String chosenFunction = functionBox.getSelectedItem().toString();
                String scenarioContent = inputTextArea.getText();
                Scenario newScenario = null;

                try {
                    JSONObject scenarioObj = new JSONObject(scenarioContent);
                    newScenario = JSONParser.parseScenarioObject(scenarioObj);

                } catch (JSONException e1) {
                    throw new RuntimeException(e1);
                }

                if (chosenFunction.equals("Step counter")){
                    ScenarioCountVisitor visitor = new ScenarioCountVisitor();
                    newScenario.accept(visitor);

                    String output = "{\"Step count\": " + visitor.getStepCount() + "}";
                    outputTextArea.setText(output);
                }
                else if (chosenFunction.equals("Keyword counter")){
                    ScenarioKeyWordCountVisitor visitor = new ScenarioKeyWordCountVisitor();
                    newScenario.accept(visitor);

                    String output = "{\"Keyword count\": " + visitor.getKeyWordCount() + "}";
                    outputTextArea.setText(output);
                }
                else if (chosenFunction.equals("Depth cutting")){
                    cuttingLevel = Integer.parseInt(
                            JOptionPane.showInputDialog(null, "Select cutting level:", 1));

                    ScenarioLevelVisitor visitor = new ScenarioLevelVisitor(cuttingLevel);
                    newScenario.accept(visitor);
                    Scenario result = visitor.getScenario();

                    ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
                    try {
                        String json = ow.writeValueAsString(result);
                        outputTextArea.setText(json);
                    } catch (JsonProcessingException ex) {
                        throw new RuntimeException(ex);
                    }
                }
                else if (chosenFunction.equals("Missing actor steps")){
                    ScenarioMissingActorVisitor visitor = new ScenarioMissingActorVisitor();
                    newScenario.accept(visitor);
                    ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
                    try {
                        String json = ow.writeValueAsString(visitor.getNoActorSteps());
                        outputTextArea.setText(json);
                    } catch (JsonProcessingException ex) {
                        throw new RuntimeException(ex);
                    }
                }
                else if (chosenFunction.equals("JSON to Text")){
                    ScenarioTextDownloadVisitor scenarioTextDownloadVisitor= new ScenarioTextDownloadVisitor();
                    newScenario.accept(scenarioTextDownloadVisitor);
                    String scenarioText = scenarioTextDownloadVisitor.getResult();

                    outputTextArea.setText(scenarioText);
                }
            }
        });

        /**
         * Download result to a file
         */
        downloadResultButton.addActionListener(new ActionListener() {
            /**
             * @param e the event to be processed
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("*.JSON", "JSON"));
                fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("*.txt", "txt"));
                int response = fileChooser.showSaveDialog(null);
                if (response == JFileChooser.APPROVE_OPTION){
                    File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
                    try {
                        FileWriter fw = new FileWriter(file, false);
                        outputTextArea.write(fw);
                        fw.close();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });

        /**
         * Clear input
         */
        clearInputButton.addActionListener(new ActionListener() {
            /**
             * @param e the event to be processed
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                inputTextArea.selectAll();
                inputTextArea.replaceSelection("");
            }
        });

        /**
         * Clear output
         */
        clearOutputButton.addActionListener(new ActionListener() {
            /**
             * @param e the event to be processed
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                outputTextArea.selectAll();
                outputTextArea.replaceSelection("");
            }
        });
    }
}
