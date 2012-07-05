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

var links = {
    // Disease
    omim: 'http://omim.org/entry/#replace#',
    orphanet: 'http://www.pdb.org/pdb/explore/explore.do?structureId=#replace#',
    nord: 'http://www.rarediseases.org/rare-disease-information/rare-diseases/viewSearchResults?term=#replace#',
    // Protein
    uniprot: 'http://www.uniprot.org/uniprot/#replace#',
    pdb: 'http://www.pdb.org/pdb/explore/explore.do?structureId=#replace#',
    prosite: 'http://prosite.expasy.org/cgi-bin/prosite/prosite-search-ac?#replace#',
    interpro: 'http://wwwdev.ebi.ac.uk/interpro/ISearch?query=#replace#',
    //Literature
    pubmedsearch: 'http://www.ncbi.nlm.nih.gov/pubmed?term=#replace#',
    pubmed: 'http://www.ncbi.nlm.nih.gov/pubmed/#replace#',
    omimref: 'http://omim.org/entry/#replace##references',
    // Locus
    hgnc: 'http://www.genenames.org/data/hgnc_data.php?match=#replace#',
    genecards: 'http://www.genecards.org/cgi-bin/carddisp.pl?gene=#replace#',
    ensembl: 'http://www.ensembl.org/Homo_sapiens/Gene/Summary?db=core;g=#replace#',
    entrez: 'http://www.ncbi.nlm.nih.gov/sites/entrez?db=gene&term=#replace#',
    // Drug
    pharmgkb: 'http://www.pharmgkb.org/do/serve?objId=#replace#',
    // Ontology
    mesh: 'http://www.nlm.nih.gov/cgi/mesh/2011/MB_cgi?term=#replace#&field=uid&exact=Find%20Exact%20Term',
    hp: 'http://www.berkeleybop.org/obo/#replace#.pro',
    // Variome
    wave: 'http://bioinformatics.ua.pt/WAVe/gene/#replace#'
// Internals
}

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
    var tmp = pair.split(':');
    var link = links[tmp[0]].replace('#replace#',tmp[1]);
    return '<iframe src="' + link + '" id="_content" width="100%" height="100%" />'    
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

/**
 * Parse Twitter strings for interoperable content.
 */
function formatTwitString(str) {
    str=' '+str;
    str = str.replace(/((ftp|https?):\/\/([-\w\.]+)+(:\d+)?(\/([\w/_\.]*(\?\S+)?)?)?)/gm,'<a href="$1" target="_blank">$1</a>');
    str = str.replace(/([^\w])\@([\w\-]+)/gm,'$1@<a href="http://twitter.com/$2" target="_blank">$2</a>');
    str = str.replace(/([^\w])\#([\w\-]+)/gm,'$1<a href="http://twitter.com/search?q=%23$2" target="_blank">#$2</a>');
    return str;
}

/** 
 * Relative time handler
 */
function relativeTime(pastTime) {	
    var origStamp = Date.parse(pastTime);
    var curDate = new Date();
    var currentStamp = curDate.getTime();	
    var difference = parseInt((currentStamp - origStamp)/1000);
    if(difference < 0)              return false;
    if(difference <= 5)             return "Now";
    if(difference <= 20)            return "Instants ago";
    if(difference <= 60)            return "A minute ago";
    if(difference < 3600)           return parseInt(difference/60)+" minutes ago";
    if(difference <= 1.5*3600)      return "One hour ago";
    if(difference < 23.5*3600)      return Math.round(difference/3600)+" hours ago";
    if(difference < 1.5*24*3600)    return "One day ago";
    var dateArr = pastTime.split(' ');
    return dateArr[4].replace(/\:\d+$/,'')+' '+dateArr[2]+' '+dateArr[1]+(dateArr[3]!=curDate.getFullYear()?' '+dateArr[3]:'');
}

function loadSolr(id) {
    $('#solr_search').spin(spin_opts);        
    var uri = encodeURI('http://bioinformatics.ua.pt/solr/select/?q="' + id + '"&rows=100&wt=json');
    $.ajax({
        url: uri,
        success: function(data) {
            if(data.response.numFound != 0) {
                $('#solr_size').html('<div id="solr_size">' + data.response.numFound + ' free text matches</div>');
                $('#solr_content').html('<div id="solr_results"></div>');
                for(var o in data.response.docs) {
                    $('#solr_results').append('<div class="results_item"><span class="solr_id"><a class="solr_link tooltip" data-tooltip="View link in Diseasecard" data-link="' + data.response.docs[o].id +'">' + data.response.docs[o].title +  '</a></span><br /><span class="solr_name"><a class="solr_link tooltip" data-tooltip="Open link in new page" data-link="'  + data.response.docs[o].id +'">'  + data.response.docs[o].id + '</a></span><a class="external tooltip" data-tooltip="Open link in new page" target="_blank" href="' + data.response.docs[o].id + '"></a></div>');
                }     
            } else {
                $('#solr_results').html('<div id="error"><h1>Diseasecard found no matching links/h1><div id="busy"></div></div>');
            }
            $('#solr_search').spin(false);   
            // add tooltips to results
            $('.tooltip').tipsy({
                title: function() {
                    return $(this).data('tooltip');
                },
                fade: true,
                live: true,
                delayIn: 344,
                gravity: $.fn.tipsy.autoWE
            });            
        },
        dataType: 'jsonp',
        jsonp: 'json.wrf'
    });
    
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
            window.location = path + '/disease/' + data.id;
        } else  if (data.status === "140") {
            $('#results_content').html('<div id="error"><h1>You search query must be at least 3 characters long</h1><div id="busy"></div></div>');
        } else { 
            $('#results_content').html('<div id="results"></div>');
            $('#results_size').html(data.size + ' disease matches');
            $.tmpl('results', data.results).appendTo('#results');                  
            $.tmpl('results_others', data.results).appendTo('#others');
            loadLinks();
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
                window.location = path + '/disease/' + $('#text_search').data('omim');                
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
        $( "#text_search" ).val( ui.item.info );
        return false;
        },
        select: function( event, ui ) {
        event.preventDefault();
        $('#text_search').data('omim',ui.item.omim);
        window.location = path + '/disease/' + ui.item.omim;       
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