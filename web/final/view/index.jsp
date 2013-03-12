<%@include file="/final/layout/taglib.jsp" %>
<s:layout-render name="/final/layout/html.jsp">
    <s:layout-component name="title">Diseasecard</s:layout-component>
    <s:layout-component name="custom_scripts">
        <script src="<c:url value="/final/assets/script/dc4.index.js" />"></script>
    </s:layout-component>
    <s:layout-component name="body"> 
        <div class="navbar navbar-inverse navbar-fixed-top">
            <div class="navbar-inner">
                <ul class="nav pull-right">
                    <li><a href="<c:url value="/about" />" rel="tooltip" data-placement="bottom" title="Jump to Diseasecard about section"><i class="icon-book"></i></a></li>
                    <li><a href="<c:url value="/browse" />" rel="tooltip" data-placement="bottom" title="Jump to Diseasecard rare diseases browsing"><i class="icon-reorder"></i></a></li>
                </ul>
            </div>
        </div>
    </div>    
    <div id="wrap">
        <div class="container" id="index">
            <div class="row-fluid">
                <div class="left span8">
                    <div id="logo">
                        <a href="<c:url value="/about" />" title="About Diseasecard" class="tooltipp" data-tooltip="About Diseasecard"><img src="<c:url value="/final/assets/image/logo.png" />" /></a>
                    </div>
                    <form class="form" id="home_search" >
                        <div class="input-append center" id="home_form">
                            <input class="input-xlarge" id="text_search" class="home_search" placeholder="Search here..."  type="text">
                            <div class="btn-group" id="search_button">
                                <button class="btn btn-inverse" type="button"  id="button_search">GO!</button>
                                <button class="btn btn-inverse dropdown-toggle" id="button_search_toggle" data-toggle="dropdown" tabindex="-1" rel="tooltip" title="Select search type">
                                    <span class="caret"></span>
                                </button>
                                <ul class="dropdown-menu radios">
                                    <li>
                                        <label rel="tooltip" data-placement="right" title="Search for identifiers and disease name only" class="radio">
                                            <input type="radio" name="search_id" id="search_id" value="search_id" checked>Identifiers only
                                        </label>
                                    </li>
                                    <li>
                                        <label rel="tooltip" data-placement="right" title="Search for full text in all connected content" class="radio">
                                            <input type="radio" name="search_full" id="search_full" value="search_full">Full text search
                                        </label>
                                    </li>
                                </ul>
                            </div>
                        </div>
                    </form>
                    <div id="helper">
                        <p>
                            Welcome to the new <strong>Diseasecard</strong>! <br /> Check the <a href="<c:url value="/about" />" rel="tooltip" data-placement="bottom" title="Jump to Diseasecard about section">about <i class="icon-book"></i></a> section to learn what's new and feel free to <a href="http://goo.gl/U0f2Z" rel="tooltip" title="View Diseasecard's form" target="_blank">give us any feedback</a>!
                        </p>
                        Need help? You can search Diseasecard for disease names, OMIM disease codes or any of the connected identifiers.<br /><br />Try <a href="#" class="search_help" title="Search for OMIM code" rel="tooltip" data-animation="true">143100</a>, <a href="#" class="search_help" title="Add disease name to search box" rel="tooltip" data-animation="true">huntington</a> or <a href="#" class="search_help" rel="tooltip" data-animation="true" title="Add gene HGNC symbol to search box">HTT</a>
                    </div>
                </div>
                <div class="right span4">
                    <div class="top_hits" id="browsing">
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
</s:layout-component>
</s:layout-render>
