public class Blocks implements gameBoard{

    public final String Tag;
    public final boolean Destroyable;
     static Object[][] map=new Object[10][20];
    public static Player[] PL=new Player[4];

    public boolean  isDestroyable() {
        return Destroyable;
    }

    public Blocks(String Tag, boolean Destroyable) {
        this.Tag = Tag;
        this.Destroyable = Destroyable;
    }

    @Override
    public String toString() {
        return Tag;
    }
}
