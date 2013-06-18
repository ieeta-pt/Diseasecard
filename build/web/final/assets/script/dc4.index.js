$(document).ready(function(){
    // search button action
    $('#button_search').click(function(){
        window.location = path + '/search/' + search + '/' + $('#text_search').val();
    });
    
    // handle multiple search options
    $('#search_id').change(function(){
          if($(this).is(":checked")) {
              $('#search_full').attr('checked',false);
              search = 'id';
          } else {              
              $('#search_full').attr('checked',true);
              search = 'full';
          }
    })
    
    $('#search_full').change(function(){
          if($(this).is(":checked")) {
              $('#search_id').attr('checked',false);
              search = 'full';
          } else {              
              $('#search_id').attr('checked',true);
              search = 'id';
          }
    })
    
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
        animation: true,
        placement: 'right',
        element: "#home_form",
        title: "Advanced search", 
        content: "Select between <strong>identifier only</strong> or <strong>full text</strong> search<br/>",
        onShow: function (tour) {
            $('#button_search_toggle').click();
        }
    });
    tour.addStep({
        animation: true,
        placement: 'bottom',
        element: "#browsing",
        title: "Browse", 
        content: "Access the rare diseases Top 10 or <strong><a href='browse'>browse</a></strong> all diseases<br/>"
    });
    tour.start();
    
    $('#text_search').focus();
    
    // load help text if there are no searches
    setTimeout(function() {
        if($('#text_search').val() == '') {
            $('#helper').fadeIn(1000);
        }
    }, 4880);
});