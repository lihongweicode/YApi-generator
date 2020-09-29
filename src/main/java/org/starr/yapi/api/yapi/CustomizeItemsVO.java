package org.starr.yapi.api.yapi;

import lombok.Data;

import java.util.Map;

/**
 * @author lhw
 * @description 自定义项VO
 * @date 2020/8/24
 */
@Data
public class CustomizeItemsVO extends ItemsVO {
    private Map<String, ItemsVO> properties;
}
