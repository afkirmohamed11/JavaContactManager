package com.DL.bll;

import com.DL.bo.Contact;
import com.DL.bo.Group;
import com.DL.data.ContactDao;
import com.DL.data.DataBaseException;
import com.DL.data.GroupDao;

import java.util.List;

public class ContactManager {
    private ContactDao contactDao = new ContactDao();
    private GroupDao groupeDao = new GroupDao();
    public ContactManager() {}

        // Méthode 1
        public void ajouterContact(Contact pcontact) throws DataBaseException, BusinessLogicException {
            Contact contact = this.contactDao.rechercherContactParId(pcontact.getId());
            if (contact != null) {
                throw new BusinessLogicException("Le contact existe déjà");
            } else {
                this.contactDao.create(pcontact);
            }
        }
    //Méthode 2
    public List<Contact> getContactOrdered() throws DataBaseException, BusinessLogicException {
        if(contactDao.getContactOrdered().isEmpty()){
            throw new BusinessLogicException("Aucun contact trouvé. ");
        }
        return contactDao.getContactOrdered();
    }

    //Methode2'
    public List<String> getContactOrdered1() throws DataBaseException, BusinessLogicException{
        if(groupeDao.getContactOrdered1().isEmpty()){
            throw new BusinessLogicException("Aucun contact trouvé. ");
        }
        return groupeDao.getContactOrdered1();
    }

        // Méthode 3
        public void supprimerParNomEtPrenom(String contactNom, String contactPrenom) throws DataBaseException, BusinessLogicException {
           if(contactDao.rechercherContactParPrenom(contactNom, contactPrenom).isEmpty()){
               throw new BusinessLogicException("Aucun contact trouvé");
           }
           this.contactDao.supprimerParNomEtPrenom(contactNom, contactPrenom);

        }

        // Méthode 4
        public void modifierContact(String nom, String prenom ,Contact contact) throws DataBaseException, BusinessLogicException {
         if(contactDao.rechercherContactParPrenom(nom, prenom).isEmpty()){
             throw new BusinessLogicException("Aucun contact trouvé.");}
            this.contactDao.modifierContact(nom, prenom ,contact);

        }
    //Méthode 5
    public List<Contact> rechercherContactParNom(String pNom) throws DataBaseException, BusinessLogicException {
        if(contactDao.rechercherContactParNom(pNom).isEmpty()){
            throw new BusinessLogicException("Aucun contact trouvé.");}
        return this.contactDao.rechercherContactParNom(pNom);
    }
    //Rechrche par Prenom
    public List<Contact> rechercherContactParPrenom(String pNom, String pPrenom) throws DataBaseException ,BusinessLogicException{
        if(contactDao.rechercherContactParPrenom(pNom, pPrenom).isEmpty()) {
            throw new BusinessLogicException("Aucun contact trouvé.");}
        return this.contactDao.rechercherContactParPrenom(pNom, pPrenom);}
    // Méthode 6
    public List<Contact> rechercherParTelephone(String numero) throws DataBaseException, BusinessLogicException {
        List<Contact> contacs;
        if(this.contactDao.rechercherParTelephone(numero).isEmpty()){
            throw new BusinessLogicException("Aucun contact trouvé.");};
        contacs =this.contactDao.rechercherParTelephone(numero);
        return contacs;
    }
    // Methode 7

    public void ajouterGroupe(Group pgroupe) throws DataBaseException, BusinessLogicException {
        Group groupe = this.groupeDao.rechercherGroupeParId(pgroupe.getId());
        if (groupe != null) {
            throw new BusinessLogicException("Le groupe existe déjà");
        } else {
            this.groupeDao.CreateGroup(pgroupe.getNom());
        }
    }
    //affichage des groupes
    public List<Group> getgroupes() throws DataBaseException, BusinessLogicException {
        if(this.groupeDao.getContactOrdered1().isEmpty()){
            throw new BusinessLogicException("Aucun groupe trouvé avec ce nom.");}
        return this.groupeDao.getOrderedGroups();}




    //recherche des groupes par nom
    public Group getGroupsParNom(String nom) throws DataBaseException, BusinessLogicException {
        if(this.groupeDao.rechercherGroupeParNom(nom)==null){
            throw new BusinessLogicException("Aucun groupe trouvé avec ce nom.");}
        return this.groupeDao.rechercherGroupeParNom(nom);
    }
       //Methode 8
    public void supprimerUngroupe(String nom ) throws DataBaseException, BusinessLogicException {
        if(this.groupeDao.rechercherGroupeParNom(nom)==null){
            throw new BusinessLogicException("Aucun groupe trouvé avec ce nom.");}
        this.groupeDao.supprimerGroupParNom(nom);
    }

        // Méthode 10
        public List<Contact> rechercherPhonetiquement(String nom) throws DataBaseException, BusinessLogicException {
        if(this.contactDao.rechercherPhonétiqument(nom).isEmpty()){
            throw new BusinessLogicException("Aucun contact trouvé.");}
            return this.contactDao.rechercherPhonétiqument(nom);
        }
        // Methode d'insertion de GRPCON:
    public void insetintoGRPCON(int id1, int id2) throws DataBaseException {
        this.groupeDao.insetintoGRPCON(id1, id2);
    }

    public List<Contact> getContactsByGroupName(String groupName) throws DataBaseException, BusinessLogicException {
        if(groupeDao.getContactsByGroupName(groupName).isEmpty()){
            throw new BusinessLogicException("Aucun contcat trouvé."); }
        return groupeDao.getContactsByGroupName(groupName);
    }
    public List<String> getGroupsByContactName(String ContactName, String ContactPrenom) throws BusinessLogicException,DataBaseException{
        if(groupeDao.getGroupsByContactName(ContactName, ContactPrenom).isEmpty()){
            throw new BusinessLogicException("Aucun groupe trouvé avec ce nom.");}
        return groupeDao.getGroupsByContactName(ContactName, ContactPrenom);
    }





    }









