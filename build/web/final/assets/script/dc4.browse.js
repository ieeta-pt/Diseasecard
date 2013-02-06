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
        element: "#tabs",
        title: "Browse", 
        content: "Select the starting letter!<br />Try with <a href='#p' id='tour_p'><strong>P</strong></a><br/>" 
    });
    tour.addStep({
        prev: 0, // number
        animation: true,
        placement: 'bottom',
        element: "#tour_target",
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
                    {
                        "sClass": "connections", 
                        "aTargets": [ 2 ]
                    }
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