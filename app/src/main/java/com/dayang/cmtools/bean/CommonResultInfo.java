package com.dayang.cmtools.bean;

/**
 * Created by 冯傲 on 2017/3/20.
 * e-mail 897840134@qq.com
 */

public class CommonResultInfo {

    /**
     * success : false
     * description :
     */

    private String success;
    private String description;

    public CommonResultInfo(String success, String description) {
        this.success = success;
        this.description = description;
    }

    public CommonResultInfo() {
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSuccess() {
        return success;
    }

    public String getDescription() {
        return description;
    }
}
