import java.util.Date;
import java.util.Random;

public interface gameBoard {
    Random rand=new Random(new Date().getTime());
    Object[][] map = new Object[10][20];

}
