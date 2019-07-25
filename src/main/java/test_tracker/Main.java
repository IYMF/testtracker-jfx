package test_tracker;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class Main extends Application {

    // Overall Components
    private BorderPane borderPane = new BorderPane();
    private TabPane tabPane = new TabPane();
    private TestViewer testViewer = new TestViewer(borderPane, tabPane);
    private JmonViewer jmonViewer = new JmonViewer();

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) {

        // Populate sections
        MenuBar menuBar = new MenuBar(borderPane, tabPane, testViewer, jmonViewer);
        borderPane.setTop(menuBar.display());

        populateCenterSection();

        // Create scene and add to stage
        Scene scene = new Scene(borderPane, 1200.0D, 1000.0D);
        primaryStage.setTitle("Test Tracker");
        primaryStage.getIcons().add(new Image("/images/favicon-area-chart.png"));
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Populates the center section of BorderPane a TabPane containing the tests and their jmons
    private void populateCenterSection() {

        Tab testTab = new Tab("Tests");
        testTab.setContent(testViewer.display());
        testTab.setClosable(false);

        Tab jmonTab = new Tab("Jmons");
        jmonTab.setContent(jmonViewer.display());
        jmonTab.setClosable(false);

        tabPane.getTabs().addAll(testTab, jmonTab);

        borderPane.setCenter(tabPane);
    }
}