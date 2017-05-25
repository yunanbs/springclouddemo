package com.sailing.facetec.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.tomcat.util.codec.binary.Base64;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Iterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
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
     *
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
     *
     * @param currentFilename 当前文件
     * @param rootPath        当前文件上级目录
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

    /**
     * 解压缩zip
     *
     * @param zipFileName
     * @param desPath
     * @return
     */
    public static int upZipFile(String zipFileName, String desPath) throws IOException {
        int result = 0;
        // 生成zip输入流
        ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zipFileName));
        BufferedInputStream bufferedInputStream = new BufferedInputStream(zipInputStream);

        // 输出文件流
        FileOutputStream fileOutputStream = null;
        BufferedOutputStream bufferedOutputStream = null;
        File des;

        // zip组件
        ZipEntry zipEntry = zipInputStream.getNextEntry();

        try {
            // 获取zip组件单元
            while (null != zipEntry) {

                // 生成目标文件
                des = new File(desPath, zipEntry.getName());
                if (zipEntry.isDirectory()) {
                    // 生成目录
                    des.mkdirs();
                    zipEntry = zipInputStream.getNextEntry();
                    continue;
                }
                // 生成文件输出流
                fileOutputStream = new FileOutputStream(des);
                bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
                // 写文件
                byte[] buffer = new byte[8196];
                while (bufferedInputStream.read(buffer) > 0) {
                    bufferedOutputStream.write(buffer);
                }
                // 关闭文件输出流
                bufferedOutputStream.close();
                fileOutputStream.close();
                // 添加文件计数
                result++;
                zipEntry = zipInputStream.getNextEntry();
            }
        } finally {
            // 关闭所有文件流
            if (null != bufferedOutputStream) {
                bufferedOutputStream.close();
            }

            if (null != fileOutputStream) {
                fileOutputStream.close();
            }

            bufferedInputStream.close();
            zipInputStream.close();
        }
        return result;
    }

    /**
     * 文件复制
     *
     * @param sourceFile
     * @param desFile
     * @return
     * @throws IOException
     */
    public static boolean copyFile(String sourceFile, String desFile) throws IOException {
        boolean result;
        Path copyResult = Files.copy(Paths.get(sourceFile), Paths.get(desFile));
        result = desFile.equals(copyResult.toString());
        return result;
    }

    /**
     * base64转文件
     *
     * @param base64
     * @param desFile
     * @return
     */
    public static boolean base64ToFile(String base64, String desFile) throws IOException {
        boolean result = false;
        byte[] bytes = Base64.decodeBase64(base64);
        // 获取文件
        File file = new File(desFile);
        // 创建目录
        File path = new File(file.getParent());
        if (!path.exists()) {
            path.mkdirs();
        }
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
        try {
            bufferedOutputStream.write(bytes);
            result = true;
        } finally {
            bufferedOutputStream.close();
        }
        return result;
    }

    /**
     * 文件转Base64
     * @param filename
     * @return
     * @throws IOException
     */
    public static String fileToBase64(String filename) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(filename);
        byte[] result;
        try {
            result = new byte[fileInputStream.available()];
            fileInputStream.read(result);
        } finally {
            fileInputStream.close();
        }

        BASE64Encoder base64Encoder = new BASE64Encoder();
        return base64Encoder.encode(result);
    }

    public static boolean streamToFile(InputStream inputStream, String desFile) throws IOException {
        boolean result = false;
        // 获取文件
        File file = new File(desFile);
        // 创建目录
        File path = new File(file.getParent());
        if (!path.exists()) {
            path.mkdirs();
        }

        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(desFile));
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
        try {

            byte[] buffer = new byte[8196];
            while (bufferedInputStream.read(buffer) != -1) {
                bufferedOutputStream.write(buffer);
            }
            result = true;
        } finally {
            inputStream.close();
            bufferedOutputStream.close();
        }
        return result;
    }
}
