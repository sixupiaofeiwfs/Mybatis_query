package com.wfs.pojo;

public class Student {

    private Integer sid;

    private String stuName;

    private Integer age;

    private Integer sex;

    private String tel;

    private Grade grade;


    @Override
    public String toString() {
        return "Student{" +
                "sid=" + sid +
                ", stuName='" + stuName + '\'' +
                ", age=" + age +
                ", sex=" + sex +
                ", tel='" + tel + '\'' +
                ", grade=" + grade +
                '}';
    }

    public Grade getGrade() {
        return grade;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }

    public Student() {
    }

    public Student(Integer sid, String stuName, Integer age, Integer sex, String tel) {
        this.sid = sid;
        this.stuName = stuName;
        this.age = age;
        this.sex = sex;
        this.tel = tel;
    }

    public Integer getSid() {
        return sid;
    }

    public void setSid(Integer sid) {
        this.sid = sid;
    }

    public String getStuName() {
        return stuName;
    }

    public void setStuName(String stuName) {
        this.stuName = stuName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }


}
