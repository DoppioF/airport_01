package models;

public class PdfTableStructureModel {

	private String title;
	private String[] tableHeads;
	private String[][] tableData;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String[] getTableHeads() {
		return tableHeads;
	}
	public void setTableHeads(String[] tableHeads) {
		this.tableHeads = tableHeads;
	}
	public String[][] getTableData() {
		return tableData;
	}
	public void setTableData(String[][] tableData) {
		this.tableData = tableData;
	}
	
}
