package cyclone.otusspring.library.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

class ResultSetUtils {

    /**
     * Retrieves the value of the designated column in the current row
     * of this {@code ResultSet} object as an {@code int} or {@code null} if value is absent
     */
    static Integer optInt(ResultSet rs, String columnLabel) throws SQLException {
        int intValue = rs.getInt(columnLabel);
        if (rs.wasNull()) {
            return null;
        } else {
            return intValue;
        }
    }
}
