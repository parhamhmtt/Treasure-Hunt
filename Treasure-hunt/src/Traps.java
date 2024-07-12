public class Traps extends Blocks{
    private  int hp;
    private int score;

    public int getHp() {
        return this.hp;
    }

    public int getScore() {
        return this.score;
    }

    public Traps(String Tag, boolean Destroyable, int score, int hp) {
        super(Tag, Destroyable);
        this.hp=hp;
        this.score=score;

    }
}
