<%@include file="/final/layout/taglib.jsp" %>
<s:layout-render name="/final/layout/html.jsp">
    <s:layout-component name="title">Diseasecard</s:layout-component>
    <s:layout-component name="custom_scripts">
        <script src="<c:url value="/final/assets/script/dc4.index.js" />"></script>
    </s:layout-component>
    <s:layout-component name="body"> 
        <noscript>
        Diseasecard requires that you enable Javascript.&lt;br /&gt;&lt;br /&gt;&lt;br /&gt;&lt;a href="http://support.google.com/bin/answer.py?hl=en&amp;answer=23852" target="_blank"&gt;Take a look here if you do not know how.&lt;/a&gt;
        </noscript>
        <div class="navbar navbar-inverse navbar-fixed-top">
            <div class="navbar-inner">

                <ul class="nav pull-right">
                    <li id="nav_about"><a href="../about">about</a></li>
                </ul>
            </div>
        </div>
    </div>    
    <div id="wrap">
        <div class="container" id="index">
             <div class="alert alert-error span6" id="alert_noresults">
                <button type="button" class="close" data-dismiss="alert">&times;</button>
                <strong>Error.</strong><span>These are not the diseases you are looking for.</span>
            </div>
            <div class="row-fluid">
                <div class="left span8">
                    <div id="logo">
                        <a href="<c:url value="/about" />" title="About Diseasecard" class="tooltipp" data-tooltip="About Diseasecard"><img src="<c:url value="/final/assets/image/logo.png" />" /></a>
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
                <div class="right span4">
                    <div class="top_hits">
                        <h3>Browse rare diseases</h3>
                        <ul>
                            <li><a href="entry/125853" rel="tooltip" title="Visit Diabetes mellitus, noninsulin-dependent's disease card">Diabetes mellitus</a><i class="icon-angle-left"></i></li>
                            <li><a href="entry/601665" rel="tooltip" title="Visit Obesity, autosomal dominant's disease card">Obesity</a><i class="icon-angle-left"></i></li>
                            <li><a href="entry/211980" rel="tooltip" title="Visit Adenocarcinoma of lung, response to tyrosine kinase inhibitor's disease card">Lung cancer</a><i class="icon-angle-left"></i></li>
                            <li><a href="entry/176807" rel="tooltip" title="Visit Prostate cancer's disease card">Prostate cancer</a><i class="icon-angle-left"></i></li>
                            <li><a href="entry/137215" rel="tooltip" title="Visit Gastric cancer's disease card">Gastric cancer</a><i class="icon-angle-left"></i></li>
                            <li><a href="entry/601626" rel="tooltip" title="Visit Leukemia's disease card">Leukemia</a><i class="icon-angle-left"></i></li>
                            <li><a href="entry/181500" rel="tooltip" title="Visit Schizophrenia's disease card">Schizophrenia</a><i class="icon-angle-left"></i></li>
                            <li><a href="entry/609423" rel="tooltip" title="Visit AIDS's disease card">AIDS</a><i class="icon-angle-left"></i></li>
                        </ul>
                    </div>
                </div>
            </div>              
        </div>            
    </div>
</s:layout-component>
</s:layout-render>
