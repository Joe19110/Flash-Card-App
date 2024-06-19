import java.time.ZonedDateTime;

public class Card {
    private String word;
    private String definition;
    private ZonedDateTime studyTime;
    private int perfectStreak;
    public Card(String word, String definition, ZonedDateTime studyTime, int perfectStreak) {
        this.word = word;
        this.definition = definition;
        this.studyTime = studyTime;
        this.perfectStreak = perfectStreak;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public ZonedDateTime getStudyTime() {
        return studyTime;
    }

    public void setStudyTime(ZonedDateTime studyTime) {
        this.studyTime = studyTime;
    }

    public int getPerfectStreak() {
        return perfectStreak;
    }
    public void incrementPerfectStreak() {
        perfectStreak++;
    }
    public void decrementPerfectStreak() {
        perfectStreak--;
    }


}
