package de.Fabtopf.System.API.Manager;

/**
 * Created by Fabi on 24.09.2017.
 */
public class TimeManager {

    private long seconds = 0;

    public TimeManager(long sec) {
        seconds = sec;
    }

    public TimeManager(int sec, int min, int h, int d) {
        seconds += sec;
        seconds += min*60;
        seconds += h*60*60;
        seconds += d*60*60*24;
    }

    public long getUnformedSeconds() {
        return seconds;
    }

    public int getFormedSeconds() {
        return (int) (seconds % 60);
    }

    public int getFormedMinutes() {
        return (int) ((seconds / 60) % 60);
    }

    public int getFormedHours() {
        return (int) ((seconds / 60 / 60) % 24);
    }

    public int getFormedDays() {
        return (int) (seconds / 60 / 60 / 24);
    }

}
