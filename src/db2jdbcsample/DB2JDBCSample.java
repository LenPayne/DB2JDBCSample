/*
 * Copyright 2014 Len Payne <len.payne@lambtoncollege.ca>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package db2jdbcsample;

import java.sql.*;
import com.ibm.as400.access.AS400JDBCDriver;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Len Payne <len.payne@lambtoncollege.ca>
 */
public class DB2JDBCSample {

    private static Connection conn;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            // Load the AS400JDBCDriver into memory so that DriverManager finds it
            try {
                Class.forName("com.ibm.as400.access.AS400JDBCDriver");
            } catch (ClassNotFoundException ex) {
                System.err.println("JDBC Driver Not Found.");
            }

            // Set up the JDBC String 
            // (Replace Credentials.user and Credentials.pass with your username/password)
            String jdbcf = "jdbc:as400://idevusr011.frankeni.com;user=%s;password=%s;";
            String jdbc = String.format(jdbcf, Credentials.user, Credentials.pass);

            // Build the Connection
            conn = DriverManager.getConnection(jdbc);

            // Perform a static query to get a ResultSet
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM \"LPAYNE\".\"KVP\"");
            int count = 0;
            while (rs.next()) {
                System.out.println(rs.getString(1) + "\t" + rs.getString(2));
                count++;
            }

            // Perform a dynamic query insert some new data
            String insertSql = "INSERT INTO \"LPAYNE\".\"KVP\" VALUES (?,?)";
            PreparedStatement pstmt = conn.prepareStatement(insertSql);
            String newKey = String.valueOf(count + 1);
            String newValue = "VALUE" + count;
            pstmt.setString(1, newKey);
            pstmt.setString(2, newValue);
            int results = pstmt.executeUpdate();

            // Feedback to User
            System.out.printf("Attempted to add: %s\t%s\n", newKey, newValue);
            System.out.printf("Updated %d Rows.\n", results);
        } catch (SQLException ex) {
            Logger.getLogger(DB2JDBCSample.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
