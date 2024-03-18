package utilities;

import java.io.FileInputStream;

import java.io.FileOutputStream;


import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class ExcelFileUtil {
	Workbook wb;
	//constructor for reading excel path
	public ExcelFileUtil(String Excelpath) throws Throwable
	{
		FileInputStream fi =new FileInputStream(Excelpath);	
		wb=WorkbookFactory.create(fi);
	}
	//method1 for counting no of rows in a sheet.
	public int rowCount(String SheetName)
	{
		return wb.getSheet(SheetName).getLastRowNum();	
	}
	//method2 for reading cell data
	public String getCellData(String sheetname,int row,int column)
	{
		String data="";
		if(wb.getSheet(sheetname).getRow(row).getCell(column).getCellType()==CellType.NUMERIC)
			//If my row or cell has int type read that into string type.
		{
			int celldata=(int)wb.getSheet(sheetname).getRow(row).getCell(column).getNumericCellValue();
			data=String.valueOf(celldata);
		}else 
		{	
			data=wb.getSheet(sheetname).getRow(row).getCell(column).getStringCellValue();	
		}
		return data;
	}
	//method3 for writing celldata
	public void setCellData(String SheetName,int row,int column,String status,String WriteExcel) throws Throwable
	{
		//get sheet from wb
		Sheet ws=wb.getSheet(SheetName);
		//get row from sheet
		Row rowNum=ws.getRow(row);
		//create cell
		Cell cell=rowNum.createCell(column);
		//write status
		cell.setCellValue(status);
		if(status.equalsIgnoreCase("Pass")) 
		{
			CellStyle style=wb.createCellStyle();
			Font font=wb.createFont();
			font.setColor(IndexedColors.GREEN.getIndex());
			font.setBold(true);
			style.setFont(font);
			ws.getRow(row).getCell(column).setCellStyle(style);

		}
		else if(status.equalsIgnoreCase("Fail")) 
		{
			CellStyle style=wb.createCellStyle();
			Font font=wb.createFont();
			font.setColor(IndexedColors.RED.getIndex());
			font.setBold(true);
			style.setFont(font);
			ws.getRow(row).getCell(column).setCellStyle(style);
		}
		else if(status.equalsIgnoreCase("Blocked"))
		{
			CellStyle style=wb.createCellStyle();
			Font font=wb.createFont();
			font.setColor(IndexedColors.BLUE.getIndex());
			font.setBold(true);
			style.setFont(font);
			ws.getRow(row).getCell(column).setCellStyle(style);
		}
FileOutputStream fo=new FileOutputStream(WriteExcel);
wb.write(fo);

	}




}
