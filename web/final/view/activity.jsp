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
                            <li class="active"><a href="#">Activity</a></li>
                        </ul>
                    </div><!--/.nav-collapse -->
                </div>
                <ul class="nav pull-right top_menu">
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
        <h3>Activity Tracking</h3>
            <table id="status_list" class="table table-condensed table-striped">
                <thead>
                    <tr>
                        <th class="span3">Timestamp</th><th class="span2">Action</th><th class="span4">Query</th><th class="span2">IP</th>
                    </tr>
                </thead>
            </table>
        </div>
    </s:layout-component>
    <s:layout-component name="custom_scripts">
        <script src="<c:url value="/final/assets/script/jquery.dataTables.min.js" />"></script>
        <script>   
            var loaded = new Array();
            /* Default class modification */
            $.extend( $.fn.dataTableExt.oStdClasses, {
                "sSortAsc": "header headerSortDown",
                "sSortDesc": "header headerSortUp",
                "sSortable": "header"
            } );

            /* Custom datatables connection sorting */
            jQuery.fn.dataTableExt.aTypes.unshift( function ( sData )
            {
                sData = typeof sData.replace == 'function' ?
                    sData.replace( /<[\s\S]*?>/g, "" ) : sData;
                sData = $.trim(sData);
      
                var sValidFirstChars = "0123456789-";
                var sValidChars = "0123456789.";
                var Char;
                var bDecimal = false;
      
                /* Check for a valid first char (no period and allow negatives) */
                Char = sData.charAt(0);
                if (sValidFirstChars.indexOf(Char) == -1)
                {
                    return null;
                }
      
                /* Check all the other characters are valid */
                for ( var i=1 ; i<sData.length ; i++ )
                {
                    Char = sData.charAt(i);
                    if (sValidChars.indexOf(Char) == -1)
                    {
                        return null;
                    }
          
                    /* Only allowed one decimal place... */
                    if ( Char == "." )
                    {
                        if ( bDecimal )
                        {
                            return null;
                        }
                        bDecimal = true;
                    }
                }
      
                return 'num-html';
            } );

            jQuery.extend( jQuery.fn.dataTableExt.oSort, {
                "num-html-pre": function ( a ) {
                    var x = String(a).replace( /<[\s\S]*?>/g, "" );
                    return parseFloat( x );
                },
 
                "num-html-asc": function ( a, b ) {
                    return ((a < b) ? -1 : ((a > b) ? 1 : 0));
                },
 
                "num-html-desc": function ( a, b ) {
                    return ((a < b) ? 1 : ((a > b) ? -1 : 0));
                }
            } );
            //


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
                        $(els[0]).bind( 'click.DT', {
                            action: "previous"
                        }, fnClickHandler );
                        $(els[1]).bind( 'click.DT', {
                            action: "next"
                        }, fnClickHandler );
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

            /** 
             * Update pagination
             */
            (function($, window, document) {
 
 
                $.fn.dataTableExt.oApi.fnPagingInfo = function ( oSettings )
                {
                    return {
                        "iStart":         oSettings._iDisplayStart,
                        "iEnd":           oSettings.fnDisplayEnd(),
                        "iLength":        oSettings._iDisplayLength,
                        "iTotal":         oSettings.fnRecordsTotal(),
                        "iFilteredTotal": oSettings.fnRecordsDisplay(),
                        "iPage":          Math.ceil( oSettings._iDisplayStart / oSettings._iDisplayLength ),
                        "iTotalPages":    Math.ceil( oSettings.fnRecordsDisplay() / oSettings._iDisplayLength )
                    };
                }
 
                var DT_PagingControl = function ( oDTSettings )
                {
                    oDTSettings.aoDrawCallback.push( {
                        "fn": function () {
                            var bShow = oDTSettings.oInstance.fnPagingInfo().iTotalPages > 1;
                            for ( var i=0, iLen=oDTSettings.aanFeatures.p.length ; i<iLen ; i++ ) {
                                oDTSettings.aanFeatures.p[i].style.display = bShow ? "block" : "none";
                            }
                        },
                        "sName": "PagingControl"
                    } );
                }
 
                if ( typeof $.fn.dataTable == "function" &&
                    typeof $.fn.dataTableExt.fnVersionCheck == "function" &&
                    $.fn.dataTableExt.fnVersionCheck('1.8.0') )
                {
                    $.fn.dataTableExt.aoFeatures.push( {
                        "fnInit": function( oDTSettings ) {
                            new DT_PagingControl( oDTSettings );
                        },
                        "cFeature": "P",
                        "sFeature": "PagingControl"
                    } );
                }
                else
                {
                    alert( "Warning: PagingControl requires DataTables 1.8.0 or greater - www.datatables.net/download");
                }
 
 
            })(jQuery, window, document);
 
           
            /** 
             * Launch datatables on given element
             **/
            function dtables(id) {
                $(id).dataTable({
                    "bProcessing": true,
                    "sAjaxSource": './services/browse/' + window.location.hash.charAt(window.location.hash.length - 1) + '.js',                
                    "bSort": true,
                    "aaSorting": [[2, 'desc']],
                    "bPaginate": true,
                    "sPaginationType": "bootstrap",
                    "bStateSave": true,
                    "sDom": "<'row-fluid'<'span6'l><'span6'f>r>t<'row-fluid'<'span6'i><'span6'pP>>",
                    "oLanguage": {
                        "sZeroRecords": "No diseases to display",
                        "sInfo": "Listing _START_ to _END_ of _TOTAL_ diseases",
                        "sInfoFiltered": " - filtering from _MAX_ diseases",
                        "sLoadingRecords": "Please wait, loading diseases...",
                        "sSearch": "",
                        "sLengthMenu": '<select>'+
                            '<option value="10">10</option>'+
                            '<option value="20">20</option>'+
                            '<option value="50">50</option>'+
                            '<option value="100">100</option>'+
                            '<option value="200">200</option>'+
                            '<option value="-1">All</option>'+
                            '</select> diseases per page'
                    },
                    "iDisplayLength": 10,
                    "bAutoWidth" : false,
                    "sCookiePrefix": "diseasecard_browse_",
                    "aoColumnDefs": [
                        {
                            "sClass": "connections", 
                            "aTargets": [ 2 ]
                        }
                    ],
                    "fnInitComplete": function() {                            
                        loaded.push(window.location.toString());
                        $(window.location.hash).tooltip({
                            selector: "*[rel=tooltip]"
                        });
                        $('.dataTables_filter input').attr('placeholder','Filter').addClass('input-large').focus();
                    }
                }); 
            }

            $(document).ready(function(){  
                $('#status_list').dataTable({
                    "bProcessing": true,
                    "sAjaxSource": './services/status/1000.js',                
                    "bSort": true,
                    "aaSorting": [[0, 'desc']],
                    "bPaginate": true,
                    "sPaginationType": "bootstrap",
                    "bStateSave": true,
                    "sDom": "<'row-fluid'<'span6'l><'span6'f>r>t<'row-fluid'<'span6'i><'span6'pP>>",
                    "oLanguage": {
                        "sZeroRecords": "No activity to display",
                        "sInfo": "Listing _START_ to _END_ of _TOTAL_ entries",
                        "sInfoFiltered": " - filtering from _MAX_ entries",
                        "sLoadingRecords": "Please wait, loading entries...",
                        "sSearch": "",
                        "sLengthMenu": '<select>'+
                            '<option value="10">10</option>'+
                            '<option value="20">20</option>'+
                            '<option value="50">50</option>'+
                            '<option value="100">100</option>'+
                            '<option value="200">200</option>'+
                            '<option value="-1">All</option>'+
                            '</select> entries per page'
                    },
                    "iDisplayLength": 100,
                    "bAutoWidth" : false,
                    "sCookiePrefix": "diseasecard_status_"
                }); 
                
                             $(document).ready(function(){                
                $('.mag').click(function() {
                    toggleTopButton('mag');
                    setTimeout(function(){                            
                        $('#text_search').focus();
                    }, 400);                    
                });                   
            });  
            });
        </script>
    </s:layout-component>
</s:layout-render>
