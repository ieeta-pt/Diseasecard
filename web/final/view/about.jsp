<%-- About subpage --%>
<%@include file="/final/layout/taglib.jsp" %>
<c:set var="this" value="${actionBean}" />
<s:layout-render name="/final/layout/html.jsp">
<s:layout-component name="title">About Diseasecard</s:layout-component>
<s:layout-component name="custom_scripts">
<script>
$(document).ready(function() {
    $('.mag').click(function() {
        toggleTopButton('mag');
        setTimeout(function() {
            $('#text_search').focus();
        }, 400);
    });
    $('#tabs a').click(function(e) {
        e.preventDefault();
        window.location.hash = e.target.hash;
        $(this).tab('show');
    });
    if (window.location.hash) {
        $('a[href=' + window.location.hash + ']').tab('show');
    }
});
</script>
</s:layout-component>
<s:layout-component name="body">
<div class="navbar navbar-inverse navbar-fixed-top">
    <div class="navbar-header">
        <a class="navbar-link" href="<c:url value="/" />"><img class="bw_logo img-responsive" src="<c:url value="/final/assets/image/logo_bw.png" />" /></a>
    </div>
    <div class="collapse navbar-collapse navbar-left">
        <ul class="nav navbar-nav">
            <li class="active"><a href="#">About Diseasecard</a></li>
        </ul>
    </div>
    <ul class="nav navbar-nav navbar-right top_menu collapse navbar-collapse">
        <li data-placement="bottom" rel="tooltip" title="Jump to Diseasecard rare diseases browsing"><a href="<c:url value="/browse" />" title="Jump to Diseasecard rare diseases browsing"><i class="icon-reorder"></i></a></li>
        <li data-placement="bottom" rel="tooltip" title="Search for rare diseases"><a href="#" class="mag" title="Search for rare diseases" data-active="false" data-toggle="dropdown"><i class="icon-search icon-white"></i></a></li>
    </ul>
</div>
<!-- Top search bar -->
<div id="top" class="col-md-4 pull-right search">
    <div class="input-group">
        <input type="text" class="form-control" id="text_search" class="home_search" placeholder="Search here...">
        <span class="input-group-btn">
            <button class="btn btn-primary" type="button" id="button_search"><i class="icon-search"></i></button>
        </span>
    </div>
