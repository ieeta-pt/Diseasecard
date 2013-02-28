<%@include file="/final/layout/taglib.jsp" %>
<c:set var="this" value="${actionBean}" />
<s:layout-render name="/final/layout/html.jsp">
    <s:layout-component name="title">Browse rare diseases - Diseasecard</s:layout-component>
    <s:layout-component name="custom_scripts">
        <script src="<c:url value="/final/assets/script/jquery.dataTables.min.js" />"></script>
        <script src="<c:url value="/final/assets/script/dc4.browse.js" />"></script>
    </s:layout-component>
    <s:layout-component name="body">
        <div class="navbar navbar-inverse navbar-fixed-top">
            <div class="navbar-inner">
                <div class="container pull-left ">
                    <a class="brand" href="<c:url value="/" />"><img src="<c:url value="/final/assets/image/logo_bw.png" />" height="18" /></a>
                    <div class="nav-collapse collapse">
                        <ul class="nav">
                            <li class="active"><a href="#">Browse all diseases</a></li>
                        </ul>
                    </div><!--/.nav-collapse -->
                </div>
                <ul class="nav pull-right">
                    <li><a href="<c:url value="/about" />" rel="tooltip" data-placement="bottom" title="Jump to Diseasecard about section"><i class="icon-book"></i></a></li>
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

        <!-- Main page content -->
        <div class="container" id="browser">
            <ul class="nav nav-pills" id="tabs">
                <li><a href="#0" >#</a></li>
                <li><a href="#a" >A</a></li>
                <li><a href="#b" >B</a></li>
                <li><a href="#c" >C</a></li>
                <li><a href="#d" >D</a></li>
                <li><a href="#e" >E</a></li>
                <li><a href="#f" >F</a></li>
                <li><a href="#g" >G</a></li>
                <li><a href="#h" >H</a></li>
                <li><a href="#i" >I</a></li>
                <li><a href="#j" >J</a></li>
                <li><a href="#k" >K</a></li>
                <li><a href="#l" >L</a></li>
                <li><a href="#m" >M</a></li>
                <li><a href="#n" >N</a></li>
                <li><a href="#o" >O</a></li>
                <li><a href="#p" id="tour_target">P</a></li>
                <li><a href="#q" >Q</a></li>
                <li><a href="#r" >R</a></li>
                <li><a href="#s" >S</a></li>
                <li><a href="#t" >T</a></li>
                <li><a href="#u" >U</a></li>
                <li><a href="#v" >V</a></li>
                <li><a href="#w" >W</a></li>
                <li><a href="#x" >X</a></li>
                <li><a href="#y" >Y</a></li>
                <li><a href="#z" >Z</a></li>
            </ul>
            <div class="tab-content" style="overflow: hidden;">
                <div class="tab-pane active" id="overview">
                    <h2>Overview</h2>
                    Here you can browse all diseases available in Diseasecard's knowledge base in alphabetical order.
                    <br />
                    <br />
                    <h4>Top 10 Diseases</h4>
                    <ul>
                        <li><i class="icon-angle-right"></i><a href="./entry/125853" rel="tooltip" title="Visit Diabetes mellitus, noninsulin-dependent's disease card">Diabetes mellitus</a></li>
                        <li><i class="icon-angle-right"></i><a href="./entry/601665" rel="tooltip" title="Visit Obesity, autosomal dominant's disease card">Obesity</a></li>
                        <li><i class="icon-angle-right"></i><a href="./entry/211980" rel="tooltip" title="Visit Adenocarcinoma of lung, response to tyrosine kinase inhibitor's disease card">Lung cancer</a></li>
                        <li><i class="icon-angle-right"></i><a href="./entry/176807" rel="tooltip" title="Visit Prostate cancer's disease card">Prostate cancer</a></li>
                        <li><i class="icon-angle-right"></i><a href="./entry/137215" rel="tooltip" title="Visit Gastric cancer's disease card">Gastric cancer</a></li>
                        <li><i class="icon-angle-right"></i><a href="./entry/601626" rel="tooltip" title="Visit Leukemia's disease card">Leukemia</a>/li>
                        <li><i class="icon-angle-right"></i><a href="./entry/181500" rel="tooltip" title="Visit Schizophrenia's disease card">Schizophrenia</a></li>
                        <li><i class="icon-angle-right"></i><a href="./entry/609423" rel="tooltip" title="Visit AIDS's disease card">AIDS</a></li>
                    </ul>
                </div>
                <div class="tab-pane" id="0"><h2>#</h2>
                    <table cellpadding="0" cellspacing="0" id="0_list" class="table table-striped">
                        <thead>
                        <tr><th class="span2">OMIM</th><th>Name</th><th class="span3">Connections</th></tr>
                        </thead>
                        <tbody>

                        </tbody>
                    </table>
                </div>
                <div class="tab-pane" id="a"><h2>A</h2>
                    <table id="a_list" class="table table-striped">
                        <thead>
                        <tr><th class="span2">OMIM</th><th>Name</th><th class="span3">Connections</th></tr>
                        </thead>
                        <tbody>

                        </tbody>
                    </table>
                </div>
                <div class="tab-pane" id="b"><h2>B</h2>
                    <table id="b_list" class="table table-striped">
                        <thead>
                        <tr><th class="span2">OMIM</th><th>Name</th><th class="span3">Connections</th></tr>
                        </thead>
                        <tbody>

                        </tbody>
                    </table>
                </div>
                <div class="tab-pane" id="c"><h2>C</h2>
                    <table id="c_list" class="table table-striped">
                        <thead>
                        <tr><th class="span2">OMIM</th><th>Name</th><th class="span3">Connections</th></tr>
                        </thead>
                        <tbody>

                        </tbody>
                    </table>
                </div>
                <div class="tab-pane" id="d"><h2>D</h2>
                    <table id="d_list" class="table table-striped">
                        <thead>
                        <tr><th class="span2">OMIM</th><th>Name</th><th class="span3">Connections</th></tr>
                        </thead>
                        <tbody>

                        </tbody>
                    </table>
                </div>
                <div class="tab-pane" id="e"><h2>E</h2>
                    <table id="e_list" class="table table-striped">
                        <thead>
                        <tr><th class="span2">OMIM</th><th>Name</th><th class="span3">Connections</th></tr>
                        </thead>
                        <tbody>

                        </tbody>
                    </table>
                </div>
                <div class="tab-pane" id="f"><h2>F</h2>
                    <table id="f_list" class="table table-striped">
                        <thead>
                        <tr><th class="span2">OMIM</th><th>Name</th><th class="span3">Connections</th></tr>
                        </thead>
                        <tbody>

                        </tbody>
                    </table>
                </div>
                <div class="tab-pane" id="g"><h2>G</h2>
                    <table id="g_list" class="table table-striped">
                        <thead>
                        <tr><th class="span2">OMIM</th><th>Name</th><th class="span3">Connections</th></tr>
                        </thead>
                        <tbody>

                        </tbody>
                    </table>
                </div>
                <div class="tab-pane" id="h"><h2>H</h2>
                    <table id="h_list" class="table table-striped">
                        <thead>
                        <tr><th class="span2">OMIM</th><th>Name</th><th class="span3">Connections</th></tr>
                        </thead>
                        <tbody>

                        </tbody>
                    </table>
                </div>
                <div class="tab-pane" id="i"><h2>I</h2>
                    <table id="i_list" class="table table-striped">
                        <thead>
                        <tr><th class="span2">OMIM</th><th>Name</th><th class="span3">Connections</th></tr>
                        </thead>
                        <tbody>

                        </tbody>
                    </table>
                </div>
                <div class="tab-pane" id="j"><h2>J</h2>
                    <table id="j_list" class="table table-striped">
                        <thead>
                        <tr><th class="span2">OMIM</th><th>Name</th><th class="span3">Connections</th></tr>
                        </thead>
                        <tbody>

                        </tbody>
                    </table>
                </div>
                <div class="tab-pane" id="k"><h2>K</h2>
                    <table id="k_list" class="table table-striped">
                        <thead>
                        <tr><th class="span2">OMIM</th><th>Name</th><th class="span3">Connections</th></tr>
                        </thead>
                        <tbody>

                        </tbody>
                    </table>
                </div>
                <div class="tab-pane" id="l"><h2>L</h2>
                    <table id="l_list" class="table table-striped">
                        <thead>
                        <tr><th class="span2">OMIM</th><th>Name</th><th class="span3">Connections</th></tr>
                        </thead>
                        <tbody>

                        </tbody>
                    </table>
                </div>
                <div class="tab-pane" id="m"><h2>M</h2>
                    <table id="m_list" class="table table-striped">
                        <thead>
                        <tr><th class="span2">OMIM</th><th>Name</th><th class="span3">Connections</th></tr>
                        </thead>
                        <tbody>

                        </tbody>
                    </table>
                </div>
                <div class="tab-pane" id="n"><h2>N</h2>
                    <table id="n_list" class="table table-striped">
                        <thead>
                        <tr><th class="span2">OMIM</th><th>Name</th><th class="span3">Connections</th></tr>
                        </thead>
                        <tbody>

                        </tbody>
                    </table>
                </div>
                <div class="tab-pane" id="o"><h2>O</h2>
                    <table id="o_list" class="table table-striped">
                        <thead>
                        <tr><th class="span2">OMIM</th><th>Name</th><th class="span3">Connections</th></tr>
                        </thead>
                        <tbody>

                        </tbody>
                    </table>
                </div>
                <div class="tab-pane" id="p"><h2>P</h2>
                    <table id="p_list" class="table table-striped">
                        <thead>
                        <tr><th class="span2">OMIM</th><th>Name</th><th class="span3">Connections</th></tr>
                        </thead>
                        <tbody>

                        </tbody>
                    </table>
                </div>
                <div class="tab-pane" id="q"><h2>Q</h2>
                    <table id="q_list" class="table table-striped">
                        <thead>
                        <tr><th class="span2">OMIM</th><th>Name</th><th class="span3">Connections</th></tr>
                        </thead>
                        <tbody>

                        </tbody>
                    </table>
                </div>
                <div class="tab-pane" id="r"><h2>R</h2>
                    <table id="r_list" class="table table-striped">
                        <thead>
                        <tr><th class="span2">OMIM</th><th>Name</th><th class="span3">Connections</th></tr>
                        </thead>
                        <tbody>

                        </tbody>
                    </table>
                </div>
                <div class="tab-pane" id="s"><h2>S</h2>
                    <table id="s_list" class="table table-striped">
                        <thead>
                        <tr><th class="span2">OMIM</th><th>Name</th><th class="span3">Connections</th></tr>
                        </thead>
                        <tbody>

                        </tbody>
                    </table>
                </div>
                <div class="tab-pane" id="t"><h2>T</h2>
                    <table id="t_list" class="table table-striped">
                        <thead>
                        <tr><th class="span2">OMIM</th><th>Name</th><th class="span3">Connections</th></tr>
                        </thead>
                        <tbody>

                        </tbody>
                    </table>
                </div>
                <div class="tab-pane" id="u"><h2>U</h2>
                    <table id="u_list" class="table table-striped">
                        <thead>
                        <tr><th class="span2">OMIM</th><th>Name</th><th class="span3">Connections</th></tr>
                        </thead>
                        <tbody>

                        </tbody>
                    </table>
                </div>
                <div class="tab-pane" id="v"><h2>V</h2>
                    <table id="v_list" class="table table-striped">
                        <thead>
                        <tr><th class="span2">OMIM</th><th>Name</th><th class="span3">Connections</th></tr>
                        </thead>
                        <tbody>

                        </tbody>
                    </table>
                </div>
                <div class="tab-pane" id="w"><h2>W</h2>
                    <table id="w_list" class="table table-striped">
                        <thead>
                        <tr><th class="span2">OMIM</th><th>Name</th><th class="span3">Connections</th></tr>
                        </thead>
                        <tbody>

                        </tbody>
                    </table>
                </div>
                <div class="tab-pane" id="x"><h2>X</h2>
                    <table id="x_list" class="table table-striped">
                        <thead>
                        <tr><th class="span2">OMIM</th><th>Name</th><th class="span3">Connections</th></tr>
                        </thead>
                        <tbody>

                        </tbody>
                    </table>
                </div>
                <div class="tab-pane" id="y"><h2>Y</h2>
                    <table id="y_list" class="table table-striped">
                        <thead>
                        <tr><th class="span2">OMIM</th><th>Name</th><th class="span3">Connections</th></tr>
                        </thead>
                        <tbody>

                        </tbody>
                    </table>
                </div>
                <div class="tab-pane" id="z"><h2>Z</h2>
                    <table id="z_list" class="table table-striped">
                        <thead>
                        <tr><th class="span2">OMIM</th><th>Name</th><th class="span3">Connections</th></tr>
                        </thead>
                        <tbody>

                        </tbody>
                    </table>
                </div>
            </div>
        </div> 
    </s:layout-component>
</s:layout-render>
