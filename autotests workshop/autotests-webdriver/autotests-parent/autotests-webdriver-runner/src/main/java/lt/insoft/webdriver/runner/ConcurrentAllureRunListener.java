package lt.insoft.webdriver.runner;

import org.junit.runner.Description;
import org.springframework.stereotype.Component;

import ru.yandex.qatools.allure.junit.AllureRunListener;

@Component
public class ConcurrentAllureRunListener extends AllureRunListener {

    @Override
    public synchronized String getSuiteUid(Description description) {
        return super.getSuiteUid(description);
    }
}
