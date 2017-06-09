package com.sailing.facetec.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sailing.facetec.entity.RlEntity;
import com.sailing.facetec.remoteservice.YTApi;
import com.sailing.facetec.remoteservice.YTTZApi;
import com.sailing.facetec.service.YTService;
import com.sailing.facetec.util.CommUtils;
import com.sun.org.apache.bcel.internal.generic.ReturnInstruction;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sun.security.util.Resources_sv;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
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

    @Autowired
    private YTTZApi yttzApi;

    @Value("${ytface.api-url}")
    private String ytServer;

    @Value("${ytface.username}")
    private String ytUsername;

    @Value("${ytface.password}")
    private String ytPassword;

    @Value("${ytface.cookie}")
    private String ytCookie;

    @Override
    public String login() {
        long expend = System.currentTimeMillis();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name",ytUsername);
        jsonObject.put("password",ytPassword);
        logger.info("yt login params:{}", jsonObject);

        String result = ytApi.login(jsonObject.toJSONString());
        logger.info("yt login result:{}", result);

        expend = System.currentTimeMillis()-expend;
        logger.info("yt login expend:{} ms", expend);
        return JSONObject.parseObject(result).getString("session_id");
    }

    @Override
    public String addCamera(String sid,String cameraName, String url, int cameraType) {
        long expend = System.currentTimeMillis();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name",cameraName);
        jsonObject.put("url",url);
        jsonObject.put("type",cameraType);
        logger.info("yt addCamera params:{}", jsonObject);


        String headerStr = String.format(ytCookie,sid,sid);
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

    /**
     * 新增人脸布控任务
     * @param sid
     * @param cameraId
     * @param repositoryId
     * @param threshold
     * @param stSec
     * @param endSec
     * @return
     */
    @Override
    public String setMonitorRepository(String sid,int cameraId, int repositoryId, double threshold, long stSec, long endSec,String name) {
        long expend = System.currentTimeMillis();

        JSONObject metajsonObject=new JSONObject();
        metajsonObject.put("createdTime",System.currentTimeMillis()/ 1000);
        metajsonObject.put("name",name);
        metajsonObject.put("sid",System.currentTimeMillis()/ 1000);


        JSONObject jsonObject = new JSONObject();
        jsonObject.put("camera_id",cameraId);
        jsonObject.put("repository_id",repositoryId);
        jsonObject.put("threshold",threshold);
        jsonObject.put("max_time_span",stSec);
        jsonObject.put("min_time_span",endSec);
        jsonObject.put("meta",metajsonObject);

        String headerStr = String.format("session_id=%s;face_platform_session_id=%s",sid,sid);

        expend = System.currentTimeMillis()-expend;
        logger.info("yt setMonitorRepository expend:{} ms", expend);
        return ytApi.setMonitorRepository(headerStr, jsonObject.toJSONString());
    }

    /**
     * 删除布控任务
     *
     * @param id 布控任务id
     * @return
     */
    @Override
    public String delMonitorReposity(String sid,Long id) {
        long expend = System.currentTimeMillis();

        String headerStr = String.format("session_id=%s;face_platform_session_id=%s",sid,sid);
        logger.info("yt delMonitorTask headerStr:{}", headerStr);
        expend = System.currentTimeMillis()-expend;
        logger.info("yt delMonitorTask result:{}", expend);
        String result =ytApi.delMonitorRepository(headerStr, id);
        logger.info("yt delMonitorTask result:{}", result);
        return result;
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

    /**
     * 删除摄像头
     * @param sid
     * @param cameraID
     * @return
     */
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

    /**
     * 新增人脸库
     * @param sid
     * @param facelibName
     * @param comment
     * @return
     */
    @Override
    public String addFaceLib(String sid, String facelibName, String comment) {
        long expend = System.currentTimeMillis();

        logger.info("yt addFaceLib params:{}", facelibName);
        logger.info("yt addFaceLib params:{}", comment);

        String headerStr = String.format("session_id=%s;face_platform_session_id=%s",sid,sid);
        logger.info("yt addFaceLib headers:{}", headerStr);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name",facelibName);
        jsonObject.put("comment",comment);
        String result = ytApi.addFaceLib(headerStr, jsonObject.toJSONString());
        logger.info("yt addFaceLib result:{}", result);

        expend = System.currentTimeMillis()-expend;
        logger.info("yt addFaceLib expend:{} ms", expend);
        return result;
    }


    /**
     * 删除人脸库
     * @param sid
     * @param rlkID
     * @return
     */
    @Override
    public String delFaceLib(String sid, String rlkID) {
        long expend = System.currentTimeMillis();


        logger.info("yt delfacelib params:{}", rlkID);

        String headerStr = String.format("session_id=%s;face_platform_session_id=%s",sid,sid);
        logger.info("yt delfacelib headers:{}", headerStr);

        String result = ytApi.delFaceLib(headerStr, rlkID);
        logger.info("yt delfacelib result:{}", result);

        expend = System.currentTimeMillis()-expend;
        logger.info("yt delfacelib expend:{} ms", expend);
        return result;
    }

    /**
     * 修改人员信息
     * @param sid
     * @param rlid
     * @param xm
     * @param qybh
     * @param csnf
     * @param xb
     * @param mz
     * @param sfzh
     * @return
     */
    @Override
    public String altPersonalInfo(String sid, String rlid, String xm, String qybh, String csnf, String xb, String mz, String sfzh) {

        long expend = System.currentTimeMillis();

        JSONObject jsonObject=new JSONObject();
        JSONObject jsonSet=new JSONObject();
        JSONObject jsonUpdate=new JSONObject();

        jsonObject.put("face_image_id",Long.parseLong(rlid));

        if(xm!=null)
        {
            jsonSet.put("name",xm);
        }
        if(qybh!=null)
        {
            jsonSet.put("region",Integer.parseInt(qybh));
        }
        if(csnf!=null)
        {
            jsonSet.put("birthday",csnf);
        }
        if(xb!=null)
        {
            jsonSet.put("gender",Integer.parseInt(xb));
        }
        if(mz!=null)
        {
            jsonSet.put("nation",Integer.parseInt(mz));
        }
        if(sfzh!=null)
        {
            jsonSet.put("person_id",sfzh);
        }
        jsonUpdate.put("$set",jsonSet);

        jsonObject.put("update",jsonUpdate);

        logger.info("yt altPersonalInfo params:{}", jsonObject.toJSONString());

        String headerStr = String.format("session_id=%s;face_platform_session_id=%s",sid,sid);
        logger.info("yt altPersonalInfo headers:{}", headerStr);

        String result = ytApi.altPersonalInfo(headerStr, jsonObject.toJSONString());
        logger.info("yt altPersonalInfo result:{}", result);

        expend = System.currentTimeMillis()-expend;
        logger.info("yt altPersonalInfo expend:{} ms", expend);
        return result;
    }

    /**
     * 删除人员
     * @param sid
     * @param rlid
     * @return
     */
    @Override
    public String delPersonal(String sid, String rlid) {
        long expend = System.currentTimeMillis();
        String result;
        JSONObject jsonObject=new JSONObject();
        Long face_image_id=Long.parseLong(rlid);
        jsonObject.put("face_image_id",face_image_id);
        logger.info("yt delPersonal face_image_id:{}", rlid);
        String headerStr = String.format("session_id=%s;face_platform_session_id=%s",sid,sid);
        logger.info("yt delPersonal headers:{}", headerStr);
        result = ytApi.delPersonal(headerStr, jsonObject.toJSONString());
        logger.info("yt delPersonal result:{}", result);
        logger.info("yt delPersonal used:{}", System.currentTimeMillis()-expend);
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
        // BASE64Encoder base64Encoder = new BASE64Encoder();
        // String baseUri = base64Encoder.encode(picUri.getBytes()).replaceAll("\r|\n", "");
        String baseUri = Base64.encodeBase64String(picUri.getBytes()).replaceAll("\r|\n", "");
        logger.info("yt download pic params: {}", baseUri);

        String requestUrl = String.format("%s/storage/v1/image?uri_base64=%s",ytServer,baseUri);
        HttpGet httpGet = new HttpGet(requestUrl);
        CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
        HttpResponse httpResponse = closeableHttpClient.execute(httpGet);
        if(200==httpResponse.getStatusLine().getStatusCode()){
            InputStream inputStream = httpResponse.getEntity().getContent();
            // result = FileUtils.streamToFile(inputStream,localFile);
            Files.copy(inputStream, Paths.get(localFile), StandardCopyOption.REPLACE_EXISTING);
            result = true;
        }
        expend = System.currentTimeMillis()-expend;
        logger.info("yt download pic expend:{} ms", expend);
        return result;
    }

    /**
     * 获取人脸对应的特征码
     *
     * @param faceID
     * @return
     */
    @Override
    public String getSpecByFaceID(String faceID) {
        byte[] spec = yttzApi.getSpecByID(faceID);
        return Base64.encodeBase64String(spec);
    }

    /**
     * 获取人像库特征码
     *
     * @param repoID
     * @return
     */
    @Override
    public String getSpecByRepoID(String repoID) {
        return yttzApi.getSpecByRepoID(repoID);
    }

    /**
     * 上传检索图片
     *
     * @param faceStr
     * @return
     */
    @Override
    public String uploadFaceToQuery(String faceStr) {

        long expend = System.currentTimeMillis();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("picture_image_content_base64",faceStr);
        String result = ytApi.uploadPic("",jsonObject.toJSONString());
        expend = System.currentTimeMillis()-expend;
        logger.info("yt upload face pic expend:{} ms", expend);
        return result;
    }

    /**
     * 人像检索
     *
     * @param faceid
     * @return
     */
    @Override
    public String queryFacesByID(String sid,long faceid,String[] repositorys,double threshold,String[] fields,JSONObject conditions,JSONObject order,int start,int limit) {

        long expend = System.currentTimeMillis();
        JSONObject jsonObject = new JSONObject();

        // 组装retrieval
        JSONObject retrie = new JSONObject();
        retrie.put("face_image_id",faceid);

        List<Integer> repos = new ArrayList<>();
        for(String repo : repositorys){
            repos.add(Integer.parseInt(repo));
        }
        retrie.put("repository_ids",repos);
        retrie.put("threshold",threshold);
        jsonObject.put("retrieval",retrie);

        jsonObject.put("fields",fields);
        jsonObject.put("condition",CommUtils.isNullObject(conditions)?new JSONObject():conditions);

        if(CommUtils.isNullObject(order)){
            order = new JSONObject();
            order.put("timestamp",-1);
        }
        jsonObject.put("order",order);

        jsonObject.put("start",start);
        jsonObject.put("limit",limit);

        String result = ytApi.queryByFaceID(String.format(ytCookie,sid,sid),jsonObject.toJSONString());

        expend = System.currentTimeMillis()-expend;
        logger.info("yt query face pic expend:{} ms", expend);
        return result;
    }

}
