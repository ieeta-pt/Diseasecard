var spin_opts = {
    lines: 8, // The number of lines to draw
    length: 8, // The length of each line
    width: 3, // The line thickness
    radius: 6, // The radius of the inner circle
    color: '#000', // #rgb or #rrggbb
    speed: 1, // Rounds per second
    trail: 64, // Afterglow percentage
    shadow: false // Whether to render a shadow
};

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

function generateReport() {
    var report = '<div class="report"><div class="report_tools"><a class="show tooltip" data-tooltip="Show all report links">Show all links</a><a class="hide tooltip" data-tooltip="Hide all report links">Hide all links</a></div>';
    var content = jsontree.children;
    for(var i in content) {
        report += '<div class="report_box" data-entity="' + content[i].id + '"><div class="report_box_entity">' + content[i].name;
        for (var j in content[i].children) {
            report += '<div class="report_box_concept"><div class="report_box_concept_title" data-concept="' + content[i].children[j].id + '">' + content[i].children[j].name + '</div>  <div class="report_box_items" id="' + content[i].children[j].id + '">';
            for(var k in content[i].children[j].children) {
                report += '<div class="report_box_item">' + content[i].children[j].children[k].name + '</div>'
            }
            report += '</div></div>'
        }
        report += '</div></div>'
    }
    
    report += '</div>';
    return report;
    s
}

function getFrame() {
    return '<iframe src="" id="_content" width="100%" height="100%" />';
}

function getFrame(pair) {
    return '<iframe src="' + path + '/services/linkout/' + pair + '" id="_content" width="100%" height="100%" />'    
}

function correctDiseaseName(name) {
    name = name.replace('{','').replace('}','').replace('[','').replace(']','');
    if(name.endsWith('1') || name.endsWith('2') || name.endsWith('3') || name.endsWith('4') || name.endsWith('5') || name.endsWith('6') || name.endsWith('7') || name.endsWith('8') || name.endsWith('') ) {
        name = name.substr(0, name.length - 2);
    }
    return name;  
}

function uri2item(uri) {
    var addr = uri.split('/');
    return addr[addr.length - 1];
}

function debug(what) {
    $('#debug').remove();
    var div = '<div id="debug">' + what + '</div>';
    $('body').append(div);
}


function loadLinks() {
    $('.links').each(function () {
        var id = $(this).data('id');
        $.ajax({
            url: path + '/service/links/' + id + '.html',
            async: true,
            success: function(data) {
                $('#info-' + id).html(data);
            }
        });    
    });
}
            
function loadResults(id) {
    $('#results_search').spin(spin_opts); 
    var uri = encodeURI(path + '/view/results/search/' + id);
    $.getJSON(uri, function(data) {
        if(data.status === '110') {
            $('#results_content').html('<div id="error"><h1>Diseasecard found no matching diseases for "' + id + '"</h1><div id="busy"></div></div>');
        } else if (data.status === "121") {
            window.location = path + '/entry/' + data.results[0].omim;
        } else  if (data.status === "140") {
            $('#results_content').html('<div id="error"><h1>You search query must be at least 3 characters long</h1><div id="busy"></div></div>');
        } else { 
            $('#results_content').html('<div id="results"></div>');
            $('#results_size').html(data.size + ' matches');
            $.tmpl('results', data.results).appendTo('#results');                  
            //$.tmpl('results_others', data.results).appendTo('#others');
            //loadLinks();
        }        
        $('#results_search').spin(false);
        // add tooltips to results
        $('.tooltip').tipsy({
            title: function() {
                return $(this).data('tooltip');
            },
            fade: true,
            live: true,
            delayIn: 344
        });
    }); 
    
}

function toggleTopButton(id) {
    switch(id) {
        case 'mag':
            if( ($('.mag').data('active')).toString() == 'false') {
                hideTopButton('syns');
                $('.search').show();
                $('.mag').addClass('mag_enabled');
                $('.mag').data('active', 'true');
                $('#text_search').focus();
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

function hideTopButton(id) {
    switch(id) {
        case 'mag':
            $('.search').hide();
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
    /** autocomplete handler **/
    $('#text_search').keypress(function(e){
        if ( e.which == 13 ) {
            e.preventDefault();
            if($('#text_search').data('omim') != undefined) {
                window.location = path + '/entry/' + $('#text_search').data('omim');                
            } else {
                window.location = path + '/search/' + $('#text_search').attr('value');
            }
        }         
    });
    
    $( "#text_search" ).autocomplete({
        minLength: 3,
        delay: 500,
        source: path + '/autocomplete',
        focus: function( event, ui ) {            
        $('#text_search').data('omim',ui.item.omim);
        $("#text_search" ).val( ui.item.info );
        return false;
        },
        select: function( event, ui ) {
        event.preventDefault();
        $('#text_search').data('omim',ui.item.omim);
        if (ui.item.info.indexOf(':') > 0) {
        window.location = path + '/entry/' + ui.item.omim + '#' + ui.item.info;       
        } else {
        window.location = path + '/entry/' + ui.item.omim;
        }
        }
        })
    .data( "autocomplete" )._renderItem = function( ul, item ) {
        return $( "<li></li>" )
        .data( "item.autocomplete", item )
        .data("item.omim",item.omim)
        .append( "<a>" + item.omim + " | "  + item.info + "</a>" )
        .appendTo( ul );
    };     
    
    
    /** logo click action **/
    $('.logo').click(function(){
        window.location = path;
    })
    
    /** feedback click action **/
    $('#dc4_feedback_submit').click(function() {
        var email = $('#dc4_feedback_email').attr('value');
        var message = $('#dc4_feedback_message').attr('value');
        if(email != '' && message != '') {
            if(email.indexOf('@') < 1) {
                $('#dc4_feedback_warning').html('Please provide a valid email address');
            } else {
                $.ajax({
                    url: path + '/feedback',
                    async: false,
                    dataType: 'json',
                    success: function(what) {
                        if(what.status == 100) {
                            $('.feedback_form').html('<h1>Thank you for your feedback!</h1>');   
                            setTimeout(function(){                            
                                $('#dc4_feedback_content').slideUp();
                            }, 1280)
                        }
                    },
                    type: 'POST',
                    data: {
                        email: email, 
                        message: message
                    }
                }); 
            }
        } else {
            $('#dc4_feedback_warning').html('Please fill in all fields');
        }
    });
    
    /** Feedback **/
    $('#dc4_feedback').click(function(){
        $('#dc4_feedback_content').slideToggle();
    });
});