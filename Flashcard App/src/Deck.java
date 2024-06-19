import java.time.ZonedDateTime;
import java.util.ArrayList;

public class Deck {
    private String name;
    private ArrayList<Card> cards;
    public void addCard(String word, String definition) {
        cards.add(new Card(word, definition, ZonedDateTime.now(), 0));
    }
    public Deck(String name, ArrayList<Card> cards) {
        this.name = name;
        this.cards = cards;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

}
