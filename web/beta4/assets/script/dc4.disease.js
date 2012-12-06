var labelType, useGradients, nativeTextSupport, animate, jsontree;            
(function() {
    var ua = navigator.userAgent,
    iStuff = ua.match(/iPhone/i) || ua.match(/iPad/i),
    typeOfCanvas = typeof HTMLCanvasElement,
    nativeCanvasSupport = (typeOfCanvas == 'object' || typeOfCanvas == 'function'),
    textSupport = nativeCanvasSupport 
    && (typeof document.createElement('canvas').getContext('2d').fillText == 'function');
    labelType = (!nativeCanvasSupport || (textSupport && !iStuff))? 'Native' : 'HTML';
    nativeTextSupport = labelType == 'Native';
    useGradients = nativeCanvasSupport;
    animate = !(iStuff || !nativeCanvasSupport);
})();

function start() {
    /** tree handling **/
    $.getJSON(path + '/content/' + key +'.js',function(data){
        jsontree = data;
        $('#disease_info').fadeIn('slow');  
        content = data.children;
        var html = '<ul id="dc4_tree" class="tree">';
        for(var i in content) {
            html += '<li class="open library"><span class="open">' + content[i].name + '</span><ul>'
            for (var j in content[i].children) {
                if(content[i].children[j].children) {
                    html += '<li class="closed folder"><span class="open">' + content[i].children[j].name + '</span>';
                    html += '<ul>';
                    for(var k in content[i].children[j].children) {
                        html += '<li id="dc4_t_' + content[i].children[j].children[k].id + '" class="point dc4_t tooltip" data-tooltip="Open \'' + content[i].children[j].children[k].id + '\'">' + content[i].children[j].children[k].name + '</li>';
                    }
                    html += '</ul>';
                } else {
                    html += '<li class="folder_empty"><span class="open">' + content[i].children[j].name + '</span>';
                }
                html += '</li>';
            }
            html += '</ul></li>';
        }
        html += '</ul>'
        $('#tree').append(html);
        $('#dc4_tree').treeview({
            animated: "medium",
            persist: "cookie",
            collapsed: true,
            control: "#dc4_tree_control",
            cookieID: "dc4_tree"
        });
        
        /** disease meta tooltips **/
        $('.tooltip').tipsy({
            title: function() {
                return $(this).data('tooltip');
            },
            fade: true,
            live: true,
            gravity: $.fn.tipsy.autoWE
        });  
        if(window.location.hash) {
            var hash = window.location.hash.substring(1);
            $.ajax({
                url: path + '/services/frame/0',
                success: function(data) {
                    $('#content').html(data);
                    if(hash.startsWith('http')) {
                        $('#diseasebar').spin(spin_opts);
                        $('#_content').attr('src',hash);
                         
                    } else {
                        $('#diseasebar').spin(spin_opts);
                        $('#_content').attr('src',path + '/services/linkout/' + hash);
                        var select = '#dc4_t_' + hash.replace(':','\\:');
                        var grandparent = $(select).parent().parent().parent();
                        if (grandparent.parent().hasClass('lastExpandable')) {
                            grandparent.parent().addClass('lastCollapsable').removeClass('lastExpandable');
                        } else {
                            grandparent.parent().addClass('collapsable').removeClass('expandable'); 
                        }                
                        grandparent.slideDown('medium');
                        // update parent
                        var parent = $(select).parent();
                        if (parent.parent().hasClass('lastExpandable')) {
                            parent.parent().addClass('lastCollapsable').removeClass('lastExpandable');
                        } else {
                            parent.parent().addClass('collapsable').removeClass('expandable'); 
                        }                
                        parent.slideDown('medium');
                        //.slideDown('medium');
                        $('#tree').find('li').each(function() {
                            $(this).removeClass('activepoint');
                        });
                        $(select).addClass('activepoint');   
                    }  
                    setTimeout(function() {
                        $('#diseasebar').spin(false);
                    }, 2880);
                },
                async: true
            });        
        } else {
            init();
        }
        
        $('#sidebar_menu,#tree').fadeIn('slow'); 
    });
}

function init(){
    //init data
                
    var infovis = document.getElementById('infovis');
    var w = infovis.offsetWidth - 50, h = infovis.offsetHeight - 50;
    
    //init Hypertree
    var ht = new $jit.Hypertree({
        //id of the visualization container
        injectInto: 'infovis',
        //canvas width and height
        width: w,
        height: h,
        //Change node and edge styles such as
        //color, width and dimensions.
        Node: {
            dim: 8,
            color: "#e15620"
        },
        Edge: {
            lineWidth: 2,
            color: "#c5d1d7"
        },
        onBeforeCompute: function(node){
                        
        },
        //Attach event handlers and add text to the
        //labels. This method is only triggered on label
        //creation
        onCreateLabel: function(domElement, node){
            domElement.innerHTML = node.name;
            $jit.util.addEvent(domElement, 'click', function () {
                ht.onClick(node.id, {
                    onComplete: function() {
                        ht.controller.onComplete();
                    }
                });
            });
        },
        //Change node styles when labels are placed
        //or moved.
        onPlaceLabel: function(domElement, node){
            var style = domElement.style;
            style.display = '';
            style.cursor = 'pointer';
            if (node._depth <= 1) {
                style.fontSize = "0.8em";
                style.color = "#333333";

            } else if(node._depth == 2){
                style.fontSize = "0.64em";
                style.color = "#444444";

            } else {
                style.display = 'none';
            }

            var left = parseInt(style.left);
            var w = domElement.offsetWidth;
            style.left = (left - w / 2) + 'px';
        },
      
        onComplete: function(){
        }
    });
    ht.loadJSON(jsontree);
    ht.refresh();
    ht.controller.onComplete();   
}

