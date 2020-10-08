USE coeus;


CREATE TABLE IF NOT EXISTS `Nodes` (
  `hash` bigint(20) NOT NULL DEFAULT '0',
  `lex` longtext CHARACTER SET utf8 COLLATE utf8_bin,
  `lang` varchar(10) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '',
  `datatype` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '',
  `type` int(10) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`hash`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE IF NOT EXISTS `Prefixes` (
  `prefix` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `uri` varchar(500) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`prefix`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE IF NOT EXISTS `Quads` (
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


CREATE TABLE IF NOT EXISTS `Triples` (
  `s` bigint(20) NOT NULL,
  `p` bigint(20) NOT NULL,
  `o` bigint(20) NOT NULL,
  PRIMARY KEY (`s`,`p`,`o`),
  KEY `ObjSubj` (`o`,`s`),
  KEY `PredObj` (`p`,`o`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;