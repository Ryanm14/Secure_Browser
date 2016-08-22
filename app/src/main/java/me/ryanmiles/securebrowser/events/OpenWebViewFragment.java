package me.ryanmiles.securebrowser.events;

/**
 * Created by Ryan Miles on 8/14/2016.
 */
public class OpenWebViewFragment {
    private String link;

    public OpenWebViewFragment(String link) {
        this.link = link;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
