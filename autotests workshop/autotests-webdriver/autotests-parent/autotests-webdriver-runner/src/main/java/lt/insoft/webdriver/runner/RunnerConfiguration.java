package lt.insoft.webdriver.runner;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lt.insoft.webdriver.browsers.Browser;

@Component
@ConfigurationProperties(prefix = "autotests.runner")
public class RunnerConfiguration {

	private int threadCount = 20;
	private String[] tests;
	private String testsPackage;
	private String listerOutFile;
	private String appUrl;
	private String jobUrl;
	private String logServerUrl;
	private Browser browser = Browser.CHROME;

	public int getThreadCount() {
		return threadCount;
	}

	public void setThreadCount(int threadCount) {
		this.threadCount = threadCount;
	}

	public String[] getTests() {
		return tests;
	}

	public void setTests(String[] tests) {
		this.tests = tests;
	}

	public String getTestsPackage() {
		return testsPackage;
	}

	public void setTestsPackage(String testsPackage) {
		this.testsPackage = testsPackage;
	}

	public String getListerOutFile() {
		return listerOutFile;
	}

	public void setListerOutFile(String listerOutFile) {
		this.listerOutFile = listerOutFile;
	}

	public String getAppUrl() {
		return appUrl;
	}

	public void setAppUrl(String appUrl) {
		this.appUrl = appUrl;
	}

	public String getJobUrl() {
		return jobUrl;
	}

	public void setJobUrl(String jobUrl) {
		if (!jobUrl.endsWith("/")) {
			jobUrl += "/";
		}
		this.jobUrl = jobUrl;
	}

	public String getLogServerUrl() {
		return logServerUrl;
	}

	public void setLogServerUrl(String logServerUrl) {
		if (!logServerUrl.endsWith("/")) {
			logServerUrl += "/";
		}
		this.logServerUrl = logServerUrl;
	}

	public Browser getBrowser() {
		return browser;
	}

	public void setBrowser(Browser browser) {
		this.browser = browser;
	}

}
