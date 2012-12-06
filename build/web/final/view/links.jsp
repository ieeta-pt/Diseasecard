<%@include file="/final/layout/taglib.jsp" %>
<div class="close"></div>
<div class="box">
    <div class="title">Ensembl</div>
    <ul>
        <c:forEach var="locus" items="${actionBean.disease.locus.ensembl}">
            <li><a class="small internal_link" data-omim="${actionBean.disease.omim.id}" target="_self" data-type="entrez" data-id="${locus.value.id}">${locus.value.id}</a></li>
        </c:forEach>
    </ul>
</div>
<div class="box">
    <div class="title">Entrez</div>
    <ul>
        <c:forEach var="entry" items="${actionBean.disease.locus.entrezgene}">
            <li><a class="small internal_link" data-omim="${actionBean.disease.omim.id}" target="_self" data-type="ensembl" data-id="${entry.value.id}">${entry.value.id}</a></li>
        </c:forEach>
    </ul>
</div>
<div class="box">
    <div class="title">HGNC</div>
    <ul>
        <c:forEach var="locus" items="${actionBean.disease.locus.hgnc}">
            <li><a class="small internal_link" data-omim="${actionBean.disease.omim.id}" target="_self" data-type="hgnc" data-id="${locus.value.id}">${locus.value.id}</a></li>
        </c:forEach>
    </ul>
</div>
<div class="box">
    <div class="title">MeSH</div>
    <ul>
        <c:forEach var="entry" items="${actionBean.disease.ontology.mesh}">
            <li><a class="small internal_link" data-omim="${actionBean.disease.omim.id}" target="_self" data-type="mesh" data-id="${entry.value.id}">${entry.value.id}</a></li>
        </c:forEach>
    </ul>
</div>
<div class="box">
    <div class="title">PharmGKB</div>
    <ul>
        <c:forEach var="entry" items="${actionBean.disease.drug.pharmgkb}">
            <li><a class="small internal_link" data-omim="${actionBean.disease.omim.id}" target="_self" data-type="pharmgkb" data-id="${entry.value.id}">${entry.value.id}</a></li>
        </c:forEach>
    </ul>
</div>
<div class="box">
    <div class="title">OMIM References</div>
    <ul>
        <li><a class="small internal_link" data-omim="${actionBean.disease.omim.id}" target="_self" data-type="omimref" data-id="${actionBean.disease.omim.id}">${actionBean.disease.omim.id}</a></li>
    </ul>
</div>
<div class="box">
    <div class="title">UniProt</div>
    <ul>
        <c:forEach var="protein" items="${actionBean.disease.protein.uniprot}">
            <li><a class="small internal_link" data-omim="${actionBean.disease.omim.id}" target="_self" data-type="uniprot" data-id="${protein.key}">${protein.key}</a></li>
        </c:forEach>
    </ul>
</div>
<div class="box">
    <div class="title">WAVe</div>
    <ul>
        <c:forEach var="locus" items="${actionBean.disease.locus.hgnc}">
            <li><a class="small internal_link" data-omim="${actionBean.disease.omim.id}" target="_self" data-type="wave" data-id="${locus.value.id}">${locus.value.id}</a></li>
        </c:forEach>
    </ul>
</div>