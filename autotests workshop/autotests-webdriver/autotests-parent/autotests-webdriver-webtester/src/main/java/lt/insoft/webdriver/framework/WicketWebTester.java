package lt.insoft.webdriver.framework;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import lt.insoft.webdriver.browsers.Browser;
import lt.insoft.webdriver.webtester.WebTester;

public class WicketWebTester extends WebTester {

	public WicketWebTester(Browser browser) {
		super(browser);
	}

	@Override
	public void waitForProcessingFinished() {
		((JavascriptExecutor) driver)
				.executeScript("if (typeof wicketAjaxBusy  == 'undefined') wicketAjaxBusy = function () {"
						+ "for (var channelName in Wicket.channelManager.channels) {"
						+ "if (Wicket.channelManager.channels[channelName].busy) { return true; }"
						+ "return false;}};");
		ExpectedCondition<Boolean> e = d -> {
			try {
				return (Boolean) ((JavascriptExecutor) d).executeScript("return !wicketAjaxBusy()");
			} catch (WebDriverException wde) {
				if (wde.getMessage().contains("is not defined")) {
					return true;
				} else {
					throw wde;
				}
			}
		};
		(new WebDriverWait(driver, getLatestImplicitWait(), 100)).withMessage("tk.activeAjaxCount should be 0")
				.until(e);
	}
}
