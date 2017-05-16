package com.sailing.facetec.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by yunan on 2017/5/16.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BkrwEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    String XH; //	序号
    String RWBH; //	任务编号 时间戳
    String SXTID; //	摄像头id
    String SBBH; //	设备编号
    String RLKID; //	人脸库id
    String QSSJ; //	布控有效期的起始时间
    String ZZSJ; //	布控有效期的终止时间
    String LGLX; //	列管类型
    String LGYY; //	列管原因
    String BKLX; //	布控类型
    String BKYY; //	布控原因
    String BKDW; //	布控单位
    String BKZL; //	布控种类 （个人布控 公共布控）
    String BKRY; //	布控人员
    String BKZT; //	布控状态 （布控 撤控）
    String BJFSX; //	报警分数线  80
    String SFKBJ; // 是否可编辑
    String BKID; //	添加布控任务返回的布控id
    String TJSJ; //	添加时间
    String XGSJ; //	修改时间
    String TJRBH; //	添加人编号
    String TJR; //	添加人
    String XGRBH; //	修改人编号
    String XGR; //	修改人
    String RWZT; //	任务状态
    String RWLX; //	任务类型
    String RWJB; //	任务级别
    String RWWCBL; //	任务完成比例
    String FWIP; //	服务ip
    String YLZD1; //	(null)
    String YLZD2; //	(null)
    String YLZD3; //	(null)
    String YLZD4; //	(null)
    String YLZD5; //	(null)

}
