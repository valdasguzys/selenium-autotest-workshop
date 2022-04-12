package lt.insoft.webdriver.framework;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import lt.insoft.webdriver.browsers.Browser;
import lt.insoft.webdriver.webtester.WebTester;

public class NETWebTester extends WebTester {

	public NETWebTester(Browser browser) {
		super(browser);
	}

	@Override
	public void waitForProcessingFinished() {
		ExpectedCondition<Boolean> e = d -> {
			boolean insoft_ajax_processing = false;
			try {
				insoft_ajax_processing = (Boolean) ((JavascriptExecutor) d)
						.executeScript("return insoft_ajax_processing == 0");
			} catch (WebDriverException wde) {
				if (!wde.getMessage().contains("Modal dialog present")
						&& !wde.getMessage().contains("is not defined")) {
					throw wde;
				}
			} catch (NullPointerException ignored) {
			}

			if (!insoft_ajax_processing) {
				return true;
			}
			boolean tIconTLoadingDisplayed = false;

			try {
				tIconTLoadingDisplayed = driver.findElement(By.className("t-icon t-loading")).isDisplayed();
			} catch (NoSuchElementException ignored) {
			}

			if (!tIconTLoadingDisplayed) {
				return true;
			}
			boolean globalLoadingDialogDisplayed = false;
			try {
				globalLoadingDialogDisplayed = driver.findElement(By.id("global-loading-dialog")).isDisplayed();
			} catch (NoSuchElementException ignored) {
			}

			return !globalLoadingDialogDisplayed;
		};

		WebDriverWait wait = new WebDriverWait(driver, getLatestImplicitWait(), 500);
		wait.withMessage("Partial loading was too long.");
		wait.until(e);
	}
	
}
