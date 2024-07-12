import com.google.gson.Gson;

import java.io.*;
import java.util.Scanner;

import static java.lang.System.exit;

public class Main implements gameBoard {
    static Scanner input= new Scanner(System.in);
     static Player[] PL=new Player[4];
    static public final String BLUE = "\033[0;94m";
    public static final String WHITE_BACKGROUND = "\033[0;107m";
    static public final String YELLOW = "\033[1;93m";
    public static final String PURPLE= "\033[1;95m";
    static public final String CYAN = "\033[0;96m";
    static public final String RED = "\033[1;91m";
    public static final String GREEN = "\033[1;92m";
    public static final String WHITE = "\033[1;97m";
    public static final String RESET = "\u001B[0m";
    public static final String BLACK = "\033[1;90m";
    static boolean darkMode=true;
    boolean b=false;
    static int numberOfPlayers;
    public static void main(String[] args) {
        new Main();
    }
    Main(){
        while (true) {
            System.out.println(CYAN+"╔════════════WELCOME════════════╗");
                 System.out.println("║1-NEW GAME     ║4-CLEAR FILES  ║");
                 System.out.println("╠═══════════════╬═══════════════╬");
                 System.out.println("║2-LOAD GAME    ║5-GAME GUIDE   ║");
                 System.out.println("╠═══════════════╬═══════════════╬");
                 System.out.println("║3-PRINT LOG    ║6-EXIT         ║");
                 System.out.println("╚═══════════════╩═══════════════╩"+RESET);
            String choice = input.next();
            switch (choice) {
                case "1":
                    clearLogfile();
                    while (true){
                        System.out.println(CYAN+"╔═══════════DARK-MODE═══════════╗");
                             System.out.println("║1-ON           ║2-OFF          ║");
                             System.out.println("╚═══════════════╩═══════════════╩"+RESET);
                        choice=input.next();
                        switch (choice){
                            case "1":
                                darkMode=true;
                                break;
                            case "2":
                                darkMode=false;
                                break;
                            default:
                                System.out.println(RED+"WRONG ENTRY"+RESET);
                                continue;
                        }
                        break;
                    }
                    while (true){
                   System.out.println(CYAN+"╔════════════PLAYERS════════════╗");
                        System.out.println("║2- 2 PLAYERS   ║4- 4 PLAYERS   ║");
                        System.out.println("╚═══════════════╩═══════════════╩"+RESET);
                        choice=input.next();
                        switch (choice){
                            case "2":
                                numberOfPlayers=2;
                                break;
                            case "4":
                                numberOfPlayers=4;
                                break;
                            default:
                                System.out.println(RED+"WRONG ENTRY"+RESET);
                                continue;
                        }
                        break;
                    }
                    firstMapFixer();
                    saveLogFile("GAME HAS JUST STARTED");
                    saveGame();
                    break;
                case "2":
                    loadGame();
                    saveGame();
                    break;
                case "3":
                    gameReport();
                    sleep(2500);
                    continue;
                case "4":
                    clearFiles();
                    System.out.println(RED+"FILES CLEARED SUCCESSFULLY!!!"+RESET);
                    sleep(2500);
                    continue;
                case "5":
                    gameGuide();
                    continue;
                case "6":
                    exit(0);
                    break;
                default:
                    continue;
            }
            break;
        }
        saveGame();
        boolean Gameover = false;
        while (!Gameover) {
            printLastLine();
                printMap();
              PL[Turn(Player.turn)].play();
              loadGame();
            // End game condition
            if(numberOfPlayers==4) {
                for (int i = 0; i < PL.length; i++) {
                    if (PL[i] != null) {
                        if (PL[i].getScore() >= 100 || (PL[(i + 1) % 4].getHP() <= 0 && (PL[(i + 2) % 4].getHP() <= 0) && (PL[(i + 3) % 4].getHP() <= 0))) {
                            System.out.println(GREEN + "CONGRATS!!!!!! " + PL[i] + " WON THE GAME." + RESET);
                            System.out.println(RED + "FILES ARE BEING CLEARED!!!!!!!" + RESET);
                            clearFiles();
                            sleep(2500);
                            new Main();
                        }
                        if (PL[i].getHP() <= 0) {
                            map[PL[i].getX()][PL[i].getY()] = new Empty("EMP", false);
                        }
                    }
                }
            }
            if(numberOfPlayers==2) {
                if (PL[0].getHP() <= 0 || PL[1].getHP() <= 0 || PL[0].getScore() >= 100 || PL[1].getScore() >= 100) {
                    Gameover = true;
                    if (PL[0].getHP() <= 0) {
                        System.out.println(GREEN+"PL1 died. PL2 Victory."+RESET);
                    } else if (PL[1].getScore() >= 100) {
                        System.out.println(GREEN+"PL1 reached +100 scores. PL1 Victory"+RESET);
                    }
                    else if (PL[0].getHP() <= 0) {
                        System.out.println(GREEN+"PL2 died. PL1 Victory."+RESET);
                    } else if (PL[1].getScore() >= 100) {
                        System.out.println(GREEN+"PL2 reached +100 scores. PL2 Victory"+RESET);
                    }
                    System.out.println(RED + "FILES ARE BEING CLEARED!!!!!!!" + RESET);
                    clearFiles();
                    sleep(2500);
                    new Main();
                }
            }
            if(!b){
                loadGame();
                if (map[0][19] instanceof Empty)
                    map[0][19] = new NOT("NOT", false);
                if (map[9][0] instanceof Empty)
                    map[9][0] = new NOT("NOT", false);
                if(numberOfPlayers==4){
                    if (map[9][19] instanceof Empty)
                        map[9][19] = new NOT("NOT", false);
                    if (map[0][0] instanceof Empty)
                        map[0][0] = new NOT("NOT", false);
                    if (map[9][0] instanceof NOT && map[0][9] instanceof NOT &&map[9][19] instanceof NOT && map[0][0] instanceof NOT)
                        b=true;
                }
                if (numberOfPlayers==2) {
                    if (map[9][0] instanceof NOT && map[0][9] instanceof NOT)
                        b = true;
                }
                saveGame();
            }
        }
    }

