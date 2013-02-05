<%@include file="/final/layout/taglib.jsp" %>
<c:set var="this" value="${actionBean}" />
<s:layout-render name="/final/layout/html.jsp">
    <s:layout-component name="title">About Diseasecard</s:layout-component>
    <s:layout-component name="custom_scripts">
        <script>    
            $(document).ready(function(){                
                $('.mag').click(function() {
                    toggleTopButton('mag');
                    setTimeout(function(){                            
                        $('#text_search').focus();
                    }, 400);                    
                });   
                
                $('#tabs a').click(function (e) {
                    e.preventDefault();                    
                    window.location.hash = e.target.hash;
                    $(this).tab('show');
                });
                
                if(window.location.hash) {           
                    $('a[href=' + window.location.hash + ']').tab('show');
                }
            });            
        </script>
    </s:layout-component>
    <s:layout-component name="body">
        <div class="navbar navbar-inverse navbar-fixed-top">
            <div class="navbar-inner">
                <div class="container pull-left">
                    <a class="brand" href="<c:url value="/" />"><img src="<c:url value="/final/assets/image/logo_bw.png" />" height="18" /></a>
                    <div class="nav-collapse collapse">
                        <ul class="nav">
                            <li class="active"><a href="#">About Diseasecard</a></li>
                        </ul>
                    </div><!--/.nav-collapse -->
                </div>
                <ul class="nav pull-right">                    
                    <li data-placement="bottom" rel="tooltip" title="Jump to Diseasecard rare diseases browsing"><a href="<c:url value="/browse" />" title="Jump to Diseasecard rare diseases browsing"><i class="icon-reorder"></i></a></li>
                    <li data-placement="bottom" rel="tooltip" title="Search for rare diseases"><a href="#" class="mag" title="Search for rare diseases" data-active="false" data-toggle="dropdown"><i class="icon-search icon-white"></i></a></li>
                </ul>
            </div>
        </div>
        <!-- Top search bar -->
        <div id="top">
            <div class="search menu">
                <form>
                    <div class="input-append pull-right">
                        <input class="input-xlarge" id="text_search" class="home_search" placeholder="Search here..."  type="text">
                        <button class="btn btn-inverse" type="button"  id="button_search">GO!</button>
                    </div>
                </form>
            </div>  
        </div>
        <!-- Main page content -->
        <div class="container" id="about">
            <div class="row-fluid">
                <div class="span12">
                    <ul class="nav nav-pills" id="tabs">
                        <li class="active">
                            <a href="#overview" title="Overview">Overview</a>
                        </li>
                        <li><a href="#faq" title="Frequently Asked Questions">FAQ</a></li>
                        <li><a href="#technology" title="Techology">Technology</a></li>
                        <li><a href="#api" title="API">API</a></li>
                        <li><a href="#publications" title="Publications">Publications</a></li>
                        <li><a href="#disclaimer" title="Disclaimer">Disclaimer</a></li>
                        <li><a href="#support" title="Support">Support</a></li>
                    </ul>
                    <div class="tab-content">
                        <div class="tab-pane active" id="overview"><h2>Overview</h2>
                            Diseasecard is an information retrieval tool 
                            for accessing and integrating genetic and medical information 
                            for health applications. Resorting to this integrated environment, clinicians are able to access and relate diseases data already available in the Internet, scattered along multiple databases. 
                            Diseasecard was developed by <a href="http://www.ua.pt">University of Aveiro</a> <a href="http://bioinformatics.ua.pt/">Bioinformatics Group</a> in collaboration with the <a href="http://bioserver.ieeta.pt/diseasecard/images/isciii.gif" target="_blank">Institute of Health Carlos III</a> <a href="http://www.isciii.es/htdocs/en/servicios/bioinformatica/bio/bioinformatica_presentacion.jsp" target="_blank">Bio-Computing and Public Health Unit</a>.
                            <h2>Feedback</h2>
                            Feel free to evaluate and give us feedback on Diseasecard at <a href="http://goo.gl/U0f2Z" title="Diseasecard evaluation form" target="_blank">http://goo.gl/U0f2Z</a>
                        </div>
                        <div class="tab-pane" id="faq"><h2>Frequently Asked Questions</h2></div>
                        <div class="tab-pane" id="technology">
                            <h2>Technology</h2>
                            Diseasecard is powered by <a href="http://bioinformatics.ua.pt/coeus/" target="_blank">COEUS</a>, an innovative framework for Semantic Web application development.
                            <br /><br />
                            <a ref="http://bioinformatics.ua.pt/coeus/" target="_blank" class="btn btn-primary">Learn more about COEUS</a>
                        </div>
                        <div class="tab-pane" id="publications"><h2>Publications</h2></div>
                        <div class="tab-pane" id="api">
                            <h2>API</h2>
                            <h3>Solr</h3>
                            <h3>SPARQL</h3>
                            <div class="row-fluid">
                                <div class="span4">
                                    COEUS enables a SPARQL endpoint by default. You can use it to query Diseasecard's knowledbge and load its data anywhere you want.
                                </div>
                                <div class="span8">
                                    <strong>SPARQL endpoint</strong>
                                    <br />
                                    <a href="http://bioinformatics.ua.pt/diseasecard/sparql" class="btn disabled btn-inverse">http://bioinformatics.ua.pt/diseasecard/sparql</a>
                                </div>
                            </div>
                            <h3>LinkedData</h3>
                            <div class="row-fluid">
                                <div class="span4">
                                    All Diseasecard knowledge can be integrated using LinkedData references. You can integrate it programmatically in your applications or you can navigate Diseasecard's network in the browser.
                                </div>
                                <div class="span8">
                                    <strong>Sample LinkedData entries</strong>
                                    <ul>
                                        <li><a href="http://bioinformatics.ua.pt/diseasecard/resource/omim_104300" target="_blank">OMIM 104300</a></li>
                                        <li><a href="http://bioinformatics.ua.pt/diseasecard/resource/hgnc_BRCA2" target="_blank">HGNC BRCA2</a></li>
                                    </ul>
                                </div>
                            </div>
                        </div>
                        <div class="tab-pane" id="disclaimer"><h2>Disclaimer</h2>

                            <ol>
                                <li>DiseaseCard Web Application is an on-going research project, 
                                    aiming at building an information retrieval tool to help users to 
                                    navigate biomedical web resources.
                                </li>
                                <li>The object of Diseasecard is a helper tool that accesses existing 
                                    resources. The information resources referenced in each disease card are 
                                    external to the project, and maintained by their owners. The Diseasecard does not 
                                    provide, nor maintains a database of disease information. In addition, the 
                                    application does not change in any way the information retrieved from 
                                    external sites.</li>
                                <li>The card's information is retrieved from the following databases:
                                    <ul><li><a href="http://www.orpha.net/">Orphanet</a>,<a href="http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?db=OMIM">OMIM</a>, 
                                            <a href="http://www.clinicaltrials.com/">Clitinal Trials</a>, <a href="http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?db=gene">Entrez-Gene</a>, 
                                            <a href="http://www.pharmgkb.org/">Pharmgkb</a>, <a href="http://www.genome.jp/kegg/pathway.html">KEGG PATHWAY</a>, 
                                            <a href="http://www.genedb.org/amigo/perl/go.cgi">AmiGO</a>,
                                            <a href="http://www.gene.ucl.ac.uk/nomenclature/">HGNC</a>, <a href="http://bioinformatics.weizmann.ac.il/cards/">Genecards</a>, 
                                            <a href="http://www.expasy.org/">Expasy</a> and <a href="http://www.rcsb.org/pdb/">PDB</a>.
                                        </li></ul>
                                </li>

                                <li>Even if this application can be helpful to navigate the web, it does 
                                    not substitute healthcare professionals. Please note that Diseasecard is 
                                    currently being developed and it was not validated for clinical use nor did 
                                    any official body approve it. </li>
                                <li>Diseasecard developers cannot be held responsible for the erroneous use of 
                                    any information retrieved and displayed by the tool. </li>
                            </ol>
                        </div>
                        <div class="tab-pane" id="support">
                            <h2>Support</h2>
                            <h3>Contact</h3>
                            <h4>Team</h4>
                            <dl>
                                <dt>Pedro Lopes</dt>
                                <dd><a href="mailto:pedrolopes@ua.pt">pedrolopes@ua.pt</a></dd>
                                <dt>José Luís Oliveira</dt>
                                <dd><a href="mailto:jlo@ua.pt">jlo@ua.pt</a></dd>
                            </dl>
                            <br />
                            <strong>Previous members</strong><br />
                            Gaspar Dias, Hugo Pais, Carlos Davide, Luís Santos, David Campos, Javier Vicente, Fernando Martin-Sanchez
                            <h3>Endorsement</h3>
                            The development of Diseasecard and <a href="http://bioinformatics.ua.pt/coeus" target="_blank">COEUS</a> is supported by the following institutions and projects.
                            <br />
                            <a href="http://www.ieeta.pt/" target="_blank"><img src="/diseasecard/final/assets/image/logo/logo_ieeta.png" /></a><br />
                            <a href="http://www.ua.pt/" target="_blank"><img src="/diseasecard/final/assets/image/logo/logo_ua.png" /></a><br />
                            <a href="http://www.gen2phen.org/" target="_blank"><img src="/diseasecard/final/assets/image/logo/logo_gen2phen.png" /></a><br />
                            <a href="http://www.isciii.es/" target="_blank"><img src="/diseasecard/final/assets/image/logo/logo_isciii.gif" /></a><br />
                            <a href="http://www.infobiomed.org/" target="_blank"><img src="/diseasecard/final/assets/image/logo/logo_infobiomed.gif" /></a><br />
                            <a href="http://infogenmed.ieeta.pt/" target="_blank"><img src="/diseasecard/final/assets/image/logo/logo_infogenmed.gif" /></a><br />
                        </div>
                    </div>
                </div>
            </div>
        </div> 
    </s:layout-component>
</s:layout-render>
