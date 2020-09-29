package org.starr.yapi.api;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.starr.yapi.api.constant.DocDataType;
import org.starr.yapi.api.vo.FieldEntry;
import org.starr.yapi.api.vo.ModelClassDocVO;
import org.starr.yapi.api.yapi.CustomizeItemsVO;
import org.starr.yapi.api.yapi.ItemsVO;
import org.starr.yapi.api.yapi.ListItemsVO;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author lhw
 * @description 生成接口文档
 * 问题：1.字段顺序 2.内部类
 * @date 2020/8/24
 */
public class GeneratorApi {
    private static final Logger logger = LoggerFactory.getLogger(GeneratorApi.class);
    //项目的基本路径
    public static String basePath = "D:\\work_space\\idea_ws\\my-model\\YApi-generator\\src\\main\\java\\";
    //排除的属性
    public static final List<String> excludeFields = new ArrayList<>();
    //java基础类型和YApi类型的转换map    key：java类的全类名  value: YApi中类型的名称
    public static final Map<String, String> typeMap = new HashMap<>();
    //对象包装  key:DocDataType  value:外层包装模板（%s会注入对象）
    public static final Map<String, String> typePackageMap = new HashMap<>();
    //项目的基础包，用来区分 类是项目里的自定义类还是基础类型  （自定义类型）
    public static String basePackage = "org.starr.yapi.api";

    static {
        /* 设置排除的属性 */
        excludeFields.add("serialVersionUID");
        /* 设置基础类型映射  java类  --》 YApi类型 */
        typeMap.put(List.class.getName(), "array");
        typeMap.put(String.class.getName(), "string");
        typeMap.put(Integer.class.getName(), "integer");
        typeMap.put(Double.class.getName(), "number");
        typeMap.put(BigDecimal.class.getName(), "number");
        typeMap.put(LocalDateTime.class.getName(), "string");
        typeMap.put(Long.class.getName(), "number");
        /* 设置YApi文档包装信息 */
        typePackageMap.put(DocDataType.RETURN_ARRAY,
                "{\"type\":\"object\",\"properties\":{\"code\":{\"description\":\"响应码\",\"type\":\"integer\"},\"data\":{\"type\": \"array\",\"description\":\"响应数据\"," +
                        "\"items\": " +
                        "%s"
                        + "},\"message\":{\"description\":\"响应消息\",\"type\":\"string\"},\"timestamp\":{\"description\":\"时间戳\",\"type\":\"number\"}}}");
        typePackageMap.put(DocDataType.RETURN_PAGE,
                "{\"type\":\"object\",\"properties\":{\"code\":{\"description\":\"响应码\",\"type\":\"integer\"},\"data\":{\"type\": \"array\",\"description\":\"响应数据\"," +
                        "\"items\": " +
                        "%s"
                        + "},\"pageInfo\": { \"type\": \"object\", \"properties\": { \"total\": { \"type\": \"number\", \"description\": \"总数\" } }, \"description\": \"分页信息\" " +
                        "},\"message\":{\"description\":\"响应消息\",\"type\":\"string\"},\"timestamp\":{\"description\":\"时间戳\",\"type\":\"number\"}}}");
        typePackageMap.put(DocDataType.RETURN_OBJECT,
                "{\"type\":\"object\",\"properties\":{\"code\":{\"description\":\"响应码\",\"type\":\"integer\"},\"data\":" +
                        "%s"
                        + ",\"message\":{\"description\":\"响应消息\",\"type\":\"string\"},\"timestamp\":{\"description\":\"时间戳\",\"type\":\"number\"}}}");
        typePackageMap.put(DocDataType.ARRAY,
                "{" +
                        "\"type\":\"array\"," +
                        "\"items\":" + "%s" +
                        "}");

    }

    /**
     * 将类信息转换为YApi文档信息
     *
     * @param clz  需要生成文档的DTO或VO类的类对象
     * @param type 生成文档的类型
     * @return 返回YApi文档的JSON字符串
     */
    public static String convertToYApiJson(Class<?> clz, String type) {
        try {
            //获取所有类的注释map
            Map<String, FieldEntry> filedEntryMap = getFiledEntryMap(clz);

            CustomizeItemsVO rootItemsVO = new CustomizeItemsVO();
            rootItemsVO.setType("object");
            /* 根据目标类生成自定义项对象 */
            createApi(clz, filedEntryMap, rootItemsVO);
            String objStr = JSONUtil.toJsonStr(rootItemsVO);
            /* 根据文档类型，进行包装 */
            String packageFormat = typePackageMap.get(type);
            if (StrUtil.isBlank(packageFormat)) {
                return objStr;
            } else {
                return String.format(packageFormat, objStr);
            }
        } catch (ClassNotFoundException e) {
            logger.error("类型转换异常");
        } catch (IllegalAccessException illegalAccessException) {
            logger.error(illegalAccessException.getMessage());
        }
        return null;
    }

