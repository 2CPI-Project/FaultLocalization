package application;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import backend.*;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Pair;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.Pane;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.io.*;
import java.util.*;

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
    
    private LineNumberHelper casTestsLineNumbers;
    private LineNumberHelper tcasIncorrectLineNumbers; 
    private LineNumberHelper tcasCorrectLineNumbers;

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
        
        
        casTestsLineNumbers = new LineNumberHelper(textAreaCasTests);
        tcasIncorrectLineNumbers = new LineNumberHelper(textAreaTcasIncorrect);
        tcasCorrectLineNumbers = new LineNumberHelper(textAreaTcasCorrect);
        mainPane.getChildren().addAll(
        	    casTestsLineNumbers.getLineNumbers(),
        	    tcasIncorrectLineNumbers.getLineNumbers(),
        	    tcasCorrectLineNumbers.getLineNumbers()
        	);
        
        updateLayout();
    }

    private void setupFixedElements() {
        // Menu bar fixed at top
        menuBar.setLayoutY(0);
        menuBar.prefWidthProperty().bind(mainPane.widthProperty());
        
        
        textAreaTcasIncorrect.setWrapText(false);
        textAreaTcasCorrect.setWrapText(false);
        textAreaCasTests.setWrapText(false);
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
        positionTextArea(textAreaTcasCorrect, width * (2.63*(BUTTON_WIDTH_PERCENT + BUTTON_SPACING_PERCENT)) + 15, height);
        
        // Update tool bar position
        buttonContainer.setLayoutY(height - buttonContainer.getHeight());
        trantula.setPrefWidth(mainPane.getWidth() * BUTTON_WIDTH_PERCENT2);
        ochiai.setPrefWidth(mainPane.getWidth() * BUTTON_WIDTH_PERCENT2);
        zoltar.setPrefWidth(mainPane.getWidth() * BUTTON_WIDTH_PERCENT2);
        jaccard.setPrefWidth(mainPane.getWidth() * BUTTON_WIDTH_PERCENT2);
        pf.setPrefWidth(mainPane.getWidth() * BUTTON_WIDTH_PERCENT3);
        classm.setPrefWidth(mainPane.getWidth() * BUTTON_WIDTH_PERCENT3);
        par.setPrefWidth(mainPane.getWidth() * BUTTON_WIDTH_PERCENT3);
        
        positionLineNumbers(casTestsLineNumbers.getLineNumbers(), 
                        width * BUTTON_SPACING_PERCENT, height);
       // casTestsLineNumbers.positionRelativeTo(textAreaCasTests);
        positionLineNumbers(tcasIncorrectLineNumbers.getLineNumbers(),
		                width * (BUTTON_WIDTH_PERCENT + BUTTON_SPACING_PERCENT) + 15, height);
        //tcasIncorrectLineNumbers.positionRelativeTo(textAreaTcasIncorrect);
		positionLineNumbers(tcasCorrectLineNumbers.getLineNumbers(),
		                width * (2.63*(BUTTON_WIDTH_PERCENT + BUTTON_SPACING_PERCENT)) + 15, height);
		//tcasCorrectLineNumbers.positionRelativeTo(textAreaTcasCorrect);
    }
    
    private void positionLineNumbers(ListView<?> lineNumbers, double xPosition, double paneHeight) {
        lineNumbers.setLayoutX(xPosition - 40);
        lineNumbers.setLayoutY(30);
        lineNumbers.setPrefHeight(paneHeight - buttonContainer.getHeight() - 30);
        
        // Only add to layout once
        if (!mainPane.getChildren().contains(lineNumbers)) {
            mainPane.getChildren().add(lineNumbers);
        }
        
        // Keep visibility state (hidden by default)
        lineNumbers.setVisible(false);
    }

    private void positionTextArea(TextArea textArea, double xPosition, double paneHeight) {
        // Adjust text area position to account for line numbers
        textArea.setLayoutX(xPosition);
        textArea.setLayoutY(30);
        textArea.setPrefWidth(mainPane.getWidth() * BUTTON_WIDTH_PERCENT - 45); // Reduce width
        textArea.setPrefHeight(paneHeight - buttonContainer.getHeight() - 30);
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

   /* private void positionTextArea(TextArea textArea, double xPosition, double paneHeight) {
        textArea.setLayoutX(xPosition);
        textArea.setLayoutY(30);
        textArea.setPrefWidth(mainPane.getWidth() * BUTTON_WIDTH_PERCENT);
        textArea.setPrefHeight(paneHeight - buttonContainer.getHeight() - 30);
    }*/

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

	private Object handleRun() {
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
            if (SEPARATOR_POSITION_2 == 0.87) {
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
        casTestsLineNumbers.hide();
        tcasIncorrectLineNumbers.hide();
        tcasCorrectLineNumbers.hide();
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
 // Modified handleFileSelection method with file saving logic
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

                // Determine target file name based on button
                String fileName;
                if (button == buttonCasTests) {
                    fileName = "cas-tests.txt";
                } else if (button == buttonTcasIncorrect) {
                    fileName = "tcas_Incorrect.c";
                } else if (button == buttonTcasCorrect) {
                    fileName = "tcas_Correct.c";
                } else {
                    throw new IllegalArgumentException("Unknown button type");
                }

                // Create target directory if it doesn't exist
                Path targetDir = Paths.get("src", "packages");
                if (!Files.exists(targetDir)) {
                    Files.createDirectories(targetDir);
                }

                // Save file to target directory
                Path targetPath = targetDir.resolve(fileName);
                Files.write(targetPath, content.getBytes(StandardCharsets.UTF_8));

                // Update UI elements
                button.setVisible(false);
                if (button != buttonTcasCorrect) {
                    textArea.setText(content);
                } else {
                    SEPARATOR_POSITION_2 = 0.87;
                    separator2.setLayoutX(mainPane.getWidth() * SEPARATOR_POSITION_2);
                    textAreaTcasIncorrect.setPrefWidth(textAreaTcasIncorrect.getWidth()*1.9);
                }
                textArea.setVisible(true);

            } catch (IOException e) {
                e.printStackTrace();
                textArea.setText("Error reading/saving file.");
                textArea.setVisible(true);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                textArea.setText("Invalid button configuration.");
                textArea.setVisible(true);
            }
        }
        
        getHelperForTextArea(textArea).show();
        LineNumberHelper helper = getHelperForTextArea(textArea);
        if (helper != null) {
            helper.getLineNumbers().setVisible(!button.isVisible());
        }
    }
    
    private LineNumberHelper getHelperForTextArea(TextArea textArea) {
        if (textArea == textAreaCasTests) return casTestsLineNumbers;
        if (textArea == textAreaTcasIncorrect) return tcasIncorrectLineNumbers;
        if (textArea == textAreaTcasCorrect) return tcasCorrectLineNumbers;
        return null;
    }
    

    private void displayCSV(String csvFile) {
        try {
            String content = Files.readString(Paths.get(csvFile));
            buttonTcasCorrect.setVisible(false);
            textAreaTcasCorrect.setVisible(true);
            textAreaTcasCorrect.setText(content);
        } catch (IOException e) {
            showAlert("Error", "Could not read " + csvFile, Alert.AlertType.ERROR);
        }
    }
    
 // Unified runTask implementation
    private void runTask(Supplier<String> task, Consumer<String> onSuccess, Consumer<Exception> onError) {
        new Thread(() -> {
            try {
                String result = task.get();
                Platform.runLater(() -> onSuccess.accept(result));
            } catch (Exception e) {
                Platform.runLater(() -> onError.accept(e));
            }
        }).start();
    }

    @FXML
    private void calculateTrantula() {
        runTask(
            () -> {
                if (!Files.exists(Paths.get("src/packages/parametres.csv"))) {
                	categorize();
                }
                return "Parameters verified";
            },
            result -> {
                runTask(
                    () -> {
                        try {
							SuspiciousnessCalculator.calculate("Tarantula");
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                        try {
							return loadCSV("Tarantula.csv");
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						return result;
                    },
                    content -> textAreaTcasCorrect.setText(content),
                    error -> showAlert("Tarantula Error", error.getMessage(), Alert.AlertType.ERROR)
                );
            },
            error -> showAlert("Parameter Error", "Failed to verify parameters: " + error.getMessage(), Alert.AlertType.ERROR)
        );
    }

    @FXML
    private void calculateOchiai() {
        runTask(
            () -> {
                if (!Files.exists(Paths.get("src/packages/parametres.csv"))) {
                	categorize();
                }
                return "Parameters verified";
            },
            result -> {
                runTask(
                    () -> {
                        try {
							SuspiciousnessCalculator.calculate("Ochiai");
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                        try {
							return loadCSV("Ochiai.csv");
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						return result;
                    },
                    content -> textAreaTcasCorrect.setText(content),
                    error -> showAlert("Ochiai Error", error.getMessage(), Alert.AlertType.ERROR)
                );
            },
            error -> showAlert("Parameter Error", "Failed to verify parameters: " + error.getMessage(), Alert.AlertType.ERROR)
        );
    }

    @FXML
    private void calculateZoltar() {
        runTask(
            () -> {
                if (!Files.exists(Paths.get("src/packages/parametres.csv"))) {
                    categorize();
                }
                return "Parameters verified";
            },
            result -> {
                runTask(
                    () -> {
                        try {
							SuspiciousnessCalculator.calculate("Zoltar");
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                        try {
							return loadCSV("Zoltar.csv");
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						return result;
                    },
                    content -> textAreaTcasCorrect.setText(content),
                    error -> showAlert("Zoltar Error", error.getMessage(), Alert.AlertType.ERROR)
                );
            },
            error -> showAlert("Parameter Error", "Failed to verify parameters: " + error.getMessage(), Alert.AlertType.ERROR)
        );
    }

    @FXML
    private void calculateJaccard() {
        runTask(
            () -> {
                if (!Files.exists(Paths.get("src/packages/parametres.csv"))) {
                    categorize();
                }
                return "Parameters verified";
            },
            result -> {
                runTask(
                    () -> {
                        try {
							SuspiciousnessCalculator.calculate("Jaccard");
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                        try {
							return loadCSV("Jaccard.csv");
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						return result;
                    },
                    content -> textAreaTcasCorrect.setText(content),
                    error -> showAlert("Jaccard Error", error.getMessage(), Alert.AlertType.ERROR)
                );
            },
            error -> showAlert("Parameter Error", "Failed to verify parameters: " + error.getMessage(), Alert.AlertType.ERROR)
        );
    }
    @FXML
    private void categorize_test_cases() {
    	//TO-Do
    }
    
    @FXML
    private TextArea classTextArea; // TextArea for Class.fxml
    
 // In your Controller class
 // In your Controller class
    @FXML
    private void display_class() {
        runTask(
            () -> {
                // 1. Ensure parameters exist
                if (!Files.exists(Path.of("src/packages/parametres.csv"))) {
                	// Step 1: Run categorization script
                    //ScriptRunner.runCategorizer();
                	// Step 2: Generate test results
                    try {
						TestResultGenerator.generateResults();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                    
                    // Step 3: Generate parameters
                    try {
						ParameterGenerator.generateParameters();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                }
                
                // 2. Generate missing formulas quietly
                generateMissingFormulas();
                
                // 3. Generate classements for existing files
                try {
					ClassementGenerator.generateAllClassements();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                
                // Verify at least one classement exists
                Path classementDir = Path.of("src/packages/classements/");
                try (DirectoryStream<Path> stream = Files.newDirectoryStream(classementDir, "*.csv")) {
                    if (!stream.iterator().hasNext()) {
                        throw new IOException("No classement files generated. Generate formulas first!");
                    }
                } catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                return "Classement files ready";
            },
            result -> showClassementWindow(),
            error -> showAlert("Classement Error", 
                "Failed to generate rankings:\n" + error.getMessage() + 
                "\n\nMake sure you:\n1. Have generated parameters\n" + 
                "2. Calculated at least one formula", 
                Alert.AlertType.ERROR)
        );
    }

    private void generateMissingFormulas() {
        String[] formulas = {"Ochiai", "Tarantula", "Jaccard", "Zoltar"};
        for (String formula : formulas) {
            Path formulaFile = Path.of("src/packages/" + formula + ".csv");
            if (!Files.exists(formulaFile)) {
                try {
                    SuspiciousnessCalculator.calculate(formula);
                } catch (Exception e) {
                    System.err.println("Failed to generate " + formula + ": " + e.getMessage());
                }
            }
        }
    }

    private void showClassementWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/class.fxml"));
            Parent root = loader.load();
            ClassementController controller = loader.getController();
            
            // Load all classement files
            Map<String, String> classements = new LinkedHashMap<>();
            String[] methods = {"Ochiai", "Tarantula", "Jaccard", "Zoltar"};
            
            for (String method : methods) {
                Path path = Path.of("src/packages/classements/" + method + "_classement.csv");
                if (Files.exists(path)) {
                    classements.put(method, Files.readString(path));
                }
            }

            Stage stage = new Stage();
            stage.setTitle("Classement Rankings");
            stage.setScene(new Scene(root, 800, 250));
            
            Platform.runLater(() -> {
                controller.initializeTabs(classements);
                stage.show();
            });
        } catch (IOException e) {
            showAlert("Error", "Failed to display classements: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    
    @FXML
    private TextArea textArea;
 // In your Controller class

    @FXML
    private void display_para() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/parametres.fxml"));
            Parent root = loader.load();
            ParametersController paraController = loader.getController();

            Stage stage = new Stage();
            stage.setTitle("Parameters");
            stage.setScene(new Scene(root));

            runTask(() -> {
                if (!Files.exists(Paths.get("src/packages/parametres.csv"))) {
                	// Step 1: Run categorization script
                    //ScriptRunner.runCategorizer();
                	// Step 2: Generate test results
                    TestResultGenerator.generateResults();
                    
                    // Step 3: Generate parameters
                    ParameterGenerator.generateParameters();
                    return parseParameters();
                }
                return parseParameters();
            }, 
            parameters -> {
                paraController.setParameters(parameters);
                stage.show();
            }, 
            "Failed to load parameters");
            
        } catch (IOException e) {
            showAlert("Error", "Window initialization failed: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private ObservableList<ParameterEntry> parseParameters() throws IOException {
        ObservableList<ParameterEntry> data = FXCollections.observableArrayList();
        List<String> lines = Files.readAllLines(Paths.get("src/packages/parametres.csv"));
        
        for (String line : lines) {
            String[] values = line.split(",");
            if (values.length == 4) {
                data.add(new ParameterEntry(
                    Integer.parseInt(values[0].trim()),
                    Integer.parseInt(values[1].trim()),
                    Integer.parseInt(values[2].trim()),
                    Integer.parseInt(values[3].trim())
                ));
            }
        }
        return data;
    }

    // Generic runTask implementation with error handling
    private <T> void runTask(Callable<T> task, Consumer<T> onSuccess, String errorMessage) {
        new Thread(() -> {
            try {
                T result = task.call();
                Platform.runLater(() -> onSuccess.accept(result));
            } catch (Exception e) {
                Platform.runLater(() -> 
                    showAlert("Error", errorMessage + ": " + e.getMessage(), Alert.AlertType.ERROR));
            }
        }).start();
    }
    
//====== Core Methods ======//
    private void categorize() { // Renamed to follow Java conventions
        runTask(
            () -> {
                // Step 1: Run categorization script
                //ScriptRunner.runCategorizer();
                
                // Step 2: Generate test results
                try {
					TestResultGenerator.generateResults();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                
                // Step 3: Generate parameters
                try {
					ParameterGenerator.generateParameters();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                
                return "Categorization completed successfully"; // Return status message
            },
            result -> {
                // Update UI after successful generation
                textAreaTcasCorrect.setText(result);
                textAreaTcasCorrect.setVisible(true);
                
                // Now display the generated files
                //displayGeneratedFiles();
            },
            error -> {
                textAreaTcasCorrect.setText("Error: " + error.getMessage());
                textAreaTcasCorrect.setVisible(true);
            }
        );
    }

    private void displayGeneratedFiles() {
        try {
            String content = Files.readString(Path.of("src/packages/parametres.csv"));
            textAreaTcasCorrect.setText(content);
        } catch (IOException e) {
            showAlert("Error", "Failed to display files: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private String loadCSV(String filename) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
            new FileReader("src/packages/" + filename))) {
            String line;
            while ((line = reader.readLine()) != null) content.append(line).append("\n");
        }
        return content.toString();
    }
    

    @FunctionalInterface
    private interface TaskHandler {
        String execute() throws Exception;
    }
}