package application;

import javafx.application.Platform;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class LineNumberHelper {
    private final ListView<Integer> lineNumbers;
    private final TextArea textArea;
    private double lineHeight = 0;

    public LineNumberHelper(TextArea textArea) {
        this.textArea = textArea;
        this.lineNumbers = createLineNumberView();
        initializeComponent();
    }

    private ListView<Integer> createLineNumberView() {
        ListView<Integer> lv = new ListView<>();
        lv.setPrefWidth(40);
        lv.getStyleClass().add("line-number-list");
        lv.setCellFactory(param -> new LineNumberCell());
        return lv;
    }

    private void initializeComponent() {
        calculateExactLineHeight();
        setupBindings();
        Platform.runLater(this::initialSync);
    }

    private void calculateExactLineHeight() {
        // Method 1: Use font metrics with padding compensation
        Text helper = new Text("X");
        helper.setFont(textArea.getFont());
        //lineHeight = helper.getLayoutBounds().getHeight() + 2; // Add line spacing
        
        // Method 2: Fallback to empirical calculation
        if (lineHeight <= 0) {
            lineHeight = textArea.getFont().getSize() * 1.41;
        }
        
        lineNumbers.setFixedCellSize(lineHeight);
    }

    private void setupBindings() {
        textArea.textProperty().addListener((obs, old, val) -> 
            Platform.runLater(this::updateLineNumbers)
        );
        
        textArea.scrollTopProperty().addListener((obs, old, val) -> 
            Platform.runLater(this::syncScrollPosition)
        );
        
        textArea.fontProperty().addListener((obs, old, val) -> 
            calculateExactLineHeight()
        );
    }

    private void initialSync() {
        updateLineNumbers();
        syncScrollPosition();
    }

    private void syncScrollPosition() {
        if (lineHeight <= 0) return;
        
        // Account for TextArea's internal padding
        double scrollTop = textArea.getScrollTop() - textArea.getInsets().getTop();
        int targetLine = (int) Math.floor(scrollTop / lineHeight);
        lineNumbers.scrollTo(targetLine);
    }

    private void updateLineNumbers() {
        ObservableList<Integer> lines = FXCollections.observableArrayList();
        int count = textArea.getParagraphs().size();
        for (int i = 1; i <= count; i++) lines.add(i);
        lineNumbers.setItems(lines);
    }

    public ListView<Integer> getLineNumbers() {
        return lineNumbers;
    }

    private static class LineNumberCell extends ListCell<Integer> {
        @Override
        protected void updateItem(Integer item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setText(null);
            } else {
                setText(item.toString());
                setStyle("-fx-alignment: center-right; -fx-padding: 0 5 0 0;");
            }
        }
    }
    public void show() {
        lineNumbers.setVisible(true);
    }

    public void hide() {
        lineNumbers.setVisible(false);
    }
}