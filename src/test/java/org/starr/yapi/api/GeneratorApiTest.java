package org.starr.yapi.api;

import cn.hutool.json.JSONUtil;
import org.junit.jupiter.api.Test;
import org.starr.yapi.api.constant.DocDataType;

/**
 * @author lhw
 * @description YApi文档生成测试类
 * @date 2020/9/28
 */

public class GeneratorApiTest {
    @Test
    public void test() {
        //TODO 项目的基本路径
        GeneratorApi.basePath = "D:\\work_space\\idea_ws\\my-model\\YApi-generator\\src\\test\\java\\";
        //排除的属性
        GeneratorApi.excludeFields.add("");
        //java基础类型和YApi类型的转换map    key：java类的全类名  value: YApi中类型的名称
        GeneratorApi.typeMap.put("","");
        //对象包装  key:DocDataType  value:外层包装模板（%s会注入对象）
        GeneratorApi.typePackageMap.put("","");
        //TODO 项目的基础包，用来区分 类是项目里的自定义类还是基础类型  （自定义类型）
        GeneratorApi.basePackage = "org.starr.yapi.api";

        //TODO 需要转换的类信息
        Class<?> clz = PayApplyVO.class;
        //TODO 需要设置数据类型  （object:对象；array:数组；return_object:对象；return_array:数组；return_page:分页）
        String objStr = GeneratorApi.convertToYApiJson(clz, DocDataType.RETURN_OBJECT);
        System.out.println("-------------------------YApi------------------------------");
        System.out.println(objStr);
        System.out.println("-------------------------YApi------------------------------");
    }
}
