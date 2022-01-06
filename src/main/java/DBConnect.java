import java.sql.*;
import java.time.LocalDate;

/**
 * This class is used to connect to the Database and execute queries.
 */
public class DBConnect {
    private static String CONNECTION_PATH = "src/main/java/BJDatabase.db";

    /**
     * This method is used to connect to the database.
     * @return Connection
     */
    public static Connection connect(String connectionPath){
        Connection connection = null;
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:"+connectionPath);
        } catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * This method creates a new User
     * @param user
     * @param pass
     * @param mail
     */
    public static void createNewUser(String user, String pass, String mail){
        Connection connection = connect(CONNECTION_PATH);
        try {
            Statement statement = connection.createStatement();
            double balance = 1000;
            statement.executeUpdate("INSERT INTO Player (USER, PASS, MAIL, BALANCE) VALUES ('"+user+"', '"+pass+"', '"+mail+"',' "+balance+"')");
            int id = getID(user);
            statement.executeUpdate("INSERT INTO DailyBonus(ID) VALUES(id)");
            statement.executeUpdate("INSERT INTO Achievements(ID) VALUES(id)");
        } catch (SQLException e){
            e.printStackTrace();
        }finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * This method checks if the entered password is valid
     * @param user
     * @param pass
     * @return if password valid -> true
     */
    public static boolean checkPassword(String user, String pass){
        Connection connection = connect(CONNECTION_PATH);
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT PASS FROM Player WHERE USER = '"+user+"'");
            if(resultSet.next()){
                if(resultSet.getString("PASS").equals(pass)){
                    return true;
                }
            }
        } catch (SQLException e){
            e.printStackTrace();
        }finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    /**
     * Checks if Email is already used by another account
     * @param email
     * @return if mail used -> true
     */
    public static boolean isEmailAlreadyUsed(String email){
        Connection connection = connect(CONNECTION_PATH);
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT MAIL FROM Player WHERE MAIL = '"+email+"'");
            if(resultSet.next()){
                return true;
            }
        } catch (SQLException e){
            e.printStackTrace();
        }finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    /**
     * Checks if username is already used by another account
     * @param user
     * @return if username used -> true
     */
    public static boolean isUsernameAlreadyUsed(String user){
        Connection connection = connect(CONNECTION_PATH);
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT USER FROM Player WHERE USER = '"+user+"'");
            if(resultSet.next()){
                return true;
            }
        } catch (SQLException e){
            e.printStackTrace();
        }finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    /**
     * Returns the balance of a current user
     * @param user
     * @return balance
     */
    public static double getBalance(String user){
        Connection connection = connect(CONNECTION_PATH);
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT BALANCE FROM Player WHERE USER = '"+user+"'");
            if(resultSet.next()){
                return resultSet.getDouble("balance");
            }
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return 0;
    }

    /**
     * This method updates the balance of a user
     * @param user
     * @param amount
     */
    public static void updateBalance(String user, double amount){
        Connection connection = connect(CONNECTION_PATH);
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate("UPDATE Player SET BALANCE ="+amount+" WHERE USER = '"+user+"'");
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }



    }


    /**
     * This method returns the ID of a user by his USERNAME
     * @param user
     * @return ID
     */
    public static int getID(String user){
        Connection connection = connect(CONNECTION_PATH);
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT ID FROM Player WHERE USER = '"+user+"'");
            if(resultSet.next()){
                return resultSet.getInt("ID");
            }
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return 0;
    }

