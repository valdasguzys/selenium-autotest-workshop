package lt.insoft.webdriver.runner;

import java.util.concurrent.Callable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;

import lt.insoft.webdriver.runner.model.TestItem;
import lt.insoft.webdriver.runner.model.ThreadId;
import lt.insoft.webdriver.runner.util.AutotestsUtils;
import lt.insoft.webdriver.webtester.WebdriverTestCase;
import ru.yandex.qatools.allure.annotations.Title;
import ru.yandex.qatools.allure.junit.AllureRunListener;

public class TestCallable implements Callable<Void> {
    private static final Log LOG = LogFactory.getLog(TestCallable.class);

    private final int index;
    private final int testsCount;
    private final TestItem testItem;
    private final RunnerConfiguration config;
    private final AllureRunListener allureRunListener;
    private Boolean success = true;

    public TestCallable(int index, int testsCount, TestItem testItem, RunnerConfiguration config, AllureRunListener allureRunListener) {
        this.index = index;
        this.testsCount = testsCount;
        this.testItem = testItem;
        this.config = config;
        this.allureRunListener = allureRunListener;
    }

    @Override
    public Void call() throws Exception {

        synchronized (TestCallable.class) {
            LOG.info("========================================================================================");
            LOG.info(String.format("Starting test %s/%s %s(%s)", index, testsCount, testItem.getTitle(), getTitle()));
            LOG.info("========================================================================================");
        }

        if (WebdriverTestCase.class.isAssignableFrom(testItem.getTestClass())) {
            executeWebtesterTest();
        } else {
            executeSimpleTest();
        }

        synchronized (TestCallable.class) {
            LOG.info("========================================================================================");
            LOG.info(String.format("Finished %s/%s %s(%s) %s", index, testsCount, testItem.getTitle(), getTitle(), success ? "Success" : "Failed"));
            LOG.info("========================================================================================");
        }

        return null;
    }

    private void executeSimpleTest() throws InterruptedException {

        Description description = Description.createTestDescription(testItem.getTestClass(), testItem.getTestMethod().getName(), testItem.getTestMethod().getAnnotations());
        allureRunListener.testStarted(description);

        try {
            Object object = testItem.getTestClass().newInstance();
            testItem.getTestMethod().invoke(object);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            TestItem.setNoSuccess();
            allureRunListener.testFailure(new Failure(description, e.getCause()));
            success = false;
        }
        allureRunListener.testFinished(description);
    }

    private void executeWebtesterTest() {
        try (WebdriverTestCase testCase = (WebdriverTestCase) testItem.getTestClass().newInstance()) {

            Description description = Description.createTestDescription(testItem.getTestClass(), testItem.getTestMethod().getName(), testItem.getTestMethod().getAnnotations());
            allureRunListener.testStarted(description);

            testCase.setThreadId(ThreadId.get());
            testCase.getTester().setBrowser(config.getBrowser());

            AutotestsUtils.invokeMultiple(testCase, AutotestsUtils.getMethodsAnnotatedWith(testItem.getTestClass(), Before.class));

            try {
                testItem.getTestMethod().invoke(testCase);
            } catch (Exception e) {
                LOG.error(e.getMessage(), e);
                TestItem.setNoSuccess();
                testCase.getTester().screenshot();
                allureRunListener.testFailure(new Failure(description, e.getCause()));
                success = false;
            }

            allureRunListener.testFinished(description);

            AutotestsUtils.invokeMultiple(testCase, AutotestsUtils.getMethodsAnnotatedWith(testItem.getTestClass(), After.class));
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }

    private String getTitle() {
        Title title = testItem.getTestMethod().getAnnotation(Title.class);
        return title != null ? title.value() : "";
    }

//	TODO Gal serverLogginimas turi buti testu projekte, o ne runneryje?

//	private List<String> getServerLog(String appId, LocalDateTime from, LocalDateTime to) {
//		RestTemplate restTemplate = new RestTemplate();
//
//		ServerLogRequest request = new ServerLogRequest();
//		request.setFrom(from);
//		request.setTo(to);
//
//		ServerLog log = restTemplate.postForObject(config.getLogServerUrl() + "log/{logId}", request, ServerLog.class, ImmutableMap.of("logId", appId));
//
//		return log.getLines();
//	}

}