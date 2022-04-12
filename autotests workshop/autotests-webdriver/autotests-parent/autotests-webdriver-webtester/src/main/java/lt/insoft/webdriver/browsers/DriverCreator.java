package lt.insoft.webdriver.browsers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

public class DriverCreator {

	public WebDriver createDriver(Browser browser) throws Exception {
		switch (browser) {
		case EDGE:
			return edgeDriverSetup();
		case SAFARI:
			return safariDriverSetup();
		case FIREFOX:
			return firefoxDriverSetup();
		case CHROME:
			return chromeDriverSetup();
		default:
			return null;
		}
	}

	public File getResource(String resourceName) throws IOException {
		if (resourceName.equals("")) {
			return null;
		}

		String fileName = resourceName;
		if (!fileName.startsWith(File.separator)) {
			fileName = File.separator + fileName;
		}

		File file = new File(System.getProperty("java.io.tmpdir") + fileName);
		if (!file.exists()) {
			FileOutputStream fos = new FileOutputStream(file, false);
			try {
				IOUtils.copy(DriverCreator.class.getClassLoader().getResourceAsStream(resourceName), fos);
				if (!isWindows()) {
					Runtime.getRuntime().exec("chmod u+x " + file.getAbsolutePath());
				}
			} finally {
				IOUtils.closeQuietly(fos);
			}
		}
		return file;
	}

	private WebDriver chromeDriverSetup() {
		WebDriverManager.chromedriver().clearResolutionCache();
		WebDriverManager.chromedriver().setup();
		ChromeOptions options = new ChromeOptions();
		options.addArguments("disable-plugins");
		options.addArguments("disable-extensions");
		options.addArguments("start-maximized");
		options.addArguments("disable-gpu");
		options.addArguments("disable-dev-shm-usage");
		options.addArguments("no-sandbox");
		options.setExperimentalOption("useAutomationExtension", false);
		options.setAcceptInsecureCerts(true);
		return new ChromeDriver(options);
	}

	private WebDriver firefoxDriverSetup() {
		WebDriverManager.firefoxdriver().clearResolutionCache();
		WebDriverManager.firefoxdriver().setup();
		WebDriver driver = new FirefoxDriver();
		driver.manage().window().maximize();
		return driver;
	}

	private WebDriver edgeDriverSetup() {
		WebDriverManager.edgedriver().clearResolutionCache();
		WebDriverManager.edgedriver().setup();
		return new EdgeDriver();
	}

	private WebDriver safariDriverSetup() {
		WebDriverManager.safaridriver().clearResolutionCache();
		WebDriverManager.safaridriver().setup();
		return new SafariDriver();
	}

	private boolean isWindows() {
		String os = System.getProperty("os.name");
		return os != null && os.toLowerCase().contains("windows");
	}

}
