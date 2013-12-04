package com.cellmania.carriers.util;

import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFRow;

/**
 * This class is a supporting tool for creating cells while writing xls files. 
 * @author abakle
 *
 */

public class XLSUtil {
	private String encode="UTF-8";
	public String getEncode() {
		return encode;
	}

	public void setEncode(String encode) {
		this.encode = encode;
	}

	/**
	 * This class created a cell for the given row 'r' with the specified location, value and style.
	 * @param r			Row in which cell needs to be created.
	 * @param iCellNum	Position of the cell	
	 * @param szValue	value of the cell of data type <code>String<code> 
	 * @param cellStyle	Style to be applied on the cell.
	 */
	public static void addCellToRow(HSSFRow r,int iCellNum,String szValue,HSSFCellStyle cellStyle){
		HSSFCell cell = r.createCell(iCellNum,Cell.CELL_TYPE_STRING);
		cell.setCellValue(new HSSFRichTextString(szValue));
		if(cellStyle!=null){
			cell.setCellStyle(cellStyle);
		}
	}
	
	public static void addCellToRow(XSSFRow r,int iCellNum,String szValue,XSSFCellStyle cellStyle){
		XSSFCell cell = r.createCell(iCellNum, Cell.CELL_TYPE_STRING);
		cell.setCellValue(new XSSFRichTextString(szValue));
		if(cellStyle!=null){
			cell.setCellStyle(cellStyle);
		}
	}
	
	/**
	 * This class created a cell for the given row 'r' with the specified location, value and style.
	 * @param r			Row in which cell needs to be created.
	 * @param iCellNum	Position of the cell	
	 * @param szValue	value of the cell of data type <code>String<code> 
	 * @param cellStyle	Style to be applied on the cell.
	 */
	public static void addCellToRow(HSSFRow r,int iCellNum,String szUrl,String szLabel,HSSFCellStyle cellStyle){
		HSSFCell cell = r.createCell(iCellNum, Cell.CELL_TYPE_FORMULA);
		String linkFormula = "HYPERLINK(\"" + szUrl + "\", \"" + szLabel + "\")"; 
		cell.setCellFormula(linkFormula);
		if(cellStyle!=null){
			cell.setCellStyle(cellStyle);
		}
	}
	
	public static void addCellToRow(XSSFRow r,int iCellNum,String szUrl,String szLabel,XSSFCellStyle cellStyle){
		XSSFCell cell = r.createCell(iCellNum, Cell.CELL_TYPE_FORMULA);
		String linkFormula = "HYPERLINK(\"" + szUrl + "\", \"" + szLabel + "\")"; 
		cell.setCellFormula(linkFormula);
		if(cellStyle!=null){
			cell.setCellStyle(cellStyle);
		}
	}
	
	/**
	 * This class created a cell for the given row 'r' with the specified location, value and style.
	 * @param r			Row in which cell needs to be created.
	 * @param iCellNum	Position of the cell	
	 * @param szValue	value of the cell of data type <code>double<code> 
	 * @param cellStyle	Style to be applied on the cell.
	 */
	public static void addCellToRow(HSSFRow r,int iCellNum,double szValue,HSSFCellStyle cellStyle){
		HSSFCell cell = r.createCell(iCellNum, Cell.CELL_TYPE_NUMERIC);
		cell.setCellValue(szValue);
		if(cellStyle!=null){
			cell.setCellStyle(cellStyle);		
		}
	}
	
	public static void addCellToRow(XSSFRow r,int iCellNum,double szValue,XSSFCellStyle cellStyle){
		XSSFCell cell = r.createCell(iCellNum, Cell.CELL_TYPE_NUMERIC);
		cell.setCellValue(szValue);
		if(cellStyle!=null){
			cell.setCellStyle(cellStyle);		
		}
	}
	
	/**
	 * This class created a cell for the given row 'r' with the specified location, value and style.
	 * @param r			Row in which cell needs to be created.
	 * @param iCellNum	Position of the cell	
	 * @param szValue	value of the cell of data type <code>java.util.Date<code> 
	 * @param cellStyle	Style to be applied on the cell.
	 */
	public static void addCellToRow(HSSFRow r,int iCellNum,Date dtValue,HSSFCellStyle cellStyle){
		HSSFCell cell = r.createCell(iCellNum);
		if(dtValue!=null){
			cell.setCellValue(dtValue);
		} else {
			cell.setCellValue(new HSSFRichTextString(""));
		}
		
		if(cellStyle!=null){
			cell.setCellStyle(cellStyle);		
		}
	}
	
	public static void addCellToRow(XSSFRow r,int iCellNum,Date dtValue,XSSFCellStyle cellStyle){
		XSSFCell cell = r.createCell(iCellNum);
		if(dtValue!=null){
			cell.setCellValue(dtValue);
		} else {
			cell.setCellValue(new XSSFRichTextString(""));
		}
		
		if(cellStyle!=null){
			cell.setCellStyle(cellStyle);		
		}
	}
	
