package lt.insoft.webdriver.webtester;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import lt.insoft.webdriver.browsers.Browser;
import lt.insoft.webdriver.utils.Highlighters;
import ru.yandex.qatools.allure.annotations.Step;

public class WebTester extends WebTesterBase {

	public WebTester() {
	}

	public WebTester(Browser browser) {
		super(browser);
	}

	/**
	 * Kintamasis, nurodantis defaultinį paieškos {@link #findVisibleElements}
	 * kontekstą.
	 */

	private static final String DEFAULT_CONTEXT = "//html";

	/**
	 * Kintamasis, nurodantis webelemento xpathą, kurio kontekste vykdomas
	 * {@link #findVisibleElements}.
	 */
	private String context = "//html";

	// --------------------------------------------------------------------------------------------
	// Context navigation
	// --------------------------------------------------------------------------------------------
	/**
	 * Metodas pagal nurodytą stringą randa webelementą, kurio kontekste bus
	 * vykdomos webelementų paieškos {@link #findVisibleElements}. Kontekstinio
	 * elemento nurodymas pagreitina elemento paiešką. Kai kuriais atvejais paieška
	 * gali neaptikti elementų, jei nėra nurodytas kontekstas. Patogu naudoti su
	 * Pop-up'ais. Nurodžius kontekstą, paiešką neaptiks elementų, esančių už
	 * konteksto ir mes NoSuchElementException. Kad išvengti šios klaidos, reikia
	 * grąžinti defaultinį kontekstą, aprėpiantį visą html, naudojant
	 * {@link #setContextToDefault()}
	 */
	public void setContext(String xpath) throws Exception {
		waitForProcessingFinished();
		this.context = xpath;
		if (!DEFAULT_CONTEXT.equals(xpath)) {
			Highlighters.highlightBlue(driver, driver.findElement(By.xpath(context)));
		}
	}

	/**
	 * Metodas užsetina defaultinį paieškos {@link #findVisibleElements} kontekstą.
	 * Paieškos kontekstas apima visą puslapio html.
	 */
	public void setContextToDefault() throws Exception {
		waitForProcessingFinished();
		this.context = DEFAULT_CONTEXT;
		Highlighters.highlightBlue(driver, driver.findElement(By.xpath(context)));
	}

	public WebElement getContextElement() throws Exception {
		return Highlighters.highlightBlue(driver, driver.findElement(By.xpath(context)));
	}

	public String getContextPath() throws Exception {
		return this.context;
	}

	// --------------------------------------------------------------------------------------------
	// Finders
	// --------------------------------------------------------------------------------------------

	/**
	 * Metodas grąžina listą WebElementų atitinkančių by sąlygą dabartiniame
	 * {@link #context}. Metodas aptinka visus elementus pagal by sąlygą, tada
	 * atrenka tuos kurie yra matomi pagal {@link #isDisplayed}. Ieškant elementų,
	 * jei iškrenta StaleElementReferenceException paieška pakartotinai įvykdoma
	 * palaukus 1 sekundę. Neradus elementų gražins tuščią listą.
	 */
	@Step
	public List<WebElement> findVisibleElements(By by) throws Exception {
		waitForProcessingFinished();
		List<WebElement> visibleElements = new ArrayList<WebElement>();
		List<WebElement> foundElements = null;
		try {
			foundElements = getContextElement().findElements(by);
		} catch (StaleElementReferenceException ex) {
			waitForProcessingFinished();
			foundElements = getContextElement().findElements(by);
		}
		for (WebElement e : foundElements) {
			int attempts = 0;
			while (attempts < 5) {
				try {
					if (e.isDisplayed()) {
						visibleElements.add(e);
					}
					break;
				} catch (StaleElementReferenceException e2) {
				}
				attempts++;
			}

		}
		return visibleElements;
	}

