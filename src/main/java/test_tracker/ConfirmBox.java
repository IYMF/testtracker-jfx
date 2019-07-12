package test_tracker;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ConfirmBox {
    static boolean answer;

    private ConfirmBox() {}

    public static boolean display(String title, String message) {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250.0D);
        Label label = new Label(message);
        Button yesButton = new Button("Yes");
        yesButton.setId("confirmYes");
        Button noButton = new Button("No");
        yesButton.setOnAction(e -> {
            answer = true;
            window.close();
        });
        noButton.setOnAction(e -> {
            answer = false;
            window.close();
        });
        HBox yesNoBtns = new HBox(10.0D);
        yesNoBtns.setAlignment(Pos.CENTER);
        yesNoBtns.setPadding(new Insets(0.0D, 0.0D, 20.0D, 0.0D));
        yesNoBtns.getChildren().addAll(new Node[]{yesButton, noButton});
        VBox layout = new VBox(10.0D);
        layout.getChildren().addAll(new Node[]{label, yesNoBtns});
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
        return answer;
    }
}
