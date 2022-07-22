package com.wfs.pojo;

import java.util.List;

public class Grade {

    private Integer gid;

    private String gName;


    private List<Student> list;

    @Override
    public String toString() {
        return "Grade{" +
                "gid=" + gid +
                ", gName='" + gName + '\'' +
                ", list=" + list +
                '}';
    }

    public List<Student> getList() {
        return list;
    }

    public void setList(List<Student> list) {
        this.list = list;
    }

    public Grade() {
    }

    public Grade(Integer gid, String gName) {
        this.gid = gid;
        this.gName = gName;
    }

    public Integer getGid() {
        return gid;
    }

    public void setGid(Integer gid) {
        this.gid = gid;
    }

    public String getgName() {
        return gName;
    }

    public void setgName(String gName) {
        this.gName = gName;
    }
}
