DROP TABLE IF EXISTS `temp_users`;
CREATE TEMPORARY TABLE temp_users(username varchar(40));
INSERT INTO temp_users VALUES 
('rodo'),('naza'),('nico'),
('maca'),('integrante5'),('integrante6'),
('seba'),('aaa'),('bbb'),
('ccc'),('ddd'),('eee'),
('fff');
 

DROP TABLE IF EXISTS `temp_tourneys`;
CREATE TEMPORARY TABLE temp_tourneys(tourneyname varchar(40));
INSERT INTO temp_tourneys VALUES 
('Xtremers De Nieve'),
('Stronggaming'),
('Mad Heeters'),
('Scarygoons'),('Mad Metroniss'),('Mastercrews'),
('Cuchillas De Acero'),('Lave Raisers'),('Bobcate Blasters'),
('Thrashers Ordenado'),('Doncella Bliss'),('Cometas Cúbicos'),
('Sunnydogs'); 
