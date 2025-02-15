package application;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.charset.StandardCharsets;

public class Controller {

    @FXML
    private Button buttonCasTests;

    @FXML
    private Button buttonTcasIncorrect;

    @FXML
    private Button buttonTcasCorrect;

    @FXML
    private TextArea textAreaCasTests;

    @FXML
    private TextArea textAreaTcasIncorrect;

    @FXML
    private TextArea textAreaTcasCorrect;

    // Method to handle file selection for cas-tests
    @FXML
    public void openFileExplorerCasTests() {
        handleFileSelection(buttonCasTests, textAreaCasTests);
    }

    // Method to handle file selection for tcas-Incorrect
    @FXML
    public void openFileExplorerTcasIncorrect() {
        handleFileSelection(buttonTcasIncorrect, textAreaTcasIncorrect);
    }

    // Method to handle file selection for tcas-Correct
    @FXML
    public void openFileExplorerTcasCorrect() {
        handleFileSelection(buttonTcasCorrect, textAreaTcasCorrect);
    }

    // Generic method to handle file selection
    private void handleFileSelection(Button button, TextArea textArea) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt", "*.c"));

        Stage stage = (Stage) button.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            try {
                // Read file content
                String content = Files.readString(selectedFile.toPath(), StandardCharsets.UTF_8);
                
                // Hide the button and show the TextArea with the content
                button.setVisible(false);
                textArea.setText(content);
                textArea.setVisible(true);
            } catch (IOException e) {
                e.printStackTrace();
                textArea.setText("Error reading file.");
                textArea.setVisible(true);
            }
        }
    }
}


