package org.starr.yapi.api.yapi;

import lombok.Data;

import java.io.Serializable;

/**
 * @author lhw
 * @description 单个项VO
 * @date 2020/8/24
 */
@Data
public class ItemsVO implements Serializable {
    /**
     * 类型
     */
    private String type;
    /**
     * 注释
     */
    private String description;
}
