package lt.insoft.webdriver.webtester;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import lt.insoft.webdriver.browsers.Browser;
import lt.insoft.webdriver.browsers.DriverCreator;
import ru.yandex.qatools.allure.annotations.Attachment;

/**
 * Klasė, skirta inicijuoti webdriverį.
 * </br> </br><b>Svarbūs laukai:</b> </br> 
 * {@link #browser} — Naršyklės pasirinkimas. Galimi variantai "Chrome", "Firefox".; </br> 
 * {@link #logTimestamp} — Kintamasis, nurodantis ar loginant rodyti laiko žymę. </br>
 * {@link #defaultTimeOut} — Kintamasis, nurodantis defaultu, kiek laiko webdriveris turi ieškoti elemento.
 */
public abstract class WebTesterBase {
	protected WebDriver driver = null;
	private final DriverCreator driverCreator = new DriverCreator();
//	protected String server = System.getProperty("serverAddress", "NONE");	

	private Browser browser;
	
	public Browser getBrowser() {
		return browser;
	}

	public void setBrowser(Browser browser) {
		this.browser = browser;
	}

	/**
	 * Kintamasis, nurodantis kiek laiko webdriveris turi ieškoti elemento. 
	 */
	private int implicitWait = Integer.parseInt(System.getProperty("defaultImplicitWait", "30"));
	
	
	public WebTesterBase(){
	}
		
	public WebTesterBase(Browser browser){
		this.browser=browser;
	}

	public void init() throws Exception {
		driver = driverCreator.createDriver(browser);
		implicitWait();
		pageLoadTimeout(implicitWait);
	}
	
	
	/**
	 * Metodas grąžina laiką, kiek sekundžių webdriveris ieškos webelemento iki kol išmes NoSuchElementException.
	 */
	public int getLatestImplicitWait(){
		return implicitWait;
	}

	/**
	 * Metodas nurodo laiką, kiek sekundžių webdriveris turi ieškoti webelemento prieš išmesdamas NoSuchElementException. 
	 * Pagal nutylėjimą pradinė laukimo reikšmė yra 30 sekundžių arba tiek, kiek nustatyta su defaultImplicitWait parametru.
	 */
	public void implicitWait(int implicitWait){
		this.implicitWait = implicitWait;
		driver.manage().timeouts().implicitlyWait(implicitWait, TimeUnit.SECONDS);
	}
	
	/**
	 * Metodas nustato laiką, kiek sekundžių webdriveris ieškos webelemento prieš išmesdamas NoSuchElementException.
	 * Laiko reikšmė nustatomą pagal paskutinę {@link #implicitWait(int)} įvestą reikšmę.
	 */
	public void implicitWait(){
		driver.manage().timeouts().implicitlyWait(implicitWait, TimeUnit.SECONDS);
	}
	
	public void pageLoadTimeout(int sec) {
		driver.manage().timeouts().pageLoadTimeout(sec, TimeUnit.SECONDS);
	}
	
	/**
	 * Metodas nustato laiką, kiek milisekundžių webdriveris ieškos webelemento prieš išmesdamas NoSuchElementException.
	 * Milisekundinis elementų paieškos laikas yra niekada nenaudojamas ieškant webelementų,
	 * nes automatiniai testai dažnai sustotų neradę elemento dėl lėto puslapio užkrovimo ar kitų problemų.
	 * Šis metodas naudingas ieškant elementų per trycatch blokus cikluose, taip sukuriant neišreikštinį(explisit) laukimo laiką,
	 * kad sutrumpinti automatinių testų vykdymo laiką. Reikia nepamiršti baigus neišreikštinę paiešką, grąžinti paieškos laiką į sekundinį,
	 * panaudojant {@link #implicitWait()}
	 */
	public void implicitMilis(int milli) {
		driver.manage().timeouts().implicitlyWait(milli, TimeUnit.MILLISECONDS);
	}

	public void destroy() {
		try {
			driver.close();
			driver.quit();
		} catch (NullPointerException e) {
			System.out.println("Could not destroy driver.");
		}
	}

	public WebDriver getDriver() {
		return driver;
	}	
	
	@Attachment
	public byte[] screenshot() throws IOException {
		if (driver instanceof TakesScreenshot) {
			return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
		} else {
			return new byte[] {};
		}
	}	
	
	public File getResource(String resourceName) throws IOException{
		if(resourceName.equals("")){
			return null;
		}
		File file = new File(System.getProperty("java.io.tmpdir") + "/" + resourceName);
		if (!file.exists()) {
			FileOutputStream fos = new FileOutputStream(file, false);
			try {
				IOUtils.copy(WebTesterBase.class.getClassLoader().getResourceAsStream(resourceName), fos);
			} finally {
				IOUtils.closeQuietly(fos);
			}
		}
		return file;
	}	

}