	@Step
	public WebElement findVisibleElement(By by) throws Exception {
		waitForProcessingFinished();
		List<WebElement> visibleElements = findVisibleElements(by);
		if (!visibleElements.isEmpty()) {
			return visibleElements.get(0);
		} else {
			throw new NoSuchElementException("Elementas nerastas " + by.toString());
		}
	}

	/**
	 * Metodas patikrina ar stringe yra '//', './', '[=@', jeigu yra bent vienas,
	 * grąžina true.
	 */
	private boolean isXpath(String labelOrPath) {
		return StringUtils.containsAny(labelOrPath, "[=@")
				|| (labelOrPath != null && (labelOrPath.startsWith("//")) || labelOrPath.startsWith("./"));
	}

	/**
	 * Metodas ieško elemento pagal pavadinimą arba xpath, jeigu yra keli elementai
	 * atitinkantys paieškos kriterijų, elementą konkretizuoti nurodant indeksą.
	 */
//	veikia kaip findVisibleElements ir findVisibleElement tik naudoja label arba xpath, yra error handlingas.
	@Step
	public WebElement find(String labelOrPath, int index) throws Exception {
		waitForProcessingFinished();
		String builtXpath = isXpath(labelOrPath) ? labelOrPath : buildLabelXpath("*", labelOrPath);
		builtXpath = builtXpath.replaceFirst("//", "./descendant::");
		By by = By.xpath(builtXpath);
		if (index == 0) {
			try {
//				Test out the scrolling
				((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);",
						Highlighters.highlightGreen(driver, findVisibleElement(by)));
				return findVisibleElement(by);
			} catch (NoSuchElementException e) {
				throw new NoSuchElementException("window " + driver.getTitle() + " " + e.getMessage(), e);
			}
		} else {
			try {
//				Test out the scrolling
				((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);",
						Highlighters.highlightGreen(driver, findVisibleElements(by).get(index)));
				return findVisibleElements(by).get(index);
			} catch (IndexOutOfBoundsException e) {
				throw new NoSuchElementException("window " + driver.getTitle() + " " + by.toString(), e);
			}
		}
	}

	/**
	 * {@link #find(String, int)}
	 * 
	 */
	@Step
	public WebElement find(String labelOrPath) throws Exception {
		return find(labelOrPath, 0);
	}

	@Step
	public WebElement findNear(String referenceLabelOrPath, String targetElement) throws Exception {
		return findNear(referenceLabelOrPath, 0, targetElement, 0);
	}

	/**
	 * Metodas grąžina elementą esantį šalia nuorodinio elemento. Pirmiausia
	 * randamas nuorodinis elementas pagal labelį arbą xpath, tada ieškomas
	 * elementas nuorodinio elemento kontekste. Radus nuorodinį elementą, paieška
	 * pagal xpath tikrina ar nėra ieškomo elemento tarp nuorodinio elemento
	 * palikuonių. Neradus, paieškos kontekstas praplečiamas iki nuorodinio elemento
	 * tėvo konteksto ir paieška kartojama. <br>
	 * Paieška vykdoma kol randamas elementas pagal paieškos kriterijus arba kol
	 * nuorodinio elemento kontekstas praplečiamas 10 kartų. Jeigu puslapio DOM
	 * objekte nėra 10 lygmenų, paieška sustoja pasiekus html elementą. Jeigu
	 * paieškos kriterijų atitinka keli nuorodiniai arba ieškomi elementai, reikia
	 * nurodyti indeksą. Vykdant metodą implicitlyWait pakeičiamas į 100
	 * milisekundžių, baigus metodą implicitlyWait atstatomas į prieš metodo
	 * kvietimą buvusią reikšmę.
	 * 
	 * @param referenceLabelOrPath nuorodinio elemento labelis arba xpath.
	 * @param referenceIndex       nuorodinio elemento indeksas.
	 * @param targetElement        ieškomo elemento xpath.
	 * @param targetIndex          ieškomo elemento indeksas.
	 * 
	 */
	@Step
	public WebElement findNear(String referenceLabelOrPath, int referenceIndex, String targetElementXpath,
			int targetIndex) throws Exception {
		int level = 0;
		try {
			WebElement reference = Highlighters.highlightGreen(driver, find(referenceLabelOrPath, referenceIndex));
			implicitMilis(300);
			WebElement descendant = null;
			do {
				try {
					descendant = reference.findElements(By.xpath("./descendant::" + targetElementXpath))
							.get(targetIndex);
				} catch (IndexOutOfBoundsException e) {
				}
				reference = reference.findElement(By.xpath("./parent::*"));
				if (level++ > 10 || "html".equalsIgnoreCase(reference.getTagName())) {
					throw new NoSuchElementException("Elementas nerastas");
				}
			} while (descendant == null);
			return Highlighters.highlightRed(driver, descendant);
		} finally {
			implicitWait();
		}
	}

