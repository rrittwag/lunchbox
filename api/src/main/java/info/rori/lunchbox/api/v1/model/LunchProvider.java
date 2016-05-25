package info.rori.lunchbox.api.v1.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Beschreibt einen Mittagsanbieter.
 */
@XmlRootElement
@ApiModel(value = "LunchProvider", description = "Mittagsanbieter")
public class LunchProvider {

    public LunchProvider() {
    }

    public LunchProvider(int id, String name, String location) {
        this.id = id;
        this.name = name;
        this.location = location;
    }

    @ApiModelProperty(value = "ID", dataType = "integer", required = true)
    public int id;

    @ApiModelProperty(value = "Bezeichnung des Mittagsanbieters", dataType = "string", required = true)
    public String name;

    @ApiModelProperty(value = "Der Umkreis, der vom Mittagsanbieter bedient wird", dataType = "string", required = true)
    public String location; // city or district
}
