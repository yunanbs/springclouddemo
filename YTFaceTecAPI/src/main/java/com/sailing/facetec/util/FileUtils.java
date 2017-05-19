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

        // 当前行计数
        int rowIndex = 0;
        HSSFRow hssfRow = null;

        // 获取HSSFCreationHelper
        HSSFCreationHelper hssfCreationHelper = hssfWorkbook.getCreationHelper();

        // 获取json数据的属性
        String[] headers = CommUtils.getJsonKeys((JSONObject) jsonArray.get(0));

        // 如果设置了自动表头 则将json设为表头
        if (autoHeader) {
            // 创建表头
            hssfRow = hssfSheet.createRow(rowIndex);
            for (int i = 0; i < headers.length; i++) {
                HSSFCell hssfCell = hssfRow.createCell(i);
                hssfCell.setCellValue(headers[i]);
                hssfCell.setCellStyle(hssfHeaderCellStyle);
            }
            rowIndex++;
        }

        for (Iterator iterator = jsonArray.iterator(); iterator.hasNext(); ) {
            // 获取数据
            JSONObject jsonObject = (JSONObject) iterator.next();
            // 创建行
            hssfRow = hssfSheet.createRow(rowIndex);
            // 按列填充数据
            for (int i = 0; i < headers.length; i++) {
                // 创建单元格
                HSSFCell hssfCell = hssfRow.createCell(i);
                // 如果是表头 则设置每列的宽度
                if (0 == rowIndex) {
                    hssfSheet.setColumnWidth(i, 5000);
                }
                // 获取列名
                String header = headers[i];
                if (header.startsWith("link-")) {
                    // 判断是否是关联属性
                    if (0 == rowIndex) {
                        // 如果是关联属性，且是第一行 则设置为表头
                        hssfCell.setCellValue(jsonObject.getString(header));
                    } else {
                        // 如果是关联属性，但不是表头则进行处理
                        // 获取关联文件存储地址
                        String fileName = jsonObject.getString(header);
                        // 将文件名作为单元格值
                        hssfCell.setCellValue(fileName);
                        // 创建文件链接 注意要使用File类型
                        HSSFHyperlink hssfHyperlink = hssfCreationHelper.createHyperlink(HyperlinkType.FILE);
                        // 设置文件链接地址
                        hssfHyperlink.setAddress(fileName);
                        // 修改单元格内容为文件链接类型
                        hssfCell.setHyperlink(hssfHyperlink);
                    }
                    // 设置单元格样式
                    hssfCell.setCellStyle(0 == rowIndex ? hssfHeaderCellStyle : hssfLinkCellStyle);
                } else {
                    // 设置单元格值
                    hssfCell.setCellValue(jsonObject.getString(header));
                    // 设置单元格样式
                    hssfCell.setCellStyle(0 == rowIndex ? hssfHeaderCellStyle : hssfCellStyle);
                }
            }
            rowIndex++;
        }

        try {
            // 获取文件
            File file = new File(excelFullName);
            // 生成Excel文件
            hssfWorkbook.write(file);
            // 获取文件名
            result = file.getName();
        } finally {
            // 关闭
            // 关闭Excel
            hssfWorkbook.close();
        }
        return result;
    }

    /**
     * 生成单元格样式
     *
     * @param hssfWorkbook 工作簿
     * @param borderStyle  边框类型
     * @param fontBold     字体
     * @return 单元格样式
     */
    private static HSSFCellStyle setCellStyle(HSSFWorkbook hssfWorkbook, BorderStyle borderStyle, boolean fontBold, boolean link) {
        // 生成单元格样式
        HSSFCellStyle hssfCellStyle = hssfWorkbook.createCellStyle();
        //设置边框样式
        hssfCellStyle.setBorderTop(borderStyle);
        hssfCellStyle.setBorderBottom(borderStyle);
        hssfCellStyle.setBorderLeft(borderStyle);
        hssfCellStyle.setBorderRight(borderStyle);
        // 设置字体粗体
        HSSFFont hssfFont = hssfWorkbook.createFont();
        hssfFont.setBold(fontBold);
        hssfCellStyle.setFont(hssfFont);
        // 设置超链接样式 蓝色+下划线
        if (link) {
            hssfFont.setColor(HSSFColor.HSSFColorPredefined.BLUE.getIndex());
            hssfFont.setUnderline((byte) 1);
        }
        return hssfCellStyle;
    }

    /**
     * 创建zip文件
     * @param fileName 需要打包的目录名
     * @return
     * @throws IOException
     */
    public static String createZipFile(String fileName) throws IOException {
        // 获取zip文件名
        String desZipFileName = fileName + ".zip";
        // 获取文件
        File zipFile = new File(desZipFileName);
        // 创建zip文件流
        ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(zipFile));
        try {
            // 向zip文件流中添加文件或文件夹
            zipFile(fileName, new File(fileName).getParent(), zipOutputStream);
        } finally {
            // 关闭zip文件流
            zipOutputStream.close();
        }
        // 返回zip文件路径
        return zipFile.getPath();
    }

    /**
     * 压缩文件
     * @param currentFilename 当前文件
     * @param rootPath 当前文件上级目录
     * @param zipOutputStream zip流
     * @throws IOException
     */
    private static void zipFile(String currentFilename, String rootPath, ZipOutputStream zipOutputStream) throws IOException {
        // 获取当前文件
        File sourceFile = new File(currentFilename);
        if (sourceFile.isDirectory()) {
            // 如果当前文件是目录 对旗下文件进行递归
            // 获取目录下的文件列表
            File[] subFiles = sourceFile.listFiles();
            if (null != subFiles) {
                // 如果目录不为空 对每个文件进行递归
                Arrays.asList(subFiles).forEach(f -> {
                    try {
                        zipFile(f.getPath(), rootPath, zipOutputStream);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        } else {
            // 如果是文件则进行压缩
            // 想zip流添加文件
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
