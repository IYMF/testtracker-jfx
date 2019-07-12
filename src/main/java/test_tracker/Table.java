package test_tracker;

import javafx.collections.ObservableList;

public class Table {

    private String tableTitle;
    private int sectionID;
    private int productID;
    private int testID;
    private ObservableList<Row> rows;

    public Table(String tableTitle, int sectionID, int productID, int testID) {
        this.tableTitle = tableTitle;
        this.sectionID = sectionID;
        this.productID = productID;
        this.testID = testID;

        rows = DatabaseUtil.getRows(testID);
    }

    public String getTableTitle() {
        return tableTitle;
    }

    public ObservableList<Row> getRows() {
        return rows;
    }

    public int getProductID() {
        return productID;
    }

    public int getSectionID() {
        return sectionID;
    }

    public int getTestID() {
        return testID;
    }
}