	/**
	 * This class created a cell for the given row 'r' with the specified location, value and style.
	 * @param r			Row in which cell needs to be created.
	 * @param iCellNum	Position of the cell	
	 * @param object	value of the cell of data type <code>Object<code> 
	 * @param cellStyle	Style to be applied on the cell.
	 */
	public static void addCellToRow(HSSFRow r, int iCellNum, Object object, HSSFCellStyle cellStyle) {
		if(object==null) {
			object=new String("");
		} else if(object instanceof java.lang.String){
			addCellToRow(r, iCellNum, object.toString(),cellStyle);
		} else {
			addCellToRow(r, iCellNum, Double.parseDouble(object.toString()),cellStyle);
		}
		
	}
	
	public static void addCellToRow(XSSFRow r, int iCellNum, Object object, XSSFCellStyle cellStyle) {
		if(object==null) {
			object=new String("");
		} else if(object instanceof java.lang.String){
			addCellToRow(r, iCellNum, object.toString(),cellStyle);
		} else {
			addCellToRow(r, iCellNum, Double.parseDouble(object.toString()),cellStyle);
		}
		
	}
	
	
	/**
	 * This method creates a range of cell with the given value and style.
	 * @param r				Row in which cell needs to be created.
	 * @param iStartCell	Position of the cell to start with.
	 * @param iCount		Number of cells to be created
	 * @param szValue		value of the cells of data type <code>String<code>
	 * @param cellStyle		Style to be applied on the cells.
	 */
	public static void addCellsToRow(HSSFRow r,int iStartCell, int iCount, String szValue,HSSFCellStyle cellStyle) {
		for (int i = 0;i<iCount;i++){
			HSSFCell cell = r.createCell(iStartCell++,Cell.CELL_TYPE_STRING);
			cell.setCellValue(new HSSFRichTextString(szValue));
			if(cellStyle!=null){
				cell.setCellStyle(cellStyle);		
			}
		}
		
	}
	
	public static void addCellsToRow(XSSFRow r,int iStartCell, int iCount, String szValue,XSSFCellStyle cellStyle) {
		for (int i = 0;i<iCount;i++){
			XSSFCell cell = r.createCell(iStartCell++, Cell.CELL_TYPE_STRING);
			cell.setCellValue(new XSSFRichTextString(szValue));
			if(cellStyle!=null){
				cell.setCellStyle(cellStyle);		
			}
		}
		
	}
	
	/**
	 * This method creates a range of cell with the given value and style.
	 * @param r				Row in which cell needs to be created.
	 * @param iStartCell	Position of the cell to start with.
	 * @param iCount		Number of cells to be created
	 * @param szValue		value of the cells of data type <code>double<code>
	 * @param cellStyle		Style to be applied on the cells.
	 */
	public static void addCellsToRow(HSSFRow r,int iStartCell, int iCount, double szValue,HSSFCellStyle cellStyle) {
		for (int i = 0;i<iCount;i++) {
			HSSFCell cell = r.createCell(iStartCell++, Cell.CELL_TYPE_NUMERIC);
			cell.setCellValue(szValue);
			if(cellStyle!=null){
				cell.setCellStyle(cellStyle);		
			}
		}
	}
	
	public static void addCellsToRow(XSSFRow r,int iStartCell, int iCount, double szValue,XSSFCellStyle cellStyle) {
		for (int i = 0;i<iCount;i++) {
			XSSFCell cell = r.createCell(iStartCell++, Cell.CELL_TYPE_NUMERIC);
			cell.setCellValue(szValue);
			if(cellStyle!=null){
				cell.setCellStyle(cellStyle);		
			}
		}
	}
	
	
	
	/**
	 * This method creates a cell with the given formula and style.
	 * @param r				Row in which cell needs to be created.
	 * @param iCellNum		Cell number to be created at
	 * @param szFormula		formula of the cells of data type <code>String<code>
	 * @param cellStyle		Style to be applied on the cells.
	 */
	public static void addFormulaCellToRow(HSSFRow r,int iCellNum, String szFormula,HSSFCellStyle cellStyle) {
		HSSFCell cell = r.createCell(iCellNum, Cell.CELL_TYPE_FORMULA);
		cell.setCellFormula(szFormula);
		if(cellStyle!=null){
			cell.setCellStyle(cellStyle);		
		}
	}
	
	public static void addFormulaCellToRow(XSSFRow r,int iCellNum, String szFormula,XSSFCellStyle cellStyle) {
		XSSFCell cell = r.createCell(iCellNum, Cell.CELL_TYPE_FORMULA);
		cell.setCellFormula(szFormula);
		if(cellStyle!=null){
			cell.setCellStyle(cellStyle);		
		}
	}
	
	
	public static String convertColumnNumberToChars(int i) {
		if (i < 0)
			throw new UnsupportedOperationException(
					"Converted number must be greater than zero.");

		int iBase = 'Z' - 'A' + 1;
		if (iBase > Character.MAX_RADIX)
			throw new UnsupportedOperationException(
					"This JRE can't convert to radix greater than "
							+ Character.MAX_RADIX);

		String interConversion = Integer.toString(i - 1, iBase).toUpperCase();

		char[] ac = interConversion.toCharArray();
		for (int j = 0; j < ac.length; j++) {
			int poziceOdzadu = ac.length - j - 1;
			char c = ac[j];
			ac[j] = (char) ('A' - poziceOdzadu + Character.digit(c, iBase));

		}
		return String.copyValueOf(ac);

	}
	
}
