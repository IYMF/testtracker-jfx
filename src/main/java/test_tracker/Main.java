package test_tracker;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
import java.util.regex.Pattern;

public class Main extends Application {

    // Overall layout
    private BorderPane borderPane = new BorderPane();

    private static final Logger LOGGER = Logger.getLogger(DatabaseUtil.class.getName());

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) {

        // Populate sections
        populateMenuBar();
//        populateLeftSection();
        populateCenterSection();

        // Create scene and add to stage
        Scene scene = new Scene(borderPane, 1200.0D, 800.0D);
        primaryStage.setTitle("Test Tracker");
        primaryStage.getIcons().add(new Image("/images/favicon-area-chart.png"));
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void populateMenuBar() {
        HBox topMenuHBox = new HBox();

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
        MenuItem clearMenu = new MenuItem("Empty");
        clearMenu.setOnAction(e -> borderPane.setCenter(null));
        viewMenu.getItems().addAll(clearMenu, refreshMenu);

        // Refresh Icon
        Button refreshBtn = new Button("Refresh");
        refreshBtn.setOnAction(e -> populateCenterSection());

        topMenuBar.getMenus().addAll(fileMenu, editMenu, viewMenu);
        topMenuHBox.getChildren().addAll(topMenuBar, refreshBtn);
        topMenuHBox.setHgrow(topMenuBar, Priority.ALWAYS);

        borderPane.setTop(topMenuHBox);
    }

    private void populateLeftSection() {
        // left section
        VBox leftMenu = new VBox();

        Button vSphereBtn = new Button("vSphere");
        vSphereBtn.setOnAction(ev -> VSphereUtil.openVSphereHere("151.155.216.27"));
        vSphereBtn.setId("vSphereBtn");

        Button refreshBtn = new Button("Refresh");
        refreshBtn.setOnAction(ev -> {
            borderPane.setCenter(null);
            populateCenterSection();
        });
        Button cBtn = new Button("c");
        cBtn.setOnAction(ev -> borderPane.setCenter(null));
        leftMenu.getChildren().addAll(vSphereBtn, refreshBtn, cBtn);
        borderPane.setLeft(leftMenu);
    }

    // Populates the center section of BorderPane with tables retrieved from the database
    private void populateCenterSection() {
        ObservableList<Table> listOfTables = FXCollections.observableArrayList();

        // Create table objects
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

        // Context menu for table rows
        ContextMenu tableContextMenu = new ContextMenu();

        Image openVSphere = new Image("/images/vsphereIcon.png");
        ImageView openVSphereView = new ImageView(openVSphere);
        MenuItem vSphereMenuItem = new MenuItem("Open vSphere");
        vSphereMenuItem.setGraphic(openVSphereView);

        Image openJmon = new Image("/images/favicon-area-chart.png");
        ImageView openJmonView = new ImageView(openJmon);
        MenuItem jmonMenuItem = new MenuItem("Open jmon");
        jmonMenuItem.setGraphic(openJmonView);

        tableContextMenu.getItems().addAll(jmonMenuItem, vSphereMenuItem);

        VBox sectionContent = new VBox();

        // Create section areas
        for (int i = 0; i < sections.size(); i++) {
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

            // Create a column for each product
            for (int j = 0; j < products.size(); j++) {
                VBox productColumn = new VBox();
                productColumn.setPadding(new Insets(0, 5, 0, 5));
                productColumn.setAlignment(Pos.TOP_CENTER);

                Label colHeader = new Label(products.get(j));
                colHeader.setTextAlignment(TextAlignment.CENTER);
                colHeader.setMinWidth(productColumn.getWidth());
                colHeader.setStyle("-fx-font-size: 14px");

                productColumn.getChildren().add(colHeader);

                // Create all tables for each product
                for (int k = 0; k < listOfTables.size(); k++) {
                    if (listOfTables.get(k).getSectionID() == i + 1 && listOfTables.get(k).getProductID() == j + 1) {

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
                        table.getColumns().addAll(descriptionColumn, ipColumn, esxIpColumn);
                        table.setItems(listOfTables.get(k).getRows());
                        table.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
                            @Override
                            public void handle(ContextMenuEvent event) {
                                tableContextMenu.show(table, event.getScreenX(), event.getScreenY());
                                vSphereMenuItem.setOnAction(ev -> {
                                    try {
                                        VSphereUtil.openVSphereHere("151.155." + table.getSelectionModel().getSelectedItem().getEsxIp());
                                    } catch (Exception e) {
                                        LOGGER.log(Level.SEVERE, "Failed to open in vSphere: ", e);
                                    }
                                });

                                jmonMenuItem.setOnAction(ev ->
                                    table.getItems().forEach(row -> {
                                        if (Pattern.compile(Pattern.quote("jmon"), Pattern.CASE_INSENSITIVE).matcher(row.getDescription()).find()) {
                                            try {
                                                getHostServices().showDocument("http://151.155." + row.getIp());
                                            } catch(Exception e) {
                                                LOGGER.log(Level.SEVERE, "Error opening link in browser: ", e);
                                            }
                                        }
                                    })
                                );
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

        ScrollPane centerSection = new ScrollPane();
        centerSection.setFitToWidth(true);
        centerSection.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        centerSection.setContent(sectionContent);

        borderPane.setCenter(centerSection);
    }
}

