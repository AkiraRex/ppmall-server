package com.ppmall.util;

import java.util.List;
import java.util.Map;

public class StringUtil {
    /**
     * <pre>
     * StringUtils.isBlank("null")    = true
     * StringUtils.isBlank(null)      = true
     * StringUtils.isBlank("")        = true
     * StringUtils.isBlank(" ")       = true
     * StringUtils.isBlank("bob")     = false
     * StringUtils.isBlank("  bob  ") = false
     * </pre>
     *
     * @param str
     * @return
     */
    public static boolean isBlank(String str) {
        int strLen;
        if ("null".equals(str) || str == null || (strLen = str.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if ((Character.isWhitespace(str.charAt(i)) == false)) {
                return false;
            }
        }
        return true;
    }

    /**
     * <pre>
     * StringUtils.isBlank("null")    = false
     * StringUtils.isBlank(null)      = false
     * StringUtils.isBlank("")        = false
     * StringUtils.isBlank(" ")       = false
     * StringUtils.isBlank("bob")     = true
     * StringUtils.isBlank("  bob  ") = true
     * </pre>
     *
     * @param str
     * @return
     */
    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    /**
     * 通过传进来的参数获得是否有进行排序，如果有就返回排序信息，没有就返回""
     *
     * @param paramMap
     * @return
     */
    public static String getOrderBy(Map paramMap) {
        String field = (String) paramMap.get("sort[field]");
        String sort = (String) paramMap.get("sort[sort]");
        String order = "";
        if (StringUtil.isNotBlank(field)) {
            order = " order by " + field;
            if (StringUtil.isNotBlank(sort)) {
                order = " order by " + field + " " + sort;
            }
        }
        return order;
    }

    /**
     * <pre>
     * 查询条件的语句拼接，与前端约定好的定义格式为 [字段名_查询条件]，
     * 若传进来的为 [字段名] 则默认查询条件为“等于”，查询条件如下：
     * eq： [equal:等于]
     * li： [like:模糊查询 后置通配符 %]
     * lt： [less than:小于]
     * gt： [greater than:大于]
     * le： [小于等于]
     * ge： [大于等于]
     * </pre>
     *
     * @param paramMap
     * @return
     */
    public static String getWhereClause(Map paramMap) {
        String whereString = "";
        List list = (List) paramMap.get("searchList");
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                String str = (String) list.get(i);
                String val = (String) paramMap.get("search[" + str + "]");
                if (StringUtil.isNotBlank(val) && StringUtil.isNotBlank(str)) {
                    String[] strs = str.split("_");
                    String field = "";
                    String condition = "";
                    if (strs.length > 1) {
                        field = strs[0];
                        condition = strs[1];
                    } else {
                        field = strs[0];
                        condition = "eq";
                    }
                    if ("eq".equals(condition)) {
                        whereString = whereString + " and " + field + " = '" + val + "'";
                    } else if ("li".equals(condition)) {
                        whereString = whereString + " and " + field + " like '" + val + "%'";
                    } else if ("lt".equals(condition)) {
                        whereString = whereString + " and " + field + " < '" + val + "'";
                    } else if ("gt".equals(condition)) {
                        whereString = whereString + " and " + field + " > '" + val + "'";
                    } else if ("le".equals(condition)) {
                        whereString = whereString + " and " + field + " <= '" + val + "'";
                    } else if ("ge".equals(condition)) {
                        whereString = whereString + " and " + field + " >= '" + val + "'";
                    }
                }
            }
        }
        return whereString;
    }
}
