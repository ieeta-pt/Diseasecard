<%@include file="/final/layout/taglib.jsp" %>
<s:layout-render name="/final/layout/html.jsp">
    <s:layout-component name="title">${actionBean.key} - Diseasecard</s:layout-component>
    <s:layout-component name="custom_scripts">
        <script>var key = '${actionBean.key}';</script>
        <script src="<c:url value="/final/assets/script/jit.js" />"></script>
        <script src="<c:url value="/final/assets/script/treeview.js" />"></script>
        <script src="<c:url value="/final/assets/script/dc4.entry.js" />"></script>
    </s:layout-component>
    <s:layout-component name="body">
                <div class="navbar navbar-inverse navbar-fixed-top">
                    <div class="navbar-inner">
                        <div class="container pull-left">
                            <a class="brand" id="dc4_logo" href="<c:url value="/" />"><img src="<c:url value="/final/assets/image/logo_bw.png" />" height="22" width="190" /></a>
                            <div class="nav-collapse collapse">
                                <ul class="nav">
                                    <li class="active">
                                        <a href="#" id="key" class="dropdown-toggle" data-toggle="dropdown"><i class="icon-chevron-down"></i></a>
                                            <ul id="related" class="dropdown-menu synonyms" role="menu" aria-labelledby="dropdownMenu" >
                                            
                                        </ul>
                                    </li>
                                </ul>
                                    <ul class="nav">
                                    <li>
                                        <a href="#" id="description" data-id="" rel="popover" data-original-title="Synonyms" data-animate="false"></a>     
                                    </li>
                                </ul>
                               </div><!--/.nav-collapse -->
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
</s:layout-component>
</s:layout-render>