    /**
     * 创建api文档
     *
     * @param clz           需要生成的类
     * @param filedEntryMap 类的属性条目信息
     * @param rootItemsVO   根对象vo
     * @throws ClassNotFoundException 类型转换异常
     */
    private static void createApi(Class<?> clz, Map<String, FieldEntry> filedEntryMap, CustomizeItemsVO rootItemsVO) throws ClassNotFoundException, IllegalAccessException {
        Map<String, ItemsVO> propertiesMap = new LinkedHashMap<>();
        //获取类的所有属性
        Field[] declaredFields = clz.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            //迭代所有的属性，进行生成文档对象
            String name = declaredField.getName();
            if (excludeFields.contains(name)) {
                continue;
            }
            Class<?> type = declaredField.getType();
            String comment = filedEntryMap.get(name).getCommentText();
            if (type.equals(List.class)) {
                // 当前集合的泛型类型
                String innerType = gainListInnerType(declaredField);

                ListItemsVO listItemsVO = new ListItemsVO();
                if (typeMap.containsKey(innerType)) {
                    //为基础类
                    ItemsVO items = new ItemsVO();
                    String yApiType = getYApiType(innerType);
                    items.setType(yApiType);
                    listItemsVO.setItems(items);
                } else {
                    //为自定义类型
                    CustomizeItemsVO customizeItemsVO = new CustomizeItemsVO();
                    customizeItemsVO.setType("object");

                    //递归调用获取内部类的属性
                    Class<?> innerClz = Class.forName(innerType);
                    Map<String, FieldEntry> innerFiledEntryMap = getFiledEntryMap(innerClz);
                    createApi(innerClz, innerFiledEntryMap, customizeItemsVO);
                    listItemsVO.setItems(customizeItemsVO);
                }
                listItemsVO.setType("array");
                listItemsVO.setDescription(comment);
                propertiesMap.put(name, listItemsVO);
            } else if (!typeMap.containsKey(type.getName())) {
                // 自定义类型
                CustomizeItemsVO itemsVO = new CustomizeItemsVO();

                itemsVO.setType("object");
                itemsVO.setDescription(comment);
                //递归调用获取内部类的属性
                Class<?> innerClz = Class.forName(type.getName());
                Map<String, FieldEntry> innerFiledEntryMap = getFiledEntryMap(innerClz);
                createApi(innerClz, innerFiledEntryMap, itemsVO);
                propertiesMap.put(name, itemsVO);
            } else {
                // 基础类
                String yApiType = getYApiType(type.getName());
                ItemsVO vo = new ItemsVO();
                vo.setType(yApiType);
                vo.setDescription(comment);
                propertiesMap.put(name, vo);
            }
        }
        rootItemsVO.setProperties(propertiesMap);
    }

    /**
     * 根据java基础类型获取YApi的类型
     *
     * @param type java基础类型类全面
     * @return YApi的类型
     */
    private static String getYApiType(String type) {
        String yApiType = typeMap.get(type);
        if (StrUtil.isBlank(yApiType)) {
            String msg = "类型转换异常： " + type;
            logger.error(msg);
        }
        return yApiType;
    }

    /**
     * 获取List 泛型集合中内部的实际类型
     */
    private static String gainListInnerType(Field declaredField) {
        Type genericType = declaredField.getGenericType();
        String typeName = genericType.getTypeName();
        return typeName.substring(15, typeName.length() - 1);
    }

    /**
     * 获取类字段注释信息的map
     */
    private static Map<String, FieldEntry> getFiledEntryMap(Class<?> clz) throws IllegalAccessException {
        if (!clz.getName().contains(basePackage)) {
            //如果该类不是当前项目中的类，则无法进行类的文档解析
            throw new IllegalAccessException(String.format("[%s]不是项目内的类无法进行解析文档", clz.getName()));
        }
        DocletUtil docletUtil = new DocletUtil(
                GeneratorApi.basePath, clz);
        ModelClassDocVO modelClassDocVO = docletUtil.exec();
        //类的属性的注释映射
        return modelClassDocVO.getFieldEntryMap();
    }
}
