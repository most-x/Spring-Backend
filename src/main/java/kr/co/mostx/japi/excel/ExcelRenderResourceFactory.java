package kr.co.mostx.japi.excel;

import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ExcelRenderResourceFactory {
    public static ExcelRenderResource prepareRenderResource(Class<?> type) {
        String fileName = getFileName(type);
        Map<String, String> headerNameMap = new LinkedHashMap<>();
        List<String> fieldNames = new ArrayList<>();

        for (Field field : SuperClassReflectionUtil.getAllFields(type)) {
            if (field.isAnnotationPresent(ExcelColumnName.class)) {
                ExcelColumnName annotation = field.getAnnotation(ExcelColumnName.class);
                fieldNames.add(field.getName());
                String headerName = annotation.headerName();
                headerName = StringUtils.hasText(headerName) ? headerName : field.getName();
                headerNameMap.put(field.getName(), headerName);
            }
        }
        return new ExcelRenderResource(fileName, headerNameMap, fieldNames);
    }

    private static String getFileName(Class<?> type) {
        String fileName = type.getSimpleName();

        if (type.isAnnotationPresent(ExcelFileName.class)) {
            fileName = type.getAnnotation(ExcelFileName.class).fileName();
            if (!StringUtils.hasText(fileName)) {
                fileName = type.getSimpleName();
            }
        }
        return fileName;
    }
}
