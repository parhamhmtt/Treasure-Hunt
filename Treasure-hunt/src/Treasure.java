import java.util.Date;
import java.util.Random;

public class Treasure extends Blocks {
    private int score=10;

    public Treasure(String Tag, boolean Destroyable) {
        super(Tag, Destroyable);
        this.score=10;
    }

    public int getScore() {
        return score;
    }

}
