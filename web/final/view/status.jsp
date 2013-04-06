<%@include file="/final/layout/taglib.jsp" %>
<c:set var="this" value="${actionBean}" />
<s:layout-render name="/final/layout/html.jsp">
    <s:layout-component name="title">Status - Diseasecard</s:layout-component>
    <s:layout-component name="body">
        <div class="navbar navbar-inverse navbar-fixed-top">
            <div class="navbar-inner">
                <div class="container pull-left ">
                    <a class="brand" href="<c:url value="/" />"><img src="<c:url value="/final/assets/image/logo_bw.png" />" height="18" /></a>
                    <div class="nav-collapse collapse">
                        <ul class="nav">
                            <li class="active"><a href="#">Information</a></li>
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
        <div class="container" style="padding-top: 64px;">
            <div class="row-fluid">
	            <h3>Status</h3>
	            <dl>
	            	<dt>Search</dt>
	            	<dd><span class="label label-success">OK</span></dd>
	            	<dt>Browse</dt>
	            	<dd><span class="label label-success">OK</span></dd>
	            	<dt>Workspace</dt>
	            	<dd><span class="label label-success">OK</span></dd>
	            	<dt>SPARQL endpoint</dt>
	            	<dd><span class="label label-success">OK</span></dd>
	            	<dt>LinkedData</dt>
	            	<dd><span class="label label-success">OK</span></dd>
	            </dl>
            	<h3>Statistics</h3>
            	<dl>
            		<dt>Triples</dt>
            		<dd><span class="label label-info">1770197</span></dd>
            		<dt>Nodes</dt>
            		<dd><span class="label label-info">586048</span></dd>
            		<dt>Associations</dt>
            		<dd><span class="label label-info">902229</span></dd>
            		<dt>Unique connections</dt>
            		<dd><span class="label label-info">301942</span></dd>
            		<dt>Indexed documents</dt>
            		<dd><span class="label label-info">151784</span> in Solr</dd>
            		<dt>Rare disease coverage</dt>
            		<dd><span class="label label-info">52.4</span> connections/rare disease</dd>
            	</dl>
                <h3>Version</h3>
                <dl>
                	<dt>Online</dt>
                	<dd><span class="label label-info">${actionBean.version}</span></dd>
                </dl>
            </div>
        </div>
    </s:layout-component>
    <s:layout-component name="custom_scripts">
       
        <script>   
             $(document).ready(function(){                
                $('.mag').click(function() {
                    toggleTopButton('mag');
                    setTimeout(function(){                            
                        $('#text_search').focus();
                    }, 400);                    
                });                   
            });    
        </script>
    </s:layout-component>
</s:layout-render>
