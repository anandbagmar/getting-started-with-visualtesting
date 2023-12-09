package utilities;

public class Wait {
    private Wait() {
    }

    public static void waitFor(int durationInSec) {
        try {
            System.out.println(String.format("Sleep for %d sec",
                    durationInSec));
            Thread.sleep(durationInSec * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
