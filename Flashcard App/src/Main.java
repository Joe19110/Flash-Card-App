import org.json.simple.*;
import org.json.simple.parser.*;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws IOException, FontFormatException {
        //Declaring font
        File fontFile = new File("src/PixelifySans-Regular.ttf");
        Font font = Font.createFont(Font.TRUETYPE_FONT, fontFile);
        setAllFont(font.deriveFont(Font.PLAIN, 14));

        //Declaring ArrayLists
        ArrayList<Deck> decks = new ArrayList<>();
        ArrayList<Deadline> deadlines = new ArrayList<>();

        loadDataFromJSON(decks, deadlines); //Loads data from JSON file

        new HomeFrame(decks, deadlines); //Launches Home window

        saveDataIntoJSON(decks, deadlines); //Saves data into JSON file
    }

    //Loads data from JSON file into program
    public static void loadDataFromJSON(ArrayList<Deck> decks, ArrayList<Deadline> deadlines) {
        JSONParser parser = new JSONParser();
        try (FileReader reader = new FileReader("flashcards.json")) { //Reading JSON file
            JSONObject jsonObject = (JSONObject) parser.parse(reader);
            JSONArray decksArray = (JSONArray) jsonObject.get("decks"); //Puts all deck JSONObjects into JSONArray
            JSONArray deadlinesArray = (JSONArray) jsonObject.get("deadlines"); //Puts all deadline JSONObjects into JSONArray
            if (decksArray != null) { //Checks if a deck exists in JSON file
                for (Object deckObj : decksArray) {
                    JSONObject deckJson = (JSONObject) deckObj;
                    String deckName = (String) deckJson.get("name");
                    ArrayList<Card> cards = new ArrayList<>();
                    JSONArray cardsArray = (JSONArray) deckJson.get("cards");
                    for (Object cardObj : cardsArray) {
                        JSONObject cardJson = (JSONObject) cardObj;
                        String word = (String) cardJson.get("word");
                        String definition = (String) cardJson.get("definition");
                        ZonedDateTime studyTime = ZonedDateTime.parse((String) cardJson.get("studyTime"), DateTimeFormatter.ISO_ZONED_DATE_TIME);
                        int perfectStreak = Math.toIntExact((Long) cardJson.get("perfectStreak"));
                        cards.add(new Card(word, definition, studyTime, perfectStreak)); //Create new Card for every instance of JSONObject card in deck
                    }
                    decks.add(new Deck(deckName, cards)); //Create new Deck for every instance of JSONObject deck
                }
            }
            if (deadlinesArray != null) { //Checks if a deadline exists in JSON file
                for (Object deadlinesObj : deadlinesArray) {
                    JSONObject deadlinesJson = (JSONObject) deadlinesObj;
                    String deadlineName = (String) deadlinesJson.get("name");
                    ZonedDateTime deadlineTime = ZonedDateTime.parse((String) deadlinesJson.get("time"), DateTimeFormatter.ISO_ZONED_DATE_TIME);
                    deadlines.add(new Deadline(deadlineName, deadlineTime));
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace(); //If file cannot be read
        }
    }

    //Rewrites data into JSON file
    public static void saveDataIntoJSON(ArrayList<Deck> decks, ArrayList<Deadline> deadlines) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            JSONObject jsonObject = new JSONObject();
            JSONArray decksArray = new JSONArray();
            JSONArray deadlinesArray = new JSONArray();
            for (Deck deck : decks) {
                JSONObject deckObject = new JSONObject();
                deckObject.put("name", deck.getName());
                JSONArray cardsArray = new JSONArray();
                for (Card card : deck.getCards()) {
                    //Conversion of arraylist to JSONArray
                    JSONObject cardObject = new JSONObject();
                    cardObject.put("word", card.getWord());
                    cardObject.put("definition", card.getDefinition());
                    cardObject.put("studyTime", card.getStudyTime().format(DateTimeFormatter.ISO_ZONED_DATE_TIME));
                    cardObject.put("perfectStreak", card.getPerfectStreak());
                    cardsArray.add(cardObject);
                }
                deckObject.put("cards", cardsArray);
                decksArray.add(deckObject);
            }
            for (Deadline deadline : deadlines) {
                //Conversion of arraylist to JSONArray
                JSONObject deadlineObject = new JSONObject();
                deadlineObject.put("name", deadline.getName());
                deadlineObject.put("time", deadline.getTime().format(DateTimeFormatter.ISO_ZONED_DATE_TIME));
                deadlinesArray.add(deadlineObject);
            }
            jsonObject.put("decks", decksArray);
            jsonObject.put("deadlines", deadlinesArray);
            try (FileWriter writer = new FileWriter("flashcards.json")) {
                writer.write(jsonObject.toJSONString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));
    }
//Sets all component text to have the same font
    public static void setAllFont(Font font) {
        UIManager.put("Button.font", font);
        UIManager.put("ComboBox.font", font);
        UIManager.put("Label.font", font);
        UIManager.put("List.font", font);
        UIManager.put("MenuBar.font", font);
        UIManager.put("MenuItem.font", font);
        UIManager.put("Menu.font", font);
        UIManager.put("PopupMenu.font", font);
        UIManager.put("OptionPane.font", font);
        UIManager.put("Panel.font", font);
        UIManager.put("ScrollPane.font", font);
        UIManager.put("Viewport.font", font);
        UIManager.put("TextArea.font", font);
        UIManager.put("TextField.font", font);
        UIManager.put("TitledBorder.font", font);
    }
}
