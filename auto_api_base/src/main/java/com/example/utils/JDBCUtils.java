package com.example.utils;

import com.example.data.Constants;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @className: JDBCUtils
 * @description: TODO 数据库工具类(一般为静态)
 * @date: 2021/4/12 下午5:17
 * @author: gqd
 * @version: 1.0
 */
public class JDBCUtils {

    /**
     * @description 获取数据库连接
     * @return java.sql.Connection
     * @author gqd
     * @date 2021/4/12 下午5:19
     * @version 1.0       
     */
    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(Constants.URL, Constants.USER, Constants.PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * @description 更新操作封装
     * @param sql  
     * @return void
     * @author gqd
     * @date 2021/4/12 下午5:19
     * @version 1.0       
     */
    public static void update(String sql) {
        Connection connection = getConnection();
        QueryRunner queryRunner = new QueryRunner();
        try {
            queryRunner.update(connection, sql);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    /**
     * @description 查询所有结果
     * @param sql  
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     * @author gqd
     * @date 2021/4/12 下午5:22
     * @version 1.0       
     */
    public static List<Map<String, Object>> queryAll(String sql) {
        Connection connection = getConnection();
        List<Map<String, Object>> result = null;
        QueryRunner queryRunner = new QueryRunner();
        try {
            result = queryRunner.query(connection, sql, new MapListHandler());
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
    
    /**
     * @description 查询结果集中的第一条
     * @param sql  
     * @return java.util.Map<java.lang.String,java.lang.Object>
     * @author gqd
     * @date 2021/4/12 下午5:27
     * @version 1.0       
     */
    public static Map<String, Object> queryOne(String sql) {
        Connection connection = getConnection();
        Map<String, Object> result = null;
        QueryRunner queryRunner = new QueryRunner();
        try {
            result = queryRunner.query(connection, sql, new MapHandler());
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    /**
     * @description 查询单条的数据
     * @param sql  
     * @return java.lang.Object
     * @author gqd
     * @date 2021/4/12 下午5:37
     * @version 1.0       
     */
    public static Object querySingleData(String sql) {
        Connection connection = getConnection();
        Object result = null;
        QueryRunner queryRunner = new QueryRunner();
        try {
            result = queryRunner.query(connection, sql, new ScalarHandler<Object>());
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
}
