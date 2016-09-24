package hakito.wifimap.POJO;

import com.google.gson.annotations.Expose;

/**
 * Created by Oleg on 20.05.2016.
 */
public class Comment {
    @Expose
    private String text;

    @Expose
    private int votes;

    public Comment(String text) {
        this.text = text;

    }

    public Comment() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }
}