</div>
<!-- Main page content -->
<div class="container" id="about">
    <ul class="nav nav-pills" id="tabs">
        <li class="active">
            <a href="#overview" title="Overview">Overview</a>
        </li>
        <li><a href="#faq" title="Frequently Asked Questions">FAQ</a></li>
        <li><a href="#technology" title="Techology">Technology</a></li>
        <li><a href="#api" title="API">API</a></li>
        <li><a href="#publications" title="Publications">Publications</a></li>
        <li><a href="#disclaimer" title="Disclaimer">Disclaimer</a></li>
        <li><a href="#contacts" title="Contacts">Contacts</a></li>
    </ul>
    <div class="tab-content" style="overflow: hidden;">
        <div class="tab-pane active" id="overview">
            <h2>Overview</h2>
            Diseasecard is an information retrieval tool
            for accessing and integrating genetic and medical information
            for health applications. Resorting to this integrated environment, clinicians are able to access and relate diseases data already available in the Internet, scattered along multiple databases.
            Diseasecard was developed by <a href="http://www.ua.pt">University of Aveiro</a> <a href="http://bioinformatics.ua.pt/">Bioinformatics Group</a> in collaboration with the <a href="http://bioserver.ieeta.pt/diseasecard/images/isciii.gif" target="_blank">Institute of Health Carlos III</a> <a href="http://www.isciii.es/htdocs/en/servicios/bioinformatica/bio/bioinformatica_presentacion.jsp" target="_blank">Bio-Computing and Public Health Unit</a>.
            <h3>Feedback</h3>
            Feel free to evaluate and give us feedback on Diseasecard at <a href="http://goo.gl/U0f2Z" title="Diseasecard evaluation form" target="_blank">http://goo.gl/U0f2Z</a>.
            <h3> What's new?</h3>
            The new Diseasecard improves the existing system with extended functionalities for searching and exploring the most relevant rare diseases knowledge.
            <br />
            <h4>Search</h4>
            <div class="row col-md-12">
                <div class="col-md-6">
                    <div class="img-thumbnail">
                        <img class="img-responsive" src="<c:url value="/final/assets/image/about/overview_search.png" />" alt="Home search" rel="tooltip" title="Searching in the homepage" />
                        <div class="caption">
                            <strong>Home search</strong> lets you search for any identifier in Diseasecard or access the powerful full-text search.
                        </div>
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="img-thumbnail">
                        <img class="img-responsive" src="<c:url value="/final/assets/image/about/overview_search_disease.png" />" alt="Quick search" rel="tooltip" title="Searching everywhere with quicksearch" />
                        <div class="caption">
                            <strong>Quick search</strong> is available everywhere within Diseasecard letting you jump right into other rare diseases.
                        </div>
                    </div>
                </div>
            </div>
            <h4>Browse</h4>
            <div class="row col-md-12">
                <div class="img-thumbnail">
                    <img class="img-responsive" src="<c:url value="/final/assets/image/about/overview_browse.png" />" alt="Browse" rel="tooltip" title="Browse all rare diseases" />
                    <div class="caption">
                        <strong>Browsing</strong> displays all rare diseases arranged by their name and sorted by the number of connections in Diseasecard's network.
                    </div>
                </div>
            </div>
            <h4>Explore</h4>
            <div class="col-md-12">
                <div class="col-md-6">
                    <div class="img-thumbnail">
                        <img class="img-responsive" src="<c:url value="/final/assets/image/about/overview_sidebar.png" />" alt="Sidebar" rel="tooltip" title="Sidebar navigation" />
                        <div class="caption">
                            <strong>Sidebar tree</strong> display a list of all the connected resources for the rare disease entry being studied.
                        </div>
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="img-thumbnail">
                        <img class="img-responsive" src="<c:url value="/final/assets/image/about/overview_hypertree.png" />" alt="Hypertree" rel="tooltip" title="Hypertree navigation" />
                        <div class="caption">
                            <strong>Hypertree</strong> is an innovative navigation approach to quickly assess the resource network for a given rare disease.
                        </div>
                    </div>
                </div>
                <br />
                <br />
                <div class="col-md-12">
                    <div class="img-thumbnail">
                        <img class="img-responsive" src="<c:url value="/final/assets/image/about/overview_liveview.png" />" alt="LiveView" rel="tooltip" title="External resources within Diseasecard" />
                        <div class="caption">
                            <strong>LiveView</strong> displays external resources within Diseasecard, letting you keep focused on your research.
                        </div>
                    </div>
                </div>
            </div>
            <h4>API</h4>
            <br />
            In addition to the web portal, Diseasecard also delivers a an API for developers, enabling everyone to include data from Diseasecard's knowledge base in new or existing applications.
            <div class="row col-md-12">
                <div class="col-md-6">
                    <div class="img-thumbnail">
                        <img class="img-responsive" src="<c:url value="/final/assets/image/about/overview_linkeddata.png" />" alt="LinkedData" rel="tooltip" title="LinkedData interfaces" />
                        <div class="caption">
                            <strong>LinkedData</strong> interface gives you direct access to all individuals in Diseasecard's rare diseases semantic network.
                        </div>
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="img-thumbnail">
                        <img class="img-responsive" src="<c:url value="/final/assets/image/about/overview_sparql.png" />" alt="SPARQL" rel="tooltip" title="SPARQL endpoint" />
                        <div class="caption">
                            <strong>SPARQL</strong> endpoint gives you access to Diseasecard's knowledge base with the most powerful query language.
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="tab-pane" id="faq">
            <h2>Frequently Asked Questions</h2>
            <dl>
                <dt>Why can't I find the disease I am looking for?</dt>
                <dd>Diseasecard uses a Solr index to provide an improved search experience. If you cannot find a rare disease, please make sure that: 1) the disease is in fact a rare disease, 2) if the disease name has more than one word, put the name in between '<strong>"</strong>', 3) check the <a href="<c:url value="/browser" />" title="Jump to Diseasecard's browser" rel="tooltip">rare diseases browser</a> to see if the disease is there, and 4) try the advanced full-text search.
                </dd>
                <br />
                <dt>Why doesn't Diseasecard work in my browser?</dt>
                <dd>To deliver the best end-user experience, Diseasecard relies on modern web interface technologies. Therefore, it is recommended that you keep your browser up to date to have the optimal Diseascard navigation experience. <a href="http://browser-update.org/update.html" title="Modern web browser" rel="tooltip" target="_blank">Learn here about updating your browser</a>.</dd>
                <br />
                <dt>I found some incorrect data in Diseasecard, what can I do?</dt>
                <dd>Diseasecard uses an automated data integration engine. Despite our efforts to enhance the quality of the autonomously extracted relationships amongst the multitude of Diseasecard's resources, sometimes these connections do not reflect the latest scientific knowledge. If you happen to find these, please provide us with your feedback. We look forward for your findings to keep improving Diseasecard.</dd>
                <br />
            </dl>
        </div>
        <div class="tab-pane" id="technology">
            <h2>Technology</h2>
            Diseasecard is powered by <a href="http://bioinformatics.ua.pt/coeus/" target="_blank">COEUS</a>, an innovative framework for Semantic Web application development.
            <br /><br />
            <a href="http://bioinformatics.ua.pt/coeus/" title="COEUS: Semantic Web Application Framework" rel="tooltip" target="_blank" class="btn btn-info">Learn more about COEUS</a>
        </div>
        <div class="tab-pane" id="publications">
            <h2>Publications</h2>
            <i class="icon-quote-left"></i> <h4>An innovative portal for rare genetic diseases research: The semantic Diseasecard</h4>
            <p>
                Pedro Lopes, José Luís Oliveira<br />
                Journal of Biomedical Informatics<br />
                <strong><a href="http://dx.doi.org/10.1016/j.jbi.2013.08.006" title="Open An innovative portal for rare genetic diseases research: The semantic Diseasecard" rel="tooltip" target="_blank">DOI: 10.1016/j.jbi.2013.08.006</a></strong>
                <blockquote>
                    Please use this reference when citing Diseasecard.
                </blockquote>
            </p>
            <i class="icon-quote-left"></i> <h4>Integration of Genetic and Medical Information Through a Web Crawler System</h4>
            <p>
                Gaspar Dias, José Luís Oliveira, Francisco-Javier Vicente, Fernando Martín-Sánchez<br />
                Lecture Notes in Computer Science Volume 3745, 2005, pp 78-88<br />
                <strong><a href="http://dx.doi.org/10.1007/11573067_9" title="Open Integration of Genetic and Medical Information Through a Web Crawler System" rel="tooltip" target="_blank">DOI: 10.1007/11573067_9</a></strong>
            </p>
            <i class="icon-quote-left"></i> <h4>DiseaseCard: A Web-Based Tool for the Collaborative Integration of Genetic and Medical Information</h4>
            <p>
                José Luís Oliveira, Gaspar Dias, Ilídio Oliveira, Patrícia Rocha, Isabel Hermosilla, Javier Vicente, Inmaculada Spiteri, Fernando Martin-Sánchez, António Sousa Pereira<br />
                Lecture Notes in Computer Science Volume 3337, 2004, pp 409-417<br />
                <strong><a href="http://dx.doi.org/10.1007/978-3-540-30547-7_41" title="Open DiseaseCard: A Web-Based Tool for the Collaborative Integration of Genetic and Medical Information" rel="tooltip" target="_blank">DOI: 10.1007/978-3-540-30547-7_41</a></strong>
            </p>
        </div>
        <div class="tab-pane" id="api">
            <h2>API</h2>
            Diseasecard's provides a comprehensive API to access all rare diseases data collected in its knowledge base. The API is powered by COEUS' services and includes LinkedData interfaces, a SPARQL endpoint and a set of REST services.
            <p>
                <br />
                <a href="<c:url value="/api/" />" title="Check the API documentation" target="_blank" rel="tooltip" class="btn btn-info">Access API</a>
            </p>
            <h3>SPARQL</h3>
            <div class="row">
                <div class="col-md-4">
                    COEUS enables a SPARQL endpoint by default. You can use it to query Diseasecard's knowledbge and load its data anywhere you want.
                </div>
                <div class="col-md-8">
                    <strong>SPARQL endpoint</strong>
                    <br />
                    <a href="http://bioinformatics.ua.pt/diseasecard/sparql" class="btn disabled btn-inverse">http://bioinformatics.ua.pt/diseasecard/sparql</a>
                    <ul>
                        <li><a href="<c:url value="/api/sparqler/" />" rel="tooltip" title="Launch COEUS' query tester" target="_blank">Query tester</a></li>
                    </ul>
                </div>
            </div>
            <h3>LinkedData</h3>
            <div class="row">
                <div class="col-md-4">
                    All Diseasecard knowledge can be integrated using LinkedData references. You can integrate it programmatically in your applications or you can navigate Diseasecard's network in the browser.
                </div>
                <div class="col-md-8">
                    <strong>Sample LinkedData entries</strong>
                    <ul>
                        <li><a href="http://bioinformatics.ua.pt/diseasecard/resource/omim_104300" target="_blank">OMIM 104300</a></li>
                        <li><a href="http://bioinformatics.ua.pt/diseasecard/resource/hgnc_BRCA2" target="_blank">HGNC BRCA2</a></li>
                    </ul>
                </div>
            </div>
        </div>
        <div class="tab-pane" id="disclaimer">
            <h2>Disclaimer</h2>
            <ol>
                <li><strong>Diseasecard</strong> is an on-going research project, aiming at the creation of a comprehensive rare diseases knowledge base.</li>
                <li>The premise behind <strong>Diseasecard</strong> is the autonomous lightweight integration of data in a single workspace, the <em>card</em>. The connected resources referenced in each rare disease card are external to the project, and maintained by their owners. The <strong>Diseasecard</strong> does not provide, nor maintains a database of disease information. In addition, the application does not change in any way the information retrieved from external sites.</li>
                <li>Collected data are freely available online and remain the intellectual property of the various original data sources. Any questions regarding acceptable use, copying, storage or distribution of individual data items should be directed to those sources. <strong>Diseasecard</strong> claims ownership and copyright on the total compilation of records in this database, and provides open and free access to the full database content for searching, browsing, record copying, and further dissemination on the strict understanding that no records are to be altered in any way (other than trivial format changes) and that <strong>Diseasecard</strong> and the original data sources are acknowledged in any subsequent usage of this information.<ul><li>An additional note must be made regarding OMIM's database. The current OMIM web application does not allow the display of internal pages within external frames. As this would disrupt Diseasecard's LiveView features, a workaround was put in place to ensure the best user experience for Diseasecard's users. Consequently, the OMIM application displayed in Diseasecard's LiveView mode may not reflect the latest version available in the original OMIM page.</li></ul></li>
                <li>Even if this application can be helpful to navigate the web, it does not substitute healthcare professionals. Please note that <strong>Diseasecard</strong> is not validated for clinical use nor did any official body approve it. </li>
                <li><strong>Diseasecard</strong> developers cannot be held responsible for the erroneous use of any information retrieved and displayed by the tool. Whilst all reasonable efforts have been made to ensure that <strong>Diseasecard</strong> and its data are of consistent high quality, we make no warranty, express or implied, as to their completeness, accuracy or utility for any particular purpose. <strong>Diseasecard</strong> software and data may be used by others on the clear understanding that no liability for any damage or negative outcome whatsoever, either direct or indirect, shall rest upon the <strong>Diseasecard</strong> team or its sponsors, or any of their employees or agents. </li>
            </ol>
        </div>
        <div class="tab-pane" id="contacts">
            <h2>Contacts</h2>
            <dl>
                <dt>José Luís Oliveira</dt>
                <dd><i class="icon-envelope-alt"></i> <a href="mailto:jlo@ua.pt">jlo@ua.pt</a></dd>
            </dl>
            <dl>
                <em>Main contributors</em>
                <dt>From ISCIII, Spain</dt>
                <dd>Fernando Martín-Sánchez, Francisco-Javier Vicente</dd>
                <dt>From University of Aveiro</dt>
                <dd>Gaspar Dias, Hugo Pais, Pedro Lopes</dd>
            </dl>
            <h3>Endorsement</h3>
            The development of Diseasecard and <a href="http://bioinformatics.ua.pt/coeus" rel="tooltip" title="COEUS Semantic Web Application Framework" target="_blank">COEUS</a> is being supported by the following institutions and projects.
            <div class="row">
                <div class="col-sm-6 col-md-3">
                    <div class="thumbnail">
                        <a href="http://www.ua.pt/" target="_blank"><img class="img-responsive" src="/diseasecard/final/assets/image/logo/logo_ua.png" /></a>
                        <div class="caption">
                            <strong>University of Aveiro</strong>
                        </div>
                    </div>
                </div>
                <div class="col-sm-6 col-md-3">
                    <div class="thumbnail">
                        <a href="http://www.isciii.es/" target="_blank"><img class="img-responsive" src="/diseasecard/final/assets/image/logo/logo_isciii.png" /></a>
                        <div class="caption">
                            <strong>ISCIII</strong>
                        </div>
                    </div>
                </div>
                <div class="col-sm-6 col-md-3">
                    <div class="thumbnail">
                        <a href="http://infogenmed.ieeta.pt/" target="_blank"><img class="img-responsive" src="/diseasecard/final/assets/image/logo/logo_infogenmed.png" /></a>
                        <div class="caption">
                            <strong>InfoGenMed</strong>
                        </div>
                    </div>
                </div>
                <div class="col-sm-6 col-md-3">
                    <div class="thumbnail">
                        <a href="http://www.infobiomed.org/" target="_blank"><img class="img-responsive" src="/diseasecard/final/assets/image/logo/logo_infobiomed.png" /></a>
                        <div class="caption">
                            <strong>InfoBioMed</strong>
                        </div>
                    </div>
                </div>
                <div class="col-sm-6 col-md-3">
                    <div class="thumbnail">
                        <a href="http://www.gen2phen.org/" target="_blank"><img class="img-responsive" src="/diseasecard/final/assets/image/logo/logo_gen2phen.png" /></a>
                        <div class="caption">
                            <strong>GEN2PHEN</strong>
                        </div>
                    </div>
                </div>
                <div class="col-sm-6 col-md-3">
                    <div class="thumbnail">
                        <a href="http://www.rd-connect.eu/" target="_blank"><img class="img-responsive" src="/diseasecard/final/assets/image/logo/logo_rdconnect.png" /></a>
                        <div class="caption">
                            <strong>RD-Connect</strong>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</s:layout-component>
</s:layout-render>