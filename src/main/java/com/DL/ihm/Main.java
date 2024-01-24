package com.DL.ihm;

import com.DL.bll.BusinessLogicException;
import com.DL.bll.ContactManager;
import com.DL.bo.Contact;
import com.DL.bo.Group;
import com.DL.data.DBInstaller;
import com.DL.data.DataBaseException;
import org.apache.log4j.Logger;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
public class Main {
    private static final Logger LOGGER = Logger.getLogger(Main.class);

    public Main() {
    }
    public static void printMenu() {
        System.out.println("1-\tCréer un nouveau contact.");
        System.out.println("2-\tAfficher la liste des contacts par ordre alphabétique.");
        System.out.println("3-\tSupprimer un contact. ");
        System.out.println("4-\tModifier un contact.");
        System.out.println("5-\tRechercher un contact par nom.");
        System.out.println("6-\tRechercher un contact par numéro.");
        System.out.println("7-\tCréer un groupe.");
        System.out.println("8-\tAfficher le liste des groupes.");
        System.out.println("9-\tAjouter un contact à un groupe.");
        System.out.println("10-\tAfficher les contacts d'un groupe.");
        System.out.println("11-\tAfficher les groupes d'un contact.");
        System.out.println("12-\tSupprimer un groupe.");
        System.out.println("13-\tRechercher phonétiquement un contact par nom.");
        System.out.println("0-\tSortir.");
    }
    public static void main(String[] args) {
        ContactManager contactManager = new ContactManager();
        try {
            if(!DBInstaller.checkIfTableExists()){
                DBInstaller.createDataBaseTables();
                LOGGER.info("La bases de données sont crées correctement.");
            }else{
            LOGGER.info("La bases de données sont déjà crées.");}
        } catch (Exception var20) {
            System.err.println("Erreur lors de la création de la base de données, voir le fichier log.txt pour plus de détails");
            System.exit(-1);
        }
        Scanner sc = new Scanner(System.in);

    while(true) {
            printMenu();
            System.out.println("Saisir le numéro de votre choix:");
        String userInput = sc.nextLine();
        int choix;
        try {
            choix = Integer.parseInt(userInput);
        } catch (NumberFormatException e) {
            System.out.println("Aucun option de ce type!");
            continue;
        }
        switch (choix) {
            case 0 -> {
                System.out.println("Aurevoir!");
                System.exit(0);
            }
            case 1 -> {
                System.out.println("Saisir le nom: ");
                String nom = sc.nextLine();
                System.out.println("Saisir le prénom: ");
                String prenom = sc.nextLine();

                String telephone1 = "";
                String telephone2 = "";
                boolean telephoneValide = false;

                while (!telephoneValide) {
                    System.out.println("Saisir le téléphone personnel: ");
                    telephone1 = sc.nextLine();
                    System.out.println("Saisir le téléphone professionnel: ");
                    telephone2 = sc.nextLine();

                    if (telephone1.matches("\\d{10}") && telephone2.matches("\\d{10}")) {
                        telephoneValide = true;
                    } else {
                        System.out.println("Au moins l'un des numéros de téléphone est invalide. Veuillez entrer des numéros de téléphone valides (10 chiffres).");
                    }
                }

                System.out.println("Saisir l'adresse: ");
                String adresse = sc.nextLine();

                String email_per = "";
                String email_pro = "";
                boolean emailValide = false;

                while (!emailValide) {
                    System.out.println("Saisir l'email personnel: ");
                    email_per = sc.nextLine();
                    System.out.println("Saisir l'email professionnel: ");
                    email_pro = sc.nextLine();

                    if (email_per.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$") && email_pro.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
                        emailValide = true;
                    } else {
                        System.out.println("Au moins l'un des emails est invalide. Veuillez entrer des adresses email valides.");
                    }
                }

                String genre = "";
                boolean genreValide = false;

                while (!genreValide) {
                    System.out.println("Saisir le genre (male/female/homme/femme): ");
                    genre = sc.nextLine().toUpperCase();

                    if (genre.equals("MALE") || genre.equals("FEMALE") || genre.equals("HOMME") || genre.equals("FEMME")) {
                        genreValide = true;
                    } else {
                        System.out.println("Genre invalide. Veuillez entrer un genre valide.");
                    }
                }


                try {
                    Contact contact = new Contact(nom, prenom, telephone1, telephone2, adresse, email_per, email_pro, genre);
                    contactManager.ajouterContact(contact);
                    System.out.println("Contact ajouté avec succès!");
                } catch (BusinessLogicException | DataBaseException var16) {
                    System.err.println(var16.getMessage());
                }
            }

            case 2 -> {
                try {
                    //on affiche la liste des contacts
                    List<Contact> orderedcontacts = contactManager.getContactOrdered();
                    if (orderedcontacts.isEmpty()) {
                        System.out.println("Aucun contact trouvé");
                    } else {
                        System.out.println("Voici la liste des contacts par ordre alphabétique");
                        System.out.println("\n");
                        System.out.println("Prénom\t\t\t Nom");
                        System.out.println("---------------------------");
                        for (Contact it : orderedcontacts) {
                            System.out.printf("%-15s %-15s%n", it.getPrenom(), it.getNom());
                        }
                        System.out.println("\n");
                    }
                } catch (DataBaseException| BusinessLogicException ex) {
                    //En cas d'une erreur base de données
                    System.err.println(ex.getMessage());
                }
            }
            case 3 -> {
                try {
                    System.out.println("Saisir le nom du contact à supprimer: ");
                    String name = sc.nextLine();
                    System.out.println("Saisir le prénom du contact à supprimer: ");
                    String famillyName = sc.nextLine();
                    List<Contact> matchingContacts = contactManager.rechercherContactParNom(name);
                    if (matchingContacts.isEmpty()) {
                        System.out.println("Aucun contact trouvé avec ce nom.");
                    } else {
                        boolean contactFound = false;
                        for (Contact contact : matchingContacts) {
                            if (contact.getPrenom().equals(famillyName)) {
                                contactManager.supprimerParNomEtPrenom(name, famillyName);
                                contactFound = true;
                                break;
                            }
                        }
                        if (!contactFound) {
                            System.out.println("Le contact avec le nom et prénom spécifiés n'a pas été trouvé.");
                        } else {
                            System.out.println("Contact supprimé avec succès.");
                        }
                    }
                } catch (DataBaseException  | BusinessLogicException e) {
                    System.out.println("Une erreur s'est produite lors de la suppression du contact: " + e.getMessage());
                }
            }
            case 4 -> {
                try {
                    System.out.println("Saisir le nom du contact à modifier: ");
                    String name = sc.nextLine();
                    System.out.println("Saisir le prenom du contact à modifier: ");
                    String prenom = sc.nextLine();
                    List<Contact> matchingContacts = contactManager.rechercherContactParPrenom(name,prenom);
                    if (matchingContacts.isEmpty()) {
                        System.out.println("Aucun contact trouvé avec ce nom et ce prénom.");
                    } else {
                        System.out.println("Saisir nouveau le nom: ");
                        String nom1 = sc.nextLine();
                        System.out.println("Saisir le nouveau prénom: ");
                        String prenom1 = sc.nextLine();

                        String telephone11 = "";
                        String telephone22 = "";
                        boolean telephoneValide = false;

                        while (!telephoneValide) {
                            System.out.println("Saisir le nouveau téléphone personnel: ");
                            telephone11 = sc.nextLine();
                            System.out.println("Saisir le nouveau téléphone professionnel: ");
                            telephone22 = sc.nextLine();

                            if (telephone11.matches("\\d{10}") && telephone22.matches("\\d{10}")) {
                                telephoneValide = true;
                            } else {
                                System.out.println("Au moins l'un des numéros de téléphone est invalide. Veuillez entrer des numéros de téléphone valides (10 chiffres).");
                            }
                        }

                        System.out.println("Saisir le nouveau adresse: ");
                        String adresse1 = sc.nextLine();

                        String email_per1 = "";
                        String email_pro1 = "";
                        boolean emailValide = false;

                        while (!emailValide) {
                            System.out.println("Saisir le nouveau email personnel: ");
                            email_per1 = sc.nextLine();
                            System.out.println("Saisir le nouveau email professionnel: ");
                            email_pro1 = sc.nextLine();

                            if (email_per1.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$") && email_pro1.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
                                emailValide = true;
                            } else {
                                System.out.println("Au moins l'un des emails est invalide. Veuillez entrer des adresses email valides.");
                            }
                        }

                        String genre1 = "";
                        boolean genreValide = false;

                        while (!genreValide) {
                            System.out.println("Saisir le genre (male/female/homme/femme): ");
                            genre1 = sc.nextLine().toUpperCase();

                            if (genre1.equals("MALE") || genre1.equals("FEMALE") || genre1.equals("HOMME") || genre1.equals("FEMME")) {
                                genreValide = true;
                            } else {
                                System.out.println("Genre invalide. Veuillez entrer un genre valide.");
                    }
                        }
                        contactManager.modifierContact(name, prenom,new Contact(nom1, prenom1, telephone11, telephone22, adresse1, email_per1, email_pro1, genre1));
                        System.out.println("Contact modifié avec succès!");
                    }

                } catch (DataBaseException | BusinessLogicException e) {
                    System.out.println("Une erreur s'est produite lors de la suppression du contact: " + e.getMessage());
                }
            }
            case 5 -> {
                try {
                    System.out.println("Saisir le nom du contact: ");
                    String nom2 = sc.nextLine();
                    List<Contact> contact = contactManager.rechercherContactParNom(nom2);
                    if (contact.isEmpty()) {
                        System.out.println("Aucun contact trouvé avec ce nom! ");
                    } else {
                        contact = contactManager.rechercherContactParNom(nom2);
                            for (int i=0; i<contact.size();i++){
                                int k=i+1;
                            System.out.println("Voici les informations de la " + k +" contact avec ce nom: ");
                            System.out.println("------------------------------");
                            System.out.println("Nom:                " + contact.get(i).getNom());
                            System.out.println("------------------------------");
                            System.out.println("Prénom:             " + contact.get(i).getPrenom());
                            System.out.println("------------------------------");
                            System.out.println("Telephone1:         " + contact.get(i).getTelephone1());
                            System.out.println("------------------------------");
                            System.out.println("Telephone2:         " + contact.get(i).getTelephone2());
                            System.out.println("------------------------------");
                            System.out.println("Adresse:            " + contact.get(i).getAdresse());
                            System.out.println("------------------------------");
                            System.out.println("Email personnel:    " + contact.get(i).getEmailPersonnel());
                            System.out.println("------------------------------");
                            System.out.println("Email professionnel: " + contact.get(i).getEmailProfessionnel());
                            System.out.println("------------------------------");
                            System.out.println("Genre:              " + contact.get(i).getGenre());
                                System.out.println("----------------------------");
                        }
                    }
                } catch (DataBaseException | BusinessLogicException ex) {
                    System.err.println(ex.getMessage());
                }
            }case 6 -> {
                try {
                    System.out.println("Saisir le numéro du contact: ");
                    String numero = sc.nextLine();
                    List<Contact> contact = contactManager.rechercherParTelephone(numero);
                    if (contact.isEmpty()) {
                        System.out.println("Aucun contact trouvé avec ce numéro! ");
                    } else {
                        for (int i=0; i<contact.size();i++){
                            int k=i+1;
                        System.out.println("Voici les informations de la "+k+" contact avec ce numéro: ");
                        System.out.println("------------------------------");
                        System.out.println("Nom:                " + contact.get(i).getNom());
                        System.out.println("------------------------------");
                        System.out.println("Prénom:             " + contact.get(i).getPrenom());
                        System.out.println("------------------------------");
                        System.out.println("Telephone1:         " + contact.get(i).getTelephone1());
                        System.out.println("------------------------------");
                        System.out.println("Telephone2:         " + contact.get(i).getTelephone2());
                        System.out.println("------------------------------");
                        System.out.println("Adresse:            " + contact.get(i).getAdresse());
                        System.out.println("------------------------------");
                        System.out.println("Email personnel:    " + contact.get(i).getEmailPersonnel());
                        System.out.println("------------------------------");
                        System.out.println("Email professionnel: " + contact.get(i).getEmailProfessionnel());
                        System.out.println("------------------------------");
                        System.out.println("Genre:              " + contact.get(i).getGenre());
                            System.out.println("------------------------------");
                    }}
                } catch (DataBaseException | BusinessLogicException ex) {
                    System.err.println(ex.getMessage());
                }
            }

            case 7 -> {
                try {
                    System.out.println("Saisir le nom du groupe à créer: ");
                    String nomGrp = sc.nextLine();
                    Group groupeExiste = contactManager.getGroupsParNom(nomGrp);
                    if (groupeExiste != null) {
                        System.out.println("Un groupe avec le même nom existe déjà.");
                    } else {
                        Group groupe = new Group(nomGrp);
                        contactManager.ajouterGroupe(groupe);
                        System.out.println("Groupe ajouté avec succès.");
                    }
                } catch (BusinessLogicException | DataBaseException var16) {
                    System.err.println(var16.getMessage());
                }
            }
            case 8 -> {
                try {
                    System.out.println("La liste des groupes normales: ");
                    System.out.println("---------------------");
                    List<Group> orderedgroupes = contactManager.getgroupes();
                    for (Group it : orderedgroupes) {
                        System.out.println(it.getNom());
                        System.out.println("--------------------");
                    }
                    System.out.println("La liste des groupes de chaque contact selon son nom.: ");
                    List<String> contactGroups=  new ArrayList<>();
                    contactGroups = contactManager.getContactOrdered1();
                    System.out.println("-----------------");
                    for (String it : contactGroups) {
                        System.out.println(it);
                        System.out.println("--------------------");
                    }
                } catch (DataBaseException  | BusinessLogicException ex) {
                    System.err.println(ex.getMessage());
                }
            }
            case 9 -> {
                try {
                    System.out.println("Saisir le nom du contact qu tu veut ajouter à un groupe: ");
                    String name = sc.nextLine();
                    List<Contact> noms = contactManager.rechercherContactParNom(name);
                    System.out.println("Saisir le prénom du contact qu tu veut ajouter à un groupe: ");
                    String prenom = sc.nextLine();
                    List<Contact> prenoms = contactManager.rechercherContactParPrenom(name, prenom);
                    if (noms.isEmpty() || prenoms.isEmpty()) {
                        System.out.println("Aucun contact trouvé avec ce nom et ce prénom!");
                    } else {
                        System.out.println("Saisir le nom du groupe: ");
                        String grpname = sc.nextLine();
                        Group nomgrp = contactManager.getGroupsParNom(grpname);
                        if (nomgrp == null) {
                            System.out.println("Aucun groupe trouvé avec ce nom avec ce nom ");
                        } else {
                            int id_co = prenoms.get(0).getId();
                            int id_grp = nomgrp.getId();
                            contactManager.insetintoGRPCON(id_co, id_grp);
                            System.out.println("Le contact a été ajouté au groupe "+grpname+" avec succès.");
                        }
                    }

                } catch (DataBaseException | BusinessLogicException e) {
                    System.err.println(e.getMessage());
                }
            }
            case 10 -> {
                try {
                    System.out.println("Saisir le nom du groupe: ");
                    String grpname = sc.nextLine();
                    Group nomgrp = contactManager.getGroupsParNom(grpname);
                        List<Contact> noms;
                        noms = contactManager.getContactsByGroupName(grpname);
                            System.out.println("Les noms et les prénoms des contacts de groupe " + grpname + " sont: ");
                            System.out.println("Nom:              Prénom: ");
                            System.out.println("---------------------------");
                            for (Contact i : noms) {
                                System.out.println(i.getNom()+"              "+i.getPrenom());
                                System.out.println("---------------------------");}

                                } catch (BusinessLogicException e) {
                                            System.err.println(e.getMessage());

                                } catch (DataBaseException e) {
                                        System.err.println(e.getMessage());
                                    }
            }
            case 11 -> {
                try {
                    System.out.println("Saisir le nom du contact: ");
                    String conName = sc.nextLine();
                    List<Contact> nomContact;
                     nomContact = contactManager.rechercherContactParNom(conName);
                    System.out.println("Saisir le prenom du contact: ");
                    String conPrenom = sc.nextLine();
                    List<Contact> prenContact;
                    prenContact = contactManager.rechercherContactParPrenom(conName, conPrenom);
                    if (nomContact.isEmpty() || prenContact.isEmpty()) {
                        System.out.println("Aucun contact trouvé avec ce nom ou ce prénom  ");
                    } else {
                        List<String> nomDEGroupes;
                        nomDEGroupes = contactManager.getGroupsByContactName(conName, conPrenom);
                        if (nomDEGroupes.isEmpty()) {
                            System.out.println("Aucun groupe trouvé avec ce nom pour le contact " + conName+" "+conPrenom);
                        } else {
                            System.out.println("Les noms des groupes de contact " + conName + " sont: ");
                            System.out.println("---------------------------");
                            for (String i : nomDEGroupes) {
                                System.out.println(i);
                                System.out.println("---------------------------");}}}
                } catch (DataBaseException | BusinessLogicException e) {
                    System.err.println(e.getMessage());
                }

            }
            case 12 -> {
                try {
                    System.out.println("Saisir le nom du groupe à supprimer: ");
                    String grpname = sc.nextLine();
                    Group nomgrp = contactManager.getGroupsParNom(grpname);
                    if (nomgrp == null) {
                        System.out.println("Aucun groupe trouvé avec ce nom avec ce nom! ");
                    } else {
                        contactManager.supprimerUngroupe(grpname);
                        System.out.println("Group supprimé avec succès");
                    }
                } catch (DataBaseException | BusinessLogicException e) {
                    System.err.println(e.getMessage());
                }
            }
            case 13 -> {
                try {
                    System.out.println("Saisir le nom du contact: ");
                    String name = sc.nextLine();
                    List<Contact> matchingContacts = contactManager.rechercherContactParNom(name);
                    if (matchingContacts.isEmpty()) {
                        System.out.println("Aucun contact trouvé avec ce nom.");
                    } else {
                        List<Contact> contacts = contactManager.rechercherPhonetiquement(name);
                        if (contacts.isEmpty()) {
                            System.out.println("Aucun nom du contact est similaire phonétiquement à " + name + "!");
                        } else {
                            System.out.println("Voici les noms qui sont phonétiquement similaires à ce nom ");
                            for (Contact it : contacts) {
                                System.out.println(it.getNom());
                            }
                        }
                    }
                } catch (DataBaseException | BusinessLogicException ex) {
                    System.err.println(ex.getMessage());
                }
            }


        }





            }}}



