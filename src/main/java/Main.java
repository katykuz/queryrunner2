import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.*;

public class Main {
    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            new MainWindow(new NoopConnectionProvider()).show();
        });
    }

    // TODO: Replace me with something that actually creates connections!
    private static class NoopConnectionProvider implements ConnectionProvider {
        @Override
        public Connection getConnection() throws SQLException, ClassNotFoundException {
            //Class.forName("com.mysql.cj.jdbc.Driver");

            Connection connection = DriverManager.getConnection(

                    "jdbc:mysql://cssql.seattleu.edu:3306/tb_cpsc5021_22_group2",
                    "tb-ekuznetsova1",
                    "OreoBoo78!"

            );

            return connection;
        }
    }
}
