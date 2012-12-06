<%@include file="/beta/layout/taglib.jsp" %>
OMIM<br />
<c:forEach var="entry" items="${actionBean.disease.diseaseMap}">
    http://bioinformatics.ua.pt/${path}/services/linkout/omim:${entry.value.id}<br />
</c:forEach>
HGNC<br />
<c:forEach var="locus" items="${actionBean.disease.locus.hgnc}">
    http://bioinformatics.ua.pt/${path}/services/linkout/hgnc:${locus.value.id}<br />
</c:forEach>
GeneCards<br />
<c:forEach var="locus" items="${actionBean.disease.locus.hgnc}">
    http://bioinformatics.ua.pt/${path}/services/linkout/genecards:${locus.value.id}<br />
</c:forEach>
Ensembl<br />
<c:forEach var="entry" items="${actionBean.disease.locus.ensembl}">
    http://bioinformatics.ua.pt/${path}/services/linkout/ensembl:${entry.value.id}<br />
</c:forEach>
Entrez<br />
<c:forEach var="locus" items="${actionBean.disease.locus.entrezgene}">
    http://bioinformatics.ua.pt/${path}/services/linkout/entrez:${locus.value.id}<br />
</c:forEach>
UniProt<br />
<c:forEach var="protein" items="${actionBean.disease.protein.uniprot}">
    http://bioinformatics.ua.pt/${path}/services/linkout/uniprot:${protein.value.id}<br />
</c:forEach>
PDB<br />
<c:forEach var="pdb" items="${actionBean.disease.protein.pdb}">
    http://bioinformatics.ua.pt/${path}/services/linkout/pdb:${pdb.key}<br />
</c:forEach>
PROSITE<br />
<c:forEach var="protein" items="${actionBean.disease.protein.prosite}">
    http://bioinformatics.ua.pt/${path}/services/linkout/interpro:${protein.key}<br />
</c:forEach>
InterPro<br />
<c:forEach var="protein" items="${actionBean.disease.protein.interpro}">
    http://bioinformatics.ua.pt/${path}/services/linkout/interpro:${protein.key}<br />
</c:forEach>
MeSH<br />
<c:forEach var="entry" items="${actionBean.disease.ontology.mesh}">
    http://bioinformatics.ua.pt/${path}/services/linkout/mesh:${entry.value.id}<br />
</c:forEach>
HPO<br />
<c:forEach var="entry" items="${actionBean.disease.ontology.hpo}">
    http://bioinformatics.ua.pt/${path}/services/linkout/hpo:${entry.value.id}<br />
</c:forEach>
PharmGKB<br />
<c:forEach var="entry" items="${actionBean.disease.drug.pharmgkb}">
    http://bioinformatics.ua.pt/${path}/services/linkout/pharmgkb:${entry.value.id}<br />
</c:forEach>
WAVe<br />
<c:forEach var="locus" items="${actionBean.disease.locus.hgnc}">
    http://bioinformatics.ua.pt/${path}/services/linkout/wave:${locus.value.id}<br />
</c:forEach>