package info.rori.lunchbox.api.v1.model;

import io.swagger.v3.oas.annotations.media.Schema;
import info.rori.lunchbox.api.v1.util.LocalDateAdapter;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDate;

/**
 * Beschreibt ein Mittagsangebot.
 */
@XmlRootElement
@Schema(name = "LunchOffer", description = "Mittagsangebot")
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

    @Schema(description = "ID", type = "integer", required = true)
    public int id;

    @Schema(description = "Bezeichnung des Mittagsangebots (mitsamt allen Beilagen)", required = true)
    public String name;

    @Schema(description = "Tag, an dem das Mittagsangebot gilt", type = "date", required = true)
    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    public LocalDate day;

    @Schema(description = "Preis in EURO-Cent", type = "integer", required = true)
    public int price;

    @Schema(description = "Anbieter des Angebots", type = "integer", required = true)
    public int provider;
}
