package org.starr.yapi.api;

import org.junit.jupiter.api.Test;
import org.starr.yapi.api.vo.ModelClassDocVO;

/**
 * @author lhw
 * @description
 * @date 2020/9/29
 */
public class DocletUtilTest {
    @Test
    public void test() {
        DocletUtil docletUtil = new DocletUtil(
                "D:\\work_space\\idea_ws\\youdi2\\business-order-disassemble\\src\\main\\java\\net\\youdi\\order\\disassemble\\purchase\\dto\\ApplyPurchaseDTO.java");
        ModelClassDocVO modelClassDocVO = docletUtil.exec();
        System.out.println("类注释：" + modelClassDocVO.getModelCommentText());
        System.out.println("属性字段注释如下：");
        modelClassDocVO.getFieldEntryList().forEach(System.out::println);
    }
}
