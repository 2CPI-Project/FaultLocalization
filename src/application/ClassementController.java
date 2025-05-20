package application;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import java.util.Map;

public class ClassementController {
    @FXML private TabPane tabPane;

    public void initializeTabs(Map<String, String> classements) {
        tabPane.getTabs().clear();
        
        for (Map.Entry<String, String> entry : classements.entrySet()) {
            Tab tab = new Tab(entry.getKey());
            VBox content = new VBox(10);
            content.setPadding(new Insets(15));
            
            Label header = new Label("Ranking for " + entry.getKey());
            header.getStyleClass().add("header-label");
            
            ListView<String> listView = new ListView<>();
            listView.getItems().addAll(entry.getValue().split("\n"));
            listView.setCellFactory(lv -> new StyledListCell());
            
            content.getChildren().addAll(header, listView);
            tab.setContent(content);
            tabPane.getTabs().add(tab);
        }
        
        if (tabPane.getTabs().isEmpty()) {
            Tab emptyTab = new Tab("No Data");
            emptyTab.setContent(new Label("No classement files found"));
            tabPane.getTabs().add(emptyTab);
        }
    }

    // Custom cell factory for styling
    private static class StyledListCell extends ListCell<String> {
        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setText(null);
                setGraphic(null);
            } else {
                setText(item);
                getStyleClass().add("ranking-cell");
            }
        }
    }
}