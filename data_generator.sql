-- todo probar un random mas performante en generate_registrations para elegir usuarios	https://stackoverflow.com/questions/4329396/mysql-select-10-random-rows-from-600k-rows-fast

-- configurar el cliente workbench mysql para que no haga timeout a los 30 segundos de query

use wordle;

-- fecha inicial a partir de la cual cargar puntajes (para usuarios nuevos) y crear torneos, fecha final hasta la cual crear torneos, cantidad de usuarios a crear, cantidad de torneos a crear, cantidad de usuarios a registrar por torneo,
	-- y: boolean old_users si registrar o no usuarios viejos (existentes de antes de esta query) en torneos (solo readys), boolean old_tourneys si volver a generar o no mas registraciones para los torneos viejos (solo en ready)
DROP TABLE IF EXISTS `opciones`;
CREATE TEMPORARY TABLE opciones(dia_pri date, dia_ulti date, limit_users int, limit_tourneys int, cant_registros int, old_users boolean, old_tourneys boolean);
INSERT INTO opciones VALUES 
(DATE_SUB(curdate(), INTERVAL 1 MONTH), DATE_ADD(curdate(), INTERVAL 1 MONTH), 160, 160, 16, true, true);

-- tabla para no afectar datos existentes, en caso de la bd no estar vacia
DROP TABLE IF EXISTS `offset`;
CREATE TEMPORARY TABLE offset(user int, tourney int);

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
	
	insert into offset(user) select id from user 
	order by id desc limit 1;

	  insert into user(username,password,email) SELECT username, md5(username), concat(username,'@wordle.com') from temp_users 
	  where username not in (select username from user)
	  limit cant_users;


END //
DELIMITER ;



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
  DECLARE last_id int DEFAULT (IFNULL((select user from offset where user is not null), 0));

	-- todo generar IQ para cada usuario para que no queden tan parejos los resultados

	while date_counter < DATE_ADD(end_date, INTERVAL 1 DAY) DO
		insert into punctuation(id_user,date,language,punctuation) SELECT id, date_counter, 'ES', score() from user where id > last_id;
		insert into punctuation(id_user,date,language,punctuation) SELECT id, date_counter, 'EN', score() from user where id > last_id;
		set date_counter=DATE_ADD(date_counter, INTERVAL 1 DAY);
	end while;
	
	set date_counter=start_date;

END //
DELIMITER ;



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

DROP TABLE IF EXISTS `esquivo_un_bug`;
CREATE TEMPORARY TABLE esquivo_un_bug(nombre varchar(80));
INSERT INTO esquivo_un_bug SELECT * FROM temp_tourneys 
		 where tourneyname not in (select name from tournament);


DROP PROCEDURE IF EXISTS `generate_tourneys`;
DELIMITER //
CREATE PROCEDURE generate_tourneys()
BEGIN

  DECLARE cant_tourneys int DEFAULT (select limit_tourneys from opciones);
  DECLARE old_users boolean DEFAULT (select old_users from opciones);
  DECLARE done INT DEFAULT FALSE;
  DECLARE tourneyname varchar(60);
  DECLARE fecha_1 date;
  DECLARE fecha_2 date;
  DECLARE random_user bigint;
  DECLARE cur CURSOR FOR SELECT * FROM esquivo_un_bug limit cant_tourneys;
  DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

	  insert into offset(tourney) select id from tournament 
	  order by id desc limit 1;

  OPEN cur;
  read_loop: LOOP
    FETCH cur INTO tourneyname;
    IF done THEN
      LEAVE read_loop;
    END IF;
	
	SET fecha_1 = tourdate();
	SET fecha_2 = tourdate();	
	
	-- seleccionar un usuario random para ser el creador del torneo. Solo considerar old_users si esta activada la opcion, y si es un torneo en ready
	IF (old_users and curdate() < fecha_1 and curdate() < fecha_2) THEN
		SET random_user = (select id from user order by rand() limit 1);
	ELSE
		SET random_user = (select id from user
						 where id > (IFNULL((select user from offset where user is not null), 0)) --  linea opcional para no registrar usuarios viejos en torneos nuevos
						order by rand() limit 1);
	END IF;

	-- insertar el nuevo torneo
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
  DECLARE start_tourney date;
  DECLARE userid bigint;
  DECLARE lang char(2);
  DECLARE inicio date;
  DECLARE fin date;
  DECLARE cant_users int DEFAULT (select cant_registros from opciones);
  DECLARE old_users boolean DEFAULT (select old_users from opciones);
  DECLARE old_tourneys boolean DEFAULT (select old_tourneys from opciones);
  DECLARE cur CURSOR FOR SELECT id, start FROM tournament where id > (IFNULL((select tourney from offset where tourney is not null), 0));
  DECLARE cur_old CURSOR FOR SELECT id FROM tournament where id <= (IFNULL((select tourney from offset where tourney is not null), 0)) and curdate() < start;
  DECLARE cur2 CURSOR FOR SELECT * FROM torneo_usuario;
  DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;


  -- crear tabla que tenga todos los torneos, y para cada uno, una cantidad cant_users de usuarios random
  DROP TABLE IF EXISTS `torneo_usuario`;
  CREATE TEMPORARY TABLE torneo_usuario(tourneyid bigint, userid bigint);
	
  OPEN cur;
  read_loop: LOOP
    FETCH cur INTO id_tourney, start_tourney;
    IF done THEN
      LEAVE read_loop;
    END IF;
	
	-- insertar en el torneo, cant_users de usuarios random. Solo considerar old_users si esta activada la opcion, y si es un torneo en ready
	-- se podria comparar una sola vez por old_users sacandolo afuera del cursor, pero duplicando otro cursor en el else
	IF (old_users and curdate() < start_tourney) THEN
		INSERT INTO torneo_usuario(tourneyid, userid) SELECT id_tourney, id FROM user
		order by rand() limit cant_users;
	ELSE
		INSERT INTO torneo_usuario(tourneyid, userid) SELECT id_tourney, id FROM user
		where id > (IFNULL((select user from offset where user is not null), 0)) -- linea opcional para no registrar usuarios viejos en torneos nuevos
		order by rand() limit cant_users;
	END IF;
  
  END LOOP;

  CLOSE cur;
  SET done=0;

  -- repetir lo mismo pero para torneos viejos (con registraciones anteriores existentes) en ready
  if old_tourneys THEN
	
	  OPEN cur_old;
	  read_loop: LOOP
		FETCH cur_old INTO id_tourney;
		IF done THEN
		  LEAVE read_loop;
		END IF;
		
		IF (old_users) THEN
			INSERT INTO torneo_usuario(tourneyid, userid) SELECT id_tourney, id FROM user
			where id not in (select id_user from registration where id_tournament = id_tourney)
			order by rand() limit cant_users;
		ELSE
			INSERT INTO torneo_usuario(tourneyid, userid) SELECT id_tourney, id FROM user
			where id not in (select id_user from registration where id_tournament = id_tourney)
			and id > (IFNULL((select user from offset where user is not null), 0)) -- linea opcional para no registrar usuarios viejos en torneos nuevos
			order by rand() limit cant_users;
		END IF;
	  
	  END LOOP;

	  CLOSE cur_old;
	  SET done=0;

  END IF;
	
	
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



START TRANSACTION;
call generate_users();
call generate_scores();
call generate_tourneys();
call generate_registrations();
COMMIT;

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





