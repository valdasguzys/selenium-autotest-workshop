package lt.insoft.webdriver;

import org.junit.Before;

import lt.insoft.webdriver.browsers.Browser;
import lt.insoft.webdriver.webtester.WebdriverTestCase;

public class TestCase extends WebdriverTestCase {
		
	protected Tester t = null;

	@Before
	public void before() throws Exception {
		t.init();	 
	}
		
	public TestCase()  {
//		setTester(new Tester(Browser.FIREFOX));
		setTester(new Tester(Browser.CHROME));
	}
	
	public void setTester(Tester t) {
		super.setTester(t);
		this.t = t;
	}
	
	@Override
	public void close() throws Exception {
		t.destroy();
	}
	
//	@After
//	@Override
//	public void after() throws Exception {
//		
//	}
	
	

	
}