    /**
     * Checks if Daily Bonus was activated today
     * @param user
     * @return if not activated today --> true
     */
    public static boolean isDailyBValid(String user){
        Connection connection = connect(CONNECTION_PATH);
        try {
            Statement statement = connection.createStatement();
            int id = getID(user);
            ResultSet resultSet = statement.executeQuery("SELECT DATE FROM DailyBonus WHERE ID = "+id);
            if(resultSet.next()){
                LocalDate localDate = LocalDate.parse(resultSet.getString("date"));
                LocalDate now = LocalDate.now();
                return !localDate.isEqual(now);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }


    /**
     * This method saves the date of the last time the daily bonus was activated
     * @param user
     */
    public static void setDailyBDate(String user){
        Connection connection = connect(CONNECTION_PATH);
        try {
            Statement statement = connection.createStatement();
            int id = getID(user);
            statement.executeUpdate("UPDATE DailyBonus SET DATE = '"+LocalDate.now().toString()+"' WHERE ID = "+id);
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * This method calculates the achievement multiplier a player gets on their daily bonus
     * Achievement A5 --> 100
     * Achievement A4 --> 50
     * Achievement A3 --> 25
     * Achievement A2 --> 10
     * Achievement A1 --> 2
     * Achievement A0 --> 1
     * @param user
     * @return multiplier
     */
    private static int calcAchievementMultiplier(String user){
        Connection connection = connect(CONNECTION_PATH);
        try {
            Statement statement = connection.createStatement();
            int id = getID(user);
            ResultSet resultSet = statement.executeQuery("SELECT A0, A1, A2, A3, A4, A5 FROM Achievements WHERE ID = "+id);
            if(resultSet.next()){
                if (resultSet.getInt("A5")==1){
                    return 100;
                } else if (resultSet.getInt("A4")==1){
                    return 50;
                }else if (resultSet.getInt("A3")==1){
                    return 25;
                } else if (resultSet.getInt("A2")==1){
                    return 10;
                } else if (resultSet.getInt("A1")==1){
                    return 2;
                } else {
                    return 1;
                }

            }
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return 1;
    }

    /**
     * Sets the Multiplier calculated by calcAchievementMultiplier(String user):int
     * @param user
     */
    private static void setAchievementMultiplier(String user){
        int multiplier = calcAchievementMultiplier(user);
        Connection connection = connect(CONNECTION_PATH);
        try {
            Statement statement = connection.createStatement();
            int id = getID(user);
            statement.executeUpdate("UPDATE DailyBonus SET MULTP = "+multiplier+" WHERE ID = "+id);
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * This method returns the multiplier saved in the database
     * @param user
     * @return multiplier
     */
    public static int getAchievementMultiplier(String user){
        setAchievementMultiplier(user);
        Connection connection = connect(CONNECTION_PATH);
        try {
            Statement statement = connection.createStatement();
            int id = getID(user);
            ResultSet resultSet = statement.executeQuery("SELECT MULTP FROM DailyBonus WHERE ID = "+id);
            if(resultSet.next()){
                return resultSet.getInt("MULTP");
            }
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return 1;
    }


    /**
     * This method calculates the achievements by the balance of a player
     * Achievement A5 --> 10000000
     * Achievement A4 --> 1000000
     * Achievement A3 --> 500000
     * Achievement A2 --> 100000
     * Achievement A1 --> 10000
     * Achievement A0 --> 1000
     * @param user
     */
    public static void setAchievements(String user){
        Connection connection = connect(CONNECTION_PATH);
        try {
            Statement statement = connection.createStatement();
            int id = getID(user);
            double balance = getBalance(user);
            if (balance>10000000){
                statement.executeUpdate("UPDATE Achievements SET A5 = 1 WHERE ID = "+id);
            }  if (balance>1000000){
                statement.executeUpdate("UPDATE Achievements SET A4 = 1 WHERE ID = "+id);
            }  if (balance>500000){
                statement.executeUpdate("UPDATE Achievements SET A3 = 1 WHERE ID = "+id);
            }  if (balance>100000){
                statement.executeUpdate("UPDATE Achievements SET A2 = 1 WHERE ID = "+id);
            }  if (balance>10000){
                statement.executeUpdate("UPDATE Achievements SET A1 = 1 WHERE ID = "+id);
            }  if (balance>1000){
                statement.executeUpdate("UPDATE Achievements SET A0 = 1 WHERE ID = "+id);
            }
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Allows changing username of player
     * @param currentUsername
     * @param newUsername
     */
    public static void changeUsername(String currentUsername, String newUsername) {
        Connection connection = connect(CONNECTION_PATH);
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate("UPDATE Player SET USER = '" + newUsername + "' WHERE USER = '" + currentUsername + "'");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Allows changing mail of a player
     * @param userName
     * @param mail
     */
    public static void changeMail(String userName, String mail) {
        //Changes the mail of a user
        Connection connection = connect(CONNECTION_PATH);
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate("UPDATE Player SET MAIL = '" + mail + "' WHERE USER = '" + userName + "'");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Allows changing password of a player
     * @param userName
     * @param newPass
     */
    public static void changePassword(String userName, String newPass) {
        Connection connection = connect(CONNECTION_PATH);
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate("UPDATE Player SET PASS = '" + newPass + "' WHERE USER = '" + userName + "'");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * Method gets all the achievements of the user and returns a boolean array
     * @param user
     * @return
     */
    public static boolean[] getAchievements(String user) {
        Connection connection = connect(CONNECTION_PATH);
        boolean[] achievements = new boolean[6];
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM Achievements WHERE ID = " + getID(user));
            while (rs.next()) {
                achievements[0] = rs.getBoolean("A0");
                achievements[1] = rs.getBoolean("A1");
                achievements[2] = rs.getBoolean("A2");
                achievements[3] = rs.getBoolean("A3");
                achievements[4] = rs.getBoolean("A4");
                achievements[5] = rs.getBoolean("A5");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return achievements;
    }
}

