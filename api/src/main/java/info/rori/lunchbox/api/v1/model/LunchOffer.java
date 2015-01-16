package info.rori.lunchbox.api.v1.model;

import info.rori.lunchbox.api.v1.util.LocalDateAdapter;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDate;

/**
 * Beschreibt ein Mittagsangebot.
 */
@XmlRootElement
public class LunchOffer {

    public LunchOffer() {
    }

    public LunchOffer(int id, String name, LocalDate day, int price, int provider) {
        this.id = id;
        this.name = name;
        this.day = day;
        this.price = price;
        this.provider = provider;
    }

    public int id;
    public String name;
    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    public LocalDate day; // ISO 8601 format: "JJJJ-MM-TT"
    public int price; // in Euro-Cent
    public int provider; // ID of lunch provider
}
