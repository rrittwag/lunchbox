package info.rori.lunchbox.api.v1.repository;

import info.rori.lunchbox.api.v1.model.LunchOffer;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Stellt Beispieldaten für Mittagsangebote bereit.
 */
public class LunchOfferRepository {

    private static List<LunchOffer> list = new ArrayList<>();

    static {
        list.add(new LunchOffer(1, "Hähnchenbrustfilet auf Ratatouille mit Gnocchi", LocalDate.now().plusDays(1), 580, 1));
        list.add(new LunchOffer(2, "Schweinegeschnetzeltes Gyros Art mit Tsatsiki und Pommes frites", LocalDate.now(), 580, 1));

        list.add(new LunchOffer(3, "Spaghetti Napoli mit schwarzen Oliven, Kapern & geriebenem Käse", LocalDate.now(), 520, 2));
        list.add(new LunchOffer(4, "Schweinebraten an Rahmsauce mit glacierten Karotten und Petersilienkartoffeln", LocalDate.now(), 550, 2));

        list.add(new LunchOffer(5, "Grüne Bohneneintopf mit Rindfleisch", LocalDate.now(), 370, 5));
        list.add(new LunchOffer(6, "Hausgemachtes Hacksteak „Toskana“ mit Feta & Tomaten, Krautsalat und Pommes frites", LocalDate.now(), 500, 5));
    }

    public List<LunchOffer> findAll() {
        return list;
    }

    public LunchOffer findById(int id) {
        return list.stream().filter(x -> id == x.id).findFirst().orElse(null);
    }

    public List<LunchOffer> findByDay(LocalDate day) {
        return list.stream().filter(x -> day.equals(x.day)).collect(Collectors.toList());
    }
}
