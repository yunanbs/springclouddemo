package com.sailing.facetec.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sailing.facetec.entity.RlEntity;
import com.sailing.facetec.remoteservice.YTApi;
import com.sailing.facetec.service.YTService;
import com.sailing.facetec.util.CommUtils;
import com.sailing.facetec.util.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.encrypt.BytesEncryptor;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by yunan on 2017/5/17.
 * 依图平台接口
 */
@Service
public class YTServiceImpl  implements YTService{

    private static final Logger logger = LoggerFactory.getLogger(YTServiceImpl.class);

    // 依图API代理
    @Autowired
    private YTApi ytApi;

    @Value("${ytface.api-url}")
    private String ytServer;

    @Override
    public String login(String userName, String passWord) {
        long expend = System.currentTimeMillis();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name",userName);
        jsonObject.put("password",passWord);
        logger.info("yt login params:{}", jsonObject);

        String result = ytApi.login(jsonObject.toJSONString());
        logger.info("yt login result:{}", result);

        expend = System.currentTimeMillis()-expend;
        logger.info("yt login expend:{} ms", expend);
        return result;
    }

    @Override
    public String addCamera(String sid,String cameraName, String url, int cameraType) {
        long expend = System.currentTimeMillis();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name",cameraName);
        jsonObject.put("url",url);
        jsonObject.put("type",cameraType);
        logger.info("yt addCamera params:{}", jsonObject);

        String headerStr = String.format("session_id=%s;face_platform_session_id=%s",sid,sid);
        logger.info("yt addCamera headers:{}", headerStr);

        String result = ytApi.addCamera(headerStr, jsonObject.toJSONString());
        logger.info("yt addCamera result:{}", result);

        expend = System.currentTimeMillis()-expend;
        logger.info("yt addCamera expend:{} ms", expend);
        return result;
    }

    @Override
    public String updateCamera(String sid, int cameraId, String cameraName, String cameraUrl, int enabled) {
        long expend = System.currentTimeMillis();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id",cameraId);
        if(!CommUtils.isNullObject(cameraName)){
            jsonObject.put("name",cameraName);
        }
        if(!CommUtils.isNullObject(cameraUrl)){
            jsonObject.put("url",cameraUrl);
        }
        if(!CommUtils.isNullObject(enabled)){
            jsonObject.put("enabled",enabled);
        }
        String headerStr = String.format("session_id=%s;face_platform_session_id=%s",sid,sid);

        String result = ytApi.updateCamera(headerStr, jsonObject.toJSONString());
        logger.info("yt updateCamera result:{}", result);

        expend = System.currentTimeMillis()-expend;
        logger.info("yt updateCamera expend:{} ms", expend);
        return result;
    }

    @Override
    public String setMonitorRepository(String sid,int cameraId, int repositoryId, double limit, long stSec, long endSec) {
        long expend = System.currentTimeMillis();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("camera_id",cameraId);
        jsonObject.put("repository_id",repositoryId);
        jsonObject.put("threshold",limit);
        jsonObject.put("max_time_span",stSec);
        jsonObject.put("min_time_span",endSec);

        String headerStr = String.format("session_id=%s;face_platform_session_id=%s",sid,sid);

        expend = System.currentTimeMillis()-expend;
        logger.info("yt setMonitorRepository expend:{} ms", expend);
        return ytApi.setMonitorRepository(headerStr, jsonObject.toJSONString());
    }


    @Override
    public String setMonitorByCamera(String sid,String monitorName, int cameraId, int repositoryId, double limit) {
        long expend = System.currentTimeMillis();

        String headerStr = String.format("session_id=%s@DEFAULT; yt_cluster_id=DEFAULT",sid);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name",monitorName);

        JSONArray jsonArray = new JSONArray();
        JSONObject monitorObject = new JSONObject();
        monitorObject.put("camera_id",String.format("%s@DEFAULT",cameraId));
        monitorObject.put("repository_id",String.format("%s@DEFAULT",repositoryId));
        monitorObject.put("threshold",limit);
        jsonArray.add(monitorObject);

        jsonObject.put("requests",jsonArray);
        String result = ytApi.setMonitorByCamera(headerStr, jsonObject.toJSONString());
        logger.info("yt setMonitorByCamera result:{}", result);

        expend = System.currentTimeMillis()-expend;
        logger.info("yt setMonitorByCamera expend:{} ms", expend);
        return result;
    }

    @Override
    public String delCamera(String sid, String cameraID) {
        long expend = System.currentTimeMillis();
        logger.info("yt delcamera params:{}", cameraID);

        String headerStr = String.format("session_id=%s;face_platform_session_id=%s",sid,sid);
        logger.info("yt delCamera headers:{}", headerStr);

        String result = ytApi.delCamera(headerStr, cameraID);
        logger.info("yt delCamera result:{}", result);

        expend = System.currentTimeMillis()-expend;
        logger.info("yt delcamera expend:{} ms", expend);
        return result;
    }

    @Override
    public String uploadPicToReopsitory(String sid, RlEntity rlEntity) {
        long expend = System.currentTimeMillis();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("repository_id",Integer.parseInt(rlEntity.getRLKID()));
        jsonObject.put("picture_image_content_base64",rlEntity.getBase64Pic());
        // jsonObject.put("face_image_type","0");
        jsonObject.put("name",rlEntity.getXM());
        // jsonObject.put("region","0");
        jsonObject.put("birthday",rlEntity.getCSNF());
        jsonObject.put("gender",rlEntity.getXB());
        // jsonObject.put("nation","0");
        jsonObject.put("person_id",rlEntity.getSFZH());
        logger.info("yt upload pic params:{}", jsonObject);

        String headerStr = String.format("session_id=%s;face_platform_session_id=%s",sid,sid);
        logger.info("yt upload pic headers:{}", headerStr);

        String result = ytApi.uploadPic(headerStr, jsonObject.toJSONString());
        logger.info("yt upload pic result:{}", result);

        expend = System.currentTimeMillis()-expend;
        logger.info("yt upload pic expend:{} ms", expend);
        return result;
    }

    /**
     * 下载图片
     * @param picUri
     * @return
     */
    @Override
    public boolean downLoadPic(String picUri,String localFile) throws IOException {
        long expend = System.currentTimeMillis();
        boolean result = false;
        // 获取base64图片地址
        BASE64Encoder base64Encoder = new BASE64Encoder();
        String baseUri = base64Encoder.encode(picUri.getBytes()).replaceAll("\r|\n", "");
        logger.info("yt download pic params: {}", baseUri);

        String requestUrl = String.format("%s/storage/v1/image?uri_base64=%s",ytServer,baseUri);
        HttpGet httpGet = new HttpGet(requestUrl);
        CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
        HttpResponse httpResponse = closeableHttpClient.execute(httpGet);
        if(200==httpResponse.getStatusLine().getStatusCode()){
            InputStream inputStream = httpResponse.getEntity().getContent();
            // result = FileUtils.streamToFile(inputStream,localFile);
            Files.copy(httpResponse.getEntity().getContent(), Paths.get(localFile));
            result = true;
        }
        expend = System.currentTimeMillis()-expend;
        logger.info("yt download pic expend:{} ms", expend);
        return result;
    }


}
