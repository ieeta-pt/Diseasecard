{
    "config": {
        "name": "Diseasecard",
        "description": "DiseaseCard v4.5",
        "solr":"http://container_solr:8983/solr/",
        "index":"jdbc:mysql://container_mysql:3306/diseasecard?user=diseasecard&password=diseasecard",
        "redis": {"host":"container_redis", "port":6379}
    }
}