package com.DL.data;

import com.DL.bo.Contact;
import com.DL.bo.Group;
import org.apache.log4j.Logger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.DL.data.ContactDao.resultToContact;


public class GroupDao {
    private final Logger logger = Logger.getLogger(this.getClass());
    public GroupDao(){}

    //Méthode 1
    public void CreateGroup(String nom) throws DataBaseException{
        try {
            Connection c = DBConnection.getInstance();
            String sqlInsert = "insert into GROUPE (nom) values(?)";
            PreparedStatement stm = c.prepareStatement(sqlInsert);
            stm.setString(1, nom);
            stm.executeUpdate();
        } catch (SQLException var5) {
            this.logger.error("Erreur à cause de : ", var5);
            throw new DataBaseException(var5);
        }
    }
    //Méthode 2
    public void supprimerGroupParNom(String nom)throws DataBaseException {
        try {
            Connection c = DBConnection.getInstance();
            String sqlDeleteGRPCON = "DELETE FROM GRPCON WHERE id_group = (SELECT id_group FROM GROUPE WHERE upper(nom) = ?)";
            PreparedStatement stmGRPCON = c.prepareStatement(sqlDeleteGRPCON);
            stmGRPCON.setString(1, nom);
            stmGRPCON.executeUpdate();
//            String sql=" DELETE FROM groupeparnom WHERE (nom) = ? ";
//            PreparedStatement st=c.prepareStatement(sql);
//            st.setString(1, nom);
//            st.executeUpdate();
            String sqlDelete = "DELETE FROM GROUPE WHERE upper(nom) = ? ";
            PreparedStatement stm = c.prepareStatement(sqlDelete);
            stm.setString(1, nom);
            stm.executeUpdate();
        } catch (SQLException e) {
            this.logger.error("Erreur due à : ", e);
            throw new DataBaseException(e);
        }

    }

    //Recherche par ID
    public Group rechercherGroupeParId(int id) throws  DataBaseException{
        Group groupe = null;
        try {
            Connection c = DBConnection.getInstance();
            String sqlSelect = "SELECT * FROM GROUPE WHERE id_group = ? ";
            PreparedStatement stm = c.prepareStatement(sqlSelect);
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                groupe = resultToGroupe(rs);
            }
        } catch (SQLException e) {
            this.logger.error("Erreur due à : ", e);
            throw new DataBaseException(e);
        }
        return groupe;
    }
    //Recherche par nom:
    public Group rechercherGroupeParNom(String nom) throws  DataBaseException{
        Group groupe = null;
        try {
            Connection c = DBConnection.getInstance();
            String sqlSelect = "SELECT * FROM GROUPE WHERE nom = ? ";
            PreparedStatement stm = c.prepareStatement(sqlSelect);
            stm.setString(1, nom);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                groupe = resultToGroupe(rs);
            }
        } catch (SQLException e) {
            this.logger.error("Erreur due à : ", e);
            throw new DataBaseException(e);
        }
        return groupe;
    }
    //Affichage des groupes normales:
