package com.dayang.cmtools.bean;

/**
 * Created by 冯傲 on 2017/3/24.
 * e-mail 897840134@qq.com
 */

public class PlayFileInfo {

    /**
     * fileInfo : {"fileType":1,"filePath":"/mnt/1.wmv"}
     */

    private FileInfoEntity fileInfo;

    public void setFileInfo(FileInfoEntity fileInfo) {
        this.fileInfo = fileInfo;
    }

    public FileInfoEntity getFileInfo() {
        return fileInfo;
    }

    public static class FileInfoEntity {
        /**
         * fileType : 1
         * filePath : /mnt/1.wmv
         */

        private int fileType;
        private String filePath;

        public void setFileType(int fileType) {
            this.fileType = fileType;
        }

        public void setFilePath(String filePath) {
            this.filePath = filePath;
        }

        public int getFileType() {
            return fileType;
        }

        public String getFilePath() {
            return filePath;
        }
    }
}

