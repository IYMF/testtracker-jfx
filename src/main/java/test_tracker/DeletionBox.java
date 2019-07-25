package test_tracker;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

class DeletionBox {

    private DeletionBox() {}

    static void display(String title, String message, ObservableList<String> items, ObservableList<Integer> itemIDs) {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250.0D);
        Label label = new Label(message);

        ListView<String> itemsListView = new ListView<>(items);
        itemsListView.setPrefHeight(20 * (items.size() + 1.2));
        itemsListView.setPrefWidth(50);

        Button selectBtn = new Button("Confirm");
        selectBtn.setOnAction(e -> {
            if (itemsListView.getSelectionModel().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information");
                alert.setHeaderText("Select an item first");
                alert.setContentText("You must select an item before you can delete it.");
                alert.showAndWait();
                System.out.println("empty");
            } else {
                int itemID = itemIDs.get(itemsListView.getSelectionModel().getSelectedIndex());

                if (title.contains("Product")) {
                    DatabaseUtil.deleteProduct(itemID);
                } else {
                    DatabaseUtil.deleteSection(itemID);
                }

                window.close();
            }
        });

        Button cancelBtn = new Button("Cancel");
        cancelBtn.setOnAction(e -> window.close());

        HBox btnsHbox = new HBox(10.0D);
        btnsHbox.setAlignment(Pos.CENTER);
        btnsHbox.setPadding(new Insets(0.0D, 0.0D, 20.0D, 0.0D));
        btnsHbox.getChildren().addAll(selectBtn, cancelBtn);

        VBox layout = new VBox(10.0D);
        layout.getChildren().addAll(label, itemsListView, btnsHbox);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }
}
