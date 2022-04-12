package lt.insoft.webdriver.utils;

import org.junit.Assert;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;


/**
 * Klasė skirta webelementų nuspalvinimui.
 */

public class Highlighters {

    /**
     * Požymis, nurodantis ar paryškinti nuorodinį web elementą.
     */
    private static final boolean highlightReference = Boolean.parseBoolean(System.getProperty("highlightReference", "true"));
    /**
     * Požymis, nurodantis ar paryškinti veikiamą web elementą.
     */
    private static final boolean highlightTarget = Boolean.parseBoolean(System.getProperty("highlightTarget", "true"));
    /**
     * Požymis, nurodantis ar paryškinti {@link #context} webelementą.
     */
    private static final boolean highlightContext = Boolean.parseBoolean(System.getProperty("highlightContext", "true"));

    public static WebElement runJavaScript(WebDriver driver, WebElement we, String js) throws Exception {
        if (driver instanceof JavascriptExecutor) {
            try {
                ((JavascriptExecutor) driver).executeScript("arguments[0]." + js, we);
            } catch (WebDriverException wde) {
                if (!wde.getMessage().contains("arguments[0] is undefined")) {
                    throw wde;
                }
            }
        }
        return we;
    }

    public static WebElement highlight(boolean highlight, WebDriver wd, WebElement we, String border) throws Exception {
        Assert.assertNotNull(we);
        if (highlight) {
            runJavaScript(wd, we, "style.border='" + border + "'");
        }
        return we;
    }

    public static WebElement highlightGreen(WebDriver wd, WebElement we) throws Exception {
        return highlight(highlightReference, wd, we, "2px solid green");
    }

    public static WebElement highlightBlue(WebDriver wd, WebElement we) throws Exception {
        return highlight(highlightContext, wd, we, "2px solid blue");
    }

    public static WebElement highlightRed(WebDriver wd, WebElement we) throws Exception {
        return highlight(highlightTarget, wd, we, "2px solid red");
    }

}
