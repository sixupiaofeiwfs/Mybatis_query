package com.wfs.mapper;

import com.wfs.pojo.Emp;
import com.wfs.pojo.Grade;
import com.wfs.pojo.Student;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface QueryMapper {

    /**
     * 通过姓名查询员工信息
     */

    Emp queryEmpByName(String name);


    /**
     *
     * 查询所有的员工信息
     */

    List<Emp> queryAllEmp();


    /**
     * 验证登录
     */

    int checkLogin(@Param("name") String name,@Param("pwd") String password);


    /**
     * 验证登录（MAP）
     */

    int checkLoginByMap(Map map);


    /**
     * 添加员工
     */
    int insertEmp(Emp emp);


    /**
     * 解决pojo类中的属性与数据库中的字段名映射问题 pojo类中的属性名是sName 字段名是s_name
     *
     */

    List<Student> getAllStudent();


    /**
     * 多对一问题  多个学生同在一个班级   查询某个学生的信息
     */
    Student getStudentAndGrade(@Param("sid") Integer sid);


    /**
     * 多对一问题 分布查询第一步  只查学生信息
     */

    Student getStudentAndGradeByStep1(@Param("sid") Integer sid);


    /**
     * 多对一问题 分布查询第二步  只查班级信息
     */

    Grade getStudentAndGradeByStep2(@Param("gid") Integer gid);


    /**
     * 一对多问题  一个班级有多名学生
        第一种方式：级联属性
        第二种方式：分布查询
     */

    Grade getGradeAndStudent(@Param("gid") Integer gid);


    //第一步：先查班级信息
    Grade getGradeAndStudentByStep1(@Param("gid") Integer gid);

    //第二步：查询学生信息
    List<Student> getGradeAndStudentByStep2(@Param("gid") Integer gid);


    /**
     * 模糊查询  查找姓名中带有“三”的学生
     */

    Student getStudentByName(@Param("stuName") String name);

    /**
     * 批量删除
     */
    int deleteStudentsByIds(@Param("ids") String ids);


    /**
     * 动态设置表名
     */

    Student getStudentByTableName(@Param("tableName") String tableName);


    /**\
     *动态sql语句查询----if
     */

    List<Student>  getStudentByIf( Student student);


    /**
     * 动态语句----where
     */
    List<Student>  getStudentByWhere( Student student);


    /**
     * 动态语句---trim
     */
    List<Student>  getStudentByTrim( Student student);


    /**
     * 动态语句----choose when  otherwise
     */

    List<Student>  getStudentByCWO( Student student);


    /**
     * 动态语句----foreach  批量删除
     */

     int deleteMoreStudent(@Param("sids") Integer[] sids);


    /**
     * 动态语句---foreach 批量插入
     */
     int insertStudnet(List<Student> list);
}
