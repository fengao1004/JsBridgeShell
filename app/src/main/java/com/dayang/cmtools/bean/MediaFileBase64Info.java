package com.dayang.cmtools.bean;

import java.util.List;

/**
 * Created by 冯傲 on 2017/3/20.
 * e-mail 897840134@qq.com
 */

public class MediaFileBase64Info {

    /**
     * taskId : tasked_1234
     * filesLocalPathArr : ["/mnt/1.jpg","/mnt/2.jpg"]
     */

    private String taskId;
    private List<String> filesLocalPathArr;

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public void setFilesLocalPathArr(List<String> filesLocalPathArr) {
        this.filesLocalPathArr = filesLocalPathArr;
    }

    public String getTaskId() {
        return taskId;
    }

    public List<String> getFilesLocalPathArr() {
        return filesLocalPathArr;
    }
}
