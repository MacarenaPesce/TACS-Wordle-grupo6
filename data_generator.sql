-- todo tomar el ultimo id actual de todas las tablas, para poder crear sin afectar los datos existentes, en caso de ejecutar sin la bd vacia
-- todo poner un random mas performante en generate_registrations para elegir usuarios	https://stackoverflow.com/questions/4329396/mysql-select-10-random-rows-from-600k-rows-fast

-- configurar mysql para que no haga timeout a los 30 segundos de query

use wordle;

-- fecha inicial a partir de la cual cargar puntajes y crear torneos, fecha final hasta la cual crear torneos, cantidad de usuarios, cantidad de torneos, y cantidad de usuarios a registrar por torneo
DROP TABLE IF EXISTS `opciones`;
CREATE TEMPORARY TABLE opciones(dia_pri date, dia_ulti date, limit_users int, limit_tourneys int, cant_registros int);
INSERT INTO opciones VALUES 
(DATE_SUB(curdate(), INTERVAL 1 MONTH), DATE_ADD(curdate(), INTERVAL 1 MONTH), 800, 160, 16);

/*
GENERAR USUARIOS
generar un user por cada uno en la temp_users
username
SELECT md5('aaa')
username@wordle.com
*/

DROP PROCEDURE IF EXISTS `generate_users`;
DELIMITER //
CREATE PROCEDURE generate_users()
BEGIN

	DECLARE cant_users int DEFAULT (select limit_users from opciones);
	
--	IF _username not in (select username from user) THEN
--  END IF;

	  insert into user(username,password,email) SELECT username, md5(username), concat(username,'@wordle.com') from temp_users limit cant_users;

END //
DELIMITER ;

CALL generate_users();


/*
GENERAR PUNCTUACTION
para cada user y para ambos idiomas:
fecha, todos los dias desde hace un año hasta dia actual-1
puntuacion, random entre 1 y 7
*/

DROP FUNCTION IF EXISTS `score`;
CREATE FUNCTION score()
RETURNS INT
NO SQL
RETURN FLOOR(1 + RAND()*7);


DROP PROCEDURE IF EXISTS `generate_scores`;
DELIMITER //
CREATE PROCEDURE generate_scores()
BEGIN

  DECLARE start_date date DEFAULT (select dia_pri from opciones);
  DECLARE end_date date DEFAULT DATE_SUB(curdate(), INTERVAL 1 DAY);
  DECLARE date_counter date DEFAULT start_date;
  
	-- todo generar IQ para cada usuario para que no queden tan parejos los resultados
	-- WARNING no ejecutar dos veces porque esto no tiene unique constraint

	while date_counter < DATE_ADD(end_date, INTERVAL 1 DAY) DO
		insert into punctuation(id_user,date,language,punctuation) SELECT id, date_counter, 'ES', score() from user;
		insert into punctuation(id_user,date,language,punctuation) SELECT id, date_counter, 'EN', score() from user;
		set date_counter=DATE_ADD(date_counter, INTERVAL 1 DAY);
	end while;
	
	set date_counter=start_date;

END //
DELIMITER ;

call generate_scores();


/*
GENERAR TORNEO
fecha aleatoria desde hace un año y hasta dentro de un año. Tomar la menor para inicio y la mayor para fin.
language y type, aleatorio para alguno de ambos
id user, aleatorio de la lista.
nombre, un torneo por cada uno de la temp_tourneys.
*/

DELIMITER //
DROP FUNCTION IF EXISTS `tourdate`;
CREATE FUNCTION tourdate()
RETURNS date
READS SQL DATA
BEGIN
	DECLARE inicio date DEFAULT (select dia_pri from opciones);
	DECLARE fin date DEFAULT (select dia_ulti from opciones);
	
	RETURN inicio + INTERVAL FLOOR(RAND() * DATEDIFF(fin,inicio)) DAY;
END //
DELIMITER ;

