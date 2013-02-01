$(document).ready(function(){
    // search button action
    $('#button_search').click(function(){
        window.location = path + '/search/' + $('#text_search').attr('value');
    });
    
    // bootstrap tooltip loader
    $('body').tooltip({
        selector: "a[rel=tooltip]"
    })
    
    $('.search_help').click(function() {
        $('#text_search').attr('value', $(this).text()); 
    });
    $('#text_search').focus();
    
    var tour = new Tour({
                    name: "diseasecard_index_tour",
                    keyboard: true
                });
    
    tour.addStep({
        next: 1, 
        animation: true,
        placement: 'top',
        element: "#home_search",
        title: "Search", 
        content: "Diseasecard starts here!<br />Type to <strong>search for anything</strong> related to rare diseases<br/>" 
    });
    tour.addStep({
        prev: 0, // number
        animation: true,
        placement: 'bottom',
        element: "#browsing",
        title: "Browse", 
        content: "Access the rare diseases Top 10 or <strong><a href='browse'>browse</a></strong> all diseases<br/>"
    });
    tour.start();
    setTimeout(function() {
        if($('#text_search').attr('value') == '') {
            $('#helper').fadeIn('medium');
        }
    }, 2880);
});