public List<Group> getOrderedGroups() throws DataBaseException {
    List<Group>  orderedGroupes = new ArrayList<>();
    try {
        Connection c = DBConnection.getInstance();
        PreparedStatement stm = c.prepareStatement("SELECT * FROM GROUPE ORDER BY nom ASC;");
        ResultSet rs = stm.executeQuery();
        while(rs.next()) {
            orderedGroupes.add(this.resultToGroupe(rs));
        }
        rs.close();
        return orderedGroupes;

    } catch (SQLException var5) {
        this.logger.error("Erreur à cause de : ", var5);
        throw new DataBaseException(var5);
    }
}

    public List<String> getContactOrdered1() throws DataBaseException {
        List<String> contacts = new ArrayList<>();

        try {
            Connection c = DBConnection.getInstance();
            String sql = "SELECT DISTINCT nom FROM groupeparnom ORDER BY nom DESC;";
            PreparedStatement stm = c.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                String nom = rs.getString("nom");
                contacts.add(nom);
            }

            rs.close();
            stm.close();
        } catch (SQLException e) {
            throw new DataBaseException(e);
        }

        return contacts;
    }

    public void insetintoGRPCON(int id_contact, int id_group)throws DataBaseException {
        try {
            Connection c = DBConnection.getInstance();
            String sqlDelete = "insert into GRPCON values(?, ?) ";
            PreparedStatement stm = c.prepareStatement(sqlDelete);
            stm.setInt(1, id_contact);
            stm.setInt(2, id_group);
            stm.executeUpdate();
        } catch (SQLException e) {
            this.logger.error("Erreur due à : ", e);
            throw new DataBaseException(e);
        }

    }

    public List<Contact> getContactsByGroupName(String groupName) throws DataBaseException {
        List<Contact> contactNames = new ArrayList<>();
        try {
            Connection con = DBConnection.getInstance();
            PreparedStatement stmt = con.prepareStatement("select * from contact where id_contact in (select id_contact from grpcon where id_group = (select id_group from groupe where nom = ? ));");
            stmt.setString(1, groupName);
            ResultSet res= stmt.executeQuery();
            while(res.next()){
                Contact contact = resultToContact(res);
                contactNames.add(contact);
            }res.close();
            return contactNames;
        } catch (SQLException e) {
            throw new DataBaseException(e);}}
    public List<String> getGroupsByContactName(String contactName, String contactPrenom) throws DataBaseException {
        List<String> groupNames = new ArrayList<>();
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet res = null;
        PreparedStatement st = null;
        ResultSet res2 = null;

        try {
            con = DBConnection.getInstance();
            String sql = "SELECT * FROM GROUPE WHERE id_group IN (SELECT id_group FROM grpcon WHERE id_contact IN (SELECT id_contact FROM contact WHERE nom = ? AND prenom = ?))";
            stmt = con.prepareStatement(sql);
            stmt.setString(1, contactName);
            stmt.setString(2, contactPrenom);
            res = stmt.executeQuery();

            while (res.next()) {
                groupNames.add(resultToGroupe(res).getNom());
            }

            String sql2 = "SELECT DISTINCT (nom) FROM groupeparnom WHERE upper(nom) = ?";
            st = con.prepareStatement(sql2);
            st.setString(1, contactName);
            res2 = st.executeQuery();

            while (res2.next()) {
                groupNames.add(res2.getString("nom"));
            }

            return groupNames;
        } catch (SQLException e) {
            throw new DataBaseException(e);
        } finally {
            try {
                if (res != null) {
                    res.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (res2 != null) {
                    res2.close();
                }
                if (st != null) {
                    st.close();
                }
            } catch (SQLException e) {
                    throw new DataBaseException(e);            }
        }
    }


    public void supprimergroupeNom(String nom) throws DataBaseException{
        try{Connection c=DBConnection.getInstance();
            PreparedStatement s=c.prepareStatement("DELETE FROM groupeparnom WHERE upper(nom) = ? ");
            s.setString(1, nom);
            s.executeUpdate();}catch (SQLException e){
            throw new DataBaseException(e);
        }
    }



    //GetGroupIdByNamr::
    public int getGroupIdByName(String name) throws DataBaseException, SQLException {
        Connection con = DBConnection.getInstance();
        PreparedStatement stmt = con.prepareStatement("SELECT id_group FROM groupe WHERE nom = ?");
        stmt.setString(1, name);
        ResultSet res = stmt.executeQuery();
        int id = 0;
        if (res.next()) {
            id = res.getInt("id_group");
        }
        return id;
    }

    private Group resultToGroupe(ResultSet rs) throws SQLException {
        Group groupe = new Group();
        groupe.setId(rs.getInt("id_group"));
        groupe.setNom(rs.getString("nom"));
        return groupe;
    }

    }


