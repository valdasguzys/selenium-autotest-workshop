package lt.insoft.webdriver.webtester;

import org.junit.After;
import org.junit.Before;

public class WebdriverTestCase implements AutoCloseable  {
	protected WebTester t = null;
	protected int threadId;
	
	@Before
	public void before() throws Exception {
		t.init();	 
	}

	@After
	public void after() throws Exception {
	}

	public WebdriverTestCase()  {
		setTester(new WebTester());
	}
	
	public WebTesterBase getTester() {
		return t;
	}
	
	public void setTester(WebTester t) {
		this.t = t;
	}
	
	@Override
	public void close() throws Exception {
		t.destroy();
	}
	
	public void setThreadId(int threadId) {
		this.threadId = threadId;
	}
	
	public int getThreadId() {
		return threadId;
	}
	
}