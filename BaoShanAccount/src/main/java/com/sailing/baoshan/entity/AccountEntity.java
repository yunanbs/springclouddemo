package com.sailing.baoshan.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by yunan on 2017/6/20.
 * 统计结果实体类
 */
@Data
public class AccountEntity implements Serializable{
    private static final long serialVersionID = -1L;

    private String id;
    private String accountKey;
    private String accountValue;
    private String accountTag;
}
