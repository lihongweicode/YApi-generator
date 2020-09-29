package org.starr.yapi.api.vo;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author lhw
 * @description
 * @date 2020/8/24
 */
@Data
public class ModelClassDocVO {
    private String modelClassName;
    private String modelCommentText;
    private List<FieldEntry> fieldEntryList;
    private Map<String, FieldEntry> fieldEntryMap;
}
