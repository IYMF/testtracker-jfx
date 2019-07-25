package test_tracker;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.logging.Level;
import java.util.logging.Logger;

public class MenuBar extends Application {

    private final BorderPane borderPane;
    private final TabPane tabPane;
    private final TestViewer testViewer;
    private final JmonViewer jmonViewer;

    private static final Logger LOGGER = Logger.getLogger(DatabaseUtil.class.getName());

    MenuBar(BorderPane borderPane, TabPane tabPane, TestViewer testViewer, JmonViewer jmonViewer) {
        this.borderPane = borderPane;
        this.tabPane = tabPane;
        this.testViewer = testViewer;
        this.jmonViewer = jmonViewer;
    }

    @Override
    public void start(Stage primaryStage) {

    }

    public HBox display() {

        HBox topMenuHBox = new HBox();
//        TestViewer testViewer = new TestViewer(borderPane);
//        JmonViewer jmonViewer = new JmonViewer();

        // Menu items - top section
        javafx.scene.control.MenuBar topMenuBar = new javafx.scene.control.MenuBar();

        // File menu
        Menu fileMenu = new Menu("File");
        fileMenu.setId("fileMenu");

        Menu newMenu = new Menu("New");
        MenuItem newSectionMenuItem = new MenuItem("Section");
        newSectionMenuItem.setOnAction(newSectionEvent -> {
            System.out.println("New section");

            HBox editHBox = new HBox();
            editHBox.setStyle("-fx-background-color: aliceblue; -fx-effect: dropshadow(gaussian, black, 30, 0, 0, 1)");

            HBox newSectionHBox = new HBox();
            newSectionHBox.setAlignment(Pos.CENTER_LEFT);
            Label newSectionLabel = new Label("Section name: ");
            TextField newSectionTextField = new TextField();
            newSectionTextField.setPrefWidth(250);
            newSectionHBox.getChildren().addAll(newSectionLabel, newSectionTextField);

            HBox detailsHBox = new HBox();
            detailsHBox.getChildren().addAll(newSectionHBox);

            Button submitBtn = new Button("Submit");
            submitBtn.setStyle("-fx-background-color: #0073e7; -fx-text-fill: aliceblue;");
            submitBtn.setAlignment(Pos.CENTER_RIGHT);
            submitBtn.setOnAction(submitEvent -> {
                try {
                    DatabaseUtil.addSection(newSectionTextField.getText());
                    refreshContents();
                } catch (Exception ex) {
                    LOGGER.log(Level.SEVERE, "Unable to create a new section: ", ex);
                }
                borderPane.setBottom(null);
            });

            Button cancelBtn = new Button("Cancel");
            cancelBtn.setStyle("-fx-background-color: #0073e7; -fx-text-fill: aliceblue;");
            cancelBtn.setAlignment(Pos.CENTER_RIGHT);
            cancelBtn.setOnAction(ex -> borderPane.setBottom(null));

            Region btnSpacer = new Region();
            btnSpacer.setMinWidth(5);

            HBox btnHBox = new HBox();
            btnHBox.getChildren().addAll(submitBtn, btnSpacer, cancelBtn);

            editHBox.getChildren().addAll(detailsHBox, btnHBox);
            editHBox.setPadding(new Insets(5));
            editHBox.setHgrow(detailsHBox, Priority.ALWAYS);
            editHBox.setAlignment(Pos.CENTER_LEFT);
            borderPane.setBottom(editHBox);
        });

        MenuItem newProductMenuItem = new MenuItem("Product");
        newProductMenuItem.setOnAction(newProductEvent -> {
            System.out.println("New Product");

            HBox editHBox = new HBox();
            editHBox.setStyle("-fx-background-color: aliceblue; -fx-effect: dropshadow(gaussian, black, 30, 0, 0, 1)");

            HBox newProductHBox = new HBox();
            newProductHBox.setAlignment(Pos.CENTER_LEFT);
            Label newProductLabel = new Label("Product name: ");
            TextField newProductTextField = new TextField();
            newProductTextField.setPrefWidth(250);
            newProductHBox.getChildren().addAll(newProductLabel, newProductTextField);

            HBox detailsHBox = new HBox();
            detailsHBox.getChildren().addAll(newProductHBox);

            Button submitBtn = new Button("Submit");
            submitBtn.setStyle("-fx-background-color: #0073e7; -fx-text-fill: aliceblue;");
            submitBtn.setAlignment(Pos.CENTER_RIGHT);
            submitBtn.setOnAction(submitEvent -> {
                try {
                    DatabaseUtil.addProduct(newProductTextField.getText());
                    refreshContents();
                } catch (Exception ex) {
                    LOGGER.log(Level.SEVERE, "Unable to create a new product: ", ex);
                }
                borderPane.setBottom(null);
            });

            Button cancelBtn = new Button("Cancel");
            cancelBtn.setStyle("-fx-background-color: #0073e7; -fx-text-fill: aliceblue;");
            cancelBtn.setAlignment(Pos.CENTER_RIGHT);
            cancelBtn.setOnAction(ex -> borderPane.setBottom(null));

            Region btnSpacer = new Region();
            btnSpacer.setMinWidth(5);

            HBox btnHBox = new HBox();
            btnHBox.getChildren().addAll(submitBtn, btnSpacer, cancelBtn);

            editHBox.getChildren().addAll(detailsHBox, btnHBox);
            editHBox.setPadding(new Insets(5));
            editHBox.setHgrow(detailsHBox, Priority.ALWAYS);
            editHBox.setAlignment(Pos.CENTER_LEFT);
            borderPane.setBottom(editHBox);
        });

        newMenu.getItems().addAll(newProductMenuItem, newSectionMenuItem);

        MenuItem exitMenu = new MenuItem("Exit");
        exitMenu.setId("exitMenu");
        exitMenu.setOnAction(e -> {
            boolean confirmExit = ConfirmBox.display("Exit Test Tracker", "Are you sure you want to exit?");
            if (confirmExit) {
                Platform.exit();
            }
        });

        fileMenu.getItems().addAll(newMenu, exitMenu);

        // Edit menu
        Menu editMenu = new Menu("Edit");
        Menu deleteMenuItem = new Menu("Delete");
        MenuItem deleteProductMenuItem = new MenuItem("Product");
        deleteProductMenuItem.setOnAction(deleteProductEvent -> {
            DeletionBox.display("Delete Product?", "Are you sure you want to delete this product?", DatabaseUtil.getProducts(), DatabaseUtil.getProductIDs());
            refreshContents();
        });
        MenuItem deleteSectionMenuItem = new MenuItem("Section");
        deleteSectionMenuItem.setOnAction(deleteSectionEvent -> {
            DeletionBox.display("Delete Section?", "Are you sure you want to delete this Section?", DatabaseUtil.getSections(), DatabaseUtil.getSectionIDs());
            refreshContents();
        });

        deleteMenuItem.getItems().addAll(deleteProductMenuItem, deleteSectionMenuItem);
        editMenu.getItems().addAll(deleteMenuItem);

        // View menu
        Menu viewMenu = new Menu("View");
        MenuItem testsMenuItem = new MenuItem("Tests");
        testsMenuItem.setOnAction(showTestsEvent -> tabPane.getSelectionModel().select(0));
        MenuItem jmonsMenuItem = new MenuItem("Jmons");
        jmonsMenuItem.setOnAction(jmonsEvent -> tabPane.getSelectionModel().select(1));
        viewMenu.getItems().addAll(testsMenuItem, jmonsMenuItem);

        // Refresh button
        Button refreshBtn = new Button("Refresh");
        refreshBtn.setOnAction(e -> refreshContents());
        topMenuBar.getMenus().addAll(fileMenu, editMenu, viewMenu);
        topMenuHBox.getChildren().addAll(topMenuBar, refreshBtn);
        topMenuHBox.setHgrow(topMenuBar, Priority.ALWAYS);

        return topMenuHBox;
    }

    private void refreshContents() {
        int tabIndex = tabPane.getSelectionModel().getSelectedIndex();

        if (tabIndex == 0) {
            tabPane.getTabs().get(0).setContent(testViewer.display());
        } else if (tabIndex == 1) {
            tabPane.getTabs().get(1).setContent(jmonViewer.display());
        }
    }
}
