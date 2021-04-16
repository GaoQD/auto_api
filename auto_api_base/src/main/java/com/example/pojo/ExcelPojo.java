package com.example.pojo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

/**
 * @className: ExcelPojo
 * @description: TODO 测试用例实体类
 * @date: 2021/4/8 下午4:37
 * @author: gqd
 * @version: 1.0
 */
@Data
public class ExcelPojo {

    @Excel(name = "序号(caseId)")
    private int caseId;

    @Excel(name = "接口模块(interface)")
    private String interfaceName;

    @Excel(name = "用例标题(title)")
    private String title;

    @Excel(name = "请求头(requestHeader)")
    private String requestHeader;

    @Excel(name = "请求方式(method)")
    private String method;

    @Excel(name = "接口地址(url)")
    private String url;

    @Excel(name = "参数输入(inputParams)")
    private String inputParam;

    @Excel(name = "期望返回结果(expected)")
    private String expected;

    @Excel(name = "提取返回数据(extract)")
    private String extract;

    @Excel(name = "数据库校验")
    private String dbAssert;
}
