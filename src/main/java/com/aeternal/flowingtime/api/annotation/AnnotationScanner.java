package com.aeternal.flowingtime.api.annotation;

import com.aeternal.flowingtime.FlowingTime;
import net.minecraftforge.fml.ModList;
import org.objectweb.asm.Type;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Usage during FMLCommonSetupEvent:
 *   List<Class<?>> classes = AnnotationScanner.findAnnotatedClasses(AutoRegister.class);
 */
public final class AnnotationScanner {

    public static List<Class<?>> findAnnotatedClasses(Class<? extends Annotation> annotationClass) {
        Type annotationType = Type.getType(annotationClass);

        return ModList.get().getAllScanData().stream()
                .flatMap(scanData -> scanData.getAnnotations().stream())
                .filter(a -> annotationType.equals(a.annotationType()))
                .map(a -> {
                    try {
                        return Class.forName(a.clazz().getClassName());
                    } catch (ClassNotFoundException e) {
                        FlowingTime.LOGGER.error("Failed to load annotated class: {}", a.clazz().getClassName(), e);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private AnnotationScanner() {}
}
