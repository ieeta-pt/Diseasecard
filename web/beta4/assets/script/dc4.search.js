var results_item_markup = '<div class="results_item" data-id="\${id}"><span class="id disease_link tooltip" data-tooltip="Go to \${id}" data-omim="\${id}">\${id}</span><br /><span class="name tooltip" data-tooltip="Go to \${id}" data-omim="\${id}">\${name}</span><a href="http://omim.org/entry/\${id}" class="omim tooltip" data-tooltip="Go to \${id} OMIM page" target="_blank" title="\${name}">OMIM</a><a data-tooltip="Show \${id} links" class="links tooltip" data-id="\${id}">Links</a><div class="info" id="info-\${id}"></div></div>';
             
$(document).ready(function(){
    /** on page filtering **/
    $('#filter').keyup(function(){
        var value = $(this).attr('value');
        if(value.length >= 3) {
            $('.results_item').each(function(){
                if ($(this).find('.name').html().toLowerCase().indexOf(value) === -1) {
                    $(this).hide();
                }
            });
        } else {
            $('.results_item').show();
        }
    });
    
    $.template('results', results_item_markup);
    $('.disease_link').live('click', function(){
        window.location = path + '/disease/' + $(this).data('omim');
    });
                
    $('.name').live('click', function(){
        window.location = path + '/disease/' + $(this).data('omim');
    });
                
                
    $('.mag').click(function() {
        if( ($(this).data('active')).toString() == 'false') {
            $('.search').show();
            $(this).addClass('mag_enabled');
            $(this).data('active', 'true');
            $('#text_search').focus();
        } else if(($(this).data('active')).toString() == 'true') {
            $('.search').hide();
            $(this).data('active', 'false');
            $(this).removeClass('mag_enabled');
        }
    });
                
                
    $('.cancel').click(function(){
        $('.search').toggle();
        $('.mag').data('active', 'false');
        $('.mag').toggleClass('mag_enabled');                                     
                    
    });
                
    $('.links').live('click', function() {
        $('#info-' + $(this).data('id')).slideToggle();
    });
                
                
    $('.close').live('click', function() {
        $(this).parent().slideUp();
    });
    
    $('.solr_link').live('click', function() {
        var item = $('.disease_link').first().data('omim');
        if(item != undefined) {
            window.location = path + '/disease/' + item + '#' + $(this).data('link')
        } else {
            window.location = $(this).data('link');
        }
    })
    
    $('.internal_link').live('click', function() {
       window.location = path + '/disease/' + $(this).data('omim') + '#' + $(this).data('type') + ':' + $(this).data('id'); 
    });
});