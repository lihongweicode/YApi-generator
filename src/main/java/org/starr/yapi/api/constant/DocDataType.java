package org.starr.yapi.api.constant;

/**
 * @author lhw
 * @description 文档数据类型
 * @date 2020/9/29
 */
public class DocDataType {
    private DocDataType(){
        throw new IllegalStateException("Utility class");
    }
    /**
     * 对象
     */
    public static final String OBJECT = "object";
    /**
     * 数组
     */
    public static final String ARRAY = "array";
    /**
     * 统一返回包装对象
     */
    public static final String RETURN_OBJECT = "return_object";
    /**
     * 统一返回包装数组
     */
    public static final String RETURN_ARRAY = "return_array";
    /**
     * 统一返回包装分页
     */
    public static final String RETURN_PAGE = "return_page";
}