	@Step
	public WebElement findNearByCssSelector(String referenceLabelOrPath, int referenceIndex, String targetElementCss,
			int targetIndex) throws Exception {
		int level = 0;
		try {
			WebElement reference = Highlighters.highlightGreen(driver, find(referenceLabelOrPath, referenceIndex));
			implicitMilis(100);
			WebElement descendant = null;
			do {
				try {
					descendant = reference.findElements(By.cssSelector(targetElementCss)).get(targetIndex);
				} catch (IndexOutOfBoundsException e) {
				}
				reference = reference.findElement(By.xpath("./parent::*"));
				if (level++ > 10 || "html".equalsIgnoreCase(reference.getTagName())) {
					throw new NoSuchElementException("Elementas nerastas");
				}
			} while (descendant == null);
			return Highlighters.highlightRed(driver, descendant);
		} finally {
			implicitWait();
		}
	}

	public void failIfFound(String labelOrPath, int index) throws Exception {
		waitForProcessingFinished();
		try {
			find(labelOrPath, index);
			Assert.fail("Element shoud not be found: " + labelOrPath);
		} catch (NoSuchElementException e) {
		} finally {
			implicitWait();
		}
	}

	@Step
	public void failIfFoundByText(String... texts) throws Exception {
		implicitMilis(100);
		for (String text : texts) {
			try {
				find(text);
				implicitWait();
				Assert.fail("Element shoud not be found: " + text);
			} catch (NoSuchElementException e) {
			}
		}
		implicitWait();
	}

	@Step
	public void failIfBrowserLogNotEmpty() throws Exception {
//		Gal but padaryti property, kuris nurodo ar atlikti si tikrinima?
		List<LogEntry> log = driver.manage().logs().get(LogType.BROWSER).filter(Level.SEVERE);
		if (!log.isEmpty()) {
			for (LogEntry logElement : log) {
				System.out.println(logElement.toString());
			}
			Assert.fail("Browser log contains errors!");
		}
	}

	// --------------------------------------------------------------------------------------------
	// Clicks
	// --------------------------------------------------------------------------------------------

	/**
	 * {@link #clickNear(String, int, String, int)}
	 * 
	 */
	@Step
	public void clickNear(String referenceLabelOrPath, String targetElementXpath) throws Exception {
		clickNear(referenceLabelOrPath, 0, targetElementXpath);
	}

	/**
	 * {@link #clickNear(String, int, String, int)}
	 * 
	 */
	@Step
	public void clickNear(String referenceLabelOrPath, int referenceIndex, String targetElementXpath) throws Exception {
		clickNear(referenceLabelOrPath, referenceIndex, targetElementXpath, 0);
	}

	/**
	 * Metodas paclickina elementą, esantį šalia kito elemento. Įvykdžius
	 * paclickinimą įvyksta patikrinimas ar nėra klaidoms būdingų labelių.
	 */
	@Step
	public void clickNear(String referenceLabelOrPath, int referenceIndex, String targetElementXpath, int targetIndex)
			throws Exception {
		findNear(referenceLabelOrPath, referenceIndex, targetElementXpath, targetIndex).click();
		assertNoError();
	}

