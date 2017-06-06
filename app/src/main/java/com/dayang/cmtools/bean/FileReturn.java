package com.dayang.cmtools.bean;

import java.util.List;

public class FileReturn {
	   private String success;
	   private String description;
	   private List<FileInfos> fileInfos;
	   private FileInfos fileInfo;
	    public void setSuccess(String success) {
	        this.success = success;
	    }
	    public String getSuccess() {
	        return success;
	    }
	    

	    public void setDescription(String description) {
	        this.description = description;
	    }
	    public String getDescription() {
	        return description;
	    }
		public List<FileInfos> getFileInfos() {
			return fileInfos;
		}
		public void setFileInfos(List<FileInfos> fileInfos) {
			this.fileInfos = fileInfos;
		}
		public FileInfos getFileInfo() {
			return fileInfo;
		}
		public void setFileInfo(FileInfos fileInfo) {
			this.fileInfo = fileInfo;
		}
	    

	    
}
