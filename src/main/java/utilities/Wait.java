package utilities;

import java.util.Calendar;

public class Wait {
    private Wait() {
    }

    public static void waitFor(int durationInSec) {
        try {
            System.out.println("\n\t"+ Calendar.getInstance().getTime());
            System.out.println(String.format("\tSleep for %d sec",
                    durationInSec));
            Thread.sleep(durationInSec * 1000);
            System.out.println("\t"+ Calendar.getInstance().getTime() + "\n");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
