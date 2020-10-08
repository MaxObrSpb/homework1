package ru.digitalhabbits.homework1.service;

import org.slf4j.Logger;
import ru.digitalhabbits.homework1.plugin.PluginInterface;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import static com.google.common.collect.Lists.newArrayList;
import static org.slf4j.LoggerFactory.getLogger;

public class PluginLoader {
    private static final Logger logger = getLogger(PluginLoader.class);

    private static final String PLUGIN_EXT = "jar";
    private static final String PACKAGE_TO_SCAN = "ru.digitalhabbits.homework1.plugin";

    @Nonnull
    public List<Class<? extends PluginInterface>> loadPlugins(@Nonnull String pluginDirName) {
        List<Class<? extends PluginInterface>> result = newArrayList();
        File path = new File(pluginDirName);
        if (path.exists() && path.isDirectory()) {
            File[] files = path.listFiles();
            for (File file: files) {
                if (file.getName().endsWith("." + PLUGIN_EXT)) {
                    File jarPath = new File(new StringBuilder(pluginDirName).append("/").append(file.getName()).toString());
                    try {
                        JarFile jarfile = new JarFile(jarPath);
                        for (Enumeration em1 = jarfile.entries(); em1.hasMoreElements();) {
                            String className = ((JarEntry) em1.nextElement()).getName();
                            if (className.endsWith(".class")) {
                                try {
                                    URLClassLoader classLoader = new URLClassLoader(
                                            new URL[] {jarPath.toURI().toURL()},
                                            this.getClass().getClassLoader()
                                    );
                                    Class<?> cl = Class.forName(className.replace(".class", "").replaceAll("/", "."), true, classLoader);
                                    for (Class implementation : cl.getInterfaces()) {
                                        if (implementation.getName().equals(PACKAGE_TO_SCAN + ".PluginInterface")) {
                                            result.add(cl.asSubclass(PluginInterface.class));
                                            break;
                                        }
                                    }
                                }
                                catch (ClassNotFoundException e) {
                                    logger.info("Class '{}' doesn't exist", className);
                                }
                            }
                        }
                    }
                    catch (IOException e) {
                        logger.info("File '{}' isn't a jar-archive inside", pluginDirName + "/" + file.getName());
                    }
                }
            }
        }

        return result;
    }
}