package gameClient;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class Data {
    private static String jdbcUrl = "jdbc:mysql://db-mysql-ams3-67328-do-user-4468260-0.db.ondigitalocean.com:25060/oop?useUnicode=yes&characterEncoding=UTF-8&useSSL=false";
    private static final String jdbcUser = "student";
    private static final String jdbcUserPassword = "OOP2020student";

    private HashMap<Integer, user> users;
    private HashMap<Integer, Integer> maxLevel;
    private static int[][] results = {
            {0, 125, 290},
            {1, 436, 580},
            {3, 713, 580},
            {5, 570, 500},
            {9, 480, 580},
            {11, 1050, 580},
            {13, 310, 580},
            {16, 235, 290},
            {19, 250, 580},
            {20, 200, 290},
            {23, 1000, 1140}};

    public HashMap<Integer, user> getUsers() {
        return users;
    }

    public HashMap<Integer, Integer> getMaxLevel() {
        return maxLevel;
    }

    public class user {
        public class info{

            private int _moves;
            private int _grade;

            public info(int _moves, int _grade) {
                this._moves = _moves;
                this._grade = _grade;
            }

            public int get_moves() {
                return _moves;
            }

            public int get_grade() {
                return _grade;
            }
        }

        private int _id;
        private int _gamesPlayed;
        private int _level;
        private List<info> _games;
        public user(int id, int level, int moves, int grade) {
            _id = id;
            _level = level;
            _games =null;
        }

        public int get_id() {
            return _id;
        }

        public int get_level() {
            return _level;
        }

        public List<info> get_games() {
            return _games;
        }

        public int get_gamesPlayed() {
            return _gamesPlayed;
        }

        public void set_gamesPlayed(int _gamesPlayed) {
            this._gamesPlayed = _gamesPlayed;
        }
    }

    public Data() {
        users = new HashMap<>();
        maxLevel = new HashMap<>();
        allUsers();
        fillGamesPlayed();
    }

    private void allUsers() {
        int id;
        int maxLvl;
        String allCustomersQuery = "SELECT * FROM Users;";
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection =
                    DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(allCustomersQuery);
            while (resultSet.next()) {
                id = resultSet.getInt("UserID");
                maxLvl = resultSet.getInt("levelNum");
                maxLevel.put(id, maxLvl);
                fillUsers(id);
            }
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException sqle) {
            System.out.println("SQLException: " + sqle.getMessage());
            System.out.println("Vendor Error: " + sqle.getErrorCode());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void addUser(int id, user player) {
        users.put(id, player);
    }

    private void fillUsers(int id) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection =
                    DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);
            Statement statement = connection.createStatement();
            String allCustomersQuery = "SELECT * FROM Logs where userID=" + id;

            ResultSet resultSet = statement.executeQuery(allCustomersQuery);
            while (resultSet.next()) {
                int idt = resultSet.getInt("UserID");
                int level = resultSet.getInt("levelID");
                int score = resultSet.getInt("score");
                int moves = resultSet.getInt("moves");
                addUser(id, new user(id, level, moves, score));
            }
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException sqle) {
            System.out.println("SQLException: " + sqle.getMessage());
            System.out.println("Vendor Error: " + sqle.getErrorCode());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    private void fillGamesPlayed(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection =
                    DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);
            Statement statement = connection.createStatement();
            String allCustomersQuery = "SELECT count(*) AS games, UserID FROM Logs WHERE userID > 123456789  GROUP BY userID;";

            ResultSet resultSet = statement.executeQuery(allCustomersQuery);
            while (resultSet.next()) {
                int idt = resultSet.getInt("UserID");
                int gamesPlayed = resultSet.getInt("games");
                users.get(idt).set_gamesPlayed(gamesPlayed);
            }
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException sqle) {
            System.out.println("SQLException: " + sqle.getMessage());
            System.out.println("Vendor Error: " + sqle.getErrorCode());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
