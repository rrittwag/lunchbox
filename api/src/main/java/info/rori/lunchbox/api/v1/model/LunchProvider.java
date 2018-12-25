package info.rori.lunchbox.api.v1.model;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Beschreibt einen Mittagsanbieter.
 */
@XmlRootElement
@Schema(name = "LunchProvider", description = "Mittagsanbieter")
public class LunchProvider {

    public LunchProvider() {
    }

    public LunchProvider(int id, String name, String location) {
        this.id = id;
        this.name = name;
        this.location = location;
    }

    @Schema(description = "ID", type = "integer", required = true)
    public int id;

    @Schema(description = "Bezeichnung des Mittagsanbieters", required = true)
    public String name;

    @Schema(description = "Der Umkreis, der vom Mittagsanbieter bedient wird", required = true)
    public String location;
}
