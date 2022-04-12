package lt.insoft.webdriver.runner.model;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.reflect.MethodUtils;

public class TestItem {
	private Class<?> testClass = null;
	private Method testMethod;

	private static boolean success = true;

	private List<String> serverLog = Collections.emptyList();
	private String appName;

	
	
	
	public TestItem() {
	}

	public TestItem(Class<?> testClass, String testMethodName) {
		this.testMethod = MethodUtils.getAccessibleMethod(testClass, testMethodName);
		this.testClass = testClass;
	}
	
	public Class<?> getTestClass() {
		return testClass;
	}
	
	public String getTitle() {
		return testClass.getSimpleName() + "." + testMethod.getName();
	}

	public Method getTestMethod() {
		return testMethod;
	}

	public void setTestMethod(Method testMethod) {
		this.testMethod = testMethod;
	}

	public static boolean getIsSuccess() {
		return success;
	}

	public static void setNoSuccess() {
		success = false;
	}

	public List<String> getServerLog() {
		return serverLog;
	}

	public void setServerLog(List<String> serverLog) {
		this.serverLog = serverLog;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

}