package kr.co.mostx.japi.excel;

import lombok.NoArgsConstructor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@NoArgsConstructor
public class SuperClassReflectionUtil {
    public static List<Field> getAllFields(Class<?> cla) {
        List<Field> fields = new ArrayList<>();

        for (Class<?> claInClasses : getAllClassesIncludingSuperClassess(cla, true)) {
            fields.addAll(Arrays.asList(claInClasses.getDeclaredFields()));
        }

        return fields;
    }

    public static Annotation getAnnotation(Class<?> cla,
                                            Class<? extends Annotation> targetAnnotation) {
        for (Class<?> claInClasses : getAllClassesIncludingSuperClassess(cla, false)) {
            if (claInClasses.isAnnotationPresent(targetAnnotation)) {
                return claInClasses.getAnnotation(targetAnnotation);
            }
        }

        return null;
    }

    public static Field getField(Class<?> cla, String name) throws NoSuchFieldException {
        for (Class<?> claInClasses : getAllClassesIncludingSuperClassess(cla, false)) {
            for (Field field : claInClasses.getDeclaredFields()) {
                if (field.getName().equals(name)) {
                    return claInClasses.getDeclaredField(name);
                }
            }
        }
        throw new NoSuchFieldException();
    }

    private static List<Class<?>> getAllClassesIncludingSuperClassess(Class<?> cla, boolean fromSuper) {
        List<Class<?>> classes = new ArrayList<>();

        while (cla != null) {
            classes.add(cla);
            cla = cla.getSuperclass();
        }

        if (fromSuper) {
            Collections.reverse(classes);
        }

        return classes;
    }
}
