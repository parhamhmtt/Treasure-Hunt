import com.google.gson.Gson;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;

import static java.lang.System.exit;
import static java.util.Collections.swap;

public class Player extends Blocks {
    static public final String RED = "\033[0;91m";
    public static final String RESET = "\u001B[0m";
    static Scanner input = new Scanner(System.in);
    private static int numberOfPlayers;
    public Player(int x, int y, String Tag, boolean Destroyable) {
        super(Tag, Destroyable);
        this.x = x;
        this.y = y;
        this.score = 0;
        this.HP = 5;
        this.LongJumpCount = 6;
        this.DestructionCount = 3;
        this.SpawnTrapCount = 3;
        this.l[0] = 0;
        this.l[1] = 0;
        this.darkMode=false;
        this.turn=0;
        this.moved=true;
    }
    private int x;
    private boolean moved;
    private int[] l = {0, 0};
    private int y;
    private int score;
    private int HP;
    private static boolean darkMode;
   public static int turn;
    private int LongJumpCount;
    private int DestructionCount;
    private int SpawnTrapCount;
    private boolean nothingHappend;
    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score += score;
    }

    public int getHP() {
        return HP;
    }

    public void setHP(int HP) {
        this.HP += HP;
    }

    public int getLongJumpCount() {
        return LongJumpCount;
    }

    public void setLongJumpCount(int longJumpCount) {
        LongJumpCount += longJumpCount;
    }

    public int getDestructionCount() {
        return DestructionCount;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setXY(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setDestructionCount(int destructionCount) {
        DestructionCount += destructionCount;
    }

    public int getSpawnTrapCount() {
        return SpawnTrapCount;
    }

    public void setSpawnTrapCount(int spawnTrapCount) {
        SpawnTrapCount += spawnTrapCount;
    }

    public String getAbilities() {
        return "Destruction: " + getDestructionCount() + " | Long Jump: " + getLongJumpCount() + " | Spawn Trap: " + getSpawnTrapCount();
    }

    public void play() {
        String choice;
        loadGame();
        choice=nextStep();
        if(moved)
        Move(choice);
      //  turn++;
        saveGame();

    }

    public void Move(String choice) {
        String str="";
       nothingHappend=true;
        if(choice.length()==1||(choice.length()==2&&choice.endsWith("j"))) {
            if (map[l[0]][l[1]] instanceof Empty) {
                saveLogFile(this + " MOVED TO "+"["+l[0]+"]["+l[1]+"]");
                map[this.x][this.y]=new Empty("EMP",false);
                swap(l[0], l[1]);
                nothingHappend=false;
            }
            ///////////TRAPS
            else if (map[l[0]][l[1]] instanceof Traps) {
                Player player;
                player=PL[Turn(turn)];
                saveLogFile(this + " GOT ON A "+ map[l[0]][l[1]]+ " AND MOVED TO ["+l[0]+"]["+l[1]+"]");
                PL[Turn(turn)].setHP(((Traps)map[l[0]][l[1]]).getHp());
                PL[Turn(turn)].setScore(((Traps) map[l[0]][l[1]]).getScore());
                map[this.x][this.y]=new Empty("EMP",false);
                if(!(player.getHP()<=0))
                   swap(l[0], l[1]);
                else {System.out.println(RED +player+ " DIED." + RESET);
                    saveLogFile(player + " DIED.");
                    Main.sleep(2000);
                    map[l[0]][l[1]] = new Empty("EMP", false);
                    turn--;
                }
                nothingHappend=false;
            }
            else if (map[l[0]][l[1]] instanceof Treasure) {
                PL[Turn(turn)].setScore(((Treasure) map[l[0]][l[1]]).getScore());
                reSpawn(map[l[0]][l[1]]);
                map[this.x][this.y]=new Empty("EMP",false);
                swap(l[0], l[1]);
                saveLogFile(this + " GOT ON A TREASURE AND EARNED 10 SCORE ");
                nothingHappend=false;
            }
            else if (map[l[0]][l[1]] instanceof Spin) {
                reSpawn(map[l[0]][l[1]]);
                map[this.x][this.y]=new Empty("EMP",false);
                swap(l[0], l[1]);
                spnMethod();
                nothingHappend=false;
            }
            else if (map[l[0]][l[1]] instanceof Portals) {
                moveToPortal();
                nothingHappend=false;
              }
            }
        if(choice.length()==2&&choice.endsWith("t")){
            char way=choice.charAt(0);
            spawnTrap(choice);
            if(nothingHappend)
            saveLogFile(this+ " SPAWNED TRAP IN THE "+choice.substring(0,1).toUpperCase()+" WAY");
            nothingHappend=false;
        }
        if(choice.length()==2&&choice.endsWith("d")){
            if(((Blocks)map[l[0]][l[1]]).isDestroyable()) {
                map[this.x][this.y]=new Empty("EMP",false);
                saveLogFile(this+" DESTROYED A "+map[l[0]][l[1]]+" AND MOVED TO ["+l[0]+"]["+l[0]+"]");
                swap(l[0], l[1]);
                nothingHappend=false;
             }
            }
        saveGame();
        if(nothingHappend)
        saveLogFile(this+" DID NOTHING.");
        }
    public String nextStep() {
        //nextStep
        String choice;
        while (true) {
            choice = input.nextLine().toLowerCase();
            switch (choice) {
                case "w", "wd","wt":
                    if (this.getX()== 0)
                        l[0] = -1;
                    else
                        l[0] = this.getX() - 1;
                    l[1] = this.getY();
                    break;
                case "s", "sd","st":
                    if (this.getX() == 9)
                        l[0] = -1;
                    else
                        l[0] = this.getX() + 1;
                    l[1] = this.getY();
                    break;
                case "d", "dd","dt":
                    if (this.getY() == 19)
                        l[0] = -1;
                    else
                        l[0] = this.getX();
                    l[1] = this.getY() + 1;
                    break;
                case "a", "ad","at":
                    if (this.getY() == 0) {
                        l[0] = -1;
                    } else
                        l[0] = this.getX();
                    l[1] = this.getY() - 1;
                    break;

                case "wj":
                    if (this.getX() <= 1)
                        l[0] = -1;
                    else
                        l[0] = this.getX() - 2;
                    l[1] = this.getY();
                    break;
                case "aj":
                    if (this.getY() <= 1)
                        l[0] = -1;
                    else
                        l[0] = this.getX();
                    l[1] = this.getY() - 2;
                    break;
                case "sj":
                    if (this.getX() >= 8)
                        l[0] = -1;
                    else
                        l[0] = this.getX() + 2;
                    l[1] = this.getY();
                    break;
                case "dj":
                    if (this.getY() >= 18)
                        l[0] = -1;
                    else
                        l[0] = this.getX();
                    l[1] = this.getY() + 2;
                    break;
                case "e":
                    saveGame();
                    new Main();
                default:
                        System.out.println(RED+"--------WRONG DIRECTION.KEY--------"+RESET);
                    continue;
            }
            if (l[0] == -1) {
                System.out.println(RED+"--------OUT OF BOUNDS--------"+RESET);
                continue;
            }
            if(map[l[0]][l[1]] instanceof NOT){
                System.out.println(RED+"--------FIRST PLACE OF PLAYERS ERROR--------"+RESET);
                continue;
            }
            if(map[l[0]][l[1]] instanceof Walls && choice.length()==1){
                System.out.println(RED+"--------NEXT STEP ON WALL ERROR--------"+RESET);
                continue;
            }
            if(map[l[0]][l[1]] instanceof Player && !(choice.endsWith("t"))){
                System.out.println(RED+"--------YOU CANT BEAT ANOTHER PLAYER--------"+RESET);
                continue;
            }
            break;
        }
        if (choice.length()==2){
            switch (choice.toLowerCase().charAt(1)){
                case 'd':
                    if (PL[Turn(turn)].getDestructionCount()==0){
                        moved=false;
                        return "d";
                    }
                    else
                        PL[Turn(turn)].setDestructionCount(-1);
                    break;
                case 't':
                    if (PL[Turn(turn)].getSpawnTrapCount()==0) {
                        moved=false;
                        return "t";
                    }
                    else
                        PL[Turn(turn)].setSpawnTrapCount(-1);
                    break;
                case 'j':
                    if (PL[Turn(turn)].getLongJumpCount()==0) {
                        moved=false;
                        return "j";
                    }
                    else
                        PL[Turn(turn)].setLongJumpCount(-1);
                    break;
            }
        }
        return choice;
    }
    public static void swap(int row1, int col1) {
        PL[Turn(turn)].setXY(row1,col1);
        map[row1][col1]=PL[Turn(turn)];
        saveGame();
    }
    private static int Turn(int turn) {
        if(Main.numberOfPlayers==2) {
            if (turn % 2 == 0)
                return 0;
            else
                return 1;
        }
        else if(Main.numberOfPlayers==4) {
            while (true) {
                if (PL[Player.turn%4].getHP() <= 0){
                    Player.turn++;
                    continue;
                }
                break;
            }
            turn=Player.turn;
            if (turn % 4 == 0)
                return 0;
            else if(turn%4==1)
                return 1;
            else if(turn%4==2)
                return 2;
            else if(turn%4==3)
                return 3;
        }
        return 0;
    }
    public static void saveGame() {
        try {
            File myFile = new File("blocks.txt");
            FileWriter myFileWriter = new FileWriter(myFile);
            if (!myFile.exists())
                myFile.createNewFile();
            for (int i = 0; i <map.length; i++) {
                for (int j = 0; j <map[i].length; j++) {
                    myFileWriter.write( map[i][j].toString() );
                    if((i+j)!=28)
                        myFileWriter.write("\n");
                }
            }
            myFileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("IOException, " + e.getMessage());
        }
        try {
            File myFile = new File("Users.txt");
            Gson gson = new Gson();
            FileWriter myFileWriter = new FileWriter(myFile);
            if (!myFile.exists()) {
                myFile.createNewFile();
            }
            myFileWriter.write(Integer.toString(numberOfPlayers));
            myFileWriter.write("\n");
            myFileWriter.write(Boolean.toString(darkMode));
            myFileWriter.write("\n");
            myFileWriter.write(Integer.toString(turn));
            myFileWriter.write("\n");
            myFileWriter.write(gson.toJson(PL[0]));
            myFileWriter.write("\n");
            myFileWriter.write(gson.toJson(PL[1]));
            if(numberOfPlayers==4) {
                myFileWriter.write("\n");
                myFileWriter.write(gson.toJson(PL[2]));
                myFileWriter.write("\n");
                myFileWriter.write(gson.toJson(PL[3]));
            }
            myFileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("IOException, " + e.getMessage());
        }
    }
    private static void loadGame() {
        try {
            File myFile = new File("Users.txt");
            Gson gson = new Gson();
            if(!myFile.exists())
                myFile.createNewFile();
            if (myFile.exists()) {
                Scanner myFileReader = new Scanner(myFile);
                int i=-2;
                while (myFileReader.hasNextLine()) {i++;
                    if (i ==-1)
                        numberOfPlayers = Integer.parseInt(gson.fromJson(myFileReader.nextLine(),String.class));
                    else if (i == 0)
                        darkMode = Boolean.parseBoolean(gson.fromJson(myFileReader.nextLine(),String.class));
                    else if (i == 1)
                        turn = Integer.parseInt(gson.fromJson(myFileReader.nextLine(),String.class));
                    else if (i == 2) {
                        PL[0] = gson.fromJson(myFileReader.nextLine(), Player.class);
                    }
                    else if (i == 3) {
                        PL[1] = gson.fromJson(myFileReader.nextLine(), Player.class);
                    }
                    if(numberOfPlayers==4 && (i==4 || i==5)) {
                        if (i == 4) {
                            PL[2] = gson.fromJson(myFileReader.nextLine(), Player.class);
                        }
                        if (i == 5) {
                            PL[3] = gson.fromJson(myFileReader.nextLine(), Player.class);
                        }
                    }
                    if(i==6) {
                        myFileReader.close();
                        break;
                    }
                }
            }
        }catch(IOException e){
            e.printStackTrace();
            System.out.println("IOException, " + e.getMessage());
        }
        try {
            File myFile = new File("blocks.txt");
            if (myFile.exists()) {
                Scanner myFileReader = new Scanner(myFile);
                while (myFileReader.hasNextLine()) {
                    for (int i = 0; i <map.length; i++) {
                        for (int j = 0; j <map[i].length; j++) {
                            switch (myFileReader.nextLine().trim()){
                                case "PL1":
                                    map[i][j]=PL[0];
                                    break;
                                case "PL2":
                                    map[i][j]=PL[1];
                                    break;
                                case "PL3":
                                    map[i][j]=PL[2];
                                    break;
                                case "PL4":
                                    map[i][j]=PL[3];
                                    break;
                                case "TNT":
                                    map[i][j]=new Traps("TNT",false,-15,-3);
                                    break;
                                case "BMB":
                                    map[i][j]=new Traps("BMB",true,-10,-2);
                                    break;
                                case "TRS":
                                    map[i][j]=new Treasure("TRS",false);
                                    break;
                                case "MST":
                                    map[i][j]=new Traps("MST",false,-5,-1);
                                    break;
                                case "UWL":
                                    map[i][j]=new Walls("UWL",false);
                                    break;
                                case "BWL":
                                    map[i][j]=new Walls("BWL",true);
                                    break;
                                case "SPN":
                                    map[i][j]=new Spin("SPN",false);
                                    break;
                                case "PRT":
                                    map[i][j]=new Portals("PRT",false);
                                    break;
                                case "NOT":
                                    map[i][j]=new NOT("NOT",false);
                                    break;
                                default:
                                    map[i][j]=new Empty("EMP",false);
                                    break;
                            }
                        }
                    }

                }
                myFileReader.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("IOException, " + e.getMessage());
        }
    }
    public void saveLogFile(String str){
        try {
            File myFile = new File("logfile.txt");
            FileWriter myFileWriter = new FileWriter(myFile,true);
            if (myFile.exists()) {
                myFileWriter.write(str);
                myFileWriter.write("\n");
            } else {
                myFile.createNewFile();
                myFileWriter.write(str);
                myFileWriter.write("\n");
            }
            myFileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("IOException, " + e.getMessage());
        }
    }
    public void reSpawn(Object ob) {

        while (true) {
            int randomx =rand.nextInt(0,map.length);
            int randomy =rand.nextInt(0, map[0].length);
            if (map[randomx][randomy] instanceof Empty) {
                if(ob instanceof Treasure) {
                    map[randomx][randomy] = new Treasure("TRS", false);
                    break;
                }
                if(ob instanceof Spin) {
                    map[randomx][randomy] = new Spin("SPN",false);
                    break;
                }
            }
        }

    }
    private void spnMethod() {
        String str="";
        int temp=rand.nextInt(0,10);
        int digit1,digit2;
        label:
        while(true) {
            int random = rand.nextInt(1, 8);
            switch (random) {
                case 1:
                    str = "ADDED AN ABILITY TO " + this;
                    switch (temp % 3) {
                        case 1:
                            PL[Turn(turn)].setLongJumpCount(1);
                            break label;
                        case 2:
                            PL[Turn(turn)].setDestructionCount(1);
                            break label;
                        case 0:
                            PL[Turn(turn)].setSpawnTrapCount(1);
                            break label;
                    }
                    break label;
                case 2:
                    if(PL[0].getHP()<=0)
                        continue label;
                    str = PL[0].Tag + " MOVED BACK TO SPAWN BLOCK";
                    map[0][19] = PL[0];
                    map[PL[0].getX()][PL[0].getY()] = new Empty("EMP", false);
                    map[l[0]][l[1]]= new Empty("EMP", false);
                    PL[0].setXY(0, 19);
                    map[0][19]=PL[0];
                    break label;
                case 3:
                    if(PL[1].getHP()<=0)
                        continue label;
                    str = PL[1].Tag + " MOVED BACK TO SPAWN BLOCK";
                    map[9][0] = PL[1];
                    map[PL[1].getX()][PL[1].getY()] = new Empty("EMP", false);
                    map[l[0]][l[1]]= new Empty("EMP", false);
                    PL[1].setXY(9, 0);
                    map[9][0]=PL[1];

                    break label;
                case 4:
                    str = this + " PLACED 3 TNTIES ON THE MAP";
                    int count = 0;
                    while (count < 3) {
                        digit1 = rand.nextInt(0, map.length);
                        digit2 = rand.nextInt(0, map[0].length);
                        if (map[digit1][digit2] instanceof Empty) {
                            map[digit1][digit2] = new Traps("TNT", false, -15, -3);
                            count++;
                        }
                    }
                    break label;
                case 5:
                    str = this + " REMOVED 3 TRAPS FROM THE MAP";
                    int count2 = 0;
                    while (count2 < 3) {
                        digit1 = rand.nextInt(0, map.length);
                        digit2 = rand.nextInt(0, map[0].length);
                        if (map[digit1][digit2] instanceof Traps) {
                            map[digit1][digit2] = new Empty("EMP", false);
                            count2++;
                        }
                    }
                    break label;
                case 6:
                    if (numberOfPlayers==4) {
                        if(PL[2].getHP()<=0)
                            continue label;
                        str = PL[2].Tag + " MOVED BACK TO SPAWN BLOCK";
                        map[0][0] = PL[2];
                        map[PL[2].getX()][PL[2].getY()] = new Empty("EMP", false);
                        map[l[0]][l[1]]= new Empty("EMP", false);
                        PL[2].setXY(0, 0);
                        map[0][0]=PL[2];
                        break label;
                    }
                    continue label;
                case 7:
                    if (numberOfPlayers==4) {
                        if(PL[3].getHP()<=0)
                            continue label;
                        str = PL[3].Tag + " MOVED BACK TO SPAWN BLOCK";
                        map[9][19] = PL[3];
                        map[PL[3].getX()][PL[3].getY()] = new Empty("EMP", false);
                        map[l[0]][l[1]]= new Empty("EMP", false);
                        PL[3].setXY(9, 19);
                        map[9][19]=PL[3];
                        break label;
                    }
                    continue label;
                default:
                    continue label;
            }
        }
        saveGame();
        saveLogFile(str);
        }
    private void moveToPortal() {
        int d1=l[0];
        int d2=l[1];
        if (l[1] == 5) {
            int random1 = rand.nextInt(0, 11);
            portal:
            for (int i = random1 % 3; i < (random1 % 3) + 3; i++) {
                for (int j = random1 % 3; j < (random1 % 3) + 3; j++) {
                    if (map[4+(i % 3)][14+(j % 3)] instanceof Empty) {
                        l[1]=14+(j % 3);
                        l[0] =4+(i % 3);
                        break portal;
                    }
                }
            }
        }portal2:
        if(l[1] == 15){
            int random1 = rand.nextInt(0, 9);
            for (int i = random1 % 3; i < (random1 % 3) + 3; i++) {
                for (int j = random1 % 3; j < (random1 % 3) + 3; j++) {
                    if (map[4+(i % 3)][4+(j % 3)] instanceof Empty) {
                        l[1]= j%3+4;
                        l[0] = i%2+4;
                        break portal2;
                    }
                }
            }
        }
        if(l[0]==d1 && l[1]==d2) {
            saveLogFile(this+"MOVED TO PORTAL BUT AROUND THE OTHER PORTAL ISN'T EMPTY");
            saveGame();
            return;
        }
            map[this.getX()][this.getY()]=new Empty("EMP",false);
            swap(l[0],l[1]);
            saveLogFile(this + " MOVED TO PORTAL AND MOVED FROM " + "[" + this.x + "][" + this.y + "] TO [" + l[0] + "][" + l[1]+ "].");
            saveGame();
    }
    private void spawnTrap( String choice) {
        int random = rand.nextInt(0, 10);
        int temp;
        switch (choice.substring(0,1)) {
            case "w":
                if (PL[Turn(turn)].getX() == 0)
                    l[0] = -1;
                else
                    l[0] = this.getX() - 1;
                l[1] = this.getY();
                break;
            case "s":
                if (this.getX() == 9)
                    l[0] = -1;
                else
                    l[0] = this.getX() + 1;
                l[1] = this.getY();
                break;
            case "d":
                if (this.getY() == 19)
                    l[0] = -1;
                else
                    l[0] = this.getX();
                l[1] = this.getY() + 1;
                break;
            case "a":
                if (this.getY() == 0) {
                    l[0] = -1;
                } else
                    l[0] = this.getX();
                l[1] = this.getY() - 1;
                break;
        }
            int i1 = l[0];
            int j1 = l[1];
            if(i1==-1)
                return;
            int t=0;
                if (map[i1][j1] instanceof Player){
                    nothingHappend=false;
                    t= Integer.parseInt(((Player) map[i1][j1]).toString().substring(2, 3))-1;
                    switch (random % 3) {
                        case 0:
                            PL[t].setScore(-15);
                            PL[t].setHP(-3);
                            break;
                        case 1:
                            PL[t].setScore(-10);
                            PL[t].setHP(-2);
                            break;
                        case 2:
                            PL[t].setScore(-5);
                            PL[t].setHP(-1);
                            break;
                    }
                        if (PL[t].getHP() <= 0) {
                            System.out.println(RED + PL[t] + " DIED." + RESET);
                            saveLogFile(PL[t] + " DIED.");
                            Main.sleep(2000);
                            map[l[0]][l[1]] = new Empty("EMP", false);
//                            turn--;
                        }
                        else
                        switch (t) {
                            case 0:
                                saveLogFile(this+ " SPAWNED TRAP IN THE "+choice.substring(0,1).toUpperCase()+" WAY AND "+PL[0].Tag + " MOVED BACK TO SPAWN BLOCK");
                                map[0][19] = PL[0];
                                map[PL[0].getX()][PL[0].getY()] = new Empty("EMP", false);
                                map[l[0]][l[1]]= new Empty("EMP", false);
                                PL[0].setXY(0, 19);
                                map[0][19]=PL[0];
                                return;
                            case 1:
                                saveLogFile(this+ " SPAWNED TRAP IN THE "+choice.substring(0,1).toUpperCase()+" WAY AND "+PL[1].Tag + " MOVED BACK TO SPAWN BLOCK");
                                map[9][0] = PL[1];
                                map[PL[1].getX()][PL[1].getY()] = new Empty("EMP", false);
                                map[l[0]][l[1]]= new Empty("EMP", false);
                                PL[1].setXY(9, 0);
                                map[9][0]=PL[1];
                                return;
                            case 2:
                                saveLogFile(this+ " SPAWNED TRAP IN THE "+choice.substring(0,1).toUpperCase()+" WAY AND "+PL[2].Tag + " MOVED BACK TO SPAWN BLOCK");
                                map[0][0] = PL[2];
                                map[PL[2].getX()][PL[2].getY()] = new Empty("EMP", false);
                                map[l[0]][l[1]]= new Empty("EMP", false);
                                PL[2].setXY(0, 0);
                                map[0][0]=PL[2];
                                return;
                            case 3:
                                saveLogFile(this+ " SPAWNED TRAP IN THE "+choice.substring(0,1).toUpperCase()+" WAY AND "+PL[3].Tag + " MOVED BACK TO SPAWN BLOCK");
                                map[9][19] = PL[3];
                                map[PL[3].getX()][PL[3].getY()] = new Empty("EMP", false);
                                map[l[0]][l[1]]= new Empty("EMP", false);
                                PL[3].setXY(9, 19);
                                map[9][19]=PL[3];
                                return;


                        }
                    }

        int random1= rand.nextInt(7 , 27);
                label:
                switch (choice.charAt(0)) {
                    case 's','w':
                        for (int k=0;k<10;k++) {
                            int i=(k+random1)%10;
                            if ((i <= this.x && i >= 0 && choice.charAt(0) == 'w') || (i >= this.x && i <=9 && choice.charAt(0) == 's')) {
                                if (map[i][this.getY()] instanceof Empty) {
                                    switch (random % 3) {
                                        case 0:
                                            map[i][this.getY()] = new Traps("TNT", false, -15, -3);
                                            break label;
                                        case 1:
                                            map[i][this.getY()] = new Traps("BMB", true, -10, -2);
                                            break label;
                                        case 2:
                                            map[i][this.getY()] = new Traps("MST", true, -5, -1);
                                            break label;

                                    }
                                }
                            }
                        }
                        break;
                    case 'a','d':
                        for (int k=0;k<10;k++) {
                            int i=(k+random1)%10;
                            if ((i >= this.y && i <= 19 && choice.charAt(0) == 'd') || (i <= this.y && i >= 0 && choice.charAt(0) == 'a')) {
                                if (map[this.getX()][i] instanceof Empty) {
                                    switch (random % 3) {
                                        case 0:
                                            map[this.getX()][i] = new Traps("TNT", false, -15, -3);
                                            break label;
                                        case 1:
                                            map[this.getX()][i] = new Traps("BMB", true, -10, -2);
                                            break label;
                                        case 2:
                                            map[this.getX()][i] = new Traps("MST", true, -5, -1);
                                            break label;
                                    }
                                }
                            }
                        }
                   break;
          }
    }
}