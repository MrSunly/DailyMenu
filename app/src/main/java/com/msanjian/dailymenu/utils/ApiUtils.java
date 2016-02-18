package com.msanjian.dailymenu.utils;

/**
 * Created by longe on 2016/2/13.
 */
public class ApiUtils {
    private static String KEY = "key=4385fdeac53948cb9b84ae666ef4e115";
    public static String CATEGORY_URL = "http://apis.juhe.cn/cook/category?"+KEY;
    public static String IMAGE_URL = "http://apis.juhe.cn/cook/index?"+KEY+"&rn=30&cid=";
    public static String QUERY_URL = "http://apis.juhe.cn/cook/query.php?"+KEY;
}
