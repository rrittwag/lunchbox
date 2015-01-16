package info.rori.lunchbox.api.v1.model;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Beschreibt einen Mittagsanbieter.
 */
@XmlRootElement
public class LunchProvider {

    public LunchProvider() {
    }

    public LunchProvider(int id, String name, String location) {
        this.id = id;
        this.name = name;
        this.location = location;
    }

    public int id;
    public String name;
    public String location; // city or district
}
