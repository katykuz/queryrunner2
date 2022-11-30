import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ReadOnlyQuery2 {
    public final String label1;
    public final String label2;
    public final String query;
    public final String description;

    public ReadOnlyQuery2(String label1, String label2, String query, String description) {
        this.label1 = label1;
        this.label2 = label2;
        this.query = query;
        this.description = description;
    }

    public ReadOnlyQueryResult run(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            try (ResultSet resultSet = statement.executeQuery(query)) {
                return ReadOnlyQueryResult.fromResultSet(resultSet);
            }
        }
    }
}
