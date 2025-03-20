package application;

import javafx.fxml.FXML;
import java.util.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.io.BufferedReader;
import javafx.scene.control.Button;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.io.PrintWriter;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.util.Pair;

public class Controller {
	@FXML
    private Button buttonCasTests;

    @FXML
    private Button buttonTcasIncorrect;

    @FXML
    private Button buttonTcasCorrect;
    
    @FXML
    private Button trantula;
    
    @FXML
    private Button ochiai;
    
    @FXML
    private Button zoltar;
    
    @FXML
    private Button jaccard;
    
    @FXML
    private Button pf;
    
    @FXML
    private Button classm;
    
    @FXML
    private Button par;

    @FXML
    private TextArea textAreaCasTests;

    @FXML
    private TextArea textAreaTcasIncorrect;

    @FXML
    private TextArea textAreaTcasCorrect;

    @FXML
    private MenuItem menuItemNew;

    @FXML
    private MenuItem menuItemSave;

    @FXML
    private MenuItem menuItemSaveAs;

    @FXML
    private MenuItem menuItemCloseWindow;

    @FXML
    private MenuItem menuItemExit;

    @FXML
    private MenuItem menuItemUndo;

    @FXML
    private MenuItem menuItemRedo;

    @FXML
    private MenuItem menuItemCut;

    @FXML
    private MenuItem menuItemCopy;

    @FXML
    private MenuItem menuItemPaste;

    @FXML
    private MenuItem menuItemDelete;

    @FXML
    private MenuItem menuItemSelectAll;

    @FXML
    private MenuItem menuItemFind;

    @FXML
    private MenuItem menuItemReplace;

    @FXML
    private MenuItem menuItemClean;

    @FXML
    private MenuItem menuItemRun;

    @FXML
    private MenuItem menuItemDebug;

    @FXML
    private MenuItem menuItemNewWindow;

    @FXML
    private MenuItem menuItemZoomIn;

    @FXML
    private MenuItem menuItemZoomOut;

    @FXML
    private MenuItem menuItemResetZoom;

    @FXML
    private CheckMenuItem checkMenuItemHideToolBar;

    @FXML
    private MenuItem menuItemDocumentation;

    @FXML
    private MenuItem menuItemAbout;
    
    @FXML 
    private StackPane rootStackPane;
    
    @FXML 
    private Pane mainPane;
    
    @FXML 
    private MenuBar menuBar;
    
    @FXML 
    private HBox buttonContainer;
    
    @FXML 
    private Separator separator1, separator2;

    // Existing UI component injections...
    private File currentCasTestsFile;
    private File currentTcasIncorrectFile;
    private File currentTcasCorrectFile;
    private double currentZoomLevel = 1.0;
    private static final double ZOOM_FACTOR = 0.1;
    private TextArea lastFocusedTextArea;
    private boolean isCasTestsUnsaved = false;
    private boolean isTcasIncorrectUnsaved = false;
    private boolean isTcasCorrectUnsaved = false;
 // Layout Constants
    private static final double BUTTON_WIDTH_PERCENT = 0.3; // 30% of parent width
    private static final double BUTTON_SPACING_PERCENT = 0.03; // 3.3% spacing
    private static final double SEPARATOR_POSITION_1 = 0.33; // 33% from left
    private static double SEPARATOR_POSITION_2 = 0.66; // 66% from left
    private static final double BUTTON_WIDTH_PERCENT2 = 0.3;
    private static final double BUTTON_WIDTH_PERCENT3 = 0.245312500000001;


