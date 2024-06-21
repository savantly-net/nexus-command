package net.savantly.nexus.webhooks.dom.userActions;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import org.apache.causeway.applib.CausewayModuleApplib.ActionDomainEvent;

public class DomainActionFinder {

    public static void main(String[] args) {
        try {
            List<Class<ActionDomainEvent>> classes = findAllClassesExtending("net.savantly", ActionDomainEvent.class);
            for (Class<?> clazz : classes) {
                System.out.println(clazz.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static <T> List<Class<T>> findAllClassesExtending(String packageName, Class<T> superClass) throws ClassNotFoundException, IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        assert classLoader != null;
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList<>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }
        ArrayList<Class<T>> classes = new ArrayList<>();
        for (File directory : dirs) {
            classes.addAll(findClasses(directory, packageName, superClass));
        }
        return classes;
    }

    private static <T> List<Class<T>> findClasses(File directory, String packageName, Class<T> superClass) throws ClassNotFoundException {
        List<Class<T>> classes = new ArrayList<>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    assert !file.getName().contains(".");
                    classes.addAll(findClasses(file, packageName + "." + file.getName(), superClass));
                } else if (file.getName().endsWith(".class")) {
                    Class<T> clazz = (Class<T>) Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6));
                    if (superClass.isAssignableFrom(clazz) && !Modifier.isAbstract(clazz.getModifiers())) {
                        classes.add(clazz);
                    }
                }
            }
        }
        return classes;
    }
}
