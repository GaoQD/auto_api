package com.example.common;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.alibaba.fastjson.JSONObject;
import com.example.data.Constants;
import com.example.data.Environment;
import com.example.pojo.ExcelPojo;
import io.restassured.response.Response;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.restassured.RestAssured.given;

/**
 * @className: BaseTest
 * @description: TODO 测试用例脚本父类，封装了get、post、patch等请求，excel操作方法
 * @date: 2021/4/8 下午4:36
 * @author: gqd
 * @version: 1.0
 */
public class BaseTest {

    /**
     * @description 对get、post、patch等请求做二次封装
     * @param excelPojo  
     * @return io.restassured.response.Response
     * @author gqd
     * @date 2021/4/8 下午4:47
     * @version 1.0       
     */
    public Response request(ExcelPojo excelPojo) {
        String url = excelPojo.getUrl();
        String method = excelPojo.getMethod();
        String requestHeader = excelPojo.getRequestHeader();
        Map<String, Object> headersMap = JSONObject.parseObject(requestHeader, Map.class);
        String params = excelPojo.getInputParam();
        Response response = null;
        /**
         * 通过接口测试用例excel中获取到对应的接口方法
         * given()后的log().all()可以看到接口请求体，then().log().all()可以看到接口的响应结果
         * 这里只写了get、post、patch接口的封装
         */
        if ("get".equalsIgnoreCase(method)) {
            response = given().log().all().headers(headersMap).when().get(url).then().log().all().extract().response();
        } else if ("post".equalsIgnoreCase(method)) {
            response = given().log().all().headers(headersMap).body(params).when().post(url).then().log().all().extract().response();
        } else if ("patch".equalsIgnoreCase(method)) {
            response = given().log().all().headers(headersMap).body(params).when().patch(url).then().log().all().extract().response();
        }
        return response;
    }
    
    /**
     * @description 将对应的接口返回字段(提取返回数据(extract))提取到环境变量中
     * @param response 
     * @param excelPojo 
     * @return void
     * @author gqd
     * @date 2021/4/8 下午5:23
     * @version 1.0       
     */
    public void extractToEnvironment(Response response, ExcelPojo excelPojo) {
        /**
         * 利用rest-assured的jsonPath提取对应的值
         */
        Map<String, Object> map = JSONObject.parseObject(excelPojo.getExtract(), Map.class);
        for (String key : map.keySet()) {
            Object path = map.get(key);
            Object value = response.jsonPath().get(path.toString());
            // 存入环境变量
            Environment.envData.put(key, value);
        }
    }

    /**
     * @description 获取指定行的excel数据
     * @param sheetNum sheet页(默认从1开始)
     * @param startRow 读取开始行(默认从0开始)
     * @param readRow 读取多少行
     * @return java.util.List<com.example.pojo.ExcelPojo>
     * @author gqd
     * @date 2021/4/8 下午5:27
     * @version 1.0
     */
    public List<ExcelPojo> readSpecifyExcelData(int sheetNum, int startRow, int readRow) {
        ImportParams importParams = new ImportParams();
        importParams.setStartSheetIndex(sheetNum - 1);
        importParams.setStartRows(startRow);
        importParams.setReadRows(readRow);
        return ExcelImportUtil.importExcel(new File(Constants.EXCEL_FILE_PATH), ExcelPojo.class, importParams);
    }
    
    /**
     * @description 获取从指定行开始到最后一行数据
     * @param sheetNum 
     * @param startRow 
     * @return java.util.List<com.example.pojo.ExcelPojo>
     * @author gqd
     * @date 2021/4/8 下午5:30
     * @version 1.0       
     */
    public List<ExcelPojo> readSpecifyExcelData(int sheetNum, int startRow) {
        ImportParams importParams = new ImportParams();
        importParams.setStartSheetIndex(sheetNum - 1);
        importParams.setStartRows(startRow);
        return ExcelImportUtil.importExcel(new File(Constants.EXCEL_FILE_PATH), ExcelPojo.class, importParams);
    }

    /**
     * @description 获取excel中所有数据
     * @param sheetNum  
     * @return java.util.List<com.example.pojo.ExcelPojo>
     * @author gqd
     * @date 2021/4/8 下午5:31
     * @version 1.0       
     */
    public List<ExcelPojo> readAllExcelData(int sheetNum) {
        ImportParams importParams = new ImportParams();
        importParams.setStartSheetIndex(sheetNum - 1);
        return ExcelImportUtil.importExcel(new File(Constants.EXCEL_FILE_PATH), Excel.class, importParams);
    }

    /**
     * @description 正则表达式替换，采用与postman一致的方式，对{{XXXX}}替换
     * @param orginal  
     * @return java.lang.String
     * @author gqd
     * @date 2021/4/8 下午5:32
     * @version 1.0       
     */
    public String regexReplace(String orginal) {
        if (orginal != null) {
            Pattern pattern = Pattern.compile("\\{\\{(.*?)}}");
            Matcher matcher = pattern.matcher(orginal);
            String result = orginal;
            while (matcher.find()) {
                String outer = matcher.group(0);
                String inner = matcher.group(1);
                Object replaceStr = Environment.envData.get(inner);
                result = result.replace(outer, replaceStr + "");
            }
            return result;
        }
        return orginal;
    }

    /**
     * @description 参数替换
     * @param excelPojo  
     * @return com.example.pojo.ExcelPojo
     * @author gqd
     * @date 2021/4/12 上午8:43
     * @version 1.0       
     */
    public ExcelPojo casesReplace(ExcelPojo excelPojo) {
        // 输入参数替换
        String inputParams = regexReplace(excelPojo.getInputParam());
        excelPojo.setInputParam(inputParams);
        // 请求头替换
        String requestHeader = regexReplace(excelPojo.getRequestHeader());
        excelPojo.setRequestHeader(requestHeader);
        // 接口地址替换
        String url = regexReplace(excelPojo.getUrl());
        excelPojo.setUrl(url);
        // 期望返回结果替换
        String expected = regexReplace(excelPojo.getExpected());
        excelPojo.setExpected(expected);
        return excelPojo;
    }
}
