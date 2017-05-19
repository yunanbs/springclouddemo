package com.sailing.facetec.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by yunan on 2017/5/16.
 * 布控任务
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BkrwEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private String XH; //	序号
    private String RWBH; //	任务编号 时间戳
    private String SXTID; //	摄像头id
    private String SBBH; //	设备编号
    private String RLKID; //	人脸库id
    private String QSSJ; //	布控有效期的起始时间
    private String ZZSJ; //	布控有效期的终止时间
    private String LGLX; //	列管类型
    private String LGYY; //	列管原因
    private String BKLX; //	布控类型
    private String BKYY; //	布控原因
    private String BKDW; //	布控单位
    private String BKZL; //	布控种类 （个人布控 公共布控）
    private String BKRY; //	布控人员
    private String BKZT; //	布控状态 （布控 撤控）
    private String BJFSX; //	报警分数线  80
    private String SFKBJ; // 是否可编辑
    private String BKID; //	添加布控任务返回的布控id
    private String TJSJ; //	添加时间
    private String XGSJ; //	修改时间
    private String TJRBH; //	添加人编号
    private String TJR; //	添加人
    private String XGRBH; //	修改人编号
    private String XGR; //	修改人
    private String RWZT; //	任务状态
    private String RWLX; //	任务类型
    private String RWJB; //	任务级别
    private String RWWCBL; //	任务完成比例
    private String FWIP; //	服务ip
    private String YLZD1; //	(null)
    private String YLZD2; //	(null)
    private String YLZD3; //	(null)
    private String YLZD4; //	(null)
    private String YLZD5; //	(null)

}
