<%@include file="/final/layout/taglib.jsp" %>
<s:layout-render name="/final/layout/html.jsp">
    <s:layout-component name="title">${actionBean.query} - Diseasecard Search</s:layout-component>
    <s:layout-component name="custom_scripts">
        <script src="<c:url value="/final/assets/script/dc4.search.js" />"></script>
        <script>
            $(document).ready(function(){
                loadResults('${actionBean.query}');  
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
                            <li class="active"><a href="#">${actionBean.query}</a></li>
                        </ul>
                    </div><!--/.nav-collapse -->
                </div>
                <ul class="nav pull-right">
                     <li><a href="<c:url value="/about" />" rel="tooltip" data-placement="bottom" title="Jump to Diseasecard about section"><i class="icon-book"></i></a></li>
                    <li data-placement="bottom" rel="tooltip" title="Jump to Diseasecard rare diseases browsing"><a href="<c:url value="/browse" />" title="Jump to Diseasecard rare diseases browsing"><i class="icon-reorder"></i></a></li>
                    <li><a href="#" class="mag" rel="tooltip"  data-placement="bottom" title="Search for rare diseases" data-active="false" data-toggle="dropdown" id="nav_search"><i class="icon-search"></i></a></li>
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
    <div class="container" id="search">
        <!-- search errors -->
        <div class="row-fluid center" id="errors">
            <div class="alert span6" id="alert_short">
                <button type="button" class="close" data-dismiss="alert">&times;</button>
                <strong>Warning!</strong> You search query must be at least 4 characters long.
            </div> 
            <div class="alert alert-error span6" id="alert_noresults">
                <button type="button" class="close" data-dismiss="alert">&times;</button>
                <strong>Warning!</strong> Diseasecard found no results for you search query <span class="label label-important">${actionBean.query}</span>.
            </div>
        </div>
        <div class="navbar" id="loading">
            <div class="navbar-inner">
                <div class="pull-left">
                    <h4><span>Your results are loading... <i class="icon-angle-right icon-spin"></i></span></h4>
                </div>
            </div>
        </div>
        <div class="navbar" id="meta">
            <div class="navbar-inner">
                <div class="pull-left">
                    <h4><span id="results_size"></span> total results for <span class="label label-inverse">${actionBean.query}</span></h4>
                </div>
                <form class="pull-right navbar-form" id="tour_filter">
                    <label>
                        <input class="input-large" type="text" placeholder="Filter" id="filter">
                    </label>
                </form> 
            </div>

        </div>
        <div class="row-fluid" id="results">
            <div class="span3" id="results_search">
                <ul class="nav nav-list bs-docs-sidenav" id="results_list">

                </ul>
            </div>
            <div class="span9" id="results_links">

            </div>
        </div>
    </div>

</div>
</s:layout-component>
</s:layout-render>
