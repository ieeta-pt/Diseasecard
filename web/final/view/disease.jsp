<%@include file="/final/layout/taglib.jsp" %>
<s:layout-render name="/final/layout/html.jsp">
    <s:layout-component name="title">${actionBean.disease.id} - ${actionBean.disease.omim.description} - Diseasecard</s:layout-component>
    <s:layout-component name="custom_scripts">
        <script>
            var key = '${actionBean.key}';
        </script>
        <script src="<c:url value="/final/assets/script/jit.js" />"></script>
        <script src="<c:url value="/final/assets/script/treeview.js" />"></script>
        <script src="<c:url value="/final/assets/script/dc4.disease.js" />"></script>
    </s:layout-component>
    <s:layout-component name="body">
        <c:choose>
            <c:when test="${actionBean.disease.omim.label != ''}">
                <div id="top">
                    <div class="info">
                        <div class="logo tooltip" data-tooltip="Go to Diseasecard home"></div>
                        <div class="omim tooltip" id="omim" data-tooltip="Go to ${actionBean.key} OMIM page" data-id="${actionBean.key}">
                            <c:if test="${actionBean.disease.omim.phenotype}" >
                                #${actionBean.disease.omim.id}                           
                            </c:if>
                            <c:if test="${actionBean.disease.omim.phenotype == false}" >
                                ${actionBean.disease.omim.id}                               
                            </c:if>
                        </div>
                        <div class="description tooltip" data-tooltip="View ${actionBean.key} disease network" id="syns">${actionBean.disease.omim.description} <span id="arrow">&#x25BE;</span></div>
                        <div class="synonyms">                                
                            <c:forEach var="entry" items="${actionBean.disease.diseaseMap}">
                                <a class="small synonym tooltip" data-tooltip="Open ${entry.key}" data-omim="${entry.key}" href="${path}/disease/${entry.key}"></a>
                            </c:forEach>
                        </div>
                    </div>
                    <div class="right_box">

                        <div class="about tooltip" data-tooltip="Go to About page">
                            <a href="${path}/about" target="_top">about</a>
                        </div>
                        <div class="mag tooltip" data-tooltip="Launch search box" data-active="false"></div>
                    </div>

                    <div class="search">
                        <form>
                            <input type="text" id="text_search" class="searcher" placeholder="Search..." type="search" />
                            <div class="cancel">
                            </div>
                        </form>
                    </div>          
                </div>
                <div id="wrap">
                    <div id="diseasebar">
                        <div id="sidebar_menu">
                            <div id="dc4_tree_control">
                                <a id="dc4_tree_collapse" class="tooltip" data-tooltip="Collapse tree content">Collapse tree content</a>
                                <a id="dc4_tree_expand" class="tooltip" data-tooltip="Expand tree content">Expand tree content</a>
                            </div>
                            <div id="dc4_disease_control">
                                <span id="dc4_disease_hypertree" class="tooltip" data-tooltip="Go to ${actionBean.key} home">Go to disease home</span>
                                <span id="dc4_disease_report" data-id="${actionBean.key}" class="tooltip" data-tooltip="Go to ${actionBean.key} report view">Go to disease report</span>
                                <span id="dc4_disease_print" data-id="${actionBean.key}" class="tooltip" data-tooltip="Go to ${actionBean.key} print view">Go to disease print view</span>
                            </div>
                            <div id="dc4_page_control">
                                <span id="dc4_page_external" class="tooltip" data-tooltip="Open this page in a new window" data-id="${actionBean.key}">Go to disease OMIM page</span>
                                <span id="dc4_page_help" class="tooltip" data-tooltip="View Diseasecard help" >View Diseasecard help</span>
                            </div>
                        </div>
                        <div id="tree">

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
            </c:when>
            <c:otherwise>
                <div id="top">
                    <div class="info">
                        <div class="logo"></div>
                        <div class="description">No data available for ${actionBean.key}</div>
                    </div>
                    <div class="right_box">
                        <div class="about tooltip" data-tooltip="Go to About page">
                            <a href="${path}/about" target="_top">about</a>
                        </div>
                        <div class="mag tooltip" data-tooltip="Launch search box" data-active="false"></div>
                    </div>
                    <div class="search">
                        <form>
                            <input type="text" id="text_search" class="searcher" placeholder="Search..." type="search" />
                            <div class="cancel">
                            </div>
                        </form>
                    </div>          
                </div>
                <div id="index">
                    <div class="null_search">
                        <form class="form" >
                            <input type="text" class="searcher" id="null_text_search" placeholder="Search here..." /><input type="button" id="button_search" value="GO" /><span data-id="" id="omim"></span>
                        </form>
                    </div>
                </div>
                <script>
                    $('#null_text_search').keypress(function(e){
                        if ( e.which == 13 ) {
                            e.preventDefault();
                            if($('#null_text_search').data('omim') != undefined) {
                                window.location = path + '/disease/' + $('#null_text_search').data('omim');                
                            } else {
                                window.location = path + '/search/' + $('#null_text_search').attr('value');
                            }
                        }         
                    });
    
                    $( "#null_text_search" ).autocomplete({
                        minLength: 3,
                        delay: 500,
                        source: path + '/autocomplete',
                        focus: function( event, ui ) {            
                            $('#null_text_search').data('omim',ui.item.omim);
                            $( "#null_text_search" ).val( ui.item.info );
                            return false;
                        },
                        select: function( event, ui ) {
                            event.preventDefault();
                            $('#null_text_search').data('omim',ui.item.omim);
                            window.location = path + '/disease/' + ui.item.omim;       
                        }
                    })
                    .data( "autocomplete" )._renderItem = function( ul, item ) {
                        return $( "<li></li>" )
                        .data( "item.autocomplete", item )
                        .data("item.omim",item.omim)
                        .append( "<a>" + item.omim + " | "  + item.info + "</a>" )
                        .appendTo( ul );
                    }; 
                </script>
            </c:otherwise>
        </c:choose>

    </s:layout-component>
</s:layout-render>
