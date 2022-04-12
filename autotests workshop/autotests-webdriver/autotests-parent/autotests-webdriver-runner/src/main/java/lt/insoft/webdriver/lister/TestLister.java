package lt.insoft.webdriver.lister;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.StringJoiner;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;

import lt.insoft.webdriver.runner.util.AutotestsUtils;


public class TestLister {
	private static final Log LOG = LogFactory.getLog(TestLister.class);

	public static final String DEFAULT_PACKAGE = "lt.insoft";

	public static void listTests(String scanPackage, String filePath) {
		Assert.hasText(scanPackage, "Package name must not be empty");
		Assert.hasText(filePath, "File path must not be empty");

		LOG.info(String.format("Listing all tests from packege %s to file %s", scanPackage, filePath));

		try (OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(filePath), StandardCharsets.UTF_8)) {
			out.write("tests=");
			out.write(buildString(scanPackage));
		} catch (Exception e) {
			System.err.println("Unable to list tests to file " + filePath);
		}

		LOG.info("Done.");
	}

	private static String buildString(String packageName) {
		List<String> methods = AutotestsUtils.getTestMethods(packageName);

		StringJoiner joiner = new StringJoiner(",\\\n");
		methods.forEach(joiner::add);

		return joiner.toString();
	}
}