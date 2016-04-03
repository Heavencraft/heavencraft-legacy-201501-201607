SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";

CREATE TABLE `users` (
  `uuid` char(36) NOT NULL,
  `name` varchar(16) NOT NULL,
  `pvp` tinyint(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


ALTER TABLE `users`
 ADD PRIMARY KEY (`id`), ADD UNIQUE KEY `name` (`name`), ADD UNIQUE KEY `uuid` (`uuid`);


ALTER TABLE `users`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;