package com.sailing.facetec.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;

import java.io.*;
import java.util.Arrays;
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
    @SuppressWarnings(value = "all")
    public static String arrayToExcel(JSONArray jsonArray, String excelFullName, String sheetName, boolean autoHeader) throws IOException {
        String result = "";
        // 创建工作簿
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
        // 创建sheet
        HSSFSheet hssfSheet = hssfWorkbook.createSheet(CommUtils.isNullObject(sheetName) ? "sheet1" : sheetName);
        // 设置普通单元格格式 边框
        HSSFCellStyle hssfCellStyle = setCellStyle(hssfWorkbook, BorderStyle.THIN, false, false);
        // 设置表头格式 边框 字体加粗
        HSSFCellStyle hssfHeaderCellStyle = setCellStyle(hssfWorkbook, BorderStyle.THIN, true, false);
        // 设置链接样式 蓝色 下划线
        HSSFCellStyle hssfLinkCellStyle = setCellStyle(hssfWorkbook, BorderStyle.THIN, false, true);

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

                if (0 == rowIndex) {
                    hssfSheet.setColumnWidth(i, 5000);
                }
                String header = headers[i];
                if (header.startsWith("link-")) {

                    if (0 == rowIndex) {
                        hssfCell.setCellValue(jsonObject.getString(header));
                    } else {
                        String fileName = jsonObject.getString(header);
                        hssfCell.setCellValue(fileName);

                        HSSFCreationHelper hssfCreationHelper = hssfWorkbook.getCreationHelper();
                        HSSFHyperlink hssfHyperlink = hssfCreationHelper.createHyperlink(HyperlinkType.FILE);
                        hssfHyperlink.setAddress(fileName);
                        hssfCell.setHyperlink(hssfHyperlink);
                    }
                    hssfCell.setCellStyle(0 == rowIndex ? hssfHeaderCellStyle : hssfLinkCellStyle);
                } else {
                    hssfCell.setCellValue(jsonObject.getString(header));
                    hssfCell.setCellStyle(0 == rowIndex ? hssfHeaderCellStyle : hssfCellStyle);
                }
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
    private static HSSFCellStyle setCellStyle(HSSFWorkbook hssfWorkbook, BorderStyle borderStyle, boolean fontBold, boolean link) {
        HSSFCellStyle hssfCellStyle = hssfWorkbook.createCellStyle();
        hssfCellStyle.setBorderTop(borderStyle);
        hssfCellStyle.setBorderBottom(borderStyle);
        hssfCellStyle.setBorderLeft(borderStyle);
        hssfCellStyle.setBorderRight(borderStyle);
        HSSFFont hssfFont = hssfWorkbook.createFont();
        hssfFont.setBold(fontBold);
        hssfCellStyle.setFont(hssfFont);
        if (link) {
            hssfFont.setColor(HSSFColor.HSSFColorPredefined.BLUE.getIndex());
            hssfFont.setUnderline((byte) 1);
        }
        return hssfCellStyle;
    }

    public static String createZipFile(String fileName) throws IOException {
        String desZipFileName = fileName + ".zip";
        File zipFile = new File(desZipFileName);
        ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(zipFile));
        try {
            zipFile(fileName, new File(fileName).getParent(), zipOutputStream);
        } finally {
            zipOutputStream.close();
        }
        return zipFile.getPath();
    }

    private static void zipFile(String currentFilename, String rootPath, ZipOutputStream zipOutputStream) throws IOException {
        File sourceFile = new File(currentFilename);
        if (sourceFile.isDirectory()) {
            File[] subFiles = sourceFile.listFiles();
            if (null != subFiles) {
                Arrays.asList(subFiles).forEach(f -> {
                    try {
                        zipFile(f.getPath(), rootPath, zipOutputStream);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        } else {
            zipOutputStream.putNextEntry(new ZipEntry(currentFilename.replace(rootPath, "")));
            BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(currentFilename));
            byte[] buffer = new byte[8192];
            while (bufferedInputStream.read(buffer) > 0) {
                zipOutputStream.write(buffer);
            }
            bufferedInputStream.close();
        }
    }
}
