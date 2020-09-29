package org.starr.yapi.api;

import cn.hutool.core.util.ReflectUtil;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.FieldDoc;
import com.sun.javadoc.RootDoc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.starr.yapi.api.vo.FieldEntry;
import org.starr.yapi.api.vo.ModelClassDocVO;

import java.util.*;

/**
 * 获取某一个java文件代码中属性对应的注释
 *
 * @author guzt
 */
public class DocletUtil {

    private static final Logger logger = LoggerFactory.getLogger(DocletUtil.class);

    private static RootDoc rootDoc;
    private final String javaBeanFilePath;
    private String startMark = "";

    public static boolean start(RootDoc root) {
        rootDoc = root;
        return true;
    }

    public DocletUtil(String javaBeanFilePath) {
        this.javaBeanFilePath = javaBeanFilePath;
    }

    public DocletUtil(String basePath, Class<?> clz) {
        String clzPath = clz.getName().replace(".", "\\\\");
        this.javaBeanFilePath = basePath + clzPath + ".java";
    }

    public DocletUtil(String basePath, Class<?> clz, String startMark) {
        String clzPath = clz.getName().replace(".", "\\\\");
        this.javaBeanFilePath = basePath + clzPath + ".java";
        this.startMark = startMark;
    }

    public ModelClassDocVO exec() {
        ModelClassDocVO modelClassDocVO = new ModelClassDocVO();
        String[] strings = {"-doclet", DocletUtil.class.getName(), "-docletpath",
                DocletUtil.class.getResource("/").getPath(), "-encoding", "utf-8", javaBeanFilePath};
        String msg1 = Arrays.toString(strings);
        logger.info(msg1);
        com.sun.tools.javadoc.Main.execute(strings);
        ClassDoc[] classes = rootDoc.classes();

        if (classes == null || classes.length == 0) {
            String msg = javaBeanFilePath + " 无ClassDoc信息";
            logger.warn(msg);
            return modelClassDocVO;
        }

        List<FieldEntry> entries = new ArrayList<>();
        Map<String, FieldEntry> map = new HashMap<>(30);
        ClassDoc classDoc = classes[0];
        // 获取类的名称
        modelClassDocVO.setModelClassName(classDoc.name());
        // 获取类的注释
        String classComment = ReflectUtil.getFieldValue(classDoc, "documentation").toString();
        String spitStr = "\n";
        for (String msg : classComment.split(spitStr)) {
            if (!msg.trim().startsWith("@") && msg.trim().length() > 0) {
                modelClassDocVO.setModelCommentText(msg);
                break;
            }
        }
        // 获取属性名称和注释
        FieldDoc[] fields = classDoc.fields(false);
        FieldEntry fildEntry;
        for (FieldDoc field : fields) {
            fildEntry = new FieldEntry(field.name(), field.type().typeName(), field.commentText());
            entries.add(fildEntry);
            map.put(startMark + field.name(), fildEntry);
        }

        modelClassDocVO.setFieldEntryList(entries);
        modelClassDocVO.setFieldEntryMap(map);
        return modelClassDocVO;
    }
}
