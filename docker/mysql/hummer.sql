USE `hummer`;

DROP TABLE IF EXISTS `hummer`;
CREATE TABLE `hummer` (
  `a` tinytext NOT NULL,
  `b` tinytext NOT NULL,
  `rel` int(11) NOT NULL,
  KEY `relation` (`rel`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


DROP TABLE IF EXISTS `rel`;
CREATE TABLE `rel` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `relationship` varchar(30) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=latin1;