package com.dayang.cmtools.bean;

import java.util.List;

/**
 * Created by 冯傲 on 2017/3/27.
 * e-mail 897840134@qq.com
 */

public class ApkUpdateInfo {

    /**
     * version : 1.6.3
     * filePath : http://appservice.dayang.com/versionupdate/v1.6.3_Build20170302_163_jiagu_sign.apk
     * description : ["1、优化快速登录逻辑","2、优化后台帐户CAS认证信息处理","3、实现通过消息点击直接跳转至详情页面"]
     */

    private String version;
    private String filePath;
    private List<String> description;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public List<String> getDescription() {
        return description;
    }

    public void setDescription(List<String> description) {
        this.description = description;
    }
}
