package com.wfs.test;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wfs.mapper.QueryMapper;
import com.wfs.pojo.Emp;
import com.wfs.pojo.Student;
import com.wfs.util.SqlSessionUtil;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueryTest {

    SqlSessionUtil sqlSessionUtil=new SqlSessionUtil();
    SqlSession sqlSession;
    @Test
    public void queryEmpByName(){

      sqlSession = sqlSessionUtil.getSqlSession();



      QueryMapper queryMapper= sqlSession.getMapper(QueryMapper.class);

      String name="张三";
      Emp emp= queryMapper.queryEmpByName(name);

        System.out.println(emp);


    }


    @Test
    public void queryAllEmp(){
        sqlSession=sqlSessionUtil.getSqlSession();
        QueryMapper queryMapper=sqlSession.getMapper(QueryMapper.class);
        System.out.println(  queryMapper.queryAllEmp());
    }


    @Test
    public void checkLogin(){
        sqlSession=sqlSessionUtil.getSqlSession();
        QueryMapper queryMapper=sqlSession.getMapper(QueryMapper.class);
        String name="张三";
        String password="123456";
        System.out.println(queryMapper.checkLogin(name, password));

    }



    @Test
    public void checkLoginByMap(){
        sqlSession=sqlSessionUtil.getSqlSession();
        QueryMapper queryMapper=sqlSession.getMapper(QueryMapper.class);
        Map map=new HashMap();
        map.put("name","张三");
        map.put("password","123456");
        System.out.println(queryMapper.checkLoginByMap(map));
    }

    @Test
    public void insertEmp(){
        sqlSession=sqlSessionUtil.getSqlSession();
        QueryMapper queryMapper=sqlSession.getMapper(QueryMapper.class);
        System.out.println(queryMapper.insertEmp(new Emp(null,"宋八",16,"424@qq.com",0,"424224")));
    }




    @Test
    public void getAllStudent(){
        //不作任何处理时 查询到的数据如下，由于pojo类中的属性sName与数据库中的s_name没有映射成功，所以所有的sName都为null
        //[Student{sid=1, sName='null', age=13, sex=1, tel='15565656565'},
        // Student{sid=2, sName='null', age=12, sex=0, tel='13345255125'},
        // Student{sid=3, sName='null', age=15, sex=1, tel='18878965689'},
        // Student{sid=4, sName='null', age=14, sex=1, tel='16532676236'},
        // Student{sid=5, sName='null', age=13, sex=0, tel='13082665456'}]



        //第一种方式：在mybatis_config.xml中添加
        //      <settings>
        //        <setting name="mapUnderscoreToCamelCase" value="true"/>  数据库中的下划线字段自动转换成驼峰命名
        //    </settings>



        //第二种方式：使用别名将下划线字段改为驼峰命名的方式
        //在XXXMapper.xml中，将 select * from student  改为 select sid, stu_name stuName,age,sex,tel  from student



        //第三种方式：使用自定义map的方式
        //在XXXMapper.xml 文件中，select中不能使用resultType 应该为resultMap  然后在XXXMapper.xml文件中创建<resultMap> 如下所示：
        //**
//         <resultMap id="resultMap" type="student">
//        <id property="sid" column="sid"></id>
//        <result property="stuName" column="stu_name"></result>
//        <result property="age" column="age"></result>
//        <result property="sex" column="sex"></result>
//        <result property="tel" column="tel"></result>
//      </resultMap>
//          <select id="getAllStudent" resultMap="resultMap">
//                select * from student
//                </select>


        sqlSession=sqlSessionUtil.getSqlSession();
        QueryMapper queryMapper=sqlSession.getMapper(QueryMapper.class);
        PageHelper.startPage(2,4);
        List<Student> list=new ArrayList<Student>();
        list=queryMapper.getAllStudent();
        PageInfo pageInfo=new PageInfo(list,2);
        System.out.println(list);
    }





    //多对一   多个学生在一个班级

    /**
     * 第一种方式：采用级联属性赋值的方式
     *  <resultMap id="getStudentAndGradeResultMap" type="student">
     *         <id property="sid" column="sid"></id>
     *         <result property="stuName" column="stu_name"></result>
     *         <result property="age" column="age"></result>
     *         <result property="sex" column="sex"></result>
     *         <result property="tel" column="tel"></result>
     *
     *         <result property="grade.gid" column="gid"></result>
     *         <result property="grade.gName" column="g_name"></result>
     *
     *     </resultMap>
     *     <!-- Student getStudentAndGrade(@Param("sid") Integer sid);-->
     *     <select id="getStudentAndGrade" resultMap="getStudentAndGradeResultMap">
     *         select * from student left join grade on student.gid= grade.gid  where sid=#{sid}
     *     </select>
     *
     *
     *
     *
     *
     *
     * 第二种方式：采用association的方式
     *
     <resultMap id="getStudentAndGradeResultMap2" type="student">
     <id property="sid" column="sid"></id>
     <result property="stuName" column="stu_name"></result>
     <result property="age" column="age"></result>
     <result property="sex" column="sex"></result>
     <result property="tel" column="tel"></result>

     <association property="grade" javaType="grade">
     <id property="gid" column="gid"></id>
     <result property="gName" column="g_name"></result>
     </association>

     </resultMap>
     <!-- Student getStudentAndGrade(@Param("sid") Integer sid);-->
     <select id="getStudentAndGrade" resultMap="getStudentAndGradeResultMap2">
     select * from student left join grade on student.gid= grade.gid  where sid=#{sid}
     </select>
     *
     *
     *
     *
     *
     * 第三种方式：分布查询  代码如下
     *  <resultMap id="getStudentAndGradeByStep1" type="student">
     *         <id property="sid" column="sid"></id>
     *         <result property="stuName" column="stu_name"></result>
     *         <result property="age" column="age"></result>
     *         <result property="sex" column="sex"></result>
     *         <result property="tel" column="tel"></result>
     *         <association property="grade" column="gid" select="com.wfs.mapper.QueryMapper.getStudentAndGradeByStep2"></association>
     *     </resultMap>
     *     <!--Student getStudentAndGradeByStep1(@Param("sid") Integer sid);-->
     *     <select id="getStudentAndGradeByStep1" resultMap="getStudentAndGradeByStep1">
     *         select * from student where sid=#{sid}
     *     </select>
     *
     *
     *     <!--Grade getStudentAndGradeByStep2(@Param("gid") Integer gid);-->
     *     <select id="getStudentAndGradeByStep2"    resultType="grade">
     *         select * from grade where gid=#{gid}
     *     </select>
     *
     *
     *
     */





    @Test
    public void getStudentAndGrade(){
        sqlSession=sqlSessionUtil.getSqlSession();
        QueryMapper queryMapper=sqlSession.getMapper(QueryMapper.class);
        System.out.println(queryMapper.getStudentAndGrade(1));
    }


    @Test
    public void getStudentAndGradeByStep(){
        sqlSession=sqlSessionUtil.getSqlSession();
        QueryMapper queryMapper=sqlSession.getMapper(QueryMapper.class);
        System.out.println(queryMapper.getStudentAndGradeByStep1(1).getSex());
    }


    /**
     * 一对多问题   一个班级有很多名学生
     *
     * 第一种方式  级联属性解决
     * <resultMap id="getGradeAndStudentResultMap" type="grade">
     *         <id property="gid" column="gid"></id>
     *         <result property="gName" column="g_name"></result>
     *         <collection property="list" ofType="student">
     *             <id property="sid" column="sid"></id>
     *             <result property="stuName" column="stu_name"></result>
     *             <result property="age" column="age"></result>
     *             <result property="sex" column="sex"></result>
     *             <result property="tel" column="tel"></result>
     *         </collection>
     *
     *     </resultMap>
     *     <!-- Grade getGradeAndStudent(@Param("gid") Integer gid);-->
     *     <select id="getGradeAndStudent" resultMap="getGradeAndStudentResultMap">
     *         select * from grade left join student on grade.gid=student.gid where grade.gid=#{gid}
     *     </select>
     *
     *
     *
     * 第二种方式：采用分布查询的方式 代码如下：
     *  <resultMap id="getGradeAndStudentResultMap1" type="grade">
     *         <id property="gid" column="gid"></id>
     *         <result property="gName" column="g_name"></result>
     *         <collection property="list" ofType="student">
     *             <id property="sid" column="sid"></id>
     *             <result property="stuName" column="stu_name"></result>
     *             <result property="age" column="age"></result>
     *             <result property="sex" column="sex"></result>
     *             <result property="tel" column="tel"></result>
     *         </collection>
     *
     *     </resultMap>
     *     <!-- Grade getGradeAndStudent(@Param("gid") Integer gid);-->
     *     <select id="getGradeAndStudent" resultMap="getGradeAndStudentResultMap1">
     *         select * from grade left join student on grade.gid=student.gid where grade.gid=#{gid}
     *     </select>
     *
     *
     *
     *     <resultMap id="getGradeAndStudentResultMap2" type="grade">
     *         <id property="gid" column="gid"></id>
     *         <result property="gName" column="g_name"></result>
     *         <collection property="list" column="gid" select="com.wfs.mapper.QueryMapper.getGradeAndStudentByStep2">
     *         </collection>
     *     </resultMap>
     *     <!-- Grade getGradeAndStudentByStep1(@Param("gid") Integer gid);-->
     *     <select id="getGradeAndStudentByStep1" resultMap="getGradeAndStudentResultMap2">
     *         select * from grade where gid=#{gid}
     *     </select>
     *
     *
     *
     *     <resultMap id="getGradeAndStudentResultMap3" type="student">
     *     <id property="sid" column="sid"></id>
     *     <result property="stuName" column="stu_name"></result>
     *     <result property="age" column="age"></result>
     *     <result property="sex" column="sex"></result>
     *     <result property="tel" column="tel"></result>
     *     </resultMap>
     *     <!--List<Student> getGradeAndStudentByStep2(@Param("gid") Integer gid);-->
     *     <select id="getGradeAndStudentByStep2" resultMap="getGradeAndStudentResultMap3">
     *         select * from student where gid=#{gid}
     *     </select>
     */

    @Test
    public void getGradeAndStudent(){
        sqlSession=sqlSessionUtil.getSqlSession();
        QueryMapper queryMapper=sqlSession.getMapper(QueryMapper.class);
        System.out.println(queryMapper.getGradeAndStudent(1));
    }


    @Test
    public void getGradeAndStudentByStep(){
        sqlSession=sqlSessionUtil.getSqlSession();
        QueryMapper queryMapper=sqlSession.getMapper(QueryMapper.class);
        System.out.println(queryMapper.getGradeAndStudentByStep1(1));
    }


    /**
     * 模糊查询：
     * 第一种方式：select * from student where stu_name like '%${stuName}%'
     * 第二种方式：select * from student where stu_name like "%"#{stuName}"%"
     * 第三种方式：select * from student where stu_name like concat('%',#{stuName},'%')
     */
    @Test
    public void getStudentByName(){
        sqlSession=sqlSessionUtil.getSqlSession();
        QueryMapper queryMapper=sqlSession.getMapper(QueryMapper.class);
        System.out.println(queryMapper.getStudentByName("三"));
    }


    /**
     * 批量删除
     *  delete from student  where sid in (${ids})
     */
    @Test
    public void deleteStudentsByIds(){
        sqlSession=sqlSessionUtil.getSqlSession();
        QueryMapper queryMapper=sqlSession.getMapper(QueryMapper.class);
        System.out.println(queryMapper.deleteStudentsByIds("5,6,7"));
    }


    /**
     * 动态设置表名
     */
    @Test
    public void getStudentByTableName(){
        sqlSession=sqlSessionUtil.getSqlSession();
        QueryMapper queryMapper=sqlSession.getMapper(QueryMapper.class);
        System.out.println(queryMapper.getStudentByTableName("student"));
    }


    /**
     * 动态sql-----if标签
     * if标签不能自动的删掉动态sql语句中的and  所以可以在多条件查询语句之前加入where 1=1 这个恒成立的条件， 然后在各个判断语句之前都加上and 代码如下：
     *  <!--List<Student>  getStudentByIf( Student student);-->
     *     <select id="getStudentByIf" resultType="student" >
     *         select * from  student where  1=1
     *         <if test="stuName !='' and stuName != null">
     *           and  stu_name=#{stuName}
     *         </if>
     *
     *         <if test="age !='' and age!= null">
     *            and age=#{age}
     *         </if>
     *     </select>
     */
    @Test
    public void getStudentByIf(){
        sqlSession=sqlSessionUtil.getSqlSession();
        QueryMapper queryMapper=sqlSession.getMapper(QueryMapper.class);
        Student student=new Student(null,"张三",23,1,"15555555555");
        System.out.println(queryMapper.getStudentByIf(student));
    }


    /**
     * 动态sql---where
     * 自动的添加where关键字  并自动根据sql语句删除前面的and或or  但是不能删除后面的and或or  代码如下：
     *  <!--List<Student>  getStudentByWhere( Student student);-->
     *     <select id="getStudentByWhere" resultType="student">
     *         select * from student
     *      <where>
     *          <if test="stuName !='' and stuName != null">
     *              and  stu_name=#{stuName}
     *          </if>
     *
     *          <if test="age !='' and age!= null">
     *              and age=#{age}
     *          </if>
     *      </where>
     *     </select>
     */

    @Test
    public void getStudentByWhere(){
        sqlSession=sqlSessionUtil.getSqlSession();
        QueryMapper queryMapper=sqlSession.getMapper(QueryMapper.class);
        Student student=new Student(null,"张三",23,1,"15555555555");
        System.out.println(queryMapper.getStudentByWhere(student));
    }


    /**
     * 动态sql---trim
     * 为了解决where后面不能自动删除无用and或or的问题，采用trim标签来实现  代码如下：
     *  <!-- List<Student>  getStudentByTrim( Student student);-->
     *     <select id="getStudentByTrim" resultType="student">
     *         select * from student
     *
     *             <trim prefix="where" suffixOverrides="and|or">
     *
     *                 <if test="stuName !='' and stuName != null">
     *                     stu_name=#{stuName} and
     *                 </if>
     *
     *                 <if test="age !='' and age!= null">
     *                    age=#{age}   and
     *                 </if>
     *             </trim>
     *
     *     </select>
     */
    @Test
    public void getStudentByTrim(){
        sqlSession=sqlSessionUtil.getSqlSession();
        QueryMapper queryMapper=sqlSession.getMapper(QueryMapper.class);
        Student student=new Student(null,"张三",13,1,"15555555555");
        System.out.println(queryMapper.getStudentByTrim(student));
    }


    /**
     * 动态语句---choose when otherwise   代码如下：
     * <!--List<Student>  getStudentByCWO( Student student);-->
     *     <select id="getStudentByCWO" resultType="student">
     *         select * from student where
     *         <choose>
     *             <when test="stuName !='' and stuName!=null ">
     *                 stu_name=#{stuName}
     *             </when>
     *
     *             <when test="age !='' and age!=null ">
     *                 age=#{age}
     *             </when>
     *             <otherwise>
     *                 sid=1
     *             </otherwise>
     *         </choose>
     *     </select>
     */
    @Test
    public void getStudentByCWO(){
        sqlSession=sqlSessionUtil.getSqlSession();
        QueryMapper queryMapper=sqlSession.getMapper(QueryMapper.class);
        Student student=new Student(null,"",null,1,"15555555555");
        System.out.println(queryMapper.getStudentByCWO(student));
    }


    /**
     * 动态sql---foreach 批量删除  代码如下：
     * <!-- int deleteMoreStudent(@Param("sids") Integer[] sids);-->
     *     <delete id="deleteMoreStudent">
     *         delete from student where sid in
     *         (
     *             <foreach collection="sids" item="sid" separator=",">
     *                 #{sid}
     *             </foreach>
     *         )
     *
     *     </delete>
     *
     *
     *
     *     或者：
     *      <!-- int deleteMoreStudent(@Param("sids") Integer[] sids);-->
     *     <delete id="deleteMoreStudent">
     *         delete from student where sid in
     *
     *             <foreach collection="sids" item="sid" separator="," open="(" close=")">
     *                 #{sid}
     *             </foreach>
     *
     *
     *     </delete>
     *
     *
     *     再或者：.
     *      <!-- int deleteMoreStudent(@Param("sids") Integer[] sids);-->
     *     <delete id="deleteMoreStudent">
     *         delete from student where
     *
     *             <foreach collection="sids" item="sid" separator="or">
     *                sid= #{sid}
     *             </foreach>
     *
     *
     *     </delete>
     */

    @Test
    public void deleteMoreStudent(){
        sqlSession=sqlSessionUtil.getSqlSession();
        QueryMapper queryMapper=sqlSession.getMapper(QueryMapper.class);

        System.out.println(queryMapper.deleteMoreStudent(new Integer[]{4,5,6}));
    }


    /**
     * 动态sql语句 foreach批量插入数据  代码如下：
     *  <!--int insertStudnet(List<Student> list);-->
     *     <insert id="insertStudnet">
     *         insert into student
     *
     *          <foreach collection="list" separator="," item="student" open="values" >
     *             (null, #{student.stuName} ,#{student.age} ,#{student.sex},#{student.tel},null)
     *          </foreach>
     *
     *     </insert>
     */


    @Test
    public void insertStudent(){
        sqlSession=sqlSessionUtil.getSqlSession();
        QueryMapper queryMapper=sqlSession.getMapper(QueryMapper.class);

        Student student1=new Student(null,"赵六",15,0,"13232323232");
        Student student2=new Student(null,"田七",12,1,"13232323232");
        Student student3=new Student(null,"钱八",13,0,"13232333232");
        List<Student> list=new ArrayList<Student>();
        list.add(student1);
        list.add(student2);
        list.add(student3);
        System.out.println(queryMapper.insertStudnet(list));
    }


}
