package com.DL.data;
import org.apache.log4j.Logger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.DL.bo.Contact;
public class ContactDao {
    private final Logger logger = Logger.getLogger(this.getClass());
    public ContactDao(){}
    //Méthode 1
    public void create(Contact pContact) throws DataBaseException {
        try {
            Connection c = DBConnection.getInstance();
            String sqlInsert = "insert into CONTACT(nom, prenom,telephone1, telephone2, adresse, email_personnel, email_professionnel, genre) values(?,?,?,?,?,?,?,?)";
            PreparedStatement stm = c.prepareStatement(sqlInsert);
            String sqlInsert1="insert into groupeparnom(nom) values(?)";
            PreparedStatement stmt=c.prepareStatement(sqlInsert1);
            stmt.setString(1, pContact.getNom());
            stm.setString(1, pContact.getNom());
            stm.setString(2, pContact.getPrenom());
            stm.setString(3, pContact.getTelephone1());
            stm.setString(4, pContact.getTelephone2());
            stm.setString(5, pContact.getAdresse());
            stm.setString(6, pContact.getEmailPersonnel());
            stm.setString(7, pContact.getEmailProfessionnel());
            stm.setString(8, pContact.getGenre());
            stm.executeUpdate();
            stmt.executeUpdate();
        } catch (SQLException var5) {
            this.logger.error("Erreur à cause de : ", var5);
            throw new DataBaseException(var5);
        }


        // Méthode 2
    } public List<Contact> getContactOrdered() throws DataBaseException {
        List<Contact> orderedContacts = new ArrayList<>();
        try {
            Connection c = DBConnection.getInstance();
            PreparedStatement stm = c.prepareStatement("SELECT * FROM CONTACT ORDER BY nom ASC;");
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                orderedContacts.add(resultToContact(rs));
            }
            rs.close();
            return orderedContacts;

        } catch (SQLException var5) {
            this.logger.error("Erreur à cause de : ", var5);
            throw new DataBaseException(var5);
        }
    }
    //Méthode 3
    public void supprimerParNomEtPrenom(String contactNom, String contactPrenom) throws DataBaseException {
        try {
            Connection c = DBConnection.getInstance();
            String sqldeletegrpcon = "DELETE FROM GRPCON WHERE id_contact = (SELECT id_contact FROM CONTACT WHERE UPPER(nom) = ? AND UPPER(prenom) = ?)";
            String sqldeletegroupe = "DELETE FROM groupeparnom WHERE upper(nom) = ? LIMIT 1";
            String sqlDelete = "DELETE FROM CONTACT WHERE upper(nom) = ? and upper(prenom) = ?";
            PreparedStatement stmt=c.prepareStatement(sqldeletegrpcon);
            stmt.setString(1, contactNom);
            stmt.setString(2, contactPrenom);
            stmt.executeUpdate();
            PreparedStatement stmtt=c.prepareStatement(sqldeletegroupe);
            stmtt.setString(1, contactNom);
            stmtt.executeUpdate();
            PreparedStatement stm = c.prepareStatement(sqlDelete);
            stm.setString(1, contactNom);
            stm.setString(2, contactPrenom);
            stm.executeUpdate();
        } catch (SQLException e) {
            this.logger.error("Erreur due à : ", e);
            throw new DataBaseException(e);
        }

    }

    //Méthode 4
    public void modifierContact(String nom,String prenom ,Contact pContact) throws DataBaseException {
        try {
            Connection c = DBConnection.getInstance();
            String sqlUpdate = "UPDATE CONTACT SET nom=?, prenom=?, adresse=?, telephone1=?, telephone2=?, email_personnel=?, email_professionnel=?, genre=? WHERE nom=? and prenom=?";
            PreparedStatement stm = c.prepareStatement(sqlUpdate);
            stm.setString(1, pContact.getNom());
            stm.setString(2, pContact.getPrenom());
            stm.setString(4, pContact.getTelephone1());
            stm.setString(5, pContact.getTelephone2());
            stm.setString(3, pContact.getAdresse());
            stm.setString(6, pContact.getEmailPersonnel());
            stm.setString(7, pContact.getEmailProfessionnel());
            stm.setString(8, pContact.getGenre());
            stm.setString(9, nom);
            stm.setString(10, prenom);
            stm.executeUpdate();
        } catch (SQLException e) {
            this.logger.error("Erreur due à : ", e);
            throw new DataBaseException(e);
        }
    }

    //Méthode 5
   public List<Contact> rechercherContactParNom(String nom) throws DataBaseException {
       List<Contact> contacts = new ArrayList<>();
       try {
           Connection c = DBConnection.getInstance();
           String sqlSelect = "SELECT * FROM CONTACT WHERE upper(nom) = ?";
           PreparedStatement stm = c.prepareStatement(sqlSelect);
           stm.setString(1, nom);
           ResultSet rs = stm.executeQuery();
           while (rs.next()) {
               contacts.add(resultToContact(rs));}
       } catch (SQLException e) {
           this.logger.error("Erreur due à : ", e);
           throw new DataBaseException(e);}
       return contacts;}
    //  Recherchre par Prenom
    public List<Contact> rechercherContactParPrenom(String nom, String prenom) throws DataBaseException{
        List<Contact> contacts = new ArrayList<>();
        try {
            Connection c = DBConnection.getInstance();
            String sqlSelect = "SELECT * FROM CONTACT WHERE upper(nom)=? AND upper(prenom) = ?";
            PreparedStatement stm = c.prepareStatement(sqlSelect);
            stm.setString(1, nom);
            stm.setString(2, prenom);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                contacts.add(resultToContact(rs));}
        } catch (SQLException e) {
            this.logger.error("Erreur due à : ", e);
            throw new DataBaseException(e);}
        return contacts;}
    // Méthode 6
    public List<Contact> rechercherParTelephone(String numero) throws DataBaseException {
        List<Contact> contacts = new ArrayList<>();
        try {
                Connection c = DBConnection.getInstance();
                String sqlSelect = "SELECT * FROM CONTACT WHERE telephone1=? OR telephone2=?";
                PreparedStatement stm = c.prepareStatement(sqlSelect);
                stm.setString(1, numero);
                stm.setString(2, numero);
                ResultSet rs = stm.executeQuery();
                while (rs.next()) {
                contacts.add(this.resultToContact(rs));}
            } catch (SQLException e) {
                this.logger.error("Erreur due à : ", e);
                throw new DataBaseException(e);
            }
            return contacts;
        }


   // Méthode 10
   public List<Contact> rechercherPhonétiqument(String nom) throws DataBaseException {
       List<Contact> contacts = new ArrayList<>();
       try {
           Connection c = DBConnection.getInstance();
           String sqlSelect = "SELECT * FROM CONTACT WHERE SOUNDEX(nom) = ?";
           PreparedStatement stm = c.prepareStatement(sqlSelect);
           stm.setString(1, nom);
           ResultSet rs = stm.executeQuery();
           while (rs.next()) {
               Contact contact = resultToContact(rs);
               contacts.add(contact);
           }

       } catch (SQLException e) {
           this.logger.error("Erreur due à : ", e);
           throw new DataBaseException(e);
       }

       return contacts;
   }
// Methode pour la recherche par ID:
public Contact rechercherContactParId(int id) throws  DataBaseException{
        Contact contact = null;
        try {
            Connection c = DBConnection.getInstance();
            String sqlSelect = "SELECT * FROM CONTACT WHERE id_contact = ? ";
            PreparedStatement stm = c.prepareStatement(sqlSelect);
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                contact = resultToContact(rs);
            }
        } catch (SQLException e) {
            this.logger.error("Erreur due à : ", e);
            throw new DataBaseException(e);
        }
        return contact;

    }



    public static Contact resultToContact(ResultSet rs) throws SQLException {
        Contact contact = new Contact();
        contact.setId(rs.getInt("id_contact"));
        contact.setNom(rs.getString("nom"));
        contact.setPrenom(rs.getString("prenom"));
        contact.setTelephone1(rs.getString("telephone1"));
        contact.setTelephone2(rs.getString("telephone2"));
        contact.setAdresse(rs.getString("adresse"));
        contact.setEmailPersonnel(rs.getString("email_personnel"));
        contact.setEmailProfessionnel(rs.getString("email_professionnel"));
        contact.setGenre(rs.getString("genre"));
        return contact;
    }

}






















