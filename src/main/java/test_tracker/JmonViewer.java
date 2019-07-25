package test_tracker;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.*;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Label;

import java.util.regex.Pattern;

import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class JmonViewer extends Application {

    JmonViewer(){}

    @Override
    public void start(Stage primaryStage) {
        display();
    }

    ScrollPane display() {
        ObservableList<Table> listOfTables = DatabaseUtil.getTests();
        ObservableList<Integer> productIDs = DatabaseUtil.getProductIDs();
        ObservableList<String> products = DatabaseUtil.getProducts();

        VBox allContentVB = new VBox();
        allContentVB.setStyle("-fx-background-color: aliceblue");

        // List all products
        for (int i = 0; i < products.size(); i++) {
            HBox sectionHbox = new HBox();
            sectionHbox.setAlignment(Pos.CENTER);
            sectionHbox.setStyle("-fx-background-color: #323435;-fx-text-fill: aliceblue; -fx-font-size: 16px; -fx-label-padding: 0; -fx-padding: 0;");
            sectionHbox.setPadding(new Insets(5.0D, 0.0D, 0, 0.0D));

            Label sectionHeader = new Label(products.get(i));
            sectionHbox.setPadding(new Insets(0));
            sectionHeader.setStyle("-fx-text-fill: aliceblue; -fx-font-weight: 700");
            sectionHbox.getChildren().add(sectionHeader);

            allContentVB.getChildren().add(sectionHbox);

            FlowPane productSection = new FlowPane();
            productSection.setAlignment(Pos.CENTER);

            // Display jmons for each section
            for (int j = 0; j < listOfTables.size(); j++) {
                // Check the rows of each table for "jmon" if they are in the same product group
                if (listOfTables.get(j).getProductID() == productIDs.get(i)) {
                    ObservableList<Row> rows = listOfTables.get(j).getRows();
                    for (int k = 0; k < rows.size(); k++) {
                        // If row description contains "jmon" create jmon web views
                        if (Pattern.compile(Pattern.quote("jmon"), Pattern.CASE_INSENSITIVE).matcher(rows.get(k).getDescription()).find()) {

                            String linkAddress = "151.155." + rows.get(k).getIp();
                            Hyperlink testName = new Hyperlink(listOfTables.get(j).getTableTitle() + " - " + linkAddress);
                            testName.setOnAction(linkEvent -> getHostServices().showDocument("http://" + linkAddress));
                            testName.setAlignment(Pos.CENTER);
                            testName.setMaxWidth(Double.MAX_VALUE);
                            testName.setStyle("-fx-background-color: #0073e7; -fx-text-fill: aliceblue; -fx-font-weight: 700");

                            WebView jmon = new WebView();
                            jmon.setZoom(.70);
                            jmon.setPrefHeight(475);
                            jmon.getEngine().load("http://151.155." + rows.get(k).getIp());

                            VBox testNameAndJmon = new VBox();
                            testNameAndJmon.setStyle("-fx-border-color: black");
                            testNameAndJmon.getChildren().addAll(testName, jmon);

                            VBox pseudoMargin = new VBox();
                            pseudoMargin.getChildren().addAll(testNameAndJmon);
                            pseudoMargin.setPadding(new Insets(3));

                            productSection.getChildren().addAll(pseudoMargin);
                        }
                    }
                }
            }

            allContentVB.getChildren().add(productSection);
        }


        ScrollPane allContentSP = new ScrollPane();
        allContentSP.setFitToWidth(true);
        allContentSP.setContent(allContentVB);
        return allContentSP;
    }
}
