$(document).ready(function(){
    $('#button_search').click(function(){
        window.location = path + '/search/' + $('#text_search').attr('value');
    });
    
    /** disease meta tooltips **/
    $('.tooltip').tipsy({
        title: function() {
            return $(this).data('tooltip');
        },
        fade: true,
        live: true,
        delayIn: 344
    });
    
    $('.search_help').click(function() {
        $('#text_search').attr('value', $(this).text()); 
    });
    $('#text_search').focus();
    
    setTimeout(function() {
        if($('#text_search').attr('value') == '') {
            $('#helpers').fadeIn('medium');
        }
    }, 2880);
});