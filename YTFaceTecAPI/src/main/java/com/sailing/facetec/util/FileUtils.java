package com.sailing.facetec.util;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.BorderStyle;

import java.io.*;
import java.rmi.ConnectIOException;
import java.util.Iterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by yunan on 2017/5/9.
 */
public class FileUtils {

    /**
     * jsonArray 转Excel
     *
     * @param jsonArray
     * @param excelFullName
     * @param sheetName
     * @param autoHeader
     * @return
     * @throws IOException
     */
    public static String arrayToExcel(JSONArray jsonArray, String excelFullName, String sheetName, boolean autoHeader) throws IOException {
        String result = "";
        // 创建工作簿
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
        // 创建sheet
        HSSFSheet hssfSheet = hssfWorkbook.createSheet(CommUtils.isNullObject(sheetName) ? "sheet1" : sheetName);
        // 设置普通单元格格式 边框
        HSSFCellStyle hssfCellStyle = setCellStyle(hssfWorkbook, BorderStyle.THIN, false);
        // 设置表头格式 边框 字体加粗
        HSSFCellStyle hssfHeaderCellStyle = setCellStyle(hssfWorkbook, BorderStyle.THIN, true);

        int rowIndex = 0;
        HSSFRow hssfRow = null;

        String[] headers = CommUtils.getJsonKeys((JSONObject) jsonArray.get(0));

        if (autoHeader) {
            hssfRow = hssfSheet.createRow(rowIndex);
            for (int i = 0; i < headers.length; i++) {
                HSSFCell hssfCell = hssfRow.createCell(i);
                hssfCell.setCellValue(headers[i]);
                hssfCell.setCellStyle(hssfHeaderCellStyle);
            }
            rowIndex++;
        }

        for (Iterator iterator = jsonArray.iterator(); iterator.hasNext(); ) {
            JSONObject jsonObject = (JSONObject) iterator.next();
            hssfRow = hssfSheet.createRow(rowIndex);
            for (int i = 0; i < headers.length; i++) {
                HSSFCell hssfCell = hssfRow.createCell(i);
                hssfCell.setCellValue(jsonObject.getString(headers[i]));
                hssfCell.setCellStyle(0 == rowIndex ? hssfHeaderCellStyle : hssfCellStyle);
            }
            rowIndex++;
        }
        try {
            File file = new File(excelFullName);
            result = file.getName();
            hssfWorkbook.write(file);
        } finally {
            hssfWorkbook.close();
        }
        return result;
    }

    /**
     * 获取单元格样式
     *
     * @param hssfWorkbook 工作簿
     * @param borderStyle  边框类型
     * @param fontBold     字体
     * @return 单元格样式
     */
    private static HSSFCellStyle setCellStyle(HSSFWorkbook hssfWorkbook, BorderStyle borderStyle, boolean fontBold) {
        HSSFCellStyle hssfCellStyle = hssfWorkbook.createCellStyle();
        hssfCellStyle.setBorderTop(borderStyle);
        hssfCellStyle.setBorderBottom(borderStyle);
        hssfCellStyle.setBorderLeft(borderStyle);
        hssfCellStyle.setBorderRight(borderStyle);
        HSSFFont hssfFont = hssfWorkbook.createFont();
        hssfFont.setBold(fontBold);
        hssfCellStyle.setFont(hssfFont);
        return hssfCellStyle;
    }

    public static String zipFiles(String fileName, String zipFileFullName) throws IOException {
        String result = "";
        byte[] buff = new byte[1024];
        InputStream inputStream=null;
        File sourceFile = new File(fileName);
        File zipFile = new File(zipFileFullName);
        ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(zipFile));
        if (sourceFile.isDirectory()) {
            File[] listFiles = sourceFile.listFiles();
            for (File file : listFiles) {
                if (file.getName().equals(zipFile.getName())) {
                    continue;
                }
                zipOutputStream.putNextEntry(new ZipEntry(String.format("%s/%s", sourceFile.getName(), file.getName())));
                inputStream = new FileInputStream(file);
                while (inputStream.read(buff)>0)
                {
                    zipOutputStream.write(buff);
                }
                inputStream.close();
            }
        }
        zipOutputStream.close();
        return result;
    }
}
