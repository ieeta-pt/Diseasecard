<%@include file="/final/layout/taglib.jsp" %>
<s:layout-render name="/final/layout/html.jsp">          
    <s:layout-component name="body"> 
        <div class="report">
            <div class="report_tools">
                <a class="show tooltip" data-tooltip="Show all report links">Show all links</a><a class="hide tooltip" data-tooltip="Hide all report links">Hide all links</a>
            </div>
            <div class="report_box" data-entity="disease">
                <div class="report_box_entity">
                    <div class="report_box_concept">
                        <div class="report_box_concept_title" data-concept="omim">OMIM</div>
                        <div class="report_concept_description">OMIM information provided by <a href="http://www.omim.org/" title="OMIM" target="_blank">OMIM</a>.</div>
                        <div class="report_box_items" id="omim">
                            <c:forEach var="entry" items="${actionBean.disease.diseaseMap}">
                                <div class="report_box_item">
                                    <a class="replace_frame item" data-type="omim" data-id="${entry.value.id}">${entry.value.id} at OMIM</a>
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                </div>
            </div>
            <div class="report_box" data-entity="locus">
                <div class="report_box_entity">
                    <div class="report_box_concept">
                        <div class="report_box_concept_title" data-concept="hgnc">HGNC</div>
                        <div class="report_concept_description">HGNC-symbol information provided by <a href="http://www.genecards.org/" title="GeneCards" target="_blank">GeneCards</a> and <a href="http://www.genenames.org/" title="HGNC" target="_blank">GeneNames</a>.</div>
                        <div class="report_box_items" id="hgnc">
                            <c:forEach var="locus" items="${actionBean.disease.locus.hgnc}">
                                <div class="report_box_item">
                                    <a class="replace_frame item" data-id="${locus.value.id}" data-type="hgnc">${locus.value.id} at genenames</a><a class="item replace_frame" data-id="${locus.value.id}" data-type="genecards">${locus.value.id} at genecards</a>
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                    <div class="report_box_concept">
                        <div class="report_box_concept_title" data-concept="entrezgene">EntrezGene</div>
                        <div class="report_concept_description">Gene information provided by <a href="http://www.ncbi.nlm.nih.gov/gene" title="EntrezGene" target="_blank">EntrezGene</a>.</div>
                        <div class="report_box_items" id="entrezgene">
                            <c:forEach var="locus" items="${actionBean.disease.locus.entrezgene}">
                                <div class="report_box_item"><a class="replace_frame item"data-id="${locus.value.id}" data-type="entrezgene">${locus.value.id} at EntrezGene</a></div>
                            </c:forEach>
                        </div>
                    </div>
                    <div class="report_box_concept">
                        <div class="report_box_concept_title" data-concept="ensembl">Ensembl</div>
                        <div class="report_concept_description">Gene information provided by <a href="http://www.ensembl.org/index.html" title="Ensembl" target="_blank">Ensembl</a>.</div>
                        <div class="report_box_items" id="ensembl">
                            <c:forEach var="entry" items="${actionBean.disease.locus.ensembl}">
                                <div class="report_box_item">
                                    <a class="item replace_frame"data-id="${entru.value.id}" data-type="entrezgene">${entry.value.id} at Ensembl</a>
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                </div>
            </div>
            <div class="report_box" data-entity="protein">
                <div class="report_box_entity">
                    <div class="report_box_concept">
                        <div class="report_box_concept_title" data-concept="uniprot">UniProt</div>
                        <div class="report_concept_description">Protein information provided by <a href="http://www.uniprot.org/" title="UniProt" target="_blank">UniProt</a>.</div>
                        <div class="report_box_items" id="uniprot">
                            <c:forEach var="protein" items="${actionBean.disease.protein.uniprot}">
                                <div class="report_box_item"><a class="item replace_frame" data-id="${protein.value.id}" data-type="uniprot">${protein.key} at UniProt</a></div>
                            </c:forEach>
                        </div>
                    </div>
                    <div class="report_box_concept">
                        <div class="report_box_concept_title" data-concept="pdb">PDB</div>
                        <div class="report_concept_description">Protein structure information provided by <a href="http://www.pdb.org/" title="PDB" target="_blank">PDB</a>.</div>
                        <div class="report_box_items" id="pdb">
                            <c:forEach var="pdb" items="${actionBean.disease.protein.pdb}">
                                <div class="report_box_item"><a class="item replace_frame" data-id="${pdb.key}" data-type="pdb">${pdb.key} at PDB</a></div>
                            </c:forEach>
                        </div>
                    </div>
                    <div class="report_box_concept">
                        <div class="report_box_concept_title" data-concept="prosite">PROSITE</div>
                        <div class="report_concept_description">Protein domain information provided by <a href="http://prosite.expasy.org/" title="PROSITE" target="_blank">PROSITE</a>.</div>
                        <div class="report_box_items" id="prosite">
                            <c:forEach var="protein" items="${actionBean.disease.protein.prosite}">
                                <div class="report_box_item"><a class="item replace_frame" data-id="${protein.key}" data-type="prosite">${protein.value.id} at PROSITE</a></div>
                            </c:forEach>
                        </div>
                    </div>
                    <div class="report_box_concept">
                        <div class="report_box_concept_title" data-concept="interpro">InterPro</div>
                        <div class="report_concept_description">Protein sequence information provided by <a href="http://www.ebi.ac.uk/interpro/" title="InterPro" target="_blank">InterPro</a>.</div>
                        <div class="report_box_items" id="interpro">
                            <c:forEach var="protein" items="${actionBean.disease.protein.interpro}">
                                <div class="report_box_item"><a class="item replace_frame" data-id="${protein.key}" data-type="interpro">${protein.value.id} at InterPro</a></div>
                            </c:forEach>
                        </div>
                    </div>
                </div>
            </div>
            <div class="report_box" data-entity="ontology">
                <div class="report_box_entity">
                    <div class="report_box_concept">
                        <div class="report_box_concept_title" data-concept="mesh">MeSH</div>
                        <div class="report_concept_description">Medical Subject Headings - MeSH - information provided by <a href="http://www.nlm.nih.gov/mesh/" title="MeSH" target="_blank"> NCBI</a>.</div>
                        <div class="report_box_items" id="mesh">
                            <c:forEach var="entry" items="${actionBean.disease.ontology.mesh}">
                                <div class="small report_box_item" value="${entry.key}">
                                    <a class="item replace_frame" data-id="${entry.value.id}" data-type="mesh">${entry.value.id} at MeSH</a>
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                    <div class="report_box_concept">
                        <div class="report_box_concept_title" data-concept="hpo">HPO</div>
                        <div class="report_concept_description">Human Phenotype Ontology information at <a href="http://www.human-phenotype-ontology.org/index.php/hpo_home.html" title="HPO" target="_blank">HPO</a>.</div>
                        <div class="report_box_items" id="hpo">
                            <c:forEach var="entry" items="${actionBean.disease.ontology.hpo}">
                                <div class="small report_box_item" value="${entry.key}">
                                    <a class="item replace_frame" data-id="${entry.value.id}" data-type="hpo">${entry.value.id} at HPO</a>
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                </div>
            </div>
            <div class="report_box">
                <div class="report_box_entity">
                    <div class="report_box_concept">
                        <div class="report_box_concept_title" data-concept="pharmgkb">PharmGKB</div>
                        <div class="report_concept_description">Drug information provided by <a href="http://www.pharmgkb.org/" title="PharmGKB" target="_blank">PharmGKB</a>.</div>
                        <div class="report_box_items" id="pharmgkb">
                            <c:forEach var="entry" items="${actionBean.disease.drug.pharmgkb}">
                                <div class="report_box_item">
                                    <a class="item replace_frame" data-id="${entry.value.id}" data-type="pharmgkb">${entry.value.id} at PharmGKB</a>
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                </div>
            </div>
            <div class="report_box">
                <div class="report_box_entity">
                    <div class="report_box_concept">
                        <div class="report_box_concept_title" data-concept="wave">WAVe</div>
                        <div class="report_concept_description">Variome information provided by <a href="http://bioinformatis.ua.pt/WAVe/" title="WAVe" target="_blank">WAVe</a>.</div>
                        <div class="report_box_items" id="wave">
                            <c:forEach var="locus" items="${actionBean.disease.locus.hgnc}">
                                <div class="report_box_item">
                                    <a class="replace_frame item" data-id="${locus.value.id}" data-type="wave">${locus.value.id} at WAVe</a>
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <script>
            $(function(){
                $('.report_box_concept_title').click(function(){
                    $('#' + $(this).data('concept')).slideToggle(200, function(){
                    });
                });

                $('.hide').click(function () {
                    $('.report_box_items').each(function() {
                        $(this).slideUp();
                    });
                });
                $('.show').click(function () {
                    $('.report_box_items').each(function() {
                        $(this).slideDown();
                    });
                });
        
                $('.replace_frame').live('click', function(){
                    var id = $(this).data('id');
                    var type = $(this).data('type');
                    var pair = type + ':' + id;
                    $('#content').load(path + '/services/frame/' + encodeURIComponent(pair));
                });
            });
        </script>
    </s:layout-component>
</s:layout-render>