    private void gameGuide() {
        System.out.println(CYAN+"╔═══════════GAME╦GUIDE══╦═══════╗");
        System.out.println("║     BREAKABLE ║   HP  ║ SCORE ║ ");
        System.out.println("╠════════╦══════╬═══════╬═══════╣");
        System.out.println("║ 1-TNT  ║  NO  ║   -3  ║  -15  ║ ");
        System.out.println("╠════════╬══════╬═══════╬═══════╣");
        System.out.println("║ 2-BMB  ║ YES  ║   -2  ║  -10  ║ ");
        System.out.println("╠════════╬══════╬═══════╬═══════╣");
        System.out.println("║ 3-MST  ║ YES  ║   -1  ║  -5   ║ ");
        System.out.println("╠════════╬══════╬═══════╬═══════╣");
        System.out.println("║ 4-TRS  ║  NO  ║       ║  +10  ║");
        System.out.println("╠════════╬══════╬═══════╬═══════╣");
        System.out.println("║ 5-UWL  ║  NO  ║       ║       ║");
        System.out.println("╠════════╬══════╬═══════╬═══════╣");
        System.out.println("║ 6-BWL  ║  YES ║       ║       ║");
        System.out.println("╠════════╬══════╬═══════╬═══════╣");
        System.out.println("║ 7-SPN  ║  NO  ║       ║       ║");
        System.out.println("╠════════╬══════╬═══════╬═══════╣");
        System.out.println("║ 8-PRT  ║  NO  ║       ║       ║");
        System.out.println("╠════════╩══════╩═══════╩═══════╬═══════════════════════════════╗");
        System.out.println("║ 1-TNT: TNT                    ║ 5-UWL: UNBREAKABLE WALL       ║");
        System.out.println("╠═══════════════════════════════╬═══════════════════════════════╣");
        System.out.println("║ 2-BMB: BOMB                   ║ 6-BWL: BREAKABLE WALL         ║");
        System.out.println("╠═══════════════════════════════╬═══════════════════════════════╣");
        System.out.println("║ 3-MST: MOUSE TRAP             ║ 7-SPN: RANDOM OF 6 ACTIONS    ║");
        System.out.println("╠═══════════════════════════════╬═══════════════════════════════╣");
        System.out.println("║ 4-TRS: TREASURE               ║ 8-PRT: MOVE TO  OTHER PORTAL  ║");
        System.out.println("╚═══════════════════════════════╩═══════════════════════════════╝"+RESET);
        System.out.println(GREEN+"-------------------REACH 100 IN SCORE TO WIN--------------------"+RESET);
        System.out.print("ENTER TO CONTINUE,.....");
        input.nextLine();
        input.nextLine();
    }

