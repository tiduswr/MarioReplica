package tiduswr.util;

public class Time {

    private static float timeStarted;

    public static float getTime(){
        return (float) ((System.nanoTime() - timeStarted) * 1E-9);
    }

}
