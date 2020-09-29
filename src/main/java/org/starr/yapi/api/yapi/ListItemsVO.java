package org.starr.yapi.api.yapi;

import lombok.Data;

/**
 * @author lhw
 * @description 集合项VO
 * @date 2020/8/24
 */
@Data
public class ListItemsVO extends ItemsVO {
    /**
     * 属性
     */
    private ItemsVO items;
}
