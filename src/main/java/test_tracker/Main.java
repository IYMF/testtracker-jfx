package test_tracker;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.*;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main extends Application {

    // Overall layout
    BorderPane borderPane = new BorderPane();
    ContextMenu tableContextMenu;
    MenuItem vSphereMenuItem;

    private static final Logger LOGGER = Logger.getLogger(DatabaseUtil.class.getName());

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) throws Exception {

        // Menu items - top section
        MenuBar topMenuBar = new MenuBar();

        // File menu
        Menu fileMenu = new Menu("File");
        fileMenu.setId("fileMenu");
        MenuItem exitMenu = new MenuItem("Exit");
        exitMenu.setId("exitMenu");
        exitMenu.setOnAction(e -> {
            boolean confirmExit = ConfirmBox.display("Exit Test Tracker", "Are you sure you want to exit?");
            if (confirmExit) {
                Platform.exit();
            }
        });
        fileMenu.getItems().add(exitMenu);

        // Edit menu
        Menu editMenu = new Menu("Edit");

        // View menu
        Menu viewMenu = new Menu("View");
        MenuItem refreshMenu = new MenuItem("Refresh");
        refreshMenu.setOnAction(e -> populateCenterSection());
        viewMenu.getItems().add(refreshMenu);

        topMenuBar.getMenus().addAll(new Menu[]{fileMenu, editMenu, viewMenu});
        borderPane.setTop(topMenuBar);

        // left section
        VBox leftMenu = new VBox();
        Button vSphereBtn = new Button("vSphere");
        vSphereBtn.setOnAction(ev -> {

//            VSphereUtil.openVSphereHere("151.155.216.27");
        });
        vSphereBtn.setId("vSphereBtn");
        Button refreshBtn = new Button("Refresh");
        refreshBtn.setOnAction(ev -> {
            borderPane.setCenter(null);
            populateCenterSection();
        });
        Button cBtn = new Button("c");
        cBtn.setOnAction(ev -> borderPane.setCenter(null));
        leftMenu.getChildren().addAll(new Node[]{vSphereBtn, refreshBtn, cBtn});
//        borderPane.setLeft(leftMenu);

        tableContextMenu = new ContextMenu();

        Image openVSphere = new Image("/images/vsphere-s.png");
        ImageView openVSphereView = new ImageView(openVSphere);

        vSphereMenuItem = new MenuItem("Open with vSphere");
        vSphereMenuItem.setGraphic(openVSphereView);
        tableContextMenu.getItems().add(vSphereMenuItem);

        // Center section
        populateCenterSection();

        // Create scene and add to stage
        Scene scene = new Scene(borderPane, 1200.0D, 800.0D);
        primaryStage.setTitle("Test Tracker");
        primaryStage.getIcons().add(new Image("/images/favicon-area-chart.png"));
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Populates the center section of BorderPane with tables retrieved from the database
    private void populateCenterSection() {
        ObservableList<Table> listOfTables = FXCollections.observableArrayList();

        // Create tables
        ResultSet tables = DatabaseUtil.getTests();
        try {
            while (tables.next()) {
                listOfTables.add(new Table(
                        tables.getString("testName"),
                        tables.getInt("section"),
                        tables.getInt("product"),
                        tables.getInt("id")
                ));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "", e);
        }

        // Center section
        ObservableList<String> sections = DatabaseUtil.getSections();
        ObservableList<String> products = DatabaseUtil.getProducts();

        VBox sectionContent = new VBox();
        for (int i = 0; i < sections.size(); i++) {

            // Create section areas
            HBox sectionHbox = new HBox();
            sectionHbox.setAlignment(Pos.CENTER);
            sectionHbox.setStyle("-fx-background-color: #323435;-fx-text-fill: aliceblue; -fx-font-size: 16px; -fx-label-padding: 0; -fx-padding: 0;");
            sectionHbox.setPadding(new Insets(5.0D, 0.0D, 5.0D, 0.0D));

            Label sectionHeader = new Label(sections.get(i));
            sectionHbox.setPadding(new Insets(0));
            sectionHeader.setStyle("-fx-text-fill: aliceblue; -fx-font-weight: 700");
            sectionHbox.getChildren().add(sectionHeader);

            sectionContent.getChildren().add(sectionHbox);

            FlowPane productSection = new FlowPane();
            productSection.setPadding(new Insets(5, 5, 5, 5));

            for (int j = 0; j < products.size(); j++) {

                // Create a column for each product
                VBox productColumn = new VBox();
                productColumn.setPadding(new Insets(0, 5, 0, 5));
                productColumn.setAlignment(Pos.TOP_CENTER);

                Label colHeader = new Label(products.get(j));
                colHeader.setTextAlignment(TextAlignment.CENTER);
                colHeader.setMinWidth(productColumn.getWidth());
                colHeader.setStyle("-fx-font-size: 14px");

                productColumn.getChildren().add(colHeader);

                for (int k = 0; k < listOfTables.size(); k++) {
                    if (listOfTables.get(k).getSectionID() == i + 1 && listOfTables.get(k).getProductID() == j + 1) {
//                        LOGGER.log(Level.INFO, "Section: " + listOfTables.get(k).getSectionID() + " - Product: " + listOfTables.get(k).getProductID() + " - TestID: " + listOfTables.get(k).getTestID());

                        Region spacer = new Region();
                        spacer.setPrefHeight(10);

                        // Label for each table
                        Label testName = new Label(listOfTables.get(k).getTableTitle());
                        testName.setMaxWidth(Double.MAX_VALUE);
                        testName.setAlignment(Pos.CENTER);
                        testName.setStyle("-fx-background-color: #0073e7; -fx-text-fill: aliceblue; -fx-font-weight: 800;");
                        testName.opaqueInsetsProperty().setValue(new Insets(10, 0, 0, 0));

                        // Create a new table
                        TableView<Row> table = new TableView();
                        table.setFixedCellSize(20);
                        table.setStyle("-fx-font-size: 11;");
                        table.setMaxHeight(table.getFixedCellSize() * (listOfTables.get(k).getRows().size() + 1.20));
                        table.setEditable(true);
                        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

                        // Create columns and set values
                        TableColumn<Row, String> descriptionColumn = new TableColumn("Description");
                        descriptionColumn.setCellValueFactory(new PropertyValueFactory("description"));
                        descriptionColumn.setMaxWidth(95);

                        TableColumn<Row, String> ipColumn = new TableColumn("IP");
                        ipColumn.setCellValueFactory(new PropertyValueFactory("ip"));
                        ipColumn.setMaxWidth(45);

                        TableColumn<Row, String> esxIpColumn = new TableColumn("ESX IP");
                        esxIpColumn.setCellValueFactory(new PropertyValueFactory("esxIp"));
                        esxIpColumn.setMaxWidth(45);

                        // Give the table the columns
                        table.getColumns().addAll(new TableColumn[]{descriptionColumn, ipColumn, esxIpColumn});
                        table.setItems(listOfTables.get(k).getRows());
                        table.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
                            @Override
                            public void handle(ContextMenuEvent event) {
                                tableContextMenu.show(table, event.getScreenX(), event.getScreenY());
                                vSphereMenuItem.setOnAction(new EventHandler<ActionEvent>() {
                                    @Override
                                    public void handle(ActionEvent event) {
                                        try {
                                            VSphereUtil.openVSphereHere("151.155." + table.getSelectionModel().getSelectedItem().getEsxIp());
                                        } catch (Exception e) {
                                            LOGGER.log(Level.SEVERE, "Failed to open in vSphere: ", e);
                                        }
                                    }
                                });
                            }
                        });

                        // Add table to column
                        productColumn.getChildren().addAll(spacer, testName, table);

                        table.setMaxWidth(descriptionColumn.getMaxWidth() + ipColumn.getMaxWidth() + esxIpColumn.getMaxWidth());
                    }
                }
                productSection.getChildren().add(productColumn);
            }
            sectionContent.getChildren().add(productSection);
        }

        sectionContent.setStyle("-fx-background-color: aliceblue");
        borderPane.setCenter(sectionContent);
    }
}

