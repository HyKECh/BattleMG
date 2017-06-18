USE `battlesmg`;


DELIMITER //
CREATE DEFINER =`root`@`%` PROCEDURE `arcade_air`()
MODIFIES SQL DATA
  BEGIN

    UPDATE times
    SET arcade_air = (SELECT COUNT(*)
                      FROM battles_in_times
                        JOIN battles ON battles_in_times.battle_id = battles.id
                      WHERE gameMode = 'Arcade' AND vehicles = 'Aircraft' AND battles_in_times.time_id = times.id);

  END//
DELIMITER ;

DELIMITER //
CREATE DEFINER =`root`@`%` PROCEDURE `arcade_tank`()
MODIFIES SQL DATA
  BEGIN

    UPDATE times
    SET arcade_tank = (SELECT COUNT(*)
                       FROM battles_in_times
                         JOIN battles ON battles_in_times.battle_id = battles.id
                       WHERE gameMode = 'Arcade' AND vehicles = 'Tank' AND battles_in_times.time_id = times.id);

  END//
DELIMITER ;

DELIMITER //
CREATE DEFINER =`root`@`%` PROCEDURE `arcade_tap`()
MODIFIES SQL DATA
  BEGIN

    UPDATE times
    SET arcade_tap = (SELECT COUNT(*)
                      FROM battles_in_times
                        JOIN battles ON battles_in_times.battle_id = battles.id
                      WHERE
                        gameMode = 'Arcade' AND vehicles = 'TanksAndPlanes' AND battles_in_times.time_id = times.id);

  END//
DELIMITER ;


DELIMITER //
CREATE DEFINER =`root`@`%` PROCEDURE `battles_in_times`()
MODIFIES SQL DATA
  BEGIN

    DROP TABLE IF EXISTS `battles_in_times`;

    CREATE TABLE `battles_in_times` (
      `id`        INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
      `battle_id` INT(11) UNSIGNED NOT NULL DEFAULT '0',
      `time_id`   INT(11) UNSIGNED NOT NULL DEFAULT '0',
      PRIMARY KEY (`id`),
      INDEX `battle_id` (`battle_id`),
      INDEX `time_id` (`time_id`)
    )
      COLLATE = 'utf8_general_ci'
      ENGINE = InnoDB;

    INSERT INTO battles_in_times (battles_in_times.battle_id, battles_in_times.time_id) (SELECT
                                                                                           battles.id,
                                                                                           times.id
                                                                                         FROM battles, times
                                                                                         WHERE
                                                                                           (times.time BETWEEN battles.start AND battles.stop));


  END//
DELIMITER ;

DELIMITER //
CREATE DEFINER =`root`@`%` PROCEDURE `realistic_air`()
MODIFIES SQL DATA
  BEGIN

    UPDATE times
    SET realistic_air = (SELECT COUNT(*)
                         FROM battles_in_times
                           JOIN battles ON battles_in_times.battle_id = battles.id
                         WHERE
                           gameMode = 'Realistic' AND vehicles = 'Aircraft' AND battles_in_times.time_id = times.id);

  END//
DELIMITER ;

DELIMITER //
CREATE DEFINER =`root`@`%` PROCEDURE `realistic_tank`()
MODIFIES SQL DATA
  BEGIN

    UPDATE times
    SET realistic_tank = (SELECT COUNT(*)
                          FROM battles_in_times
                            JOIN battles ON battles_in_times.battle_id = battles.id
                          WHERE gameMode = 'Realistic' AND vehicles = 'Tank' AND battles_in_times.time_id = times.id);

  END//
DELIMITER ;

DELIMITER //
CREATE DEFINER =`root`@`%` PROCEDURE `realistic_tap`()
  BEGIN

    UPDATE times
    SET realistic_tap = (SELECT COUNT(*)
                         FROM battles_in_times
                           JOIN battles ON battles_in_times.battle_id = battles.id
                         WHERE gameMode = 'Realistic' AND vehicles = 'TanksAndPlanes' AND
                               battles_in_times.time_id = times.id);

  END//
DELIMITER ;

DELIMITER //
CREATE DEFINER =`root`@`%` PROCEDURE `run_me`()
MODIFIES SQL DATA
  BEGIN

    DELETE FROM battles
    WHERE players = 0;
    CALL times();
    CALL battles_in_times();
    CALL arcade_air();
    CALL arcade_tank();
    CALL realistic_air();
    CALL realistic_tank();
    CALL arcade_tap();
    CALL realistic_tap();
    CALL total();

  END//
DELIMITER ;

DELIMITER //
CREATE DEFINER =`root`@`%` PROCEDURE `times`()
MODIFIES SQL DATA
  BEGIN

    DROP TABLE IF EXISTS `times`;

    CREATE TABLE IF NOT EXISTS `times` (
      `id`             INT(11) UNSIGNED       NOT NULL AUTO_INCREMENT,
      `arcade_tank`    SMALLINT(11) UNSIGNED  NOT NULL DEFAULT '0',
      `arcade_air`     SMALLINT(11) UNSIGNED  NOT NULL DEFAULT '0',
      `time`           TIMESTAMP              NULL     DEFAULT NULL,
      `realistic_tank` SMALLINT(11) UNSIGNED  NOT NULL DEFAULT '0',
      `realistic_air`  SMALLINT(11) UNSIGNED  NOT NULL DEFAULT '0',
      `arcade_tap`     SMALLINT(11) UNSIGNED  NOT NULL DEFAULT '0',
      `realistic_tap`  SMALLINT(11) UNSIGNED  NOT NULL DEFAULT '0',
      `total`          MEDIUMINT(11) UNSIGNED NOT NULL DEFAULT '0',
      `players`        MEDIUMINT(11) UNSIGNED NOT NULL DEFAULT '0',
      PRIMARY KEY (`id`),
      INDEX `time` (`time`) USING HASH
    )
      COLLATE = 'utf8_general_ci'
      ENGINE = InnoDB;

    INSERT INTO times (times.time) (SELECT DISTINCT battles.start
                                    FROM battles
                                    ORDER BY start);

  END//
DELIMITER ;

DELIMITER //
CREATE DEFINER =`root`@`%` PROCEDURE `total`()
  BEGIN

    UPDATE times
    SET total = arcade_air + arcade_tank + realistic_air + realistic_tank + arcade_tap + realistic_tap;

  END//
DELIMITER ;