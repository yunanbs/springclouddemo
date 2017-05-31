package com.sailing.facetec.util;

import org.apache.commons.codec.binary.Base64;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by eagle on 2017-5-3.
 */
public class ImageUtils {

    /**
     * 图片转化成base64字符串
     *
     * @param path 图片存储路径
     * @return
     */
    public static String picToBase64(String path) {
        //将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        InputStream in = null;
        byte[] data = null;
        //读取图片字节数组
        try {
            in = new FileInputStream(path);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // //对字节数组Base64编码
        // BASE64Encoder encoder = new BASE64Encoder();
        // return encoder.encode(data);//返回Base64编码过的字节数组字符串
        return Base64.encodeBase64String(data);
    }

    public static String getURLImage(String imageUrl){
        try {
            //new一个URL对象
            URL url = new URL(imageUrl);
            //打开链接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //设置请求方式为"GET"
            conn.setRequestMethod("GET");
            //超时响应时间为5秒
            conn.setConnectTimeout(5 * 1000);
            //通过输入流获取图片数据
            InputStream inStream = conn.getInputStream();
            //得到图片的二进制数据，以二进制封装得到数据，具有通用性
            byte[] data = readInputStream(inStream);
            // BASE64Encoder encode = new BASE64Encoder();
            // String s = encode.encode(data);
            String s = Base64.encodeBase64String(data);
            return s;
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

    private static byte[] readInputStream(InputStream inStream) throws Exception{
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        //创建一个Buffer字符串
        byte[] buffer = new byte[1024];
        //每次读取的字符串长度，如果为-1，代表全部读取完毕
        int len = 0;
        //使用一个输入流从buffer里把数据读取出来
        while( (len=inStream.read(buffer)) != -1 ){
            //用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
            outStream.write(buffer, 0, len);
        }
        //关闭输入流
        inStream.close();
        //把outStream里的数据写入内存
        return outStream.toByteArray();
    }

    /**
     * 网络图片转化成base64字符串
     *
     * @param destUrl
     * @return
     */
    public static String httpPicToBase64(String destUrl) {
        URL url = null;
        InputStream in = null;
        try {
            url = new URL(destUrl);
            HttpURLConnection httpUrl = null;
            httpUrl = (HttpURLConnection) url.openConnection();
            httpUrl.connect();
            in = httpUrl.getInputStream();
            byte[] data = null;
            // 读取图片字节数组
            try {
                data = new byte[in.available()];
                in.read(data);
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // // 对字节数组Base64编码
            // BASE64Encoder encoder = new BASE64Encoder();
            return Base64.encodeBase64String(data);// 返回Base64编码过的字节数组字符串
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
