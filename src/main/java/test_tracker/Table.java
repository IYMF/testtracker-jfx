package test_tracker;

import javafx.collections.ObservableList;

public class Table {

    private String tableTitle;
    private int id;
    private int sectionID;
    private int productID;
    private String notes;
    private int showNotes;
    private ObservableList<Row> rows;

    public Table(int id, String tableTitle, int sectionID, int productID, String notes, int showNotes) {
        this.id = id;
        this.tableTitle = tableTitle;
        this.sectionID = sectionID;
        this.productID = productID;
        this.notes = notes;
        this.showNotes = showNotes;

        rows = DatabaseUtil.getRows(id);
    }

    public int getTableId() {
        return this.id;
    }

    public String getTableTitle() {
        return this.tableTitle;
    }

    public ObservableList<Row> getRows() {
        return this.rows;
    }

    public int getProductID() {
        return this.productID;
    }

    public int getSectionID() {
        return this.sectionID;
    }

    public String getNotes() { return this.notes; }

    public int showNotes() { return this.showNotes; }

}
