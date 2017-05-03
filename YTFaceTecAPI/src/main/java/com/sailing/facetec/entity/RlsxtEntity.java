package com.sailing.facetec.entity;

import java.io.Serializable;

/**
 * Created by yunan on 2017/4/28.
 */
public class RlsxtEntity implements Serializable{

    private static final long serialVersionID=1L;

    private Long XH;
    private String SBBH;
    private String SXTID;
    private String SXTMC;
    private Integer SXTLX;
    private String SPDZ;
    private String LRKID;
    private String YLZD1;
    private String YLZD2;
    private String YLZD3;
    private String YLZD4;
    private String YLZD5;

    public RlsxtEntity() {
        super();
    }

    public Long getXH() {
        return XH;
    }

    public void setXH(Long XH) {
        this.XH = XH;
    }

    public String getSBBH() {
        return SBBH;
    }

    public void setSBBH(String SBBH) {
        this.SBBH = SBBH;
    }

    public String getSXTID() {
        return SXTID;
    }

    public void setSXTID(String SXTID) {
        this.SXTID = SXTID;
    }

    public String getSXTMC() {
        return SXTMC;
    }

    public void setSXTMC(String SXTMC) {
        this.SXTMC = SXTMC;
    }

    public Integer getSXTLX() {
        return SXTLX;
    }

    public void setSXTLX(Integer SXTLX) {
        this.SXTLX = SXTLX;
    }

    public String getSPDZ() {
        return SPDZ;
    }

    public void setSPDZ(String SPDZ) {
        this.SPDZ = SPDZ;
    }

    public String getLRKID() {
        return LRKID;
    }

    public void setLRKID(String LRKID) {
        this.LRKID = LRKID;
    }

    public String getYLZD1() {
        return YLZD1;
    }

    public void setYLZD1(String YLZD1) {
        this.YLZD1 = YLZD1;
    }

    public String getYLZD2() {
        return YLZD2;
    }

    public void setYLZD2(String YLZD2) {
        this.YLZD2 = YLZD2;
    }

    public String getYLZD3() {
        return YLZD3;
    }

    public void setYLZD3(String YLZD3) {
        this.YLZD3 = YLZD3;
    }

    public String getYLZD4() {
        return YLZD4;
    }

    public void setYLZD4(String YLZD4) {
        this.YLZD4 = YLZD4;
    }

    public String getYLZD5() {
        return YLZD5;
    }

    public void setYLZD5(String YLZD5) {
        this.YLZD5 = YLZD5;
    }

    @Override
    public String toString() {
        return "RlsxtEntity{" +
                "XH=" + XH +
                ", SBBH='" + SBBH + '\'' +
                ", SXTID='" + SXTID + '\'' +
                ", SXTMC='" + SXTMC + '\'' +
                ", SXTLX=" + SXTLX +
                ", SPDZ='" + SPDZ + '\'' +
                ", LRKID='" + LRKID + '\'' +
                ", YLZD1='" + YLZD1 + '\'' +
                ", YLZD2='" + YLZD2 + '\'' +
                ", YLZD3='" + YLZD3 + '\'' +
                ", YLZD4='" + YLZD4 + '\'' +
                ", YLZD5='" + YLZD5 + '\'' +
                '}';
    }
}
