package me.ryanmiles.securebrowser.model;

/**
 * Created by Ryan Miles on 8/22/2016.
 */
public class TabOut {
    private long startTime;
    private long endTime;
    private double duration;
    private double severity_point;

    public TabOut() {
    }

    public TabOut(long startTime, long end_time) {
        this.startTime = startTime;
        this.endTime = end_time;
        calcDuration();
        calcSeverity();
    }

    private void calcSeverity() {

        if (duration >= 15) {
            severity_point = 10;
        } else if (duration >= 5) {
            severity_point = 2;
        } else if (duration >= 1.2) {
            severity_point = .25;
        } else {
            severity_point = 0;
        }
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public void calcDuration() {
        duration = (double) (endTime - startTime) / 1000;
    }

    @Override
    public String toString() {
        return "TabOut{" +
                "duration=" + duration +
                '}';
    }

    public double getSeverity_point() {
        return severity_point;
    }
}
