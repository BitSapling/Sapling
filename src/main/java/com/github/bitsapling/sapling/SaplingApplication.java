package com.github.bitsapling.sapling;

import com.github.bitsapling.sapling.plugin.PluginManager;
import com.github.bitsapling.sapling.plugin.java.DummyStub;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ClassFile;
import javassist.bytecode.ConstPool;
import javassist.bytecode.annotation.ArrayMemberValue;
import javassist.bytecode.annotation.StringMemberValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.Arrays;

@SpringBootApplication
public class SaplingApplication {
    private static final Logger LOGGER = LoggerFactory.getLogger(SaplingApplication.class);
    @SuppressWarnings("FieldCanBeLocal")
    private PluginManager pluginManager;

    public static void main(String[] args) throws Throwable {
        ClassPool pool = ClassPool.getDefault();
        CtClass ctClass = pool.get("com.github.bitsapling.sapling.plugin.java.DummyScanObject");
        ClassFile classFile = ctClass.getClassFile();
        ConstPool constpool = classFile.getConstPool();
        AnnotationsAttribute info = (AnnotationsAttribute) classFile.getAttribute("RuntimeVisibleAnnotations");
        var scan = info.getAnnotation("org.springframework.context.annotation.ComponentScan");
        var arr = new ArrayMemberValue(constpool);
        arr.setValue(new StringMemberValue[]{
                new StringMemberValue("org.test", constpool),
                // new StringMemberValue("test.b", constpool),
        });
        scan.addMemberValue("basePackages", arr);
        var base = scan.getMemberValue("basePackages");
        classFile.removeAttribute("RuntimeVisibleAnnotations");
        info.setAnnotation(scan);
        classFile.addAttribute(info);
        System.out.println(base.toString());
        var clazz = ctClass.toClass(DummyStub.class);
        //DummyScanObject scanObject = new DummyScanObject();
        // System.out.println(Arrays.toString(scanObject.getClass().getDeclaredAnnotations()));
        System.out.println(Arrays.toString(clazz.getDeclaredAnnotations()));
        SpringApplication.run(SaplingApplication.class, args);
    }

    private void preInit() {
        pluginManager = new PluginManager();
        pluginManager.setLoading(true);
        LOGGER.info("Loading plugins, please wait...");
        try {
            pluginManager.loadPlugins();
        } catch (IOException e) {
            LOGGER.error("Failed to load plugins", e);
        }
        pluginManager.setLoading(false);
    }

}
