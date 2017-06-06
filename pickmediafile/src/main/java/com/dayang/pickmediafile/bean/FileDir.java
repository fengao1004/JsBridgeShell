package com.dayang.pickmediafile.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 冯傲 on 2017/3/17.
 * e-mail 897840134@qq.com
 */

public class FileDir {
    public FileDir(ArrayList<String> files, String name) {
        this.files = files;
        this.name = name;
    }

    public FileDir() {
    }

    public List<String> files;
    public String name;
}
