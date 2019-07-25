package test_tracker;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.*;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class TestViewer extends Application {

    private static final Logger LOGGER = Logger.getLogger(DatabaseUtil.class.getName());

    private BorderPane borderPane;
    private TabPane tabPane;

    TestViewer(BorderPane borderPane, TabPane tabPane) {
        this.borderPane = borderPane;
        this.tabPane = tabPane;
    }

    @Override
    public void start(Stage primaryStage) {
        display();
    }

    ScrollPane display() {

        // Create table objects
        ObservableList<Table> listOfTables = DatabaseUtil.getTests();

        // Center section
        ObservableList<Integer> sectionIDs = DatabaseUtil.getSectionIDs();
        ObservableList<String> sections = DatabaseUtil.getSections();
        ObservableList<Integer> productIDs = DatabaseUtil.getProductIDs();
        ObservableList<String> products = DatabaseUtil.getProducts();

        ContextMenu productContextMenu = new ContextMenu();
        MenuItem addTableMenuItem = new MenuItem("Add table");
        addTableMenuItem.setGraphic(new ImageView(new Image("/images/plus.png")));

        productContextMenu.getItems().addAll(addTableMenuItem);

        // Context menu for table rows
        ContextMenu tableContextMenu = new ContextMenu();

        Menu vSphereMenu = new Menu("vSphere");
        vSphereMenu.setGraphic(new ImageView(new Image("/images/vsphereIcon.png")));
        MenuItem vSphereThickClientMenuItem = new MenuItem("Thick Client");
        vSphereThickClientMenuItem.setGraphic(new ImageView(new Image("/images/vsphereIcon.png")));
        MenuItem vSphereWebClientMenuItem = new MenuItem("Web Client");
        vSphereWebClientMenuItem.setGraphic(new ImageView(new Image("/images/vsphereIcon.png")));
        vSphereMenu.getItems().addAll(vSphereThickClientMenuItem, vSphereWebClientMenuItem);

        MenuItem jmonMenuItem = new MenuItem("Open jmon");
        jmonMenuItem.setGraphic(new ImageView(new Image("/images/favicon-area-chart.png")));

        MenuItem addRowMenuItem = new MenuItem("Add row");
        addRowMenuItem.setGraphic(new ImageView(new Image("/images/plus.png")));

        MenuItem editRowMenuItem = new MenuItem("Edit row");
        editRowMenuItem.setGraphic(new ImageView(new Image("/images/edit.png")));

        MenuItem deleteRowMenuItem = new MenuItem("Delete row");
        deleteRowMenuItem.setGraphic(new ImageView(new Image("/images/minus.png")));

        MenuItem toggleNotesMenuItem = new MenuItem("Toggle notes");
        toggleNotesMenuItem.setGraphic(new ImageView(new Image("/images/toggle.png")));

        SeparatorMenuItem separator = new SeparatorMenuItem();
        SeparatorMenuItem separator2 = new SeparatorMenuItem();

        tableContextMenu.getItems().addAll(editRowMenuItem, addRowMenuItem, deleteRowMenuItem, separator, toggleNotesMenuItem, separator2, jmonMenuItem, vSphereMenu);

        VBox sectionContent = new VBox();

        // Create section areas
        for (int i = 0; i < sections.size(); i++) {
            HBox sectionHbox = new HBox();
            sectionHbox.setAlignment(Pos.CENTER);
            sectionHbox.setStyle("-fx-background-color: #323435;-fx-text-fill: aliceblue; -fx-font-size: 16px; -fx-label-padding: 0; -fx-padding: 0;");
            sectionHbox.setPadding(new Insets(5.0D, 0.0D, 0, 0.0D));

            Label sectionHeader = new Label(sections.get(i));
            sectionHeader.setId(Integer.toString(sectionIDs.get(i)));
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
                colHeader.setId(Integer.toString(productIDs.get(j)));
                colHeader.setTextAlignment(TextAlignment.CENTER);
                colHeader.setMinWidth(productColumn.getWidth());
                colHeader.setStyle("-fx-font-size: 14px");
                colHeader.setOnContextMenuRequested(productContextMenuEvent -> {
                    productContextMenu.show(colHeader, productContextMenuEvent.getScreenX(), productContextMenuEvent.getScreenY());

                    addTableMenuItem.setOnAction(addTableEvent -> {
                        HBox editHBox = new HBox();
                        editHBox.setStyle("-fx-background-color: aliceblue; -fx-effect: dropshadow(gaussian, black, 30, 0, 0, 1)");

                        HBox addTableHBox = new HBox();
                        addTableHBox.setAlignment(Pos.CENTER_LEFT);
                        Label addTableLabel = new Label("Test name: ");
                        TextField addTableTextField = new TextField();
                        addTableTextField.setPrefWidth(250);
                        addTableHBox.getChildren().addAll(addTableLabel, addTableTextField);

                        HBox detailsHBox = new HBox();
                        detailsHBox.getChildren().addAll(addTableHBox);

                        Button submitBtn = new Button("Submit");
                        submitBtn.setStyle("-fx-background-color: #0073e7; -fx-text-fill: aliceblue;");
                        submitBtn.setAlignment(Pos.CENTER_RIGHT);
                        submitBtn.setOnAction(submitEvent -> {
                            try {
                                DatabaseUtil.addTest(
                                        addTableTextField.getText(),
                                        Integer.parseInt(sectionHeader.getId()),
                                        Integer.parseInt(colHeader.getId())
                                );

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
                });

                productColumn.getChildren().add(colHeader);

                // Create all tables for each product
                for (int k = 0; k < listOfTables.size(); k++) {
                    if (listOfTables.get(k).getSectionID() == sectionIDs.get(i) && listOfTables.get(k).getProductID() == productIDs.get(j)) {

                        Region spacer = new Region();
                        spacer.setPrefHeight(5);

                        // Label for each table
                        Label testName = new Label(listOfTables.get(k).getTableTitle());
                        testName.setId(Integer.toString(listOfTables.get(k).getTableId()));

                        ContextMenu labelContextMenu = new ContextMenu();
                        MenuItem labelEditMenuItem = new MenuItem("Edit test name");
                        labelEditMenuItem.setGraphic(new ImageView(new Image("/images/edit.png")));

                        SeparatorMenuItem testSeparator = new SeparatorMenuItem();

                        MenuItem deleteTestMenuItem = new MenuItem("Delete test");
                        deleteTestMenuItem.setGraphic(new ImageView(new Image("/images/minus.png")));

                        labelContextMenu.getItems().addAll(labelEditMenuItem, testSeparator, deleteTestMenuItem);

                        testName.setOnContextMenuRequested(e -> {
                            labelContextMenu.show(testName, e.getScreenX(), e.getScreenY());
                            labelEditMenuItem.setOnAction(labelEditevent -> {

                                HBox editHBox = new HBox();
                                editHBox.setStyle("-fx-background-color: aliceblue; -fx-effect: dropshadow(gaussian, black, 30, 0, 0, 1)");

                                HBox tableTitleHBox = new HBox();
                                tableTitleHBox.setAlignment(Pos.CENTER_LEFT);
                                Label tableTitleLabel = new Label("Table Title: ");
                                TextField tableTitleTextField = new TextField(testName.getText());
                                tableTitleTextField.setPrefWidth(250);
                                tableTitleHBox.getChildren().addAll(tableTitleLabel, tableTitleTextField);

                                HBox detailsHBox = new HBox();
                                detailsHBox.getChildren().addAll(tableTitleHBox);

                                Button submitBtn = new Button("Submit");
                                submitBtn.setStyle("-fx-background-color: #0073e7; -fx-text-fill: aliceblue;");
                                submitBtn.setAlignment(Pos.CENTER_RIGHT);
                                submitBtn.setOnAction(submitEvent -> {
                                    String[] splitId = testName.getId().split("=");
                                    try {
                                        DatabaseUtil.updateTableTitle(
                                                Integer.parseInt(splitId[0]),
                                                tableTitleTextField.getText()
                                        );

                                    } catch (Exception ex) {
                                        LOGGER.log(Level.SEVERE, "Unable to update table title: ", e);
                                    }

                                    testName.setText(tableTitleTextField.getText());
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

                            deleteTestMenuItem.setOnAction(deleteTestEvent -> {
                                Boolean confirmDeleteTest = ConfirmBox.display("Delete test", "Are you sure you want to delete this test?");

                                if (confirmDeleteTest) {
                                    DatabaseUtil.deleteTest(Integer.parseInt(testName.getId()));
                                    refreshContents();
                                }
                            });
                        });
                        testName.setMaxWidth(Double.MAX_VALUE);
                        testName.setAlignment(Pos.CENTER);
                        testName.setStyle("-fx-background-color: #0073e7; -fx-text-fill: aliceblue; -fx-font-weight: 800;");

                        // Create a new table
                        TableView<Row> table = new TableView();
                        table.setId(Integer.toString(listOfTables.get(k).getTableId()) + "," + Integer.toString(listOfTables.get(k).showNotes()));
                        table.setFixedCellSize(20);
                        table.setMaxHeight(table.getFixedCellSize() * (listOfTables.get(k).getRows().size() + 1.2));
                        table.setEditable(true);
                        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

                        // Create columns and set values
                        TableColumn<Row, String> descriptionColumn = new TableColumn("Description");
//                        descriptionColumn.setResizable(false);
                        descriptionColumn.setCellFactory(TextFieldTableCell.forTableColumn());
                        descriptionColumn.setCellValueFactory(new PropertyValueFactory("description"));
                        descriptionColumn.setMaxWidth(95);
                        descriptionColumn.setOnEditCommit(ev -> {
                            DatabaseUtil.updateCell(
                                    ev.getTableView().getItems().get(ev.getTablePosition().getRow()).getID(), // row id
                                    columnName(ev.getTableView().getVisibleLeafIndex(ev.getTableColumn())), // column name
                                    ev.getNewValue() // changed value
                            );
                        });

                        TableColumn<Row, String> ipColumn = new TableColumn("IP");
//                        ipColumn.setResizable(false);
                        ipColumn.setCellFactory(TextFieldTableCell.forTableColumn());
                        ipColumn.setCellValueFactory(new PropertyValueFactory("ip"));
                        ipColumn.setStyle("-fx-alignment: CENTER");
                        ipColumn.setMaxWidth(60);
                        ipColumn.setOnEditCommit(ev -> {
                            DatabaseUtil.updateCell(
                                    ev.getTableView().getItems().get(ev.getTablePosition().getRow()).getID(), // row id
                                    columnName(ev.getTableView().getVisibleLeafIndex(ev.getTableColumn())), // column name
                                    ev.getNewValue() // changed value
                            );
                        });

                        TableColumn<Row, String> esxIpColumn = new TableColumn("ESX IP");
//                        esxIpColumn.setResizable(false);
                        esxIpColumn.setCellFactory(TextFieldTableCell.forTableColumn());
                        esxIpColumn.setCellValueFactory(new PropertyValueFactory("esxIp"));
                        esxIpColumn.setStyle("-fx-alignment: CENTER");
                        esxIpColumn.setMaxWidth(60);
                        esxIpColumn.setOnEditCommit(ev -> {
                            DatabaseUtil.updateCell(
                                    ev.getTableView().getItems().get(ev.getTablePosition().getRow()).getID(), // row id
                                    columnName(ev.getTableView().getVisibleLeafIndex(ev.getTableColumn())), // column name
                                    ev.getNewValue() // changed value
                            );
                        });

                        // Give the table the columns
                        table.getColumns().addAll(descriptionColumn, ipColumn, esxIpColumn);
                        table.setItems(listOfTables.get(k).getRows());
                        table.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
                            @Override
                            public void handle(ContextMenuEvent event) {
                                tableContextMenu.show(table, event.getScreenX(), event.getScreenY());
                                vSphereThickClientMenuItem.setOnAction(ev -> {
                                    try {
                                        VSphereUtil.openVSphereHere("151.155." + table.getSelectionModel().getSelectedItem().getEsxIp());
                                    } catch (Exception e) {
                                        LOGGER.log(Level.SEVERE, "Failed to open in vSphere: ", e);
                                    }
                                });

                                vSphereWebClientMenuItem.setOnAction(webClientEvent -> {
                                    getHostServices().showDocument("https://151.155." + table.getSelectionModel().getSelectedItem().getEsxIp() + "/ui/#/login");
                                });

                                jmonMenuItem.setOnAction(ev ->
                                        table.getItems().forEach(row -> {
                                            if (Pattern.compile(Pattern.quote("jmon"), Pattern.CASE_INSENSITIVE).matcher(row.getDescription()).find()) {
                                                try {
                                                    getHostServices().showDocument("http://151.155." + row.getIp());
                                                } catch (Exception e) {
                                                    LOGGER.log(Level.SEVERE, "Error opening link in browser: ", e);
                                                }
                                            }
                                        })
                                );

                                editRowMenuItem.setOnAction(ev -> {
                                    System.out.println(table.getSelectionModel().getSelectedItem().getID());
                                    HBox editHBox = new HBox();
                                    editHBox.setStyle("-fx-background-color: aliceblue; -fx-effect: dropshadow(gaussian, black, 30, 0, 0, 1)");

                                    Region spacer1 = new Region();
                                    spacer1.setMinWidth(20);
                                    Region spacer2 = new Region();
                                    spacer2.setMinWidth(20);

                                    HBox descHBox = new HBox();
                                    descHBox.setAlignment(Pos.CENTER_LEFT);
                                    Label descLabel = new Label("Description: ");
                                    TextField descTextField = new TextField(table.getSelectionModel().getSelectedItem().getDescription());
                                    descTextField.setMaxWidth(130);
                                    descHBox.getChildren().addAll(descLabel, descTextField);

                                    HBox ipHBox = new HBox();
                                    ipHBox.setAlignment(Pos.CENTER_LEFT);
                                    Label ipLabel = new Label("IP: ");
                                    TextField ipTextField = new TextField(table.getSelectionModel().getSelectedItem().getIp());
                                    ipTextField.setMaxWidth(60);
                                    ipHBox.getChildren().addAll(ipLabel, ipTextField);

                                    HBox esxIpHBox = new HBox();
                                    esxIpHBox.setAlignment(Pos.CENTER_LEFT);
                                    Label esxIpLabel = new Label("ESX IP: ");
                                    TextField esxIpTextField = new TextField(table.getSelectionModel().getSelectedItem().getEsxIp());
                                    esxIpTextField.setMaxWidth(60);
                                    esxIpHBox.getChildren().addAll(esxIpLabel, esxIpTextField);

                                    HBox detailsHBox = new HBox();
                                    detailsHBox.getChildren().addAll(descHBox, spacer1, ipHBox, spacer2, esxIpHBox);

                                    Button submitBtn = new Button("Submit");
                                    submitBtn.setStyle("-fx-background-color: #0073e7; -fx-text-fill: aliceblue;");
                                    submitBtn.setAlignment(Pos.CENTER_RIGHT);
                                    submitBtn.setOnAction(submitEvent -> {
                                        try {
                                            DatabaseUtil.updateRow(
                                                    table.getSelectionModel().getSelectedItem().getID(),
                                                    descTextField.getText(),
                                                    ipTextField.getText(),
                                                    esxIpTextField.getText()
                                            );

                                        } catch (Exception e) {
                                            LOGGER.log(Level.SEVERE, "Unable to update row: ", e);
                                        }

                                        borderPane.setBottom(null);

                                        // REFACTOR LATER
                                        refreshContents();
                                    });

                                    Button cancelBtn = new Button("Cancel");
                                    cancelBtn.setStyle("-fx-background-color: #0073e7; -fx-text-fill: aliceblue;");
                                    cancelBtn.setAlignment(Pos.CENTER_RIGHT);
                                    cancelBtn.setOnAction(e -> borderPane.setBottom(null));

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

                                addRowMenuItem.setOnAction(addRowEvent -> {
                                    HBox addHBox = new HBox();
                                    addHBox.setStyle("-fx-background-color: aliceblue; -fx-effect: dropshadow(gaussian, black, 30, 0, 0, 1)");

                                    Region spacer1 = new Region();
                                    spacer1.setMinWidth(20);
                                    Region spacer2 = new Region();
                                    spacer2.setMinWidth(20);

                                    HBox descHBox = new HBox();
                                    descHBox.setAlignment(Pos.CENTER_LEFT);
                                    Label descLabel = new Label("Description: ");
                                    TextField descTextField = new TextField();
                                    descTextField.setMaxWidth(130);
                                    descHBox.getChildren().addAll(descLabel, descTextField);

                                    HBox ipHBox = new HBox();
                                    ipHBox.setAlignment(Pos.CENTER_LEFT);
                                    Label ipLabel = new Label("IP: ");
                                    TextField ipTextField = new TextField();
                                    ipTextField.setMaxWidth(60);
                                    ipHBox.getChildren().addAll(ipLabel, ipTextField);

                                    HBox esxIpHBox = new HBox();
                                    esxIpHBox.setAlignment(Pos.CENTER_LEFT);
                                    Label esxIpLabel = new Label("ESX IP: ");
                                    TextField esxIpTextField = new TextField();
                                    esxIpTextField.setMaxWidth(60);
                                    esxIpHBox.getChildren().addAll(esxIpLabel, esxIpTextField);

                                    HBox detailsHBox = new HBox();
                                    detailsHBox.getChildren().addAll(descHBox, spacer1, ipHBox, spacer2, esxIpHBox);

                                    Button submitBtn = new Button("Submit");
                                    submitBtn.setStyle("-fx-background-color: #0073e7; -fx-text-fill: aliceblue;");
                                    submitBtn.setAlignment(Pos.CENTER_RIGHT);
                                    submitBtn.setOnAction(submitEvent -> {
                                        try {
                                            DatabaseUtil.addRow(
                                                    descTextField.getText(),
                                                    ipTextField.getText(),
                                                    esxIpTextField.getText(),
                                                    Integer.parseInt(testName.getId())
//                                                    table.getSelectionModel().getSelectedItem().getTestID()
                                            );
                                            System.out.println("add row");
                                        } catch (Exception e) {
                                            LOGGER.log(Level.SEVERE, "Unable to update row: ", e);
                                        }

                                        borderPane.setBottom(null);

                                        // REFACTOR LATER
                                        refreshContents();
                                    });

                                    Button cancelBtn = new Button("Cancel");
                                    cancelBtn.setStyle("-fx-background-color: #0073e7; -fx-text-fill: aliceblue;");
                                    cancelBtn.setAlignment(Pos.CENTER_RIGHT);
                                    cancelBtn.setOnAction(e -> borderPane.setBottom(null));

                                    Region btnSpacer = new Region();
                                    btnSpacer.setMinWidth(5);

                                    HBox btnHBox = new HBox();
                                    btnHBox.getChildren().addAll(submitBtn, btnSpacer, cancelBtn);

                                    addHBox.getChildren().addAll(detailsHBox, btnHBox);
                                    addHBox.setPadding(new Insets(5));
                                    addHBox.setHgrow(detailsHBox, Priority.ALWAYS);
                                    addHBox.setAlignment(Pos.CENTER_LEFT);
                                    borderPane.setBottom(addHBox);
                                });

                                deleteRowMenuItem.setOnAction(deleteEvent -> {
                                    Row row = table.getSelectionModel().getSelectedItem();

                                    Boolean confirmDelete = ConfirmBox.display("Delete row", "Are you sure you want to delete this row?");

                                    if (confirmDelete) {
                                        DatabaseUtil.deleteRow(row.getID());
                                        refreshContents();
                                    }
                                });

                                toggleNotesMenuItem.setOnAction(toggleNotesEvent -> {
                                    String[] splitId = table.getId().split(",");
                                    int testID = Integer.parseInt(splitId[0]);
                                    int showNote = Integer.parseInt(splitId[1]);

                                    DatabaseUtil.updateShowNotes(testID, showNote == 1 ? 0 : 1);

                                    refreshContents();
                                });
                            }
                        });

                        table.setMaxWidth(descriptionColumn.getMaxWidth() + ipColumn.getMaxWidth() + esxIpColumn.getMaxWidth());

                        TextArea notesTextArea = new TextArea();
                        notesTextArea.setId(Integer.toString(listOfTables.get(k).getTableId()));
                        notesTextArea.setText(listOfTables.get(k).getNotes());
                        notesTextArea.setMaxWidth(table.getMaxWidth());
                        notesTextArea.setWrapText(true);
                        notesTextArea.setStyle("-fx-background-color: aliceblue");
                        if (listOfTables.get(k).showNotes() == 0) {
                            notesTextArea.setVisible(false);
                            notesTextArea.setMaxHeight(0);
                            notesTextArea.setMinHeight(0);
                            notesTextArea.setPrefHeight(0);
                        } else {
                            notesTextArea.setVisible(true);
                            notesTextArea.setMinHeight(40);
                            notesTextArea.setMaxHeight(40);
                            notesTextArea.setPrefHeight(40);
                        }
                        notesTextArea.setStyle("-fx-font-size: 11");
                        notesTextArea.setOnKeyReleased(typingHandler -> {
                            DatabaseUtil.updateNotes(Integer.parseInt(notesTextArea.getId()), notesTextArea.getText());
                        });

                        VBox testNameAndTable = new VBox();
                        testNameAndTable.setStyle("-fx-font-size: 11; -fx-effect: dropshadow(gaussian, gray, 20, 0, 2, 2)");
                        testNameAndTable.getChildren().addAll(testName, table, notesTextArea);

                        // Add table to column
                        productColumn.getChildren().addAll(spacer, testNameAndTable);

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

        return centerSection;
    }

    private String columnName(int colIndex) {
        if (colIndex == 0) {
            return "description";
        } else if (colIndex == 1) {
            return "ip";
        } else if (colIndex == 2) {
            return "esxIP";
        } else {
            return "invalid column name";
        }
    }

    private void refreshContents() {
        tabPane.getTabs().get(0).setContent(this.display());
    }
}