-- WARNING no ejecutar dos veces porque el name del tournament no tiene unique constraint
DROP PROCEDURE IF EXISTS `generate_tourneys`;
DELIMITER //
CREATE PROCEDURE generate_tourneys()
BEGIN
  DECLARE done INT DEFAULT FALSE;
  DECLARE tourneyname varchar(60);
  DECLARE fecha_1 date;
  DECLARE fecha_2 date;
  DECLARE random_user bigint;
  DECLARE cant_tourneys int DEFAULT (select limit_tourneys from opciones);
  DECLARE cur CURSOR FOR SELECT * FROM temp_tourneys limit cant_tourneys;
  DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

  OPEN cur;

  read_loop: LOOP
    FETCH cur INTO tourneyname;
    IF done THEN
      LEAVE read_loop;
    END IF;
		
	SET random_user = (select id from user order by rand() limit 1);
	
	SET fecha_1 = tourdate();
	SET fecha_2 = tourdate();	
		
	IF fecha_1 < fecha_2 THEN
		insert into tournament(name,type,language, id_user, start, finish) VALUES (tourneyname, IF(ROUND(RAND()), 'PUBLIC', 'PRIVATE'), IF(ROUND(RAND()), 'ES', 'EN'), random_user, 
																					ADDTIME(CONVERT(fecha_1, DATETIME), '00:00:00'), ADDTIME(CONVERT(fecha_2, DATETIME), '23:59:59.99'));
	ELSE
		insert into tournament(name,type,language, id_user, start, finish) VALUES (tourneyname, IF(ROUND(RAND()), 'PUBLIC', 'PRIVATE'), IF(ROUND(RAND()), 'ES', 'EN'), random_user, 
																					ADDTIME(CONVERT(fecha_2, DATETIME), '00:00:00'), ADDTIME(CONVERT(fecha_1, DATETIME), '23:59:59.99'));
	END IF;
	

	-- se podria cambiar a la siguiente query, pero no se como hacer la comparacion de fechas sin cursor. Igual este procedure no tiene mala performance para solo 1000 torneos.
	-- insert into tournament(name,type,language, id_user, start, finish) SELECT tourneyname, IF(ROUND(RAND()), 'PUBLIC', 'PRIVATE'), IF(ROUND(RAND()), 'ES', 'EN'), random_user, ____, ____ from temp_tourneys;
	
  END LOOP;

  CLOSE cur;

END //
DELIMITER ;

call generate_tourneys();


/*
GENERAR REGISTRATION
para cada torneo, tomar una cantidad aleatoria de usuarios para registrar
days played, hacer que todos hayan jugado todos los dias. Contar desde la fecha de inicio, hasta lo que sea menor. Fecha de fin, o dia actual-1.
last submited score, sale del anterior la ultima fecha jugada. Poner a las 9hs
registered, un dia antes de empezar el torneo, o fecha actual-1, lo que sea menor. Poner a las 9hs
total score, sumar todas las puntuaciones del usuario, desde la fecha de inicio del torneo. Hasta fin o hasta dia actual-1, lo que sea menor. Asegurar por otro medio que no haya duplicados.
*/

DROP FUNCTION IF EXISTS `days_played`;
DELIMITER //
CREATE FUNCTION days_played(tourney_start date, tourney_end date)
RETURNS bigint
NO SQL
BEGIN
	DECLARE end_date date;

	-- si hoy es antes del comienzo
	IF tourney_start >= curdate() THEN
		return 0;
	END IF;
	
	-- si hoy es despues del fin
	IF tourney_end < curdate() THEN
		SET end_date = DATE_ADD(tourney_end, INTERVAL 1 DAY); -- se cuenta el ultimo dia solo al estar finalizado
	ELSE  -- si estoy entre inicio y fin
		SET end_date = curdate();
	END IF;

	RETURN DATEDIFF(end_date, tourney_start);
END //
DELIMITER ;

DROP FUNCTION IF EXISTS `last_submitted`;
DELIMITER //
CREATE FUNCTION last_submitted(tourney_start date, tourney_end date)
RETURNS datetime
NO SQL
BEGIN
	DECLARE end_date date;

	-- si hoy es antes del comienzo
	IF tourney_start >= curdate() THEN
		return null;
	END IF;
	
	-- si hoy es despues del fin
	IF tourney_end < curdate() THEN
		SET end_date = tourney_end;
	ELSE  -- si estoy entre inicio y fin
		SET end_date = DATE_SUB(curdate(), INTERVAL 1 DAY);
	END IF;

	RETURN ADDTIME(CONVERT(end_date, DATETIME), '09:00:00');
END //
DELIMITER ;

