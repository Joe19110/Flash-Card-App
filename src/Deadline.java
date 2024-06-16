import java.time.ZonedDateTime;

public class Deadline {
    private String name;
    private ZonedDateTime time;

    public Deadline(String name, ZonedDateTime time) {
        this.name = name;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ZonedDateTime getTime() {
        return time;
    }

}
