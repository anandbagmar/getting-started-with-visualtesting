package utilities;

import org.assertj.core.api.SoftAssertions;

public class SoftAssertionsLib {
    private static ThreadLocal<SoftAssertions> softAssertionsThreadLocal = new ThreadLocal<>();

    public static void createSoftAssertions() {
        SoftAssertions softly = new SoftAssertions();
        softAssertionsThreadLocal.set(softly);
    }

    public static SoftAssertions getSoftAssertions() {
        return softAssertionsThreadLocal.get();
    }
}
