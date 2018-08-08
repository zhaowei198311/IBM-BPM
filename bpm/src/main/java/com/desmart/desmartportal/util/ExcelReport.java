package com.desmart.desmartportal.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Map;

import net.sf.jxls.exception.ParsePropertyException;
import net.sf.jxls.transformer.Configuration;
import net.sf.jxls.transformer.XLSTransformer;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
/**
 * Excel报表类
 *
 */
public class ExcelReport implements Report {
	public static final String POSTIFIX = ".xls";

	protected String reportName;
	protected Workbook workBook = new HSSFWorkbook();
	protected Sheet sheet;
	protected String sheetName;
	protected InputStream excelStream;

	public ExcelReport(String reportName, String sheetName) {
		super();
		this.reportName = reportName;
		this.sheetName = sheetName;
		sheet = workBook.createSheet(sheetName);
	}

	public ExcelReport() {
		super();
	}

	@SuppressWarnings({ "rawtypes" })
	@Override
	public void makeReport(Collection collection, String filePath) {

	}

	@Override
	public void makeReport(String[] dataStr, String filePath) {

	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public void makeReport(Collection collection, String[] collumHead,
			String filePath) {

	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public InputStream makeReportFromTemplet(String templetFileName, Map beans) {
		Configuration config = new Configuration();
		XLSTransformer transformer = new XLSTransformer(config);
		InputStream is = null;
		try {
			is = new FileInputStream(templetFileName);
			try {
				workBook = transformer.transformXLS(is, beans);
			} catch (ParsePropertyException e) {
				e.printStackTrace();
			} catch (InvalidFormatException e) {
				e.printStackTrace();
			}	
			// 产生POI输出流
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			workBook.write(os);
			excelStream = new ByteArrayInputStream(os.toByteArray());
			is.close();
		} catch (IOException ie) {
			ie.printStackTrace();
		}
		return excelStream;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void makeReportFromTemplet(String templetFileName, Map beans,
			String targetFileName) {
		Configuration config = new Configuration();
		XLSTransformer transformer = new XLSTransformer(config);
		try{
			try {
				transformer.transformXLS(templetFileName, beans, targetFileName);
			} catch (ParsePropertyException e) {
				e.printStackTrace();
			} catch (InvalidFormatException e) {
				e.printStackTrace();
			}		
		}catch(IOException ie){
			ie.printStackTrace();
		}
	}

	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	public Workbook getWorkBook() {
		return workBook;
	}

	public void setWorkBook(HSSFWorkbook workBook) {
		this.workBook = workBook;
	}

	public Sheet getSheet() {
		return sheet;
	}

	public void setSheet(HSSFSheet sheet) {
		this.sheet = sheet;
	}

	public String getSheetName() {
		return sheetName;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}
	
	public InputStream getExcelStream() {
		return excelStream;
	}

	public void setExcelStream(InputStream excelStream) {
		this.excelStream = excelStream;
	}

}
