USE diseasecard;


DROP TABLE IF EXISTS `Activity`;
CREATE TABLE `Activity` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `query` varchar(300) DEFAULT NULL,
  `action` varchar(20) DEFAULT NULL,
  `url` varchar(1000) DEFAULT NULL,
  `useragent` varchar(400) DEFAULT NULL,
  `ip` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3679 DEFAULT CHARSET=latin1;


DROP TABLE IF EXISTS `DiseaseIndex`;
CREATE TABLE `DiseaseIndex` (
  `omim` int(11) DEFAULT NULL,
  `info` varchar(200) DEFAULT NULL,
  `type` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


DROP TABLE IF EXISTS `Diseases`;
CREATE TABLE `Diseases` (
  `omim` int(11) NOT NULL,
  `c` int(11) NOT NULL,
  `name` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`omim`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


DROP TABLE IF EXISTS `Nodes`;
CREATE TABLE `Nodes` (
  `hash` bigint(20) NOT NULL DEFAULT '0',
  `lex` longtext CHARACTER SET utf8 COLLATE utf8_bin,
  `lang` varchar(10) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '',
  `datatype` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '',
  `type` int(10) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`hash`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `Prefixes`;
CREATE TABLE `Prefixes` (
  `prefix` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `uri` varchar(500) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`prefix`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `Quads`;
CREATE TABLE `Quads` (
  `g` bigint(20) NOT NULL,
  `s` bigint(20) NOT NULL,
  `p` bigint(20) NOT NULL,
  `o` bigint(20) NOT NULL,
  PRIMARY KEY (`g`,`s`,`p`,`o`),
  KEY `SubjPredObj` (`s`,`p`,`o`),
  KEY `PredObjSubj` (`p`,`o`,`s`),
  KEY `ObjSubjPred` (`o`,`s`,`p`),
  KEY `GraPredObj` (`g`,`p`,`o`),
  KEY `GraObjSubj` (`g`,`o`,`s`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


DROP TABLE IF EXISTS `Triples`;
CREATE TABLE `Triples` (
  `s` bigint(20) NOT NULL,
  `p` bigint(20) NOT NULL,
  `o` bigint(20) NOT NULL,
  PRIMARY KEY (`s`,`p`,`o`),
  KEY `ObjSubj` (`o`,`s`),
  KEY `PredObj` (`p`,`o`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;