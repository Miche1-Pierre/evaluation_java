INSERT INTO entreprise (nom) VALUES
                            ("Entreprise1"),
                            ("Entreprise2"),
                            ("Entreprise3");

INSERT INTO convention (entreprise_id, nom, subvention, salarie_maximum) VALUES
                               (1,"Convention1", 100, 10),
                               (2, "Convention2", 150, 15),
                               (3, "Convention3", 200, 20);

-- mot de passe = root --
INSERT INTO utilisateur (entreprise_id, email, password) VALUES
                                                             (null, "a@a.com",  "$2a$10$31nhEmGLow2iIug.qqq6RuG3GXv1fo6wXfojXNswxqYqwR8kUJUEm"),
                                                             (1, "b@b.com",  "$2a$10$31nhEmGLow2iIug.qqq6RuG3GXv1fo6wXfojXNswxqYqwR8kUJUEm"),
                                                             (2, "c@c.com",  "$2a$10$31nhEmGLow2iIug.qqq6RuG3GXv1fo6wXfojXNswxqYqwR8kUJUEm");


INSERT INTO salarie (convention_id, entreprise_id, matricule, code_barre) VALUES
                                                              (1, 2, "111", "12024"),
                                                              (2, 3, "222", "22024"),
                                                              (3, 1, "333", "32024");