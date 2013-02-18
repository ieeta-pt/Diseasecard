// OMIM result list template
var results_item_markup = '<li><a data-id="\${omim}" href="#\${omim}" class="opensearch"><i class="icon-chevron-right"></i>\${omim}</a></li>';//<span class="id disease_link" data-tooltip="Go to \${omim}" data-omim="\${omim}">\${omim}</span><br /><span class="name" data-tooltip="Go to \${omim}" data-omim="\${omim}">\${name}</span><a href="http://omim.org/entry/\${id}" class="omim" data-tooltip="Go to \${omim} OMIM page" target="_blank" title="\${name}">OMIM</a><a data-tooltip="Show \${omim} links" class="links" data-id="\${omim}">Links</a><div class="info" id="info-\${omim}"></div></li>';

$(document).ready(function(){
    // event handler to fix components sizes on resize
    $(window).resize(function() {
        if(this.resizeTO) clearTimeout(this.resizeTO);
        this.resizeTO = setTimeout(function() {
            $(this).trigger('resizeEnd');
        }, 50);
    });
    
    $(window).bind('resizeEnd', function() {
        $('#results_search').height($('html').height() - 110);
        $('#results_links').height($('html').height() - 110);
        $('#results').width($('#meta').width());
    });
    
    // page content filter
    $('#filter').live('keyup', function(){
        var value = $(this).attr('value');
        if(value.length >= 3) {
            $('.results_list').each(function(){
                if ($(this).html().toLowerCase().indexOf(value) === -1) {
                    $(this).hide();
                }
            });
        } else {
            $('.results_list').show();
        }
    });
    
    $.template('results', results_item_markup);     
                
    $('.mag').click(function() {
        showSearch($(this));
        setTimeout(function(){                            
            $('#text_search').focus();
        }, 400);                    
    });       

});