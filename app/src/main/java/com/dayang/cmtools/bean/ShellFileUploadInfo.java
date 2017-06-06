package com.dayang.cmtools.bean;

import java.util.List;

/**
 * Created by 冯傲 on 2017/3/24.
 * e-mail 897840134@qq.com
 */

public class ShellFileUploadInfo {

    /**
     * taskId : tasked_1111
     * tenantId : 033DC39-44C5-80A2-97C7-30E67F555559
     * storageURL : http://server:port/huiju/upload
     * fileStatusNotifyURL : http://server:port/huiju/fileStatus
     * remoteRootPath : /huiju
     * filesLocalPathArr : [{"fileSessionId":"033DC39-44C5-80A2-97C7-30E67F555559","isRename":false,"path":"/mnt/1.jpg","indexNO":0},{"fileSessionId":"033DC39-44C5-80A2-97C7-30E67F555559","isRename":false,"path":"/mnt/2.jpg","indexNO":1},"..."]
     * async : true
     */

    private String taskId;
    private String tenantId;
    private String storageURL;
    private String fileStatusNotifyURL;
    private String uploadTrunkInfoURL;
    private String remoteRootPath;
    private boolean async;
    private List<FilesLocalPathArrEntity> filesLocalPathArr;

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public void setUploadTrunkInfoURL(String uploadTrunkInfoURL) {
        this.uploadTrunkInfoURL = uploadTrunkInfoURL;
    }

    public String getUploadTrunkInfoURL() {
        if (uploadTrunkInfoURL == null) {
            return "";
        }
        return uploadTrunkInfoURL;
    }

    public void setStorageURL(String storageURL) {

        this.storageURL = storageURL;
    }

    public void setFileStatusNotifyURL(String fileStatusNotifyURL) {
        this.fileStatusNotifyURL = fileStatusNotifyURL;
    }

    public void setRemoteRootPath(String remoteRootPath) {
        this.remoteRootPath = remoteRootPath;
    }

    public void setAsync(boolean async) {
        this.async = async;
    }

    public void setFilesLocalPathArr(List<FilesLocalPathArrEntity> filesLocalPathArr) {
        this.filesLocalPathArr = filesLocalPathArr;
    }

    public String getTaskId() {
        if (taskId == null) {
            return "";
        }
        return taskId;
    }

    public String getTenantId() {
        if (tenantId == null) {
            return "";
        }
        return tenantId;
    }

    public String getStorageURL() {
        if (storageURL == null) {
            return "";
        }
        return storageURL;
    }

    public String getFileStatusNotifyURL() {
        if (fileStatusNotifyURL == null) {
            return "";
        }
        return fileStatusNotifyURL;
    }

    public String getRemoteRootPath() {
        if (remoteRootPath == null) {
            return "";
        }
        return remoteRootPath;
    }

    public boolean getAsync() {
        return async;
    }

    public List<FilesLocalPathArrEntity> getFilesLocalPathArr() {
        return filesLocalPathArr;
    }

    public static class FilesLocalPathArrEntity {
        /**
         * fileSessionId : 033DC39-44C5-80A2-97C7-30E67F555559
         * isRename : false
         * path : /mnt/1.jpg
         * indexNO : 0
         */

        private String fileSessionId;
        private boolean isRename;
        private String path;
        private int indexNO;

        public void setFileSessionId(String fileSessionId) {
            this.fileSessionId = fileSessionId;
        }

        public void setIsRename(boolean isRename) {
            this.isRename = isRename;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public void setIndexNO(int indexNO) {
            this.indexNO = indexNO;
        }

        public String getFileSessionId() {
            if (fileSessionId == null) {
                return "";
            }
            return fileSessionId;
        }

        public boolean getIsRename() {
            return isRename;
        }

        public String getPath() {
            if (path == null) {
                return "";
            }
            return path;
        }

        public int getIndexNO() {
            return indexNO;
        }
    }
}
