package com.sailing.facetec.entity;

import java.math.BigInteger;

/**
 * Created by eagle on 2017-5-4.
 */
public class YtImportResultDetailEntity {

    private BigInteger face_image_id;
    private String face_image_id_str;
    private String face_image_uri;
    private String face_image_url;
    private YtFaceRectEntity face_rect;

    public BigInteger getFace_image_id() {
        return face_image_id;
    }
    public void setFace_image_id(BigInteger face_image_id) {
        this.face_image_id = face_image_id;
    }
    public String getFace_image_id_str() {
        return face_image_id_str;
    }
    public void setFace_image_id_str(String face_image_id_str) {
        this.face_image_id_str = face_image_id_str;
    }
    public String getFace_image_uri() {
        return face_image_uri;
    }
    public void setFace_image_uri(String face_image_uri) {
        this.face_image_uri = face_image_uri;
    }
    public String getFace_image_url() {
        return face_image_url;
    }
    public void setFace_image_url(String face_image_url) {
        this.face_image_url = face_image_url;
    }
    public YtFaceRectEntity getFace_rect() {
        return face_rect;
    }
    public void setFace_rect(YtFaceRectEntity face_rect) {
        this.face_rect = face_rect;
    }

}
