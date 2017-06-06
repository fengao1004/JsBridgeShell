package com.dayang.cmtools.bean;

import java.util.List;

public class BrowerBase {
    private String success;
    private String description;
    private String taskId;
    private List<FileInfos> fileInfos;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<FileInfos> getFileInfos() {
        return fileInfos;
    }

    public void setFileInfos(List<FileInfos> fileInfos) {
        this.fileInfos = fileInfos;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

}