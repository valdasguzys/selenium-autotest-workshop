package lt.insoft.webdriver.framework;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import lt.insoft.webdriver.browsers.Browser;
import lt.insoft.webdriver.webtester.WebTester;

public class ZKWebTester extends WebTester {
	
	public ZKWebTester(Browser browser) {
		super(browser);
	}

	@Override
	public void waitForProcessingFinished() {
		ExpectedCondition<Boolean> e = d -> {
			try {
				return ((JavascriptExecutor) d).executeScript("return !!zAu.processing()") == Boolean.FALSE;
			} catch (WebDriverException wde) {
				if (wde.getMessage().contains("is not defined")) {
					return true;
				} else {
					throw wde;
				}
			}
		};
		(new WebDriverWait(driver, getLatestImplicitWait(), 100)).withMessage("zAu.processing() should be false").until(e);
	}
}
