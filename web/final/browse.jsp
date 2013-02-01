<%@include file="/final/layout/taglib.jsp" %>
<c:set var="this" value="${actionBean}" />
<s:layout-render name="/final/layout/html.jsp">
    <s:layout-component name="title">Browse all diseases - Diseasecard</s:layout-component>
    <s:layout-component name="custom_scripts">
        <script>    
            /* Default class modification */
            $.extend( $.fn.dataTableExt.oStdClasses, {
                "sSortAsc": "header headerSortDown",
                "sSortDesc": "header headerSortUp",
                "sSortable": "header"
            } );

            /* API method to get paging information */
            $.fn.dataTableExt.oApi.fnPagingInfo = function ( oSettings )
            {
                return {
                    "iStart":         oSettings._iDisplayStart,
                    "iEnd":           oSettings.fnDisplayEnd(),
                    "iLength":        oSettings._iDisplayLength,
                    "iTotal":         oSettings.fnRecordsTotal(),
                    "iFilteredTotal": oSettings.fnRecordsDisplay(),
                    "iPage":          Math.ceil( oSettings._iDisplayStart / oSettings._iDisplayLength ),
                    "iTotalPages":    Math.ceil( oSettings.fnRecordsDisplay() / oSettings._iDisplayLength )
                };
            }

            /* Bootstrap style pagination control */
            $.extend( $.fn.dataTableExt.oPagination, {
                "bootstrap": {
                    "fnInit": function( oSettings, nPaging, fnDraw ) {
                        var oLang = oSettings.oLanguage.oPaginate;
                        var fnClickHandler = function ( e ) {
                            e.preventDefault();
                            if ( oSettings.oApi._fnPageChange(oSettings, e.data.action) ) {
                                fnDraw( oSettings );
                            }
                        };

                        $(nPaging).addClass('pagination').append(
                        '<ul>'+
                            '<li class="prev disabled"><a href="#">&larr; '+oLang.sPrevious+'</a></li>'+
                            '<li class="next disabled"><a href="#">'+oLang.sNext+' &rarr; </a></li>'+
                            '</ul>'
                    );
                        var els = $('a', nPaging);
                        $(els[0]).bind( 'click.DT', { action: "previous" }, fnClickHandler );
                        $(els[1]).bind( 'click.DT', { action: "next" }, fnClickHandler );
                    },

                    "fnUpdate": function ( oSettings, fnDraw ) {
                        var iListLength = 5;
                        var oPaging = oSettings.oInstance.fnPagingInfo();
                        var an = oSettings.aanFeatures.p;
                        var i, j, sClass, iStart, iEnd, iHalf=Math.floor(iListLength/2);

                        if ( oPaging.iTotalPages < iListLength) {
                            iStart = 1;
                            iEnd = oPaging.iTotalPages;
                        }
                        else if ( oPaging.iPage <= iHalf ) {
                            iStart = 1;
                            iEnd = iListLength;
                        } else if ( oPaging.iPage >= (oPaging.iTotalPages-iHalf) ) {
                            iStart = oPaging.iTotalPages - iListLength + 1;
                            iEnd = oPaging.iTotalPages;
                        } else {
                            iStart = oPaging.iPage - iHalf + 1;
                            iEnd = iStart + iListLength - 1;
                        }

                        for ( i=0, iLen=an.length ; i<iLen ; i++ ) {
                            // Remove the middle elements
                            $('li:gt(0)', an[i]).filter(':not(:last)').remove();

                            // Add the new list items and their event handlers
                            for ( j=iStart ; j<=iEnd ; j++ ) {
                                sClass = (j==oPaging.iPage+1) ? 'class="active"' : '';
                                $('<li '+sClass+'><a href="#">'+j+'</a></li>')
                                .insertBefore( $('li:last', an[i])[0] )
                                .bind('click', function (e) {
                                    e.preventDefault();
                                    oSettings._iDisplayStart = (parseInt($('a', this).text(),10)-1) * oPaging.iLength;
                                    fnDraw( oSettings );
                                } );
                            }

                            // Add / remove disabled classes from the static elements
                            if ( oPaging.iPage === 0 ) {
                                $('li:first', an[i]).addClass('disabled');
                            } else {
                                $('li:first', an[i]).removeClass('disabled');
                            }

                            if ( oPaging.iPage === oPaging.iTotalPages-1 || oPaging.iTotalPages === 0 ) {
                                $('li:last', an[i]).addClass('disabled');
                            } else {
                                $('li:last', an[i]).removeClass('disabled');
                            }
                        }
                    }
                }
            } );
           

            $(document).ready(function(){  
                var loaded = new Array();
                
                var tour = new Tour({
                    name: "diseasecard_browse_tour",
                    keyboard: true
                });
    
                tour.addStep({
                    next: 1, 
                    animation: true,
                    placement: 'bottom',
                    element: "#browser",
                    title: "Browse", 
                    content: "Select the starting letter!<br />Try with <a href='#p' id='tour_p'><strong>P</strong></a><br/>" 
                });
                tour.addStep({
                    prev: 0, // number
                    animation: true,
                    placement: 'top',
                    element: "#p",
                    title: "List", 
                    content: "Check the table for all the rare diseases starting with <strong>P</strong><br/>"
                });
                tour.start();
                
                $('#tour_p').click(function(e) {
                    e.preventDefault();
                    $('#tour_target').tab('show');
                })
            
                $('#tabs a').click(function (e) {
                    e.preventDefault();
                    $(this).tab('show');
                });
                
                if(window.location.hash) {           
                    if(window.location.hash != '#overview') {
                        $('a[href=' + window.location.hash + ']').tab('show');
                        $(window.location.hash + '_list').dataTable({
                            "bProcessing": true,
                            "sAjaxSource": './services/browse/' + window.location.hash.charAt(window.location.hash.length - 1) + '.js',
                            "bPaginate": true,
                            "sPaginationType": "bootstrap",
                            "bSort": true,
                            "bStateSave": true,
                            "sDom": "<'row-fluid'<'span6'l><'span6'f>r>t<'row-fluid'<'span6'i><'span6'p>>",
                            "oLanguage": {
                                "sZeroRecords": "No diseases to display",
                                "sSearch": "Filter diseases: ",
                                "sLengthMenu": 'Display <select>'+
                                    '<option value="10">10</option>'+
                                    '<option value="20">20</option>'+
                                    '<option value="50">50</option>'+
                                    '<option value="100">100</option>'+
                                    '<option value="200">200</option>'+
                                    '<option value="-1">All</option>'+
                                    '</select> diseases'
                            },
                            "iDisplayLength": 10,
                            "bAutoWidth" : false,
                            "aoColumnDefs": [
                                { "sClass": "connections", "aTargets": [ 2 ] }
                            ],
                            "fnInitComplete": function() {                            
                                loaded.push(window.location.toString());
                                $(window.location.hash).tooltip({
                                    selector: "*[rel=tooltip]"
                                });
                            }
                        }); 
                    }
                }
                
                $('.mag').click(function() {
                    toggleTopButton('mag');
                    setTimeout(function(){                            
                        $('#text_search').focus();
                    }, 400);                    
                });    
                
                $('#tabs a').on('show', function (e) {                    
                    if(!e.target.toString().endsWith('#overview')) {
                        if(!loaded.contains(e.target)) {
                            $('#' + e.target.toString().charAt(e.target.toString().length - 1) + '_list').dataTable({
                                "bProcessing": true,
                                "sAjaxSource": './services/browse/' + e.target.toString().charAt(e.target.toString().length - 1) + '.js',
                                "bPaginate": true,
                                "sPaginationType": "bootstrap",
                                "bSort": true,
                                "bStateSave": true,
                                "sDom": "<'row-fluid'<'span6'l><'span6'f>r>t<'row-fluid'<'span6'i><'span6'p>>",
                                "oLanguage": {
                                    "sZeroRecords": "No diseases to display",
                                    "sSearch": "Filter diseases: ",
                                    "sLengthMenu": 'Display <select>'+
                                        '<option value="10">10</option>'+
                                        '<option value="20">20</option>'+
                                        '<option value="50">50</option>'+
                                        '<option value="100">100</option>'+
                                        '<option value="200">200</option>'+
                                        '<option value="-1">All</option>'+
                                        '</select> diseases'
                                },
                                "iDisplayLength": 10,
                                "bAutoWidth" : false,
                                "aoColumnDefs": [
                                    { "sClass": "connections", "aTargets": [ 2 ] }
                                ],
                                "fnInitComplete": function() {
                                    loaded.push(e.target.toString());
                                    $('#' + e.target.toString().charAt(e.target.toString().length - 1)).tooltip({
                                        selector: "*[rel=tooltip]"
                                    });
                                }
                            });  
                        } 
                    }
                    window.location.hash = e.target.hash;
                });
            });            
        </script>
    </s:layout-component>
    <s:layout-component name="body">
        <div class="navbar navbar-inverse navbar-fixed-top">
            <div class="navbar-inner">
                <div class="container pull-left ">
                    <a class="brand" href="./"><img src="/diseasecard/final/assets/image/logo_bw.png" height="18" /></a>
                    <div class="nav-collapse collapse">
                        <ul class="nav">
                            <li class="active"><a href="#">Browse all diseases</a></li>
                        </ul>
                    </div><!--/.nav-collapse -->
                </div>
                <ul class="nav pull-right">
                    <li><a href="./about"><i class="icon-book"></i></a></li>
                    <li><a href="#" class="mag" data-active="false" data-toggle="dropdown" id="nav_search"><i class="icon-search"></i></a></li>
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
            <div class="tab-content">
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
                    <table id="0_list" class="table table-striped">
                        <thead>
                        <th class="span1"><h4>OMIM</h4></th><th><h4>Name</h4></th><th class="span3"><h4>Connections</h4></th>
                        </thead>
                        <tbody>

                        </tbody>
                    </table>
                </div>
                <div class="tab-pane" id="a"><h2>A</h2>
                    <table id="a_list" class="table table-striped">
                        <thead>
                        <th class="span1"><h4>OMIM</h4></th><th><h4>Name</h4></th><th class="span3"><h4>Connections</h4></th>
                        </thead>
                        <tbody>

                        </tbody>
                    </table>
                </div>
                <div class="tab-pane" id="b"><h2>B</h2>
                    <table id="b_list" class="table table-striped">
                        <thead>
                        <th class="span1"><h4>OMIM</h4></th><th><h4>Name</h4></th><th class="span3"><h4>Connections</h4></th>
                        </thead>
                        <tbody>

                        </tbody>
                    </table>
                </div>
                <div class="tab-pane" id="c"><h2>C</h2>
                    <table id="c_list" class="table table-striped">
                        <thead>
                        <th class="span1"><h4>OMIM</h4></th><th><h4>Name</h4></th><th class="span3"><h4>Connections</h4></th>
                        </thead>
                        <tbody>

                        </tbody>
                    </table>
                </div>
                <div class="tab-pane" id="d"><h2>D</h2>
                    <table id="d_list" class="table table-striped">
                        <thead>
                        <th class="span1"><h4>OMIM</h4></th><th><h4>Name</h4></th><th class="span3"><h4>Connections</h4></th>
                        </thead>
                        <tbody>

                        </tbody>
                    </table>
                </div>
                <div class="tab-pane" id="e"><h2>E</h2>
                    <table id="e_list" class="table table-striped">
                        <thead>
                        <th class="span1"><h4>OMIM</h4></th><th><h4>Name</h4></th><th class="span3"><h4>Connections</h4></th>
                        </thead>
                        <tbody>

                        </tbody>
                    </table>
                </div>
                <div class="tab-pane" id="f"><h2>F</h2>
                    <table id="f_list" class="table table-striped">
                        <thead>
                        <th class="span1"><h4>OMIM</h4></th><th><h4>Name</h4></th><th class="span3"><h4>Connections</h4></th>
                        </thead>
                        <tbody>

                        </tbody>
                    </table>
                </div>
                <div class="tab-pane" id="g"><h2>G</h2>
                    <table id="g_list" class="table table-striped">
                        <thead>
                        <th class="span1"><h4>OMIM</h4></th><th><h4>Name</h4></th><th class="span3"><h4>Connections</h4></th>
                        </thead>
                        <tbody>

                        </tbody>
                    </table>
                </div>
                <div class="tab-pane" id="h"><h2>H</h2>
                    <table id="h_list" class="table table-striped">
                        <thead>
                        <th class="span1"><h4>OMIM</h4></th><th><h4>Name</h4></th><th class="span3"><h4>Connections</h4></th>
                        </thead>
                        <tbody>

                        </tbody>
                    </table>
                </div>
                <div class="tab-pane" id="i"><h2>I</h2>
                    <table id="i_list" class="table table-striped">
                        <thead>
                        <th class="span1"><h4>OMIM</h4></th><th><h4>Name</h4></th><th class="span3"><h4>Connections</h4></th>
                        </thead>
                        <tbody>

                        </tbody>
                    </table>
                </div>
                <div class="tab-pane" id="j"><h2>J</h2>
                    <table id="j_list" class="table table-striped">
                        <thead>
                        <th class="span1"><h4>OMIM</h4></th><th><h4>Name</h4></th><th class="span3"><h4>Connections</h4></th>
                        </thead>
                        <tbody>

                        </tbody>
                    </table>
                </div>
                <div class="tab-pane" id="k"><h2>K</h2>
                    <table id="k_list" class="table table-striped">
                        <thead>
                        <th class="span1"><h4>OMIM</h4></th><th><h4>Name</h4></th><th class="span3"><h4>Connections</h4></th>
                        </thead>
                        <tbody>

                        </tbody>
                    </table>
                </div>
                <div class="tab-pane" id="l"><h2>L</h2>
                    <table id="l_list" class="table table-striped">
                        <thead>
                        <th class="span1"><h4>OMIM</h4></th><th><h4>Name</h4></th><th class="span3"><h4>Connections</h4></th>
                        </thead>
                        <tbody>

                        </tbody>
                    </table>
                </div>
                <div class="tab-pane" id="m"><h2>M</h2>
                    <table id="m_list" class="table table-striped">
                        <thead>
                        <th class="span1"><h4>OMIM</h4></th><th><h4>Name</h4></th><th class="span3"><h4>Connections</h4></th>
                        </thead>
                        <tbody>

                        </tbody>
                    </table>
                </div>
                <div class="tab-pane" id="n"><h2>N</h2>
                    <table id="n_list" class="table table-striped">
                        <thead>
                        <th class="span1"><h4>OMIM</h4></th><th><h4>Name</h4></th><th class="span3"><h4>Connections</h4></th>
                        </thead>
                        <tbody>

                        </tbody>
                    </table>
                </div>
                <div class="tab-pane" id="o"><h2>O</h2>
                    <table id="o_list" class="table table-striped">
                        <thead>
                        <th class="span1"><h4>OMIM</h4></th><th><h4>Name</h4></th><th class="span3"><h4>Connections</h4></th>
                        </thead>
                        <tbody>

                        </tbody>
                    </table>
                </div>
                <div class="tab-pane" id="p"><h2>P</h2>
                    <table id="p_list" class="table table-striped">
                        <thead>
                        <th class="span1"><h4>OMIM</h4></th><th><h4>Name</h4></th><th class="span3"><h4>Connections</h4></th>
                        </thead>
                        <tbody>

                        </tbody>
                    </table>
                </div>
                <div class="tab-pane" id="q"><h2>Q</h2>
                    <table id="q_list" class="table table-striped">
                        <thead>
                        <th class="span1"><h4>OMIM</h4></th><th><h4>Name</h4></th><th class="span3"><h4>Connections</h4></th>
                        </thead>
                        <tbody>

                        </tbody>
                    </table>
                </div>
                <div class="tab-pane" id="r"><h2>R</h2>
                    <table id="r_list" class="table table-striped">
                        <thead>
                        <th class="span1"><h4>OMIM</h4></th><th><h4>Name</h4></th><th class="span3"><h4>Connections</h4></th>
                        </thead>
                        <tbody>

                        </tbody>
                    </table>
                </div>
                <div class="tab-pane" id="s"><h2>S</h2>
                    <table id="s_list" class="table table-striped">
                        <thead>
                        <th class="span1"><h4>OMIM</h4></th><th><h4>Name</h4></th><th class="span3"><h4>Connections</h4></th>
                        </thead>
                        <tbody>

                        </tbody>
                    </table>
                </div>
                <div class="tab-pane" id="t"><h2>T</h2>
                    <table id="t_list" class="table table-striped">
                        <thead>
                        <th class="span1"><h4>OMIM</h4></th><th><h4>Name</h4></th><th class="span3"><h4>Connections</h4></th>
                        </thead>
                        <tbody>

                        </tbody>
                    </table>
                </div>
                <div class="tab-pane" id="u"><h2>U</h2>
                    <table id="u_list" class="table table-striped">
                        <thead>
                        <th class="span1"><h4>OMIM</h4></th><th><h4>Name</h4></th><th class="span3"><h4>Connections</h4></th>
                        </thead>
                        <tbody>

                        </tbody>
                    </table>
                </div>
                <div class="tab-pane" id="v"><h2>V</h2>
                    <table id="v_list" class="table table-striped">
                        <thead>
                        <th class="span1"><h4>OMIM</h4></th><th><h4>Name</h4></th><th class="span3"><h4>Connections</h4></th>
                        </thead>
                        <tbody>

                        </tbody>
                    </table>
                </div>
                <div class="tab-pane" id="w"><h2>W</h2>
                    <table id="w_list" class="table table-striped">
                        <thead>
                        <th class="span1"><h4>OMIM</h4></th><th><h4>Name</h4></th><th class="span3"><h4>Connections</h4></th>
                        </thead>
                        <tbody>

                        </tbody>
                    </table>
                </div>
                <div class="tab-pane" id="x"><h2>X</h2>
                    <table id="x_list" class="table table-striped">
                        <thead>
                        <th class="span1"><h4>OMIM</h4></th><th><h4>Name</h4></th><th class="span3"><h4>Connections</h4></th>
                        </thead>
                        <tbody>

                        </tbody>
                    </table>
                </div>
                <div class="tab-pane" id="y"><h2>Y</h2>
                    <table id="y_list" class="table table-striped">
                        <thead>
                        <th class="span1"><h4>OMIM</h4></th><th><h4>Name</h4></th><th class="span3"><h4>Connections</h4></th>
                        </thead>
                        <tbody>

                        </tbody>
                    </table>
                </div>
                <div class="tab-pane" id="z"><h2>Z</h2>
                    <table id="z_list" class="table table-striped">
                        <thead>
                        <th class="span1"><h4>OMIM</h4></th><th><h4>Name</h4></th><th class="span3"><h4>Connections</h4></th>
                        </thead>
                        <tbody>

                        </tbody>
                    </table>
                </div>
            </div>
        </div> 
    </s:layout-component>
</s:layout-render>
