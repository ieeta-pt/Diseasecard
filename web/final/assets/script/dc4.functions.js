var jsontree = {};

String.prototype.capitalize = function() {
    return this.charAt(0).toUpperCase() + this.slice(1);
}
String.prototype.endsWith = function(pattern) {
    var d = this.length - pattern.length;
    return d >= 0 && this.lastIndexOf(pattern) === d;
};
String.prototype.startsWith = function(str) 
{
    return (this.match("^"+str)==str)
}
Array.prototype.contains = function ( needle ) {
    for (i in this) {
        if (this[i] == needle) return true;
    }
    return false;
}

function getFrame() {
    return '<iframe src="" id="_content" width="100%" height="100%" />';
}

function getFrame(pair) {
    return '<iframe src="' + path + '/services/linkout/' + pair + '" id="_content" width="100%" height="100%" />'    
}

function uri2item(uri) {
    var addr = uri.split('/');
    return addr[addr.length - 1];
}
            
function loadResults(id) { 
    var uri = '';
    if (window.location.toString().indexOf('full') > 0) 
    {
        uri = encodeURI(path + '/services/results/full/' + id);  
    } else {
        uri = encodeURI(path + '/services/results/id/' + id);   
    }
    
    $.getJSON(uri, function(data) {
        if(data.status === 110) {   
            $('#loading').fadeOut().remove();         
            $('#errors').fadeIn();  
            $('#alert_noresults').fadeIn();
            toggleTopButton('mag');
            setTimeout(function(){                            
                $('#text_search').focus();
            }, 400); 
        } else if (data.status === 121) {
            window.location = path + '/entry/' + data.results[0].omim;
        } else if (data.status === 140) { 
            $('#loading').fadeOut().remove();
            $('#errors').fadeIn();  
            $('#alert_short').fadeIn();
            toggleTopButton('mag');
            setTimeout(function(){                            
                $('#text_search').focus();
            }, 400); 
        } else { 
            $('#results_size').html(data.size);
            $.tmpl('results', data.results).appendTo('#results_list');                  
            $.each(data.results, function(i, value) {
                var box = $('<div/>', {
                    'class' : 'results_list well', 
                    'data-omim' : value.omim,
                    'id' : value.omim
                });
                box.append('<a href="../../entry/' + value.omim + '"><h3><small><i class="icon-play"></i></small> ' + value.omim + ' <small> ' + value.name + '</small></h3></a>');
                
                $.each(value.links, function(j, link) {
                    var dlink = $('<ul/>', {
                        'class' : 'results_items', 
                        'data-id' : link
                    }).append('<li><i class="icon-angle-right"></i><a href="../../entry/' + value.omim + '#' + link.substring(7, link.length) + '"> ' + link.substring(7, link.length) + '</a></li>');
                    box.append(dlink);
                })                
                $('#results_links').append(box);
            })  
            $('#loading').fadeOut().remove();
            $('#meta').fadeIn(); /*show();*/
            $('#results_list').fadeIn(); /*show();*/
            $('#results').fadeIn(); /*show();*/
            $('#results_search').height($('html').height() - 110);
            $('#results_links').height($('html').height() - 110);
            $('#results').width($('#meta').width());
            var tour = new Tour({
                name: "diseasecard_search_tour",
                keyboard: true
            });
    
            tour.addStep({
                next: 1, 
                animation: true,
                placement: 'right',
                element: "#results_search",
                title: "Identifiers", 
                content: "Browse the results for the OMIM identifiers associated with your <strong>" + id + "</strong> query<br />Identifiers are ordered by <strong>relevance</strong><br/>" 
            });
            tour.addStep({
                prev: 0, // number
                animation: true,
                placement: 'left',
                element: "#results_links",
                title: "Full results", 
                content: "View and access all the pages where <strong>" + id + "</strong> was found<br/>"
            });
            tour.addStep({
                prev: 0, // number
                animation: true,
                placement: 'bottom',
                element: "#tour_filter",
                title: "Filter", 
                content: "Reduce the result set by adding new <strong>filtering</strong> criteria<br/>"
            });
            tour.start();
            $('#filter').focus();
        }        
    });     
}