    private void clearFiles() {
        try {
            File myFile = new File("logfile.txt");
            if (myFile.exists()) {
                myFile.delete();
                myFile.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("IOException, " + e.getMessage());
        }
        try {
            File myFile = new File("Users.txt");
            if (myFile.exists()) {
                myFile.delete();
                myFile.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("IOException, " + e.getMessage());
        }
        try {
            File myFile = new File("blocks.txt");
            if (myFile.exists()) {
                myFile.delete();
                myFile.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("IOException, " + e.getMessage());
        }
    }

    private void gameReport() {
        try {
            File myFile = new File("logfile.txt");
            Scanner myFileReader = new Scanner(myFile);
            if(!myFile.exists())
                myFile.createNewFile();
                if (myFile.length() == 0) {
                    System.out.println(RED + "FILES WERE DELETED,START A NEW GAME" + RESET);
                    sleep(2000);
                    new Main();
                }

            while (myFileReader.hasNextLine()) {
                System.out.println(GREEN+myFileReader.nextLine()+RESET);
            }
            myFileReader.close(); // Close the scanner after reading from the file
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("IOException: " + e.getMessage());
        }
    }

    private void printLastLine() {
        File myFile = new File("logfile.txt");
        if (myFile.length()==0)
            return;
        String filePath = "logfile.txt";
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            String lastLine = "";
            while ((currentLine = bufferedReader.readLine()) != null) {
                lastLine = currentLine;
            }
            System.out.println(lastLine);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void clearLogfile(){
        try {
            File myFile = new File("logfile.txt");
            if (!myFile.exists()) {
                myFile.createNewFile();
            }
            myFile.delete();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("IOException, " + e.getMessage());
        }
    }
    private void firstMapFixer() {
        for (int i= 0; i<map.length; i++) {
            for (int j = 0; j <map[i].length; j++) {
                map[i][j]=new Empty("EMP",false);
            }
        }
        int digit1,digit2;
        ////players
        if(numberOfPlayers==4){
            PL[0] = new Player(0, 19, "PL1", false);
            PL[1] = new Player(9, 0, "PL2", false);
            PL[2] = new Player(0, 0, "PL3", false);
            PL[3] = new Player(9, 19, "PL4", false);
            map[0][19] = PL[0];
            map[9][0] = PL[1];
            map[0][0] = PL[2];
            map[9][19] = PL[3];
        }
        if(numberOfPlayers==2) {
            PL[0] = new Player(0, 19, "PL1", false);
            PL[1] = new Player(9, 0, "PL2", false);
            map[0][19] = PL[0];
            map[9][0] = PL[1];
        }
        ////////
        map[5][5]=new Portals("PRT",false);
        map[5][15]=new Portals("PRT",false);
        ////TRS
        while (true) {
            digit1 = rand.nextInt(0,map.length);
            digit2 = rand.nextInt(0,map[0].length);
            if (map[digit1][digit2] instanceof Empty) {
                map[digit1][digit2] = new Treasure("TRS",false);
                break;
            }
        }

        ////SPN
        while(true) {
            digit1 = rand.nextInt(0,map.length);
            digit2 = rand.nextInt(0,map[0].length);
            if (map[digit1][digit2] instanceof Empty) {
                map[digit1][digit2] = new Spin("SPN",false);
                break;
            }
        }
        //////WALLS
        for (int i = 0; i <40; i++) {
            while(true) {
                digit1 = rand.nextInt(0,map.length);
                digit2 = rand.nextInt(0,map[0].length);
                if (map[digit1][digit2] instanceof Empty) {
                    if(digit2%2==0)
                        map[digit1][digit2] = new Walls("BWL",true);
                    else
                        map[digit1][digit2] = new Walls("UWL",true);
                    break;
                }
            }
        }
        ///////TRAPS
        for (int i = 0; i <60; i++) {
            while(true) {
                digit1 = rand.nextInt(0,map.length);
                digit2 = rand.nextInt(0,map[0].length);
                if (map[digit1][digit2] instanceof Empty) {
                    if(digit2%3==0)
                        map[digit1][digit2] = new Traps("TNT",false,-15,-3);
                    if(digit2%3==1)
                        map[digit1][digit2] = new Traps("BMB",false,-10,-2);
                    if(digit2%3==2)
                        map[digit1][digit2] = new Traps("MST",false,-5,-1);
                    break;
                }
            }
        }
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
//            FileOutputStream fileOutputStream = new FileOutputStream(myFile);
//            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            for (int i = 0; i < map.length; i++) {
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
            myFileWriter.write(Integer.toString(Player.turn));
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
    public void saveLogFile(String str){
        try {
            File myFile = new File("logfile.txt");
            FileWriter myFileWriter = new FileWriter(myFile,true);
            if (!myFile.exists())
                myFile.createNewFile();
                myFileWriter.write(str);
                myFileWriter.write("\n");
            myFileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("IOException, " + e.getMessage());
        }
    }
    void printMap(){
        Player player;
        if(!darkMode){
            System.out.println(CYAN+"╔═════╦═════╦═════╦═════╦═════╦═════╦═════╦═════╦═════╦═════╦═════╦═════╦═════╦═════╦═════╦═════╦═════╦═════╦═════╦═════╗"+RESET);
            for (int i = 0; i < map.length; i++) {
                System.out.print(CYAN+"║"+RESET);
                for (int j = 0; j < map[i].length; j++) {
                    if (map[i][j].toString().equals("EMP")) {
                        System.out.print(CYAN+"     ║"+RESET);
                    } else {
                        System.out.print(" ");
                        switch (map[i][j].toString()) {
                            case "PL1", "PL2","PL3","PL4":
                                System.out.print(WHITE_BACKGROUND + BLACK +map[i][j].toString()+ RESET);
                                break;
                            case "EMP":
                                System.out.print(BLUE + "   " + RESET);
                                break;
                            case "TRS":
                                System.out.print(GREEN +map[i][j].toString()+ RESET);
                                break;
                            case "UWL", "BWL":
                                System.out.print(YELLOW +map[i][j].toString()+ RESET);
                                break;
                            case "BMB", "TNT", "MST":
                                System.out.print(RED +map[i][j].toString()+ RESET);
                                break;
                            case "PRT":
                                System.out.print(PURPLE+map[i][j].toString()+ RESET);
                                break;
                            case "SPN":
                                System.out.print(WHITE+map[i][j].toString()+ RESET);
                                break;
                            default:
                                System.out.print(BLUE + "   " + RESET);
                        }
                        System.out.print(CYAN+" ║"+CYAN);
                    }
                }
                System.out.println();
                if (i < map.length - 1) {
                    System.out.println(CYAN+"╠═════╬═════╬═════╬═════╬═════╬═════╬═════╬═════╬═════╬═════╬═════╬═════╬═════╬═════╬═════╬═════╬═════╬═════╬═════╬═════╣"+RESET);
                }
            }
            System.out.println(CYAN+"╚═════╩═════╩═════╩═════╩═════╩═════╩═════╩═════╩═════╩═════╩═════╩═════╩═════╩═════╩═════╩═════╩═════╩═════╩═════╩═════╝"+RESET);
        }
        if(darkMode) {
            player=PL[Turn(Player.turn)];
            System.out.println(CYAN+"╔═════╦═════╦═════╦═════╦═════╦═════╦═════╦═════╦═════╦═════╦═════╦═════╦═════╦═════╦═════╦═════╦═════╦═════╦═════╦═════╗"+RESET);
            for (int i = 0; i < map.length; i++) {
                System.out.print(CYAN+"║"+RESET);
                for (int j = 0; j < map[i].length; j++) {
                    if (i >= player.getX() - 4 && i <= player.getX() + 4 && j >= player.getY() - 4 && j <= player.getY() + 4) {
                        if (map[i][j].toString().equals("EMP")) {
                            System.out.print(CYAN+"     ║"+RESET);
                        } else {
                            System.out.print(" ");
                            switch (map[i][j].toString()) {
                                case "PL1", "PL2","PL3","PL4":
                                    System.out.print(WHITE_BACKGROUND + BLACK +map[i][j].toString()+ RESET);
                                    break;
                                case "EMP":
                                    System.out.print(BLUE + "   " + RESET);
                                    break;
                                case "TRS":
                                    System.out.print(GREEN +map[i][j].toString()+ RESET);
                                    break;
                                case "UWL", "BWL":
                                    System.out.print(YELLOW +map[i][j].toString()+ RESET);
                                    break;
                                case "BMB", "TNT", "MST":
                                    System.out.print(RED +map[i][j].toString()+ RESET);
                                    break;
                                case "PRT":
                                    System.out.print(PURPLE+map[i][j].toString()+ RESET);
                                    break;
                                case "SPN":
                                    System.out.print(WHITE+map[i][j].toString()+ RESET);
                                    break;
                                default:
                                    System.out.print(BLUE + "   " + RESET);
                            }
                            System.out.print(CYAN+" ║"+RESET);
                        }
                    } else {
                        System.out.print(" ??? ");
                        System.out.print(CYAN+"║"+RESET);
                    }
                }
                System.out.println();
                if (i < map.length - 1) {
                    System.out.println(CYAN+"╠═════╬═════╬═════╬═════╬═════╬═════╬═════╬═════╬═════╬═════╬═════╬═════╬═════╬═════╬═════╬═════╬═════╬═════╬═════╬═════╣"+RESET);
                }
            }
            System.out.println(CYAN+"╚═════╩═════╩═════╩═════╩═════╩═════╩═════╩═════╩═════╩═════╩═════╩═════╩═════╩═════╩═════╩═════╩═════╩═════╩═════╩═════╝"+RESET);
        }
        if(numberOfPlayers==2) {
            System.out.println(GREEN+"PL1"+RESET+ " Score: " + PL[0].getScore() + "  |  " + GREEN+"PL2"+RESET + " Score: " + PL[1].getScore());
            System.out.println(GREEN+"PL1"+RESET+ " HP: " + PL[0].getHP() + "  |  " + GREEN+"PL2"+RESET + " HP: " + PL[1].getHP());
            System.out.println(GREEN+"PL1"+RESET+ " Abilities -> " + PL[0].getAbilities()+ "  |  "  + GREEN+"PL2"+RESET + " Abilities -> " + PL[1].getAbilities());
        }
        if(numberOfPlayers==4){
            System.out.println(GREEN+"PL1"+RESET + " Score: " + PL[0].getScore() + "  |  " + GREEN+"PL2"+RESET + " Score: " + PL[1].getScore() + "  |  " +GREEN+"PL3"+RESET + " Score: " + PL[2].getScore() + "  |  " + GREEN+"PL4"+RESET + " Score: " + PL[3].getScore());
            System.out.println(GREEN+"PL1"+RESET + " HP: " + PL[0].getHP() + "     |  " + GREEN+"PL2"+RESET + " HP: " + PL[1].getHP()+ "     |  " +GREEN+"PL3"+RESET + " HP: " + PL[2].getHP() +"     |  "+ GREEN+"PL4"+RESET + " HP: " + PL[3].getHP());
            System.out.println(GREEN+"PL1"+RESET + " Abilities -> " + PL[0].getAbilities()+PURPLE+ "   ###   "+RESET+ GREEN+"PL2"+RESET + " Abilities -> " + PL[1].getAbilities());
            System.out.println( GREEN+"PL3"+RESET + " Abilities -> " + PL[2].getAbilities()+PURPLE+"   ###   "+RESET + GREEN+"PL4"+RESET + " Abilities -> " + PL[3].getAbilities());
        }
        System.out.println("------------- Turn: "+GREEN+PL[Turn(Player.turn)].toString()+RESET+"'s Turn , Choose an action -------------");
        System.out.println("W. Move Up - A. Move Left - S. Move Down - D. Move Right - D. Destruction - J. Long Jump - T. Spawn Trap");
        System.out.println("in order to use your abilities first Enter the Direction key then the key representing the ability (e.g., DT,(E to exit)");
    }
    public static void loadGame() {
        try {File myFile = new File("logfile.txt");
            Gson gson = new Gson();
            if(!myFile.exists())
                myFile.createNewFile();
            if (myFile.exists()) {
                if (myFile.length() == 0) {
                    System.out.println(RED + "FILES WERE DELETED,START A NEW GAME" + RESET);
                    sleep(2000);
                    new Main();
                }
            }

        }catch(IOException e){
            e.printStackTrace();
            System.out.println("IOException, " + e.getMessage());
        }
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
                        Player.turn = Integer.parseInt(gson.fromJson(myFileReader.nextLine(),String.class));
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
    static void sleep(int millis){
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}