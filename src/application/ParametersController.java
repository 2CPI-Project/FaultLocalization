package application;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

public class ParametersController {
    @FXML private TableView<ParameterEntry> paramTable;
    @FXML private TableColumn<ParameterEntry, Number> epColumn;
    @FXML private TableColumn<ParameterEntry, Number> efColumn;
    @FXML private TableColumn<ParameterEntry, Number> npColumn;
    @FXML private TableColumn<ParameterEntry, Number> nfColumn;

    @FXML
    private void initialize() {
        // Configure column value bindings
        epColumn.setCellValueFactory(cellData -> cellData.getValue().epProperty());
        efColumn.setCellValueFactory(cellData -> cellData.getValue().efProperty());
        npColumn.setCellValueFactory(cellData -> cellData.getValue().npProperty());
        nfColumn.setCellValueFactory(cellData -> cellData.getValue().nfProperty());
    }

    public void setParameters(ObservableList<ParameterEntry> parameters) {
        paramTable.setItems(parameters);
    }
}