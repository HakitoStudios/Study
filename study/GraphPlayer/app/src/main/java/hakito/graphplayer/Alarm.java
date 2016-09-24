package hakito.graphplayer;

import org.w3c.dom.Text;

import java.io.Serializable;

/**
 * Created by Oleg on 24-Dec-15.
 */
public class Alarm implements Serializable {
    int id;
    String label;
    long time;
    boolean[] days;
    String songs;
    boolean isNight;
    float[] graph;
    long duration;
    boolean enabled;

public Alarm()
{
    id=-1;
    days = new boolean[7];
    graph = new float[10];
    for(int i = 0; i < graph.length; i++)
        graph[i] = 1;
    label="";
    time=0;
    songs="";
    duration=1;
    enabled = true;
}

    public Alarm(int id, String label, long time, boolean[] days, String songs, boolean isNight, float[] graph, long duration, boolean enabled)  {
        this.id = id;
        this.graph = graph;
        this.label = label;
        this.time = time;
        this.days = days;
        this.songs = songs;
        this.isNight = isNight;
        this.duration = duration;
        this.enabled = enabled;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setGraphString(String graph)
    {
        this.graph = TextConverter.getGraph(graph);
    }

    public String getGraphString()
    {
        return  TextConverter.getGraph(graph);
    }

    public float[] getGraph() {
        return graph;
    }

    public void setGraph(float[] graph) {
        this.graph = graph;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean[] getDays() {
        return days;
    }

    public void setDays(boolean[] days) {
        this.days = days;
    }

    public String getSongs() {
        return songs;
    }

    public void setSongs(String songs) {
        this.songs = songs;
    }

    public boolean isNight() {
        return isNight;
    }

    public void setIsNight(boolean isNight) {
        this.isNight = isNight;
    }

    public void setDaysString(String days)
    {
        this.days = TextConverter.getDays(days);
    }

    public String getDaysString()
    {
        return TextConverter.getDays(days);
    }
}
