package lt.insoft.webdriver;

import org.junit.Test;
import org.openqa.selenium.By;

import ru.yandex.qatools.allure.annotations.Description;
import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Stories;
import ru.yandex.qatools.allure.annotations.Title;

@Title("Test suite by annotation")
@Description("Test suite describtion")
public class Testai extends TestCase {

//TODO iškelti UI elementus į atskirą klasę

//TODO parašyti random string metodą
	public String firstName = "vardas1234";
	public String lastName = "pavarde1234";
	public String email = firstName + lastName + "@test.com";
	public String password = firstName + lastName;
	
	
	@Test
	@Features({"Demo Web Shop"})
    @Stories({"1. Navigacija per visus meniu punktus"})
	@Title("Test01")
	public void TC01() throws Exception {
		t.get("http://demowebshop.tricentis.com/");
//TODO parašyti hover metodą, kad galima būtų pasirinkti is drop down meniu
		t.click("//ul[@class='top-menu']//a[contains(., 'Books')]");
		t.click("//ul[@class='top-menu']//a[contains(., 'Computers')]");
		t.click("//ul[@class='top-menu']//a[contains(., 'Electronis')]");
	}
	
	@Test
	@Features({"Demo Web Shop"})
    @Stories({"2. Užsiregistruoti parduotuvės puslapyje"})
	@Title("Test02")
	public void TC02() throws Exception {
		t.get("http://demowebshop.tricentis.com/");
		registerNewAccount(firstName, lastName, email, password);	
	}			
	
	private void registerNewAccount(String firstName, String lastName, String email, String password) throws Exception {
		t.click("//a[contains(., 'Register')]");
		t.click("//input[@id='gender-male']");
		t.setText("//input[@id='FirstName']", firstName);
		t.setText("//input[@id='LastName']", lastName);
		t.setText("//input[@id='Email']", email);
		t.setText("//input[@id='Password']", password);
		t.setText("//input[@id='ConfirmPassword']", password);
		t.click("//input[@id='register-button']");
		t.findVisibleElement(By.xpath("//div[@class='result'][contains(., 'Your registration completed')]"));
		t.click("//input[@value='Continue']");
	}
}
