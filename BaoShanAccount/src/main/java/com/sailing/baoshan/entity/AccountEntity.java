package com.sailing.baoshan.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by yunan on 2017/6/20.
 * 统计结果实体类
 */
@Data
public class AccountEntity implements Serializable {
    private static final long serialVersionID = -1L;
    // 序号
    private String id;
    // 统计字段
    private String accountKey;
    // 统计结果
    private String accountValue;
    // 统计标签
    private String accountTag;
}
