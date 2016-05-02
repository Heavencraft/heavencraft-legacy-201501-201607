SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";

CREATE TABLE IF NOT EXISTS `users` (
  `uuid` char(36) NOT NULL,
  `name` varchar(16) NOT NULL,
  `pvp` tinyint(1) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

ALTER TABLE `users`
 ADD PRIMARY KEY (`uuid`), ADD UNIQUE KEY `name` (`name`);
