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
    $.getJSON(path + '/services/content/' + key +'.js',function(data){
        jsontree = data;
        $('#disease_info').fadeIn('slow');  
        content = data.children;
        var html = '<ul id="dc4_tree" class="tree">';
        $.each(content, function(i) {
            html += '<li class="open library"><span class="open"><i class="icon-folder-open"></i> ' + content[i].name + '</span><ul>'
            $.each(content[i].children, function(j) {         
                if(content[i].children[j].children) {
                    if(content[i].children[j].size === 1) {
                        html += '<li class="closed folder"><span class="open folder_block" rel="tooltip" data-animation="true" title="' + content[i].children[j].size + ' connection"><i class="icon-folder-open-alt folder_icon"></i> ' + content[i].children[j].name + '</span>';
                    } else {
                        html += '<li class="closed folder"><span class="open folder_block" rel="tooltip" data-animation="true" title="' + content[i].children[j].size + ' connections"><i class="icon-folder-open-alt folder_icon"></i> ' + content[i].children[j].name + '</span>';
                    }                                        
                    html += '<ul>';
                    $.each(content[i].children[j].children, function(k) {
                        html += '<li id="dc4_t_' + content[i].children[j].children[k].id + '" class="point dc4_t" rel="tooltip" data-animation="true" title="Open \'' + content[i].children[j].children[k].id + '\'"><i class="icon-file-alt file_icon"></i> ' + content[i].children[j].children[k].name + '</li>';
                    })
                    html += '</ul>';
                } else {
                    html += '<li class="folder_empty"><span class="open"><i class="icon-folder-close-alt folder_empty_icon"></i> ' + content[i].children[j].name + '</span>';
                }
                html += '</li>';  
            });
            html += '</ul></li>';
        });
        
        html += '</ul>'
        $('#tree').append(html);
        $('#dc4_tree').treeview({
            animated: "medium",
            persist: "cookie",
            collapsed: true,
            control: "#dc4_tree_control",
            cookieID: "dc4_tree"
        });
        
     
        if(window.location.hash) {
            var hash = window.location.hash.substring(1);
            if(!hash.startsWith('name')) {
                $.ajax({
                    url: path + '/services/frame/0',
                    success: function(data) {
                        $('#content').html(data);
    
                        if(hash.startsWith('http')) {
                            $('#_content').attr('src',hash);
                         
                        } else {
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
                    },
                    async: true
                });        
            } else {
                init();
            }
        }else {
            init();
        }
        $('#sidebar_menu,#tree').fadeIn(); 
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
    // set main content area width
    $('#content').width($('html').width() - $('#diseasebar').width());
    
    // update content width on window resize
    $(window).resize(function() {
        if(this.resizeTO) clearTimeout(this.resizeTO);
        this.resizeTO = setTimeout(function() {
            $(this).trigger('resizeEnd');
        }, 50);
    });
    
    $(window).bind('resizeEnd', function() {
        $('#content').width($('html').width() - $('#diseasebar').width());
    });
    
    // load diseasebar and navigation content
    start();
    
    // load bootstrap popovers
    $("a[rel=popover]")
    .popover({
        'html' : true, 
        'animation' : true, 
        'placement' : 'bottom', 
        'delay':1000, 
        'trigger':'hover',
        'content':synonyms_html
    })
    .click(function(e) {
        e.preventDefault()
    })
    
    /** event handler for URL # changes **/
    window.onhashchange = function(event) {
        if(window.location.hash.substring(1).indexOf(':') > 0) 
        {
            $('#content').html(getFrame(window.location.hash.substring(1)));
        } 
        if(window.location.hash == '') {
            $('#content').html('<div id="container"><div id="center-container"><div id="infovis"></div></div></div>');
            init();
        }
    };    
    
    /** Top menu handlers **/
    /** search menu **/
    $('.mag').click(function() {
        showSearch($(this));
        setTimeout(function(){                            
            $('#text_search').focus();
        }, 400); 
        
    });       
    
    $('#omim').click(function() {
        window.location.hash = 'omim:' + $(this).data('id');
    });
  
  
    // diseasebar buttons actions
    // show navigation tree
    $('#dc4_disease_hypertree').click(function() {
        $('#content').html('<div id="container"><div id="center-container"><div id="infovis"></div></div></div>');
        init();
        window.location.hash = '';
    });

    // open content in external window
    $('#dc4_page_external').click(function() {
        if($('#_content').length) {
            window.open($('#_content').attr('src'));
        }        
    });
    
    $('#dc4_page_help').click(function() {
        window.location = path + '/about#faq';
    });
    
    /** load synonyms info **/
    $('.synonym').each(function() {
        var omim =  $(this).data('omim');
        var li = $(this);
        $.ajax({
            url: path + '/api/triple/diseasecard:omim_' + omim + '/dc:description/obj/js',
            success: function(data) {
                li.html(omim + ' | ' + data.results.bindings[0].obj.value);
            } 
        });
    });
    
    /** Frame links in Tree **/
    $('#tree .framer').live('click', function(){        
        var link = $(this);
        window.location.hash = $(this).data('id');
        $('#tree').find('li').each(function() {
            $(this).removeClass('activepoint');
        });
        link.parent().addClass('activepoint');     
        return false;
    });
    
    /** Frame links in HyperTree **/
    $('#infovis .framer').live('click',function() {
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
                
        return false;       
    }); 
    
    var tour = new Tour({
        name: "diseasecard_disease_tour",
        keyboard: true
    });
    
    tour.addStep({
        animation: true,
        placement: 'bottom',
        element: "#key",
        title: "OMIM", 
        content: "Press the OMIM identifier to quickly view the <strong>associated</strong> rare diseases<br />" 
    });
    tour.addStep({
        animation: true,
        placement: 'bottom',
        element: "#omim",
        title: "Disease name", 
        content: "<strong>Hover</strong> over the disease name to quickly view alternative names<br />"
    });    
    tour.addStep({
        animation: true,
        placement: 'bottom',
        element: "#infovis",
        title: "Diseasebar", 
        content: "Explore the <strong>rare disease network</strong> in Diseasecard's hypertree<br/>Links open in the main content area using <strong>LiveView</strong><br />"
    });
    tour.addStep({
        animation: true,
        placement: 'right',
        element: "#tree",
        title: "Diseasebar", 
        content: "Navigate all connections in the <strong>rare disease network</strong><br/>Links also open in the main content area using <strong>LiveView</strong><br />Try it out!"
    });
    tour.addStep({
        animation: true,
        placement: 'bottom',
        element: "#dc4_page_external",
        title: "External View", 
        content: "Press the <strong>external view</strong> button to opean the current LiveView page in a new browser window<br />"
    });
    tour.addStep({
        animation: true,
        placement: 'bottom',
        element: "#nav_search",
        title: "Search", 
        content: "Quickly access Diseasecard's <strong>search</strong><br />"
    });    
    tour.addStep({
        animation: true,
        placement: 'bottom',
        element: "#tour_about",
        title: "Help", 
        content: "If you need any further assistance, check out Diseasecard's documentation<br />"
    });
    tour.start();
    
});