function toggleTopButton(id) {
    switch(id) {
        case 'mag':
            if( ($('.mag').data('active')).toString() == 'false') {
                hideTopButton('syns');
                $('.search').fadeIn(300);
                $('.mag').addClass('mag_enabled');
                $('.mag').data('active', 'true');
            } else if(($('.mag').data('active')).toString() == 'true') {
                $('.search').hide();
                $('.mag').data('active', 'false');
                $('.mag').removeClass('mag_enabled');
            }
            break;
        case 'syns':
            hideTopButton('mag');
            $('.synonyms').toggle();
            break;
    }
}

function showSearch(i) {
    if( (i.data('active')).toString() == 'false') {
        i.addClass('mag_enabled').data('active', 'true');
        $('.search').fadeIn(300);
        setTimeout(function() {
            if($('#text_search').attr('value') === '') {
                $('.search').fadeOut('slow');
                i.data('active', 'false');
                i.removeClass('mag_enabled');
            }
        },10000);
    } else if((i.data('active')).toString() == 'true') {
        $('.search').hide();
        i.data('active', 'false');
        i.removeClass('mag_enabled');
    }
}

function hideTopButton(id) {
    switch(id) {
        case 'mag':
            $('.search').fadeOut(300);
            $('.mag').data('active', 'false');
            $('.mag').removeClass('mag_enabled');
            break;
        case 'syns':
            $('.synonyms').hide();
            break;
    }
}

function hideTopButtons() {   
    $('.search').hide();
    $('.mag').data('active', 'false');
    $('.mag').removeClass('mag_enabled');
    $('.synonyms').hide();
}

function showTopButton(id) {
    switch(id) {
        case 'mag':
            hideTopButton('syns'); 
            $('.search').show();
            $('.mag').addClass('mag_enabled');
            $('.mag').data('active', 'true');
            $('#text_search').focus();
            break;
        case 'syns':
            $('.synonyms').show();     
            hideTopButton('syns'); 
            break;
    }
}
jQuery.extend({
    random: function(X) {
        return Math.floor(X * (Math.random() % 1));
    },
    randomBetween: function(MinV, MaxV) {
        return MinV + jQuery.random(MaxV - MinV + 1);
    }
});

$(document).ready(function(){
    // bootstrap tooltip loader
    $('body').tooltip({
        selector: "*[rel=tooltip]"
    })
    
    /** autocomplete handler **/
    $('#text_search').keypress(function(e){
        if ( e.which == 13 ) {
            e.preventDefault();
            if($('#text_search').data('omim') != undefined) {
                window.location = path + '/entry/' + $('#text_search').data('omim');                
            } else {
                window.location = path + '/search/' + search + '/' + $('#text_search').attr('value');
            }
        }         
    });
    
    $( "#text_search" ).autocomplete({
        minLength: 3,
        delay: 500,
        source: path + '/services/autocomplete/' + search,
        focus: function( event, ui ) {            
        $('#text_search').data('omim',ui.item.omim);
        $("#text_search" ).val( ui.item.info );
        return false;
        },
        open :function(){
        $(this).autocomplete('widget').css('z-index', 1000);
        return false;        },
        select: function( event, ui ) {
        event.preventDefault();
        $('#text_search').data('omim',ui.item.omim);
        if (ui.item.info.indexOf(':') > 0) {
        window.location = path + '/entry/' + ui.item.omim + '#' + ui.item.info.replace('[','').replace(']','');       
        } else {
        window.location = path + '/entry/' + ui.item.omim;
        }
        }
        })
    .data( "autocomplete" )._renderItem = function( ul, item ) {
        return $( "<li></li>" )
        .data( "item.autocomplete", item )
        .data("item.omim",item.omim)
        .append( '<a>' + item.omim + ' <i class="icon-angle-right"></i> '  + item.info + '</a>' )
        .appendTo( ul );
    };     
});