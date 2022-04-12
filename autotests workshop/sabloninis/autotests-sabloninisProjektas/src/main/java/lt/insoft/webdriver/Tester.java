package lt.insoft.webdriver;

import lt.insoft.webdriver.browsers.Browser;
import lt.insoft.webdriver.framework.ZKWebTester;
import lt.insoft.webdriver.utils.RunnerConstants;
import ru.yandex.qatools.allure.annotations.Step;

public class Tester extends ZKWebTester{
	
	public Tester(Browser browser) {
		super(browser);
	}
	
	public static final String DEFAULT_APP_URL = "http://vmi.insoft.lt/isaf";
	
	public void getApplicationUrl() {
		String path = System.getProperty(RunnerConstants.APP_URL_PROPERTY, DEFAULT_APP_URL);
		get(path);
	}
	
	
	@Step
	public void setDropdown(String referencelabelOrPath, int referenceIndex, String dropdownListElement) throws Exception {
		
	}
	
	@Override
	@Step
	public void assertNoError() throws Exception {
		waitForProcessingFinished();
		failIfFoundByText("KLAIDA", "Klaida", "Klaidos", "KLAIDOS", "Exception", "exception", "ERROR", "error");
//		failIfBrowserLogNotEmpty();
	}
	
}
