package lt.insoft.webdriver.runner;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ru.yandex.qatools.commons.model.Environment;
import ru.yandex.qatools.commons.model.Parameter;

@Component
public class XmlBuilder {

	@Autowired
	private RunnerConfiguration runnerConfiguration;
	
	private static final String REPORT_FOLDER = "target/allure-results";
	
	public void buildEnvironmentDescription() throws JAXBException, IOException{
		ru.yandex.qatools.commons.model.ObjectFactory factory = new ru.yandex.qatools.commons.model.ObjectFactory();
		Environment environment = new Environment();
		List<Parameter> parameters = new ArrayList<Parameter>();
		
		Parameter browser = new Parameter();
		browser.setName("Browser");
		browser.setValue(runnerConfiguration.getBrowser().name());
		browser.setKey("Browser");
		parameters.add(browser);
		
		Parameter threadCount = new Parameter();
		threadCount.setName("Thread count");
		threadCount.setValue(Integer.toString(runnerConfiguration.getThreadCount()));
		threadCount.setKey("Thread count");
		parameters.add(threadCount);		
		
		Parameter appUrl = new Parameter();
		appUrl.setName("App url");
		appUrl.setValue(runnerConfiguration.getAppUrl());
		appUrl.setKey("App url");
		parameters.add(appUrl);	
		
		environment.withParameter(parameters);
//		TODO
//		environment.setName("");
		
		JAXBContext context = JAXBContext.newInstance(environment.getClass());
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
		File directory = new File(REPORT_FOLDER);
	    if (!directory.exists()){
	        directory.mkdir();
	    }
		try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(new File(REPORT_FOLDER + "/environment.xml")), StandardCharsets.UTF_8)) {
			marshaller.marshal(factory.createEnvironment(environment), writer);
		}
	}
	
}
