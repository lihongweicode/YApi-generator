package org.starr.yapi.api.vo;

import lombok.Data;

/**
 * @author lhw
 * @description 类的属性条目信息
 * @date 2020/8/24
 */
@Data
public class FieldEntry {
    /**
     * 属性名
     */
    private String name;
    /**
     * 类型名称
     */
    private String typeName;
    /**
     * 注释信息
     */
    private String commentText;

    public FieldEntry(String name, String typeName, String commentText) {
        this.name = name;
        this.typeName = typeName;
        this.commentText = commentText;
    }
}
