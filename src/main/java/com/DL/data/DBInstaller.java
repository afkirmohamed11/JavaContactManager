package com.DL.data;

import com.DL.bll.ContactManager;
import org.apache.log4j.Logger;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
public class DBInstaller {
    private static Logger LOGGER = Logger.getLogger(DBInstaller.class);

    public DBInstaller() {
    }

    public static void createDataBaseTables() throws DataBaseException {
        try {
            Connection con = DBConnection.getInstance();
            String[] sqlStatements = {
                    "use contact",
                    "DROP TABLE IF EXISTS groupeparnom",
                    "DROP TABLE IF EXISTS GRPCON",
                    "DROP TABLE IF EXISTS CONTACT",
                    "DROP TABLE IF EXISTS GROUPE",
                    "CREATE TABLE CONTACT ( " +
                            "id_contact bigint auto_increment primary key, " +
                            "nom varchar(255), " +
                            "prenom varchar(255), " +
                            "telephone1 varchar(500) , " +
                            "telephone2 varchar(500) , " +
                            "adresse varchar(255), " +
                            "email_personnel varchar(500), " +
                            "email_professionnel varchar(500), " +
                            "genre varchar(50)" +
                            ")",
                    "CREATE TABLE GROUPE ( " +
                            "id_group bigint auto_increment primary key, " +
                            "nom varchar(255) unique" +
                            ")",
                    "CREATE TABLE GRPCON ( " +
                            "id_contact bigint, " +
                            "id_group bigint, " +
                            "CONSTRAINT fk_contact FOREIGN KEY (id_contact) REFERENCES CONTACT (id_contact), " +
                            "CONSTRAINT fk_group FOREIGN KEY (id_group) REFERENCES GROUPE (id_group)" +
                            ")",
                            "create table groupeparnom(" +
                            "id_groupe_par_nom bigint auto_increment primary key ," +
                            "nom varchar(100)" +
                            ");"
            };

            Statement stmt = con.createStatement();
            for (String sqlStatement : sqlStatements) {
                stmt.executeUpdate(sqlStatement);
            }

            stmt.close();
        } catch (Exception e) {
            LOGGER.error(e);
            throw new DataBaseException(e);
        }
    }

    public static boolean checkIfTableExists() throws DataBaseException {
        try {
            Connection con = DBConnection.getInstance();
            Statement stmt = con.createStatement();
            ResultSet rs1 = stmt.executeQuery("SHOW TABLES LIKE 'CONTACT' ");
            boolean table1Exists = rs1.next();
            rs1.close();
            ResultSet rs2 = stmt.executeQuery("SHOW TABLES LIKE 'GROUPE' ");
            boolean table2Exists = rs2.next();
            rs2.close();
            ResultSet rs3 = stmt.executeQuery("SHOW TABLES LIKE 'GRPCON' ");
            boolean table3Exists = rs3.next();
            rs3.close();
            ResultSet rs4 = stmt.executeQuery("SHOW TABLES LIKE 'groupeparnom' ");
            boolean table4Exists = rs4.next();
            rs3.close();
            stmt.close();
            return table1Exists && table2Exists && table3Exists && table4Exists;
        } catch (SQLException e) {
            LOGGER.error(e);
            throw new DataBaseException(e);
        }
    }






    }