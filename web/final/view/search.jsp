<%@include file="/final/layout/taglib.jsp" %>
<s:layout-render name="/final/layout/html.jsp">
    <s:layout-component name="title">${actionBean.query} - Diseasecard Search</s:layout-component>
    <s:layout-component name="custom_scripts">
        <script src="<c:url value="/final/assets/script/dc4.search.js" />"></script>
        <script>
            $(document).ready(function() {
                loadResults('${actionBean.query}');
            });
        </script>
    </s:layout-component>
    <s:layout-component name="body">
        <div class="navbar navbar-inverse navbar-fixed-top">
            <div class="navbar-header">
                <a class="navbar-brand" href="<c:url value="/" />"><img src="<c:url value="/final/assets/image/logo_bw.png" />" height="18" /></a>
            </div>
            <div class="collapse navbar-collapse navbar-left">
                <ul class="nav navbar-nav">
                    <li class="active"><a href="#">${actionBean.query}</a></li>
                </ul>
            </div><!--/.navbar-collapse -->
        
        <ul class="nav navbar-nav navbar-right top_menu collapse navbar-collapse">  
            <li><a href="<c:url value="/about" />" rel="tooltip" data-placement="bottom" title="Jump to Diseasecard about section"><i class="icon-book"></i></a></li>
            <li data-placement="bottom" rel="tooltip" title="Jump to Diseasecard rare diseases browsing"><a href="<c:url value="/browse" />" title="Jump to Diseasecard rare diseases browsing"><i class="icon-reorder"></i></a></li>
            <li><a href="#" class="mag" rel="tooltip"  data-placement="bottom" title="Search for rare diseases" data-active="false" data-toggle="dropdown" id="nav_search"><i class="icon-search"></i></a></li>
        </ul>

    </div>
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
<div class="container" id="search">
    <!-- search errors -->
    <div class="row center" id="errors">
        <div class="alert col-md-6" id="alert_short">
            <button type="button" class="close" data-dismiss="alert">&times;</button>
            <strong>Warning!</strong> Your search query must be at least 4 characters long.
        </div> 
        <div class="alert alert-error col-md-6" id="alert_noresults">
            <button type="button" class="close" data-dismiss="alert">&times;</button>
            <strong>Warning!</strong> Diseasecard found no results for you search query <span class="label label-important">${actionBean.query}</span>.
            <br />
            <br />
            <a href="<c:url value="/search/full/${actionBean.query}" />" target="_top" rel="tooltip" title="Diseasecard full text search on ">Do you want to try full text search instead?</a>
        </div>
    </div>
    <div class="navbar" id="loading">

        <div class="pull-left">
            <h4><span>Your results for <span class="label label-inverse">${actionBean.query}</span> are being processed.</h4>
        </div>
        <div class="pull-right">
            <div class="progress progress-striped active loading_results">
                <div class="bar" style="width: 100%;"> Loading... </div>
            </div>
        </div>

    </div>
    <div class="navbar" id="meta">

        <div class="pull-left">
            <h4><span id="results_size"></span> total results for <span class="label label-inverse">${actionBean.query}</span></h4>
        </div>
        <form class="pull-right navbar-form" id="tour_filter">
            <label>
                <input class="input-lg" type="text" placeholder="Filter" id="filter">
            </label>
        </form> 


    </div>
    <div class="row" id="results">
        <div class="col-md-3" id="results_search">
            <ul class="nav nav-list bs-docs-sidenav" id="results_list">

            </ul>
        </div>
        <div class="col-md-9" id="results_links">

        </div>
    </div>
</div>

</div>
</s:layout-component>
</s:layout-render>
