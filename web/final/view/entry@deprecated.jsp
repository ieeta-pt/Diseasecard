<%@include file="/final/layout/taglib.jsp" %>
<s:layout-render name="/final/layout/html.jsp">
    <s:layout-component name="title">${actionBean.disease.id} - ${actionBean.disease.omim.description} - Diseasecard</s:layout-component>
    <s:layout-component name="custom_scripts">
        <script>var key = '${actionBean.key}';</script>
        <script src="<c:url value="/final/assets/script/jit.js" />"></script>
        <script src="<c:url value="/final/assets/script/treeview.js" />"></script>
        <script src="<c:url value="/final/assets/script/dc4.disease.js" />"></script>
    </s:layout-component>
    <s:layout-component name="body">
        <c:choose>
            <c:when test="${actionBean.disease.omim.label != ''}">
                <div class="navbar navbar-inverse navbar-fixed-top">
                    <div class="navbar-inner">
                        <div class="container pull-left">
                            <a class="navbar-brand" href="<c:url value="/" />"><img src="<c:url value="/final/assets/image/logo_bw.png" />" height="18" /></a>
                            <div class="navbar-collapse collapse">
                                <ul class="nav">
                                    <li class="active">
                                        <a href="#" id="key" class="dropdown-toggle" data-toggle="dropdown"><i class="icon-chevron-down"></i> <c:if test="${actionBean.disease.omim.phenotype}" >#${actionBean.disease.omim.id}</c:if><c:if test="${actionBean.disease.omim.phenotype == false}" >${actionBean.disease.omim.id}</c:if></a>
                                            <ul class="dropdown-menu synonyms" role="menu" aria-labelledby="dropdownMenu" >
                                            <c:forEach var="entry" items="${actionBean.disease.diseaseMap}">
                                                <li><a class="small synonym" data-omim="${entry.key}" href="${path}/entry/${entry.key}"></a></li>
                                                </c:forEach>
                                        </ul>
                                    </li>
                                </ul>
                                <ul class="nav">
                                    <li>
                                        <a href="#" id="omim" data-id="${actionBean.disease.omim.id}" rel="popover" data-original-title="Synonyms" data-animate="false"> ${actionBean.disease.omim.description}</a>
                                    </li>
                                </ul>

                            </div><!--/.navbar-collapse -->
                        </div>
                        <ul class="nav pull-right">
                            <li><a href="<c:url value="/about" />" id="tour_about" rel="tooltip" data-placement="bottom" title="Jump to Diseasecard about section"><i class="icon-book"></i></a></li>
                            <li data-placement="bottom" rel="tooltip" title="Jump to Diseasecard rare diseases browsing"><a href="<c:url value="/browse" />" title="Jump to Diseasecard rare diseases browsing"><i class="icon-reorder"></i></a></li>
                            <li><a href="#" class="mag" data-active="false" data-toggle="dropdown" id="nav_search" rel="tooltip" title="Search for rare diseases" data-placement="bottom"><i class="icon-search"></i></a></li>
                        </ul>
                    </div>
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
            <div id="wrap">
                <div id="diseasebar">
                    <div id="sidebar_menu">
                        <div class="btn-group" id="dc4_tree_control">
                            <a data-placement="bottom" data-container="body" rel="tooltip" data-title="Expand all tree nodes" id="dc4_tree_expand" class="btn"><i class="icon-double-angle-down"></i></a>
                            <a data-placement="bottom" rel="tooltip" data-container="body" data-title="Collapse all tree nodes" id="dc4_tree_collapse" class="btn"><i class="icon-double-angle-up"></i></a>
                        </div>
                        <div class="btn-group" id="dc4_disease_control">
                            <a data-placement="bottom" rel="tooltip" data-container="body" title="Show hypertree" class="btn disabled" id="dc4_disease_hypertree"><i class="icon-sitemap"></i></a>
                            <a data-placement="bottom" rel="tooltip" data-container="body" title="Open LiveView in external page" class="btn disabled" id="dc4_page_external"><i class="icon-external-link"></i></a>
                            <a data-placement="bottom" rel="tooltip" data-container="body" title="Go to help section" class="btn" id="dc4_page_help"><i class="icon-question-sign"></i></a>
                        </div>
                    </div>
                    <div id="tree">

                    </div>
                </div>
                <div id="frame_loading">
                    <div class="progress progress-striped active">
                        <div class="bar" style="width: 100%;">Loading...</div>
                    </div>
                </div>
                <div id="content">
                    <div id="container">
                        <div id="center-container">
                            <div id="infovis"></div>
                        </div>
                    </div>
                </div>
            </div>
            <script>
            var synonyms_html = '<ul class="synonym_list"><c:forEach var="entry" items="${actionBean.disease.omim.names}"><li><i class="icon-angle-right"></i>${entry}</li></c:forEach></ul>';
            </script>
        </c:when>
        <c:otherwise>
            <div class="navbar navbar-inverse navbar-fixed-top">
                <div class="navbar-inner">
                    <div class="container pull-left">
                        <a class="navbar-brand" href="../"><img src="/diseasecard/final/assets/image/logo_bw.png" height="18" /></a>
                        <ul class="nav">
                            <li class="active"><a href="#">No data for entry <em>${key}</em></a></li>
                        </ul>
                    </div>
                    <ul class="nav pull-right">
                        <li><a href="<c:url value="/about" />" id="tour_about" rel="tooltip" data-placement="bottom" title="Jump to Diseasecard about section"><i class="icon-book"></i></a></li>
                        <li data-placement="bottom" rel="tooltip" title="Jump to Diseasecard rare diseases browsing"><a href="<c:url value="/browse" />" title="Jump to Diseasecard rare diseases browsing"><i class="icon-reorder"></i></a></li>
                        <li><a href="#" class="mag" data-active="false" data-toggle="dropdown" id="nav_search" rel="tooltip" title="Search for rare diseases" data-placement="bottom"><i class="icon-search"></i></a></li>
                    </ul>
                </div>
            </div>
        </div>
        <div id="wrap">
            <div class="container" id="index">
                <div class="row">
                    <div class="left col-md-8">
                        <div id="no_entry">
                        </div>
                        <form class="form" id="home_search" >
                            <div class="input-append center">
                                <input class="input-xlarge" id="text_search" class="home_search" placeholder="Search here..."  type="text">
                                <button class="btn btn-inverse" type="button"  id="button_search">GO!</button>
                            </div>
                        </form>
                        <div id="helper">
                            Need help? You can search Diseasecard for disease names, OMIM disease codes or any of the connected identifiers.<br /><br />Try <a href="#" class="search_help" title="Search for OMIM code" rel="tooltip" data-animation="true">114480</a>, <a class="search_help" title="Add disease name to search box" rel="tooltip" data-animation="true">breast cancer</a> or <a href="#" class="search_help" rel="tooltip" data-animation="true" title="Add gene HGNC symbol to search box">BRCA2</a>
                        </div>
                    </div>
                    <div class="right col-md-4">
                        <div class="top_hits">
                            <h3>Browse rare diseases</h3>
                            <ul>
                                <li><a href="<c:url value="/entry/125853" />" rel="tooltip" title="Visit Diabetes mellitus, noninsulin-dependent's disease card">Diabetes mellitus</a> <i class="icon-double-angle-left"></i></li>
                                <li><a href="<c:url value="/entry/259730" />" rel="tooltip" title="Visit Osteopetrosis, autosomal recessive 3, with renal tubular acidosis' disease card">Osteopetrosis</a> <i class="icon-double-angle-left"></i></li>
                                <li><a href="<c:url value="/entry/188050" />" rel="tooltip" title="Visit Thrombophilia due to thrombin defect's disease card">Thrombophilia</a> <i class="icon-double-angle-left"></i></li>
                                <li><a href="<c:url value="/entry/115150" />" rel="tooltip" title="Visit Cardiofaciocutaneous syndrome's disease card">Cardiofaciocutaneous syndrome</a> <i class="icon-double-angle-left"></i></li>
                                <li><a href="<c:url value="/entry/180849" />" rel="tooltip" title="Visit Rubinstein-Taybi syndrome's disease card">Rubinstein-Taybi syndrome</a> <i class="icon-double-angle-left"></i></li>
                                <li><a href="<c:url value="/entry/104300" />" rel="tooltip" title="Visit Alzheimer disease's disease card">Alzheimer disease</a> <i class="icon-double-angle-left"></i></li>
                                <li><a href="<c:url value="/entry/312870" />" rel="tooltip" title="Visit Simpson-Golabi-Behmel syndrome's disease card">Simpson-Golabi-Behmel syndrome</a> <i class="icon-double-angle-left"></i></li>
                                <li><a href="<c:url value="/entry/143100" />" rel="tooltip" title="Visit Huntington disease's disease card">Huntington disease</a> <i class="icon-double-angle-left"></i></li>
                                <li><br /></li>
                                <li><a href="<c:url value="/browse" />" rel="tooltip" title="Browse all diseases">Browse all</a> <i class="icon-reorder"></i></li>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </c:otherwise>
</c:choose>
</s:layout-component>
</s:layout-render>