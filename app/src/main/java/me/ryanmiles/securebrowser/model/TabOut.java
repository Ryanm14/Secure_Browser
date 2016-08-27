package me.ryanmiles.securebrowser.model;

/**
 * Created by Ryan Miles on 8/22/2016.
 */
public class TabOut {
    private long startTime;
    private long endTime;
    private double duration;
    private String severity;

    public TabOut() {
    }

    public TabOut(long startTime, long end_time) {
        this.startTime = startTime;
        this.endTime = end_time;
        calcDuration();
        calcSeverity();
    }

    private void calcSeverity() {
        if (duration >= 5) {
            severity = "High";
        } else if (duration >= 1.2) {
            severity = "Medium";
        } else {
            severity = "Low";
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

    public String getSeverity() {
        return severity;
    }
}
