package com.sailing.facetec.entity;

import java.math.BigInteger;

/**
 * Created by eagle on 2017-5-4.
 */
public class YtRetrievalResultDetailEntity {
    private int annotation;
    private int born_year;
    private BigInteger face_image_id;
    private String face_image_id_str;
    private String face_image_uri;
    private String face_image_url;
    private int gender;
    private boolean is_writable;
    private String name;
    private int nation;
    private String person_id;
    private String picture_uri;
    private String picture_url;
    private int repository_id;
    private double similarity;
    private int timestamp;
    public int getAnnotation() {
        return annotation;
    }
    public void setAnnotation(int annotation) {
        this.annotation = annotation;
    }
    public int getBorn_year() {
        return born_year;
    }
    public void setBorn_year(int born_year) {
        this.born_year = born_year;
    }

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
    public int getGender() {
        return gender;
    }
    public void setGender(int gender) {
        this.gender = gender;
    }
    public boolean isIs_writable() {
        return is_writable;
    }
    public void setIs_writable(boolean is_writable) {
        this.is_writable = is_writable;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getNation() {
        return nation;
    }
    public void setNation(int nation) {
        this.nation = nation;
    }
    public String getPerson_id() {
        return person_id;
    }
    public void setPerson_id(String person_id) {
        this.person_id = person_id;
    }
    public String getPicture_uri() {
        return picture_uri;
    }
    public void setPicture_uri(String picture_uri) {
        this.picture_uri = picture_uri;
    }
    public String getPicture_url() {
        return picture_url;
    }
    public void setPicture_url(String picture_url) {
        this.picture_url = picture_url;
    }
    public int getRepository_id() {
        return repository_id;
    }
    public void setRepository_id(int repository_id) {
        this.repository_id = repository_id;
    }
    public double getSimilarity() {
        return similarity;
    }
    public void setSimilarity(double similarity) {
        this.similarity = similarity;
    }
    public int getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }
}
