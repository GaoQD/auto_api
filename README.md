# auto_api

#### 介绍
基于Java+RestAssured+testNG+EasyPoi搭建的接口自动化测试基础框架

#### 软件架构
采用技术：
   1.RestAssured：发送接口请求/接收接口响应
   2.TestNG：管理测试用例
   3.EasyPoi：读取excel数据
   4.FastJson：json与map相互转换
   5.JDBC：Java里面操作数据库(包括增删改查)，DBUtils
   6.常量类设计：(包括项目地址、文件路径、数据地址、用户名信息)
   7.引入JDBC，实现随机工具类(身份证号码、手机号码、姓名)
   8.数据库断言