DROP FUNCTION IF EXISTS `get_register_date`;
DELIMITER //
CREATE FUNCTION get_register_date(tourney_start date)
RETURNS datetime
NO SQL
BEGIN

	-- si el comienzo es despues de hoy
	IF tourney_start > curdate() THEN
		return current_timestamp();
	END IF;

	-- si el torneo comenzaba hoy o antes
	RETURN ADDTIME(CONVERT(DATE_SUB(tourney_start, INTERVAL 1 DAY), DATETIME), '09:00:00');
END //
DELIMITER ;

DROP FUNCTION IF EXISTS `get_user_score`;
DELIMITER //
CREATE FUNCTION get_user_score(userid bigint, lang char(2), inicio date, fin date)
RETURNS bigint
DETERMINISTIC	-- en el ciclo de vida del procedure se va a repetir esta function si el usuario aparece en varios torneos. No se si el deterministic optimiza algo. No se si va a romper algo si alguien usa la function despues
BEGIN
	-- no existen puntuaciones cargadas para fechas futuras, entonces no se compara fechas como las 3 function anteriores
	RETURN (IFNULL(
				(select sum(punctuation) from punctuation
						where language = lang and id_user = userid and (date between inicio and fin))
			, 0));
END //
DELIMITER ;

DROP PROCEDURE IF EXISTS `generate_registrations`;
DELIMITER //
CREATE PROCEDURE generate_registrations()
BEGIN
  DECLARE done INT DEFAULT FALSE;
  DECLARE id_tourney bigint;
  DECLARE userid bigint;
  DECLARE lang char(2);
  DECLARE inicio date;
  DECLARE fin date;
  DECLARE cant_users int DEFAULT (select cant_registros from opciones);
  DECLARE cur CURSOR FOR SELECT id FROM tournament;
  DECLARE cur2 CURSOR FOR SELECT * FROM torneo_usuario;
  DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;


  -- crear tabla que tenga todos los torneos, y para cada uno, una cantidad random de usuarios distintos
  DROP TABLE IF EXISTS `torneo_usuario`;
  CREATE TEMPORARY TABLE torneo_usuario(tourneyid bigint, userid bigint);
	
  OPEN cur;

  read_loop: LOOP
    FETCH cur INTO id_tourney;
    IF done THEN
      LEAVE read_loop;
    END IF;
	
	INSERT INTO torneo_usuario(tourneyid, userid) SELECT id_tourney, id FROM user order by rand() limit cant_users;
  
  END LOOP;

  CLOSE cur;
  SET done=0;
    
	
  -- iterar la tabla anterior para crear registraciones y sus tablas intermedias
  OPEN cur2;

  read_loop2: LOOP
    FETCH cur2 INTO id_tourney, userid;
    IF done THEN
      LEAVE read_loop2;
    END IF;
	
	SELECT start, finish, language INTO inicio, fin, lang FROM tournament where id = id_tourney;

			-- crear registracion
		insert into registration(id_tournament, id_user, days_played, last_submitted_score, registered, total_score) 
				VALUES (id_tourney, userid, days_played(inicio, fin), last_submitted(inicio, fin), get_register_date(inicio), get_user_score(userid, lang, inicio, fin));		
				
			-- tabla de hibernate
		insert into tournament_registrations(tournament_entity_id, registrations_id) VALUES (id_tourney, last_insert_id());
		
			-- tabla registration_punctuation
			-- mete a la vez todas las puntuaciones que tenga ya cargadas el usuario
		INSERT INTO registration_punctuation select id, last_insert_id() from punctuation
												where language = lang and id_user = userid;
    
  END LOOP;

  CLOSE cur2;

END //
DELIMITER ;

call generate_registrations();



-- cursor doble
/*
  DECLARE cur CURSOR FOR SELECT id FROM tournament;
  DECLARE cur2 CURSOR FOR SELECT * FROM users_to_register;
  DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

  OPEN cur;

  read_loop: LOOP
    FETCH cur INTO id_tourney;
    IF done THEN
      LEAVE read_loop;
    END IF;
	
	-- nivel 1

	OPEN cur2;
	read_loop2: LOOP
		FETCH cur2 INTO userid;
		IF done THEN
			LEAVE read_loop2;
		END IF;
		
		-- nivel 2
  
	END LOOP;
	CLOSE cur2;
    SET done=0;
	  
  END LOOP;

  CLOSE cur;
*/





