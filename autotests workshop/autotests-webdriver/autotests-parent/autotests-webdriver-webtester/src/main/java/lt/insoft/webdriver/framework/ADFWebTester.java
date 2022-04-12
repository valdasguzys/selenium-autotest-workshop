package lt.insoft.webdriver.framework;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import lt.insoft.webdriver.browsers.Browser;
import lt.insoft.webdriver.webtester.WebTester;

public class ADFWebTester extends WebTester {

	public ADFWebTester(Browser browser) {
		super(browser);
	}

	@Override
	public void waitForProcessingFinished() {
		ExpectedCondition<Boolean> e = d -> {
			try {
				return !(Boolean) ((JavascriptExecutor) d).executeScript("return _pprBlocking");
			} catch (WebDriverException wde) {
				if (wde.getMessage().contains("Modal dialog present")
						|| wde.getMessage().contains("is not defined")) {
					return false;
				} else {
					throw wde;
				}
			} catch (NullPointerException npe) {
				return false;
			}
		};
		WebDriverWait wait = new WebDriverWait(driver, getLatestImplicitWait(), 100);
		wait.withMessage("_pprBlocking should be false");
		wait.until(e);
	}
	
}
