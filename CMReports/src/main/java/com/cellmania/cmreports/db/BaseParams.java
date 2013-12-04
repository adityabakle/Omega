package com.cellmania.cmreports.db;

import com.cellmania.cmreports.common.ObjectConvertor;

public class BaseParams {
	 	public static final String SORT_ASC = "ASC";
	    public static final String SORT_DESC = "DESC";
	    
	    public static final int SORT_COLUMN_ID = 0; // Primary Key of every table
	    public static final int SORT_COLUMN_ENABLED = 1;
	    public static final int SORT_COLUMN_CREATEDDATE = 99;
	    public static final int SORT_COLUMN_MODIFIEDDATE = 100;
	    
	    
		private int sortCol;
		private String sortOrder = "ASC";
		private int startRow;
		private int numRows = 999999999;
		
		public int getSortCol() {
			return sortCol;
		}
		public void setSortCol(int sortCol) {
			this.sortCol = sortCol;
		}
		public String getSortOrder() {
			return sortOrder;
		}
		public void setSortOrder(String sortOrder) {
			this.sortOrder = sortOrder;
		}
		public int getStartRow() {
			return startRow;
		}
		public void setStartRow(int startRow) {
			this.startRow = startRow;
		}
		public int getNumRows() {
			return numRows;
		}
		public void setNumRows(int numRows) {
			this.numRows = numRows;
		}
		
		public String toString() {
			return ObjectConvertor.convertToString(this);
		}
}
