package lt.insoft.webdriver.runner.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

public class AutotestsUtils {

	public static List<String> getTestMethods(String packageName) {
		Reflections reflections = new Reflections(new ConfigurationBuilder().setUrls(ClasspathHelper.forPackage(packageName)).setScanners(new MethodAnnotationsScanner()));	
		Collection<Method> methods = reflections.getMethodsAnnotatedWith(Test.class);

		List<String> result = new ArrayList<>();
		for (Method method : methods) {
			result.add(method.getDeclaringClass().getName() + ":" + method.getName());
		}
		Collections.sort(result);

		return result;
	}

	public static Set<Method> getMethodsAnnotatedWith(Class<?> type, Class<? extends Annotation> annotation) {
		Reflections reflections = new Reflections(new ConfigurationBuilder().setUrls(ClasspathHelper.forClass(type)).setScanners(new MethodAnnotationsScanner()));
		return reflections.getMethodsAnnotatedWith(annotation);
	}

	public static void invokeMultiple(Collection<Method> methods) throws Exception {
		invokeMultiple(null, methods);
	}

	public static void invokeMultiple(Object instance, Collection<Method> methods) throws Exception {
		for (Method method : methods) {
			method.invoke(instance);
		}
	}


}