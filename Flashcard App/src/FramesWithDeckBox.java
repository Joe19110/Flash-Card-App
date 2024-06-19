import java.util.ArrayList;

public interface FramesWithDeckBox {
    default String[] getDeckNames(ArrayList<Deck> decks) {
        String[] deckNames = new String[decks.size()];
        for (int i = 0; i < decks.size(); i++) {
            deckNames[i] = decks.get(i).getName();
        }
        return deckNames;
    }
}
