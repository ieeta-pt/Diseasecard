var jsontree = {};

/** 
 * Capitalizes given string.
 * 
 * @returns {@exp;@call;@call;toUpperCase}
 */
String.prototype.capitalize = function() {
    return this.charAt(0).toUpperCase() + this.slice(1);
};

/**
 * Check if given string ends with pattern.
 * 
 * @param {String} pattern
 * @returns {Boolean} for match.
 */
String.prototype.endsWith = function(pattern) {
    var d = this.length - pattern.length;
    return d >= 0 && this.lastIndexOf(pattern) === d;
};

/**
 * Check if given string starts with pattern.
 * 
 * @param {type} str
 * @returns {Boolean} match
 */
String.prototype.startsWith = function(str) 
{
    return (this.match("^"+str) === str);
};

/**
 * Check if element is present in Array.
 * 
 * @param {type} needle
 * @returns {Boolean} contains
 */
Array.prototype.contains = function ( needle ) {
    for (i in this) {
        if (this[i] === needle) return true;
    }
    return false;
};

/**
 * Get an empty _content frame.
 * 
 * @returns {String}
 */
function getFrame() {
    return '<iframe src="" id="_content" width="100%" height="100%" />';
};

/**
 * Gets content frame with LiveView link.
 * 
 * @param {type} pair
 * @returns {String}
 */
function getFrame(pair) {
    return '<iframe src="' + path + '/services/linkout/' + pair + '" id="_content" width="100%" height="100%" />';
};

/**
 * Returns item in <key>_<value> format for given URI.
 * 
 * @param {type} uri
 * @returns {unresolved}
 */
function uri2item(uri) {
    var addr = uri.split('/');
    return addr[addr.length - 1];
};


function toggleTopButton(id) {
    switch(id) {
        case 'mag':
            if( ($('.mag').data('active')).toString() === 'false') {
                hideTopButton('syns');
                $('.search').fadeIn(300);
                $('.mag').addClass('mag_enabled');
                $('.mag').data('active', 'true');
            } else if(($('.mag').data('active')).toString() === 'true') {
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
    if( (i.data('active')).toString() === 'false') {
        i.addClass('mag_enabled').data('active', 'true');
        $('.search').fadeIn(300);
        setTimeout(function() {
            if($('#text_search').val() === '') {
                $('.search').fadeOut('slow');
                i.data('active', 'false');
                i.removeClass('mag_enabled');
            }
        },10000);
    } else if((i.data('active')).toString() === 'true') {
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

$(document).ready(function(){
    // bootstrap tooltip loader
    $('*[rel=tooltip]').tooltip();
    
    /** autocomplete handler **/
    $('#text_search').keypress(function(e){
        if ( e.which == 13 ) {
            e.preventDefault();
            if($('#text_search').data('omim') != undefined) {
                window.location = path + '/entry/' + $('#text_search').data('omim');                
            } else {
                window.location = path + '/search/' + search + '/' + $('#text_search').val();
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