$(document).ready(function(){
    start();
    
    /** event handler for URL # changes **/
    window.onhashchange = function(event) {
        if(window.location.hash.substring(1).indexOf(':') > 0)
            $('#content').html(getFrame(window.location.hash.substring(1)));
        
        if(window.location.hash == '') {
            $('#content').html('<div id="container"><div id="center-container"><div id="infovis"></div></div></div>');
            init();
        }
    };    
    
    /** Top menu handlers **/
    /** search menu **/
    $('.mag').click(function() {
        toggleTopButton('mag');
    });               
                
    $('.cancel').click(function(){
        hideTopButton('mag');                                                   
    });
    
    $('#omim').click(function() {
        $('#wrap').spin(spin_opts);        
        window.location.hash = 'omim:' + $(this).data('id');
        setTimeout(function() {
            $('#wrap').spin(false);
        }, 2880);   
    });
    
    /**
     * ReportView handlers
     */
    $('.report_box_concept_title').live ('click', function(){
        $(('#' + $(this).data('concept')).replace(':','\\:')).slideToggle(200, function(){
            });
    });

    $('.hide').live('click',function () {
        $('.report_box_items').each(function() {
            $(this).slideUp();
        });
    });
    $('.show').live('click',function () {
        $('.report_box_items').each(function() {
            $(this).slideDown();
        });
    });
        
    
    $('#dc4_disease_report').live('click',function(){   
        $('#content').html(generateReport());
    });
    
    $('#dc4_disease_print').click(function(){
        window.open(path + '/print/' + $(this).data('id'));
    });
    
    $('#dc4_disease_hypertree').click(function() {
        $('#content').html('<div id="container"><div id="center-container"><div id="infovis"></div></div></div>');
        init();
        window.location.hash = '';
    });
    
    $('#dc4_disease_omim').click(function() {
        $.ajax({
            url: path + '/services/frame/omim:' + $(this).data('id'),
            async: true,
            success: function(data) {
                $('#content').html(data);
            }
        });        
    });
    
    $('#dc4_page_external').click(function() {
        if($('#_content').length) {
            window.open($('#_content').attr('src'));
        }        
    });
    
    /** synonyms handling **/
    $('#syns').live('click', function(){
        toggleTopButton('syns');
    });
    
    $('.synonym').each(function() {
        var omim =  $(this).data('omim');
        var li = $(this);
        $.ajax({
            url: path + '/api/triple/coeus:omim_' + omim + '/dc:description/obj/js',
            success: function(data) {
                li.html(omim + ' | ' + data.results.bindings[0].obj.value);
            } 
        });
    });
    
    /** Frame links in Tree **/
    $('#tree .framer').live('click', function(){
        $('#diseasebar').spin(spin_opts);
        var link = $(this);
        window.location.hash = $(this).data('id');
        $('#tree').find('li').each(function() {
            $(this).removeClass('activepoint');
        });
        link.parent().addClass('activepoint');     
        setTimeout(function() {
            $('#diseasebar').spin(false);
        }, 3440); 
        return false;
    });
    
    /** Frame links in ReportView **/
    $('.report .framer').live('click',function() {
        $('#diseasebar').spin(spin_opts);
        var select = ('#dc4_t_' + $(this).data('id')).replace(':','\\:');
        window.location.hash = $(this).data('id');
                               
        // update grandparent
        var grandparent = $(select).parent().parent().parent();
        if (grandparent.parent().hasClass('lastExpandable')) {
            grandparent.parent().addClass('lastCollapsable').removeClass('lastExpandable');
        } else {
            grandparent.parent().addClass('collapsable').removeClass('expandable'); 
        }                
        grandparent.slideDown('medium');
        // update parent
        var parent = $(select).parent();
        if (parent.parent().hasClass('lastExpandable')) {
            parent.parent().addClass('lastCollapsable').removeClass('lastExpandable');
        } else {
            parent.parent().addClass('collapsable').removeClass('expandable'); 
        }                
        parent.slideDown('medium');
        $('#tree').find('li').each(function() {
            $(this).removeClass('activepoint');
        });
        $(select).addClass('activepoint');   
        setTimeout(function() {
            $('#diseasebar').spin(false);
        }, 2880);
                
        return false;        
    });
    
    /** Frame links in HyperTree **/
    $('#infovis .framer').live('click',function() {
        $('#diseasebar').spin(spin_opts);
        var select = ('#dc4_t_' + $(this).data('id')).replace(':','\\:');
        window.location.hash = $(this).data('id');                               
        // update grandparent
        var grandparent = $(select).parent().parent().parent();
        if (grandparent.parent().hasClass('lastExpandable')) {
            grandparent.parent().addClass('lastCollapsable').removeClass('lastExpandable');
        } else {
            grandparent.parent().addClass('collapsable').removeClass('expandable'); 
        }                
        grandparent.slideDown('medium');
        // update parent
        var parent = $(select).parent();
        if (parent.parent().hasClass('lastExpandable')) {
            parent.parent().addClass('lastCollapsable').removeClass('lastExpandable');
        } else {
            parent.parent().addClass('collapsable').removeClass('expandable'); 
        }                
        parent.slideDown('medium');
        //.slideDown('medium');
        $('#tree').find('li').each(function() {
            $(this).removeClass('activepoint');
        });
        $(select).addClass('activepoint');   
        setTimeout(function() {
            $('#diseasebar').spin(false);
        }, 2880);
                
        return false;       
    });
});
