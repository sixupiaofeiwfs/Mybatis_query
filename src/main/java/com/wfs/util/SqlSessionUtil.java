package com.wfs.util;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

public class SqlSessionUtil {

    SqlSession sqlSession;
    String resource="mybatis_config.xml";
    InputStream inputStream=null;

    public SqlSession getSqlSession(){

        try {
            inputStream=Resources.getResourceAsStream(resource);
        } catch (IOException e) {
            e.printStackTrace();
        }

        SqlSessionFactory sqlSessionFactory=new  SqlSessionFactoryBuilder().build(inputStream);
        sqlSession=sqlSessionFactory.openSession(true);

        return sqlSession;
    }


}
