package com.dayang.cmtools.bean;

import java.util.List;

/**
 * Created by 冯傲 on 2017/3/24.
 * e-mail 897840134@qq.com
 */

public class ShellGetThumbnailsInfo {

    /**
     * taskId : tasked_1111
     * filesLocalPathArr : [{"path":"/mnt/1.jpg","indexNO":0},{"path":"/mnt/2.jpg","indexNO":1}]
     */

    private String taskId;
    private List<FilesLocalPathArrEntity> filesLocalPathArr;

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public void setFilesLocalPathArr(List<FilesLocalPathArrEntity> filesLocalPathArr) {
        this.filesLocalPathArr = filesLocalPathArr;
    }

    public String getTaskId() {
        return taskId;
    }

    public List<FilesLocalPathArrEntity> getFilesLocalPathArr() {
        return filesLocalPathArr;
    }

    public static class FilesLocalPathArrEntity {
        /**
         * path : /mnt/1.jpg
         * indexNO : 0
         */

        private String path;
        private int indexNO;

        public void setPath(String path) {
            this.path = path;
        }

        public void setIndexNO(int indexNO) {
            this.indexNO = indexNO;
        }

        public String getPath() {
            return path;
        }

        public int getIndexNO() {
            return indexNO;
        }
    }
}
