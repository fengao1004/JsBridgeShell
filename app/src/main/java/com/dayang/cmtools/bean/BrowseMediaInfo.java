package com.dayang.cmtools.bean;

import java.util.List;

/**
 * Created by 冯傲 on 2017/3/20.
 * e-mail 897840134@qq.com
 */

public class BrowseMediaInfo {

    /**
     * allFiles : [{"fileType":0,"filePath":"/mnt/1.jpg","thumbPath ":"/mnt/1.jpg"}]
     * currentClick : {"fileType":0,"filePath":"/mnt/1.jpg"}
     */

    private CurrentClickEntity currentClick;
    private List<AllFilesEntity> allFiles;

    public void setCurrentClick(CurrentClickEntity currentClick) {
        this.currentClick = currentClick;
    }

    public void setAllFiles(List<AllFilesEntity> allFiles) {
        this.allFiles = allFiles;
    }

    public CurrentClickEntity getCurrentClick() {
        return currentClick;
    }

    public List<AllFilesEntity> getAllFiles() {
        return allFiles;
    }

    public static class CurrentClickEntity {
        /**
         * fileType : 0
         * filePath : /mnt/1.jpg
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

    public static class AllFilesEntity {
        /**
         * fileType : 0
         * filePath : /mnt/1.jpg
         * thumbPath  : /mnt/1.jpg
         */

        private int fileType;
        private String filePath;
        private String thumbPath;

        public void setFileType(int fileType) {
            this.fileType = fileType;
        }

        public void setFilePath(String filePath) {
            this.filePath = filePath;
        }

        public void setThumbPath(String thumbPath) {
            this.thumbPath = thumbPath;
        }

        public int getFileType() {
            return fileType;
        }

        public String getFilePath() {
            return filePath;
        }

        public String getThumbPath() {
            return thumbPath;
        }
    }
}
