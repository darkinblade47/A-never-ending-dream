package Main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DataBase {
    private volatile static DataBase instance = null;

    private Connection con = null;
    private Statement statement = null;

    public DataBase() {
        try {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:DreamDB.db");
            statement = con.createStatement();
            this.Create();

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }

    }
    //Singleton database
    public static DataBase getInstance() {
        if (instance == null)
            instance = new DataBase();
        return instance;
    }

    public void Create() {
        if (con != null) {
            try {
                String playerDB =
                        "CREATE TABLE IF NOT EXISTS player " +
                                "(id INT PRIMARY KEY NOT NULL, " +
                                "score INT NOT NULL, " +
                                "playerX DOUBLE NOT NULL, " +
                                "playerY DOUBLE NOT NULL, " +
                                "health INT NOT NULL, " +
                                "lives INT NOT NULL);";

                String enemyDB =
                        "CREATE TABLE IF NOT EXISTS enemies" +
                                "(id INT PRIMARY KEY NOT NULL, " +
                                "enemyX DOUBLE NOT NULL, " +
                                "enemyY DOUBLE NOT NULL);";

                String StateDB =
                        "CREATE TABLE IF NOT EXISTS currentState" +
                                "(state INT DEFAULT 0);";

                String FlagsDB =
                        "CREATE TABLE IF NOT EXISTS flag" +
                                "(playerspeech INT NOT NULL, " +
                                "manspeech INT NOT NULL, " +
                                "girlspeech INT NOT NULL, " +
                                "boyspeech INT NOT NULL, " +
                                "bosspeech INT, " +
                                "ciorescuspeech INT, " +
                                "map2loaded INT, " +
                                "map3loaded INT, " +
                                "draw1 INT NOT NULL, " +
                                "turn INT NOT NULL);";

                statement.executeUpdate(playerDB);
                statement.executeUpdate(enemyDB);
                statement.executeUpdate(StateDB);
                statement.executeUpdate(FlagsDB);


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void InsertPlayer(double x, double y, int score, int health, int lives) {
        try {
            String s;
            s = "INSERT INTO player (id, score, playerX, playerY, health, lives) VALUES " +
                    "(1," + score + "," + x + "," + y + "," + health + "," + lives + ");";
            statement.executeUpdate(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void InsertFlagsL1(int p, int m, int b, int g, int turn, int draw1) {
        try {
            String s;

            s = "INSERT INTO flag (playerspeech, manspeech, boyspeech, girlspeech, turn, draw1) VALUES " +
                    "(" + p + "," + m + "," + b + "," + g + "," + turn + "," + draw1 + ");";
            statement.executeUpdate(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void InsertEnemy(int id, double x, double y) {
        try {
            String s;
            s = "INSERT INTO enemies (id, enemyX, enemyY) " + "VALUES (" + id + "," + x + "," + y + ");";
            statement.executeUpdate(s);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void InsertState() {
        try {
            String s = "INSERT INTO currentState(state) VALUES (0);";
            statement.executeUpdate(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void UpdateState(int state) {
        try {
            String s;
            s = "UPDATE currentState set state =" + state + ";";
            statement.executeUpdate(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void UpdateManCount(int count) {
        try {
            String s;
            s = "UPDATE flag set manspeech =" + count + ";";
            statement.executeUpdate(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void UpdateGirlCount(int count) {
        try {
            String s;
            s = "UPDATE flag set girlspeech =" + count + ";";
            statement.executeUpdate(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void UpdateBoyCount(int count) {
        try {
            String s;
            s = "UPDATE flag set boyspeech =" + count + ";";
            statement.executeUpdate(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void UpdateBossCount(int count) {
        try {
            String s;
            s = "UPDATE flag set bosspeech =" + count + ";";
            statement.executeUpdate(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void UpdateCiorescuCount(int count) {
        try {
            String s;
            s = "UPDATE flag set ciorescuspeech =" + count + ";";
            statement.executeUpdate(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void UpdatePlayerCount(int count) {
        try {
            String s;
            s = "UPDATE flag set playerspeech =" + count + ";";
            statement.executeUpdate(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void UpdateMap2(int load) {
        try {
            String s;
            s = "UPDATE flag set map2loaded ="+load+";";
            statement.executeUpdate(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void UpdateMap3(int load) {
        try {
            String s;
            s = "UPDATE flag set map3loaded ="+load+";";
            statement.executeUpdate(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void UpdateTurn(int turn) {
        try {
            String s;
            s = "UPDATE flag set turn =" + turn + ";";
            statement.executeUpdate(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void UpdateDraw1(int draw1) {
        try {
            String s;
            s = "UPDATE flag set draw1 =" + draw1 + ";";
            statement.executeUpdate(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void UpdatePlayer(double x, double y, int score, int health, int lives) {
        try {
            String s = "UPDATE player set playerX=" + x + ";";
            String s1 = "UPDATE player set playerY=" + y + ";";
            String s2 = "UPDATE player set score=" + score + ";";
            String s3 = "UPDATE player set health=" + health + ";";
            String s4 = "UPDATE player set lives=" + lives + ";";
            statement.executeUpdate(s);
            statement.executeUpdate(s1);
            statement.executeUpdate(s2);
            statement.executeUpdate(s3);
            statement.executeUpdate(s4);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void UpdateLives(int lives) {
        try {
            String s = "UPDATE player set lives=" + lives + ";";
            statement.executeUpdate(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void UpdateHealth(int health) {
        try {
            String s = "UPDATE player set health=" + health + ";";
            statement.executeUpdate(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ResultSet LoadPlayer() {
        try {
            String s = "SELECT * FROM player";
            ResultSet res = statement.executeQuery(s);
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public ResultSet LoadFlags() {
        try {
            String s = "SELECT * FROM flag";
            ResultSet res = statement.executeQuery(s);
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public ResultSet LoadEnemy() {
        try {
            String s = "SELECT * FROM enemies;";
            ResultSet res = statement.executeQuery(s);
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public int LoadState() {
        try {
            String s = "SELECT * FROM currentState";
            ResultSet res = statement.executeQuery(s);
            return res.getInt("state");
        } catch (Exception e) {
            e.printStackTrace();
            return 0;

        }
    }

    public void DeletePlayer(int id) {
        try {
            String s = "DELETE from player where id=" + id + ";";
            statement.executeUpdate(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void DeleteEnemy(int id) {
        try {
            String s = "DELETE from enemies where id=" + id + ";";
            statement.executeUpdate(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void DeleteAllEnemies() {
        try {
            String s = "DELETE from enemies;";
            statement.executeUpdate(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void DeleteFlags() {
        try {
            String s = "DELETE from flag;";
            statement.executeUpdate(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void DeleteState() {
        try {
            String s = "DELETE from currentState;";
            statement.executeUpdate(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