	/**
	 * Metodas paclickina elementą. Įvykdžius paclickinimą įvyksta patikrinimas ar
	 * nėra klaidoms būdingų labelių.
	 */
	@Step
	public void click(String labelOrPath, int index) throws Exception {
		Highlighters.highlightRed(driver, find(labelOrPath, index)).click();
		assertNoError();
	}

	/**
	 * {@link #click(String, int)}
	 */
	@Step
	public void click(String labelOrPath) throws Exception {
		click(labelOrPath, 0);
	}

	/**
	 * Metodas paclickina elementą pagal By strategiją. Įvykdžius paclickinimą
	 * įvyksta patikrinimas ar nėra klaidoms būdingų labelių.
	 */
	@Step
	public void click(By by) throws Exception {
		Highlighters.highlightRed(driver, findVisibleElement(by)).click();
//		assertNoError();
	}

	// --------------------------------------------------------------------------------------------
	// setText
	// --------------------------------------------------------------------------------------------

	/**
	 * Metodas užpildo tekstinį lauką tekstu. Randami laukai kurie yra input arba
	 * textarea.
	 * 
	 * @param referenceLabelOrPath nuorodinio elemento labelis arba xpath.
	 * @param referenceIndex       nuorodinio elemento indeksas.
	 * @param value                įvedamo teksto reikšmė.
	 * @param inputFieldIndex      tekstinio lauko indeksas.
	 */
	@Step
	public void setText(String referenceLabelOrPath, int referenceIndex, String value, int inputFieldIndex)
			throws Exception {
		WebElement input = findNear(referenceLabelOrPath, referenceIndex, "*[self::input or self::textarea]",
				inputFieldIndex);
		scrollToWebElement(input);
		Assert.assertTrue(referenceLabelOrPath + " input should be enabled.", input.isEnabled());
		Assert.assertTrue(referenceLabelOrPath + " input should be displayed.", input.isDisplayed());
		input.sendKeys(value);
	}
	
	@Step
	public void setRichText(String referenceLabelOrPath, String value) throws Exception {
		setRichText(referenceLabelOrPath, 0, value, 0);
	}
	
	@Step
	public void setRichText(String referenceLabelOrPath, int referenceIndex, String value, int inputFieldIndex) throws Exception {
		WebElement richText = findNear(referenceLabelOrPath, referenceIndex, "div[@contenteditable='true']",
				inputFieldIndex);
		scrollToWebElement(richText);
		Assert.assertTrue(referenceLabelOrPath + " richText should be enabled.", richText.isEnabled());
		Assert.assertTrue(referenceLabelOrPath + " richText should be displayed.", richText.isDisplayed());
		richText.click();
		richText.sendKeys(value);
	}

	/**
	 * Metodas užpildo tekstinį lauką tekstu, elementas randamas naudojantis by
	 * strategiją.
	 */
	@Step
	public void setText(By by, String value) throws Exception {
		WebElement input = driver.findElement(by);
		Highlighters.highlightGreen(driver, input);
		Assert.assertTrue(by + " input should be enabled.", input.isEnabled());
		Assert.assertTrue(by + " input should be displayed.", input.isDisplayed());
		String existing = input.getAttribute("value");
		if (!"".equals(existing)) {
			input.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
		}
		input.sendKeys(value);
		Highlighters.highlightRed(driver, input);
	}

	/**
	 * {@link #setText(String, int, String, int)}
	 * 
	 */
	@Step
	public void setText(String label, String value) throws Exception {
		setText(label, 0, value, 0);
	}

	/**
	 * {@link #setText(String, int, String, int)}
	 * 
	 */
	@Step
	public void setText(String label, String value, int index) throws Exception {
		setText(label, 0, value, index);
	}

	/**
	 * {@link #setText(String, int, String, int)}
	 * 
	 */
	@Step
	public void setText(String label, int index, String value) throws Exception {
		setText(label, index, value, 0);
	}

	// --------------------------------------------------------------------------------------------
	// Uploads
	// --------------------------------------------------------------------------------------------
	@Step
	public void uploadFile(String referenceLabelOrPath, String fileName) throws Exception {
		uploadFile(referenceLabelOrPath, 0, 0, fileName);
	}

	@Step
	public void uploadFile(String referenceLabelOrPath, int referenceIndex, int targetIndex, String fileName)
			throws Exception {
		findNear(referenceLabelOrPath, referenceIndex, "input[@type='file']", targetIndex)
				.sendKeys(getResource(fileName).getAbsolutePath());
	}

	@Step
	public void uploadPDF(String referenceLabelOrPath) throws Exception {
		uploadFile(referenceLabelOrPath, "pdftest.pdf");
	}

	@Step
	public void uploadPhoto(String referenceLabelOrPath) throws Exception {
		uploadFile(referenceLabelOrPath, "picturetest.jpg");
	}

	// --------------------------------------------------------------------------------------------
	// Waits
	// --------------------------------------------------------------------------------------------

	public void waitUntilElementDoesntExist(final By by) throws Exception {
		new WebDriverWait(driver, 10, 200).until(new ExpectedCondition<Boolean>() {
			@Override
			public Boolean apply(WebDriver input) {
				try {
					Assert.assertTrue(input.findElements(by).isEmpty());
					return true;
				} catch (AssertionError e) {
					return false;
				}
			}
		});
		implicitWait();
	}

	/**
	 * Sustabdo webdriverį nurodytam laikui. Naudoti šį metodą tik jei kitaip
	 * nesigauna.
	 * 
	 */
	public void sleep(long sec) throws Exception {
		sleep(sec, TimeUnit.SECONDS);
	}

	/**
	 * {@link #sleep(long)}
	 * 
	 */
	public void sleepMillis(long millis) throws Exception {
		sleep(millis, TimeUnit.MILLISECONDS);
	}

	private void sleep(long amount, TimeUnit tu) throws Exception {
		long millis = tu.toMillis(amount);
		if (millis > 999) {
			System.out.println("sleep " + amount + " " + tu.toString());
		}
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Step
	public void waitForElementToLoad(WebTester t, final By by) throws Exception {
		ExpectedCondition<Boolean> e = new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver d) {
				try {
					WebElement we = d.findElement(by);
					return we != null && we.isDisplayed();
				} catch (Exception e) {
					return false;
				}
			}
		};
		(new WebDriverWait(driver, getLatestImplicitWait(), 100)).withMessage("Elementas nebuvo rastas").until(e);
	}

	public void waitForProcessingFinished() {

	}

	// --------------------------------------------------------------------------------------------
	// Other
	// --------------------------------------------------------------------------------------------

	/**
	 * Metodas atidaro interneto puslapį pagal nurodytą stringą. butinas http://.
	 */
	@Step
	public void get(String url) {
		driver.get(url);
	}

	@Step
	public void scrollToWebElement(WebElement element) throws Exception {
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
	}

	@Step
	public void assertNoError() throws Exception {
		waitForProcessingFinished();
		failIfFoundByText("KLAIDA", "Klaida", "Klaidos", "KLAIDOS", "Exception", "exception", "ERROR", "error");
		failIfBrowserLogNotEmpty();
	}

	@Step
	public void acceptConfirmation() throws Exception {
		Alert pranesimas = driver.switchTo().alert();
		pranesimas.accept();
	}

	public String buildLabelXpath(String element, String label) {
		return "//" + element + "[contains(text(),'" + label + "') or contains(@title,'" + label
				+ "') or contains(@value,'" + label + "')]";
	}

}