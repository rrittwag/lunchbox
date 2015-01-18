package info.rori.lunchbox.api.v1.model;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import info.rori.lunchbox.api.v1.util.LocalDateAdapter;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDate;

/**
 * Beschreibt ein Mittagsangebot.
 */
@XmlRootElement
@ApiModel(value = "LunchOffer", description = "Mittagsangebot")
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

    @ApiModelProperty(value = "ID", dataType = "[0-9]+", required = true)
    public int id;
    @ApiModelProperty(value = "Bezeichnung des Mittagsangebots (mitsamt allen Beilagen)", dataType = "string(1024)", required = true)
    public String name;
    @ApiModelProperty(value = "Tag, an dem das Mittagsangebot gilt", dataType = "ISO 8601-Format 'YYYY-MM-DD'", required = true)
    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    public LocalDate day;
    @ApiModelProperty(value = "Preis in EURO-Cent", dataType = "[0-9]+", required = true)
    public int price;
    @ApiModelProperty(value = "Anbieter des Angebots", dataType = "[0-9]+", required = true)
    public int provider;
}
