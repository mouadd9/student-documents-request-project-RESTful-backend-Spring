INSERT INTO Etudiant (nom, email, numApogee, cin, dateNaissance, lieuNaissance, nationalite, filiere, niveau, anneeUniversitaire) 
VALUES
('ELKIHAL YOUNESS', 'youness.elkihal@etu.uae.ac.ma', 123456, 'AB123456', '2002-12-10', 'Laayoune', 'Marocaine', 'Genie informatique', 'Cycle d''ingénieurie', '2024-2025'),
('RAISSOUNI ABDELLAH', 'raissouni.abdellah@etu.uae.ac.ma', 123457, 'CD234567', '2002-06-15', 'Tetouan', 'Marocaine', 'Genie informatique', 'Cycle d''ingénieurie', '2024-2025'),
('DEMBELE MOUSSA', 'moussa.dembele@etu.uae.ac.ma', 123458, 'EF345678', '2002-03-20', 'Tanger', 'Marocaine', 'Genie informatique', 'Cycle d''ingénieurie', '2024-2025'),
('BENCAID MOUAD', 'bencaid.mouad@etu.uae.ac.ma', 123459, 'GH456789', '2002-09-25', 'Casablanca', 'Marocaine', 'Genie informatique', 'Cycle d''ingénieurie', '2024-2025');

INSERT INTO Note (module, valeur, etudiant_id) VALUES
('Vision Artificielle', 16.5, 1),
('Modélisation et Programmation Objet', 16.0, 1),
('Réseaux informatique II', 14.5, 1),
('Méthodologies et Génie Logiciel', 15.0, 1),
('Langues et Communication II', 13.5, 1),
('Administration et Optimisation des BD', 13.0, 1);

INSERT INTO Note (module, valeur, etudiant_id) VALUES
('Vision Artificielle', 15.5, 2),
('Modélisation et Programmation Objet', 14.0, 2),
('Réseaux informatique II', 15.5, 2),
('Méthodologies et Génie Logiciel', 16.0, 2),
('Langues et Communication II', 14.5, 2),
('Administration et Optimisation des BD', 15.0, 2);

INSERT INTO Note (module, valeur, etudiant_id) VALUES
('Vision Artificielle', 14.5, 3),
('Modélisation et Programmation Objet', 15.0, 3),
('Réseaux informatique II', 13.5, 3),
('Méthodologies et Génie Logiciel', 14.0, 3),
('Langues et Communication II', 15.5, 3),
('Administration et Optimisation des BD', 14.5, 3);

INSERT INTO Note (module, valeur, etudiant_id) VALUES
('Vision Artificielle', 15.0, 4),
('Modélisation et Programmation Objet', 14.5, 4),
('Réseaux informatique II', 16.0, 4),
('Méthodologies et Génie Logiciel', 15.5, 4),
('Langues et Communication II', 14.0, 4),
('Administration et Optimisation des BD', 15.5, 4);