    @FXML
    public void initialize() {
    	// Verify all injections first
        assert menuBar != null : "menuBar not injected!";
        assert buttonContainer != null : "toolBar not injected!";
        
        setupDynamicLayout();
        setupFixedElements();
        setupTextAreaFocusListeners();
     // File Menu
        menuItemNew.setOnAction(event -> handleNew());
        menuItemSave.setOnAction(event -> handleSave());
        menuItemSaveAs.setOnAction(event -> handleSaveAs());
        menuItemCloseWindow.setOnAction(event -> handleCloseWindow());
        menuItemExit.setOnAction(event -> handleExit());

        // Edit Menu
        menuItemUndo.setOnAction(event -> handleUndo());
        menuItemRedo.setOnAction(event -> handleRedo());
        menuItemCut.setOnAction(event -> handleCut());
        menuItemCopy.setOnAction(event -> handleCopy());
        menuItemPaste.setOnAction(event -> handlePaste());
        menuItemDelete.setOnAction(event -> handleDelete());
        menuItemSelectAll.setOnAction(event -> handleSelectAll());
        menuItemFind.setOnAction(event -> handleFind());
        menuItemReplace.setOnAction(event -> handleReplace());

        // Project Menu
        menuItemClean.setOnAction(event -> handleClean());

        // Run Menu
        menuItemDebug.setOnAction(event -> handleDebug());

        // Window Menu
        menuItemNewWindow.setOnAction(event -> handleNewWindow());
        menuItemZoomIn.setOnAction(event -> handleZoomIn());
        menuItemZoomOut.setOnAction(event -> handleZoomOut());
        menuItemResetZoom.setOnAction(event -> handleResetZoom());

        // Help Menu
        menuItemDocumentation.setOnAction(event -> handleDocumentation());
        menuItemAbout.setOnAction(event -> handleAbout());
    }
    
    
    private void setupDynamicLayout() {
        // Listen for pane size changes
        mainPane.widthProperty().addListener((obs, old, newVal) -> updateLayout());
        mainPane.heightProperty().addListener((obs, old, newVal) -> updateLayout());
        
        // Initial layout
        updateLayout();
    }

    private void setupFixedElements() {
        // Menu bar fixed at top
        menuBar.setLayoutY(0);
        menuBar.prefWidthProperty().bind(mainPane.widthProperty());
        
        // Tool bar fixed at bottom
        buttonContainer.setLayoutY(mainPane.getHeight() - buttonContainer.getHeight());
        buttonContainer.prefWidthProperty().bind(mainPane.widthProperty());
    }

    private void updateLayout() {
        double width = mainPane.getWidth();
        double height = mainPane.getHeight();
        
        // Update buttons
        positionButton(buttonCasTests, width * BUTTON_SPACING_PERCENT);
        positionButton(buttonTcasIncorrect, width * (BUTTON_WIDTH_PERCENT + 2*BUTTON_SPACING_PERCENT));
        positionButton(buttonTcasCorrect, width * (2*BUTTON_WIDTH_PERCENT + 3*BUTTON_SPACING_PERCENT));
        
        // Update separators
        positionSeparator(separator1, width * SEPARATOR_POSITION_1, height);
        positionSeparator(separator2, width * SEPARATOR_POSITION_2, height);
        
        // Update text areas
        positionTextArea(textAreaCasTests, width * BUTTON_SPACING_PERCENT, height);
        positionTextArea(textAreaTcasIncorrect, width * (BUTTON_WIDTH_PERCENT + BUTTON_SPACING_PERCENT) + 15, height);
        positionTextArea(textAreaTcasCorrect, width * (4*(BUTTON_WIDTH_PERCENT + BUTTON_SPACING_PERCENT)) + 15, height);
        
        // Update tool bar position
        buttonContainer.setLayoutY(height - buttonContainer.getHeight());
        trantula.setPrefWidth(mainPane.getWidth() * BUTTON_WIDTH_PERCENT2);
        ochiai.setPrefWidth(mainPane.getWidth() * BUTTON_WIDTH_PERCENT2);
        zoltar.setPrefWidth(mainPane.getWidth() * BUTTON_WIDTH_PERCENT2);
        jaccard.setPrefWidth(mainPane.getWidth() * BUTTON_WIDTH_PERCENT2);
        pf.setPrefWidth(mainPane.getWidth() * BUTTON_WIDTH_PERCENT3);
        classm.setPrefWidth(mainPane.getWidth() * BUTTON_WIDTH_PERCENT3);
        par.setPrefWidth(mainPane.getWidth() * BUTTON_WIDTH_PERCENT3);
    }

    private void positionButton(Button button, double xPosition) {
        button.setLayoutX(xPosition);
        button.setLayoutY(mainPane.getHeight() * 0.47);
        button.setPrefWidth(mainPane.getWidth() * BUTTON_WIDTH_PERCENT);
        button.setPrefHeight(mainPane.getHeight() * 0.07);
    }

    private void positionSeparator(Separator separator, double xPosition, double paneHeight) {
        separator.setLayoutX(xPosition + 5);
        separator.setLayoutY(25.0);
        separator.setPrefHeight(paneHeight);
    }

    private void positionTextArea(TextArea textArea, double xPosition, double paneHeight) {
        textArea.setLayoutX(xPosition);
        textArea.setLayoutY(30);
        textArea.setPrefWidth(mainPane.getWidth() * BUTTON_WIDTH_PERCENT);
        textArea.setPrefHeight(paneHeight - buttonContainer.getHeight() - 30);
    }

    private Object handleAbout() {
		// TODO Auto-generated method stub
		return null;
	}

	private Object handleDocumentation() {
		// TODO Auto-generated method stub
		return null;
	}

	private Object handleDebug() {
		// TODO Auto-generated method stub
		return null;
	}

	private void setupTextAreaFocusListeners() {
        textAreaCasTests.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) lastFocusedTextArea = textAreaCasTests;
            isCasTestsUnsaved = true;
        });
        textAreaTcasIncorrect.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) lastFocusedTextArea = textAreaTcasIncorrect;
            isTcasIncorrectUnsaved = true;
        });
        textAreaTcasCorrect.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) lastFocusedTextArea = textAreaTcasCorrect;
            isTcasCorrectUnsaved = true;
        });
    }

    // File Menu Handlers
    private void handleNew() {
        if (showUnsavedChangesAlert()) {
            resetAllTextAreas();
            showAllButtons();
            if (SEPARATOR_POSITION_2 == 0.9) {
            	SEPARATOR_POSITION_2 = 0.66;
            	separator2.setLayoutX(mainPane.getWidth() * SEPARATOR_POSITION_2 + 5);
            }
        }
    }

    private void handleSave() {
        if (lastFocusedTextArea == null) return;
        
        File currentFile = getCurrentFileForTextArea(lastFocusedTextArea);
        if (currentFile != null) {
            saveToFile(currentFile, lastFocusedTextArea.getText());
        } else {
            handleSaveAs();
        }
    }

    private void handleSaveAs() {
        if (lastFocusedTextArea == null) return;
        
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save File");
        File file = fileChooser.showSaveDialog(lastFocusedTextArea.getScene().getWindow());
        if (file != null) {
            saveToFile(file, lastFocusedTextArea.getText());
            updateCurrentFileForTextArea(lastFocusedTextArea, file);
        }
    }

    private void saveToFile(File file, String content) {
        try {
            Files.writeString(file.toPath(), content);
            showAlert("Success", "File saved successfully!", Alert.AlertType.INFORMATION);
            resetUnsavedFlagForTextArea(lastFocusedTextArea);
        } catch (IOException e) {
            showAlert("Error", "Failed to save file: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void resetUnsavedFlagForTextArea(TextArea textArea) {
        if (textArea == textAreaCasTests) {
            isCasTestsUnsaved = false;
        } else if (textArea == textAreaTcasIncorrect) {
            isTcasIncorrectUnsaved = false;
        } else if (textArea == textAreaTcasCorrect) {
            isTcasCorrectUnsaved = false;
        }
    }
    
    private void handleCloseWindow() {
        // Check for unsaved changes
        if (showUnsavedChangesAlert()) {
            // If user confirms or there are no unsaved changes, close the window
            Stage stage = (Stage) textAreaCasTests.getScene().getWindow(); // Get the current window
            stage.close(); // Close the window
        }
    }
    
    private void handleExit() {
		Stage  stage = (Stage) textAreaCasTests.getScene().getWindow();
		stage.close();
	}

    // Edit Menu Handlers
    private void handleUndo() {
        if (lastFocusedTextArea != null) {
            lastFocusedTextArea.undo();
        }
    }

    private void handleRedo() {
        if (lastFocusedTextArea != null) {
            lastFocusedTextArea.redo();
        }
    }

    private void handleCut() {
        if (lastFocusedTextArea != null) {
            Clipboard clipboard = Clipboard.getSystemClipboard();
            ClipboardContent content = new ClipboardContent();
            content.putString(lastFocusedTextArea.getSelectedText());
            clipboard.setContent(content);
            lastFocusedTextArea.replaceSelection("");
        }
    }

    private void handleCopy() {
        if (lastFocusedTextArea != null) {
            Clipboard clipboard = Clipboard.getSystemClipboard();
            ClipboardContent content = new ClipboardContent();
            content.putString(lastFocusedTextArea.getSelectedText());
            clipboard.setContent(content);
        }
    }

    private void handlePaste() {
        if (lastFocusedTextArea != null) {
            Clipboard clipboard = Clipboard.getSystemClipboard();
            if (clipboard.hasString()) {
                lastFocusedTextArea.replaceSelection(clipboard.getString());
            }
        }
    }
    
    private void handleDelete() {
        if (lastFocusedTextArea != null) {
            // Delete selected text in the focused TextArea
            int start = lastFocusedTextArea.getSelection().getStart();
            int end = lastFocusedTextArea.getSelection().getEnd();
            
            if (start != end) { // If text is selected
                lastFocusedTextArea.deleteText(start, end);
            } else { // If no text is selected, delete the character at the caret position
                int caretPosition = lastFocusedTextArea.getCaretPosition();
                if (caretPosition < lastFocusedTextArea.getLength()) {
                    lastFocusedTextArea.deleteText(caretPosition, caretPosition + 1);
                }
            }
        } else {
            showAlert("Delete Error", "No text area is focused.", Alert.AlertType.WARNING);
        }
    }
    
    private void handleSelectAll() {
        if (lastFocusedTextArea != null) {
            // Select all text in the focused TextArea
            lastFocusedTextArea.selectAll();
        } else {
            showAlert("Selection Error", "No text area is focused.", Alert.AlertType.WARNING);
        }
    }
    
    private void handleFind() {
        if (lastFocusedTextArea == null) {
            showAlert("Find Error", "No text area is focused.", Alert.AlertType.WARNING);
            return;
        }

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Find");
        dialog.setHeaderText("Enter text to find:");
        Optional<String> result = dialog.showAndWait();

        result.ifPresent(searchText -> {
            String content = lastFocusedTextArea.getText();
            int index = content.indexOf(searchText);
            
            if (index != -1) {
                // Select the first occurrence
                lastFocusedTextArea.selectRange(index, index + searchText.length());
            } else {
                showAlert("Find", "Text not found: " + searchText, Alert.AlertType.INFORMATION);
            }
        });
    }
    
    private void handleReplace() {
        if (lastFocusedTextArea == null) {
            showAlert("Replace Error", "No text area is focused.", Alert.AlertType.WARNING);
            return;
        }

        // Create custom dialog
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Replace");
        dialog.setHeaderText("Enter search and replacement text:");

        // Set buttons
        ButtonType replaceButton = new ButtonType("Replace", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(replaceButton, ButtonType.CANCEL);

        // Create input fields
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField searchField = new TextField();
        TextField replaceField = new TextField();

        grid.add(new Label("Search for:"), 0, 0);
        grid.add(searchField, 1, 0);
        grid.add(new Label("Replace with:"), 0, 1);
        grid.add(replaceField, 1, 1);

        dialog.getDialogPane().setContent(grid);

        // Convert result to search-replace pair
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == replaceButton) {
                return new Pair<>(searchField.getText(), replaceField.getText());
            }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();

        result.ifPresent(pair -> {
            String searchText = pair.getKey();
            String replaceText = pair.getValue();
            String content = lastFocusedTextArea.getText();

            if (content.contains(searchText)) {
                String newContent = content.replace(searchText, replaceText);
                lastFocusedTextArea.setText(newContent);
                showAlert("Replace", "Replacement completed.", Alert.AlertType.INFORMATION);
            } else {
                showAlert("Replace", "Search text not found.", Alert.AlertType.WARNING);
            }
        });
    }
    
    private void handleNewWindow() {
        try {
            // Load the FXML file for the new window
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Main.fxml"));
            Parent root = loader.load();
            
            // Create new stage and scene
            Stage newStage = new Stage();
            newStage.setTitle("New Window - Debugger Interface");
            newStage.setScene(new Scene(root));
            
            // Set position offset from current window
            Stage currentStage = (Stage) textAreaCasTests.getScene().getWindow();
            newStage.setX(currentStage.getX() + 20);
            newStage.setY(currentStage.getY() + 20);
            
            newStage.show();
        } catch (IOException e) {
            showAlert("Window Error", "Could not create new window: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    // Window Menu Handlers
    private void handleZoomIn() {
        currentZoomLevel += ZOOM_FACTOR;
        applyZoom();
    }

    private void handleZoomOut() {
        currentZoomLevel = Math.max(0.5, currentZoomLevel - ZOOM_FACTOR);
        applyZoom();
    }
    
    private void handleResetZoom() {
		currentZoomLevel = 1.0;
		applyZoom();
	}

    private void applyZoom() {
        textAreaCasTests.setStyle("-fx-font-size: " + (14 * currentZoomLevel) + "px;");
        textAreaTcasIncorrect.setStyle("-fx-font-size: " + (14 * currentZoomLevel) + "px;");
        textAreaTcasCorrect.setStyle("-fx-font-size: " + (14 * currentZoomLevel) + "px;");
    }
    
    private void handleClean() {
        // Confirm with the user before cleaning
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Clean Project");
        confirmAlert.setHeaderText("Are you sure you want to clean the project?");
        confirmAlert.setContentText("This will remove all generated files and reset the workspace.");
        
        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                // Define directories to clean
                File projectDir = new File(System.getProperty("user.dir"));
                File[] dirsToClean = {
                    new File(projectDir, "cas-tests"),
                    new File(projectDir, "tcas-incorrect"),
                    new File(projectDir, "tcas-correct")
                };
                
                // Delete contents of directories
                for (File dir : dirsToClean) {
                    if (dir.exists()) {
                        Files.walk(dir.toPath())
                             .sorted(Comparator.reverseOrder())
                             .map(Path::toFile)
                             .forEach(File::delete);
                    }
                }
                
                // Reset UI components
                resetAllTextAreas();
                showAllButtons();
                
                // Show success message
                showAlert("Clean Successful", 
                         "Project cleaned successfully!\nRemoved files from:\n"
                         + "- cas-tests\n- tcas-incorrect\n- tcas-correct",
                         Alert.AlertType.INFORMATION);
            } catch (IOException e) {
                showAlert("Clean Error", 
                         "Failed to clean project: " + e.getMessage(),
                         Alert.AlertType.ERROR);
            }
        }
    }

    // Helper Methods
    private boolean showUnsavedChangesAlert() {
        if (!hasUnsavedChanges()) {
            return true; // No unsaved changes, proceed without alert
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Unsaved Changes");
        alert.setHeaderText("You have unsaved changes!");
        alert.setContentText("Do you want to proceed without saving?");

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    private boolean hasUnsavedChanges() {
        if (lastFocusedTextArea == textAreaCasTests) {
            return isCasTestsUnsaved;
        } else if (lastFocusedTextArea == textAreaTcasIncorrect) {
            return isTcasIncorrectUnsaved;
        } else if (lastFocusedTextArea == textAreaTcasCorrect) {
            return isTcasCorrectUnsaved;
        }
        return false; // No focused TextArea
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void resetAllTextAreas() {
        textAreaCasTests.clear();
        textAreaCasTests.setVisible(false);
        textAreaTcasIncorrect.clear();
        textAreaTcasIncorrect.setVisible(false);
        textAreaTcasCorrect.clear();
        textAreaTcasCorrect.setVisible(false);
    }

    private void showAllButtons() {
        buttonCasTests.setVisible(true);
        buttonTcasIncorrect.setVisible(true);
        buttonTcasCorrect.setVisible(true);
    }

    private File getCurrentFileForTextArea(TextArea textArea) {
        if (textArea == textAreaCasTests) return currentCasTestsFile;
        if (textArea == textAreaTcasIncorrect) return currentTcasIncorrectFile;
        if (textArea == textAreaTcasCorrect) return currentTcasCorrectFile;
        return null;
    }

    private void updateCurrentFileForTextArea(TextArea textArea, File file) {
        if (textArea == textAreaCasTests) currentCasTestsFile = file;
        else if (textArea == textAreaTcasIncorrect) currentTcasIncorrectFile = file;
        else if (textArea == textAreaTcasCorrect) currentTcasCorrectFile = file;
    }

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

    //handle file selection
    private void handleFileSelection(Button button, TextArea textArea) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt", "*.c"));

        Stage stage = (Stage) button.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);
        updateCurrentFileForTextArea(textArea, selectedFile);

        if (selectedFile != null) {
            try {
                // Read file content
                String content = Files.readString(selectedFile.toPath(), StandardCharsets.UTF_8);

                // Hide the button and show the TextArea with the content
                button.setVisible(false);
                if (button != buttonTcasCorrect) {
                	textArea.setText(content);
                } else {
                	SEPARATOR_POSITION_2 = 0.9;
                	separator2.setLayoutX(mainPane.getWidth() * SEPARATOR_POSITION_2);
                }
                textArea.setVisible(true);
            } catch (IOException e) {
                e.printStackTrace();
                textArea.setText("Error reading file.");
                textArea.setVisible(true);
            }
        }
    }
    public class scriptreader {
        //call a bash script in ur java code and run it
          public static void main(String[] args) {
            String[] cmd = new String[] {"./src/packages/outputGenerator.sh"}; //your script here
            ProcessBuilder pb = new ProcessBuilder(cmd);

            try {
                Process p= pb.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
          }
        }
    public class ScriptReader2 {
    	//call a bash script in ur java code and run it
    	  public static void main(String[] args) {
    	    String[] cmd = new String[] {"./src/packages/categorizeTests_cases.sh"}; //your script here
    	    ProcessBuilder pb = new ProcessBuilder(cmd);

    	    try {
    	        Process p= pb.start();
    	    } catch (IOException e) {
    	        e.printStackTrace();
    	    }
    	  }
    	}
    
    
    public class genresults {

        public static Set<String> readFileToSet(String filename) throws IOException {
            Set<String> set = new HashSet<>();
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;
            while ((line = reader.readLine()) != null) {
                set.add(line.trim());
            }
            reader.close();
            return set;
        }

        public static void main(String[] args) throws IOException {
            String cas_tests = "cas-tests.txt";
            String failing_tests = "Failing_test_cases.txt";
            String passing_tests = "Passing_test_cases.txt";
            String csv_file = "results.csv";

            Set<String> failingTests = readFileToSet(failing_tests);
            Set<String> passingTests = readFileToSet(passing_tests);

            BufferedReader reader = new BufferedReader(new FileReader(cas_tests));
            FileWriter writer = new FileWriter(csv_file);

            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (failingTests.contains(line)) {
                    writer.append("F\n");
                } else if (passingTests.contains(line)) {
                    writer.append("P\n");
                } else {
                    writer.append("MAKACH\n");
                }
            }

            reader.close();
            writer.close();
        }
    }
    
    
   

    public class susdegree {

        public static void main(String[] args) {
            
            String filenameParametres = "parametres.csv";
            
            String filenameOchiai      = "Ochiai.csv";
            String filenameTarantula   = "Tarantula.csv";
            String filenameJaccard         = "Jaccard.csv";
            String filenameZoltar        = "Zoltar.csv";
            
            try (
                BufferedReader lecteurFichier = new BufferedReader(new FileReader(filenameParametres));
                PrintWriter ecrivainOchiai    = new PrintWriter(filenameOchiai);
                PrintWriter ecrivainTarantula = new PrintWriter(filenameTarantula);
                PrintWriter ecrivainJaccard       = new PrintWriter(filenameJaccard);
                PrintWriter ecrivainZoltar      = new PrintWriter(filenameZoltar);
            ) {
                String ligne;
                
                while ((ligne = lecteurFichier.readLine()) != null) {
                    String[] morceaux = ligne.split(",");
                    if (morceaux.length < 4) {
                        //kch error wla
                        continue;
                    }
                    
                    int yanisEp = Integer.parseInt(morceaux[0].trim());
                    int yanisEf = Integer.parseInt(morceaux[1].trim());
                    int douaaNp      = Integer.parseInt(morceaux[2].trim());
                    int douaaNf      = Integer.parseInt(morceaux[3].trim());
                    
                    
                    double ep = (double) yanisEp;
                    double ef = (double) yanisEf;
                    double np = (double) douaaNp;
                    double nf = (double) douaaNf;
                    
                    
                    // Ochiai = ef / sqrt((ef + nf)*(ef + ep))
                    double ochiaiValeur = 0.0;
                    double denomOchiai = (ef + nf) * (ef + ep);
                    if (denomOchiai > 0) {
                        ochiaiValeur = ef / Math.sqrt(denomOchiai);
                    }
                    
                    // Tarantula = [ef/(ef+nf)] / ([ef/(ef+nf)] + [ep/(ep+np)])
                    double tarantulaValeur = 0.0;
                    double failRatio = 0.0;
                    double passRatio = 0.0;
                    
                    if ((ef + nf) > 0) {
                        failRatio = ef / (ef + nf);
                    }
                    if ((ep + np) > 0) {
                        passRatio = ep / (ep + np);
                    }
                    
                    double denomTarantula = failRatio + passRatio;
                    if (denomTarantula > 0) {
                        tarantulaValeur = failRatio / denomTarantula;
                    }
                    
                    // Jaccard = ef / (ef + nf + ep)
                    double Jaccard = ef / (ef + nf + ep);
                    
                    
                    // Zoltar = ef / (ef+nf+ep+((10000*nf*ep)/ef))
                    double Zoltar = 0.0;
                    if (ef != 0) {
                        Zoltar = ef / (ef + nf + ep + ((10000.0 * nf * ep) / ef));
                    }
                    
                    ecrivainOchiai.println(ochiaiValeur);
                    ecrivainTarantula.println(tarantulaValeur);
                    ecrivainJaccard.println(Jaccard);
                    ecrivainZoltar.println(Zoltar);
                }
                
                
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
   
    public class classement {

        public static void main(String[] args) {
            String[] methods = {"Jaccard", "Ochiai", "Tarantula", "Zoltar"};
            
            for (String method : methods) {
                String inputFileName = method + ".csv";
                String outputFileName = method + "_classement.csv";
                processFile(inputFileName, outputFileName);
            }
        }
        
        private static void processFile(String inputFile, String outputFile) {
            Map<Double, List<Integer>> valueToOffsets = new HashMap<>();
            try (BufferedReader br = new BufferedReader(new FileReader(inputFile))) {
                String line;
                int lineNumber = 1; 
                while ((line = br.readLine()) != null) {
                    double value = Double.parseDouble(line.trim());
                    valueToOffsets.computeIfAbsent(value, k -> new ArrayList<>()).add(lineNumber);
                    lineNumber++;
                }
            } catch (IOException e) {
                System.err.println("Error reading file: " + inputFile);
                e.printStackTrace();
                return;
            }
            for (List<Integer> offsets : valueToOffsets.values()) {
                Collections.sort(offsets);
            }
            
            List<Double> values = new ArrayList<>(valueToOffsets.keySet());
            values.sort(Collections.reverseOrder());
            try (PrintWriter pw = new PrintWriter(new FileWriter(outputFile))) {
                for (Double value : values) {
                    List<Integer> offsets = valueToOffsets.get(value);
                    String outputLine = joinList(offsets);
                    pw.println(outputLine);
                }
            } catch (IOException e) {
                System.err.println("Error writing file: " + outputFile);
                e.printStackTrace();
            }
        }
        

        private static String joinList(List<Integer> list) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < list.size(); i++) {
                sb.append(list.get(i));
                if (i != list.size() - 1) {
                    sb.append(",");
                }
            }
            return sb.toString();
        }
    }
    
    
    @FXML
    private void calculateTrantula() {
    	//TO-Do
    	//textAreaTcasCorrect.setText(content);
    }
    @FXML
    private void calculateOchiai() {
    	//TO-Do
    	//textAreaTcasCorrect.setText(content);
    }
    @FXML
    private void calculateZoltar() {
    	//TO-Do
    	//textAreaTcasCorrect.setText(content);
    }
    @FXML
    private void calculateJaccard() {
    	//TO-Do
    	//textAreaTcasCorrect.setText(content);
    }
    @FXML
    private void categorize_test_cases() {
    	//TO-Do
    }
    
    
    
    @FXML
    private TextArea classTextArea; // TextArea for Class.fxml
    @FXML
    private void display_class() {
        try {
            // Load Class.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Class.fxml"));
            Parent root = loader.load();

            // Get the controller of Class.fxml
            Controller classController = loader.getController();
            classController.loadClassementFiles(); // Load all classement files into classTextArea

            // Create and show a new window
            Stage stage = new Stage();
            stage.setTitle("Classement Files");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Function to load CSV file into the TextArea of Para.fxml
    private void loadCSV(String fileName) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(fileName));
            textArea.setText(String.join("\n", lines));
        } catch (IOException e) {
            textArea.setText("Error loading " + fileName);
            e.printStackTrace();
        }
    }

    // Function to load all classement files into the TextArea of Class.fxml
    private void loadClassementFiles() {
        String[] files = {
            "Ochiaii_classement.csv",
            "Tarantulai_classement.csv",
            "Jacardi_classement.csv",
            "Zoltari_classement.csv"
        };

        StringBuilder content = new StringBuilder();
        for (String file : files) {
            content.append("=== ").append(file).append(" ===\n");
            try {
                List<String> lines = Files.readAllLines(Paths.get(file));
                content.append(String.join("\n", lines)).append("\n\n");
            } catch (IOException e) {
                content.append("Error loading ").append(file).append("\n\n");
                e.printStackTrace();
            }
        }
        classTextArea.setText(content.toString());
    }
    
    
    
    
    @FXML
    private TextArea textArea; // This must match the fx:id in Para.fxml
   
    @FXML
    private void display_para() {
        try {
            // Load Para.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Para.fxml"));
            Parent root = loader.load();

            // Get the controller of Para.fxml to access its TextArea
            Controller paraController = loader.getController();
            paraController.loadCSV(); // Load parameters.csv into TextArea

            // Create and show a new window
            Stage stage = new Stage();
            stage.setTitle("Parameters File");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Function to load parameters.csv into the TextArea
    private void loadCSV() {
        try {
            List<String> lines = Files.readAllLines(Paths.get("parameters.csv"));
            String content = String.join("\n", lines);
            textArea.setText(content);
        } catch (IOException e) {
            textArea.setText("Error loading parameters.csv");
            e.printStackTrace();
        }
    }
}


