var labelType, useGradients, nativeTextSupport, animate, jsontree;
var network = {id: '<h6>' + key + '</h6>', name: key, children: []};
var map = {};

(function() {
    var ua = navigator.userAgent,
            iStuff = ua.match(/iPhone/i) || ua.match(/iPad/i),
            typeOfCanvas = typeof HTMLCanvasElement,
            nativeCanvasSupport = (typeOfCanvas == 'object' || typeOfCanvas == 'function'),
            textSupport = nativeCanvasSupport
            && (typeof document.createElement('canvas').getContext('2d').fillText == 'function');
    labelType = (!nativeCanvasSupport || (textSupport && !iStuff)) ? 'Native' : 'HTML';
    nativeTextSupport = labelType == 'Native';
    useGradients = nativeCanvasSupport;
    animate = !(iStuff || !nativeCanvasSupport);
})();

/**
 * Load query results from Solr index into internal javascript network.
 * 
 * @returns {undefined}
 */
function query() {
    $.getJSON('../services/query/' + key, function(data) {
        //$('#content').html('');
        // start entities
        var disease = {id: 'entity:disease', name: '<h5>Disease</h5>', children: []}
        var drug = {id: 'entity:drug', name: '<h5>Drug</h5>', children: []};
        var pathway = {id: 'entity:pathway', name: '<h5>Pathway</h5>', children: []};
        var locus = {id: 'entity:locus', name: '<h5>Locus</h5>', children: []};
        var study = {id: 'entity:study', name: '<h5>Study</h5>', children: []};
        var ontology = {id: 'entity:ontology', name: '<h5>Ontology</h5>', children: []};
        var protein = {id: 'entity:protein', name: '<h5>Protein</h5>', children: []};
        var variome = {id: 'entity:variome', name: '<h5>Variome</h5>', children: []};

        // start concepts
        var omim = {id: 'concept:omim', name: '<h6>OMIM</h6>', children: []};
        var orphanet = {id: 'concept:orphanet', name: '<h6>OrphaNet</h6>', children: []};
        var pharmgkb = {id: 'concept:pharmgkb', name: '<h6>PharmGKB</h6>', children: []};
        var kegg = {id: 'concept:kegg', name: '<h6>KEGG</h6>', children: []};
        var enzyme = {id: 'concept:enzyme', name: '<h6>Enzyme</h6>', children: []};
        var ensembl = {id: 'concept:ensembl', name: '<h6>Ensembl</h6>', children: []};
        var entrez = {id: 'concept:entrez', name: '<h6>Entrez</h6>', children: []};
        var genecards = {id: 'concept:genecards', name: '<h6>GeneCards</h6>', children: []};
        var hgnc = {id: 'concept:hgnc', name: '<h6>HGNC</h6>', children: []};
        var clinicaltrials = {id: 'concept:clinicaltrials', name: '<h6>Clinical Trials</h6>', children: []};
        var mesh = {id: 'concept:mesh', name: '<h6>MeSH</h6>', children: []};
        var icd10 = {id: 'concept:icd10', name: '<h6>ICD10</h6>', children: []};
        var interpro = {id: 'concept:interpro', name: '<h6>InterPro</h6>', children: []};
        var prosite = {id: 'concept:prosite', name: '<h6>PROSITE</h6>', children: []};
        var pdb = {id: 'concept:pdb', name: '<h6>PDB</h6>', children: []};
        var uniprot = {id: 'concept:uniprot', name: '<h6>UniProt</h6>', children: []};
        var wave = {id: 'concept:wave', name: '<h6>WAVe</h6>', children: []};
        var name = {id: 'concept:name', name: '<h6>name</h6>', children: []};

        // build
        var i = 0;
        $.each(data.response.docs, function(i, k) {


            var item = data.response.docs[i].id.split(':');
            if (map[item[1] + ':' + item[2]] === undefined) {
                eval(item[1]).children[eval(item[1]).children.length] = {'id': item[1] + ':' + item[2], 'name': '<a data-id="' + item[1] + ':' + item[2] + '" target="_content" class="framer dc4_ht">' + item[2] + '</a>'};
                map[item[1] + ':' + item[2]] = true;
            }

            if (map[item[0]] === undefined && i < 100) {
                omim.children[omim.children.length] = {'id': 'omim:' + item[0], 'name': '<a data-id="omim:' + item[0] + '" target="_content" class="framer dc4_ht">' + item[0] + '</a>'};
                map[item[0]] = true;
                i++;
            }
        })

        // connect graph
        disease.children[disease.children.length] = omim;
        disease.children[disease.children.length] = orphanet;
        drug.children[drug.children.length] = pharmgkb;
        pathway.children[pathway.children.length] = kegg;
        pathway.children[pathway.children.length] = enzyme;
        locus.children[locus.children.length] = ensembl;
        locus.children[locus.children.length] = entrez;
        locus.children[locus.children.length] = genecards;
        locus.children[locus.children.length] = hgnc;
        study.children[study.children.length] = clinicaltrials;
        ontology.children[ontology.children.length] = mesh;
        ontology.children[ontology.children.length] = icd10;
        protein.children[protein.children.length] = interpro;
        protein.children[protein.children.length] = prosite;
        protein.children[protein.children.length] = pdb;
        protein.children[protein.children.length] = uniprot;
        variome.children[variome.children.length] = wave;
        network.children[network.children.length] = disease;
        network.children[network.children.length] = drug;
        network.children[network.children.length] = pathway;
        network.children[network.children.length] = locus;
        network.children[network.children.length] = study;
        network.children[network.children.length] = ontology;
        network.children[network.children.length] = protein;
        network.children[network.children.length] = variome;

        start();
    });
}

/**
 * Initialize page loading (query index, build tree, build hypertree);
 * 
 * @returns {undefined}
 */
function start() {
    jsontree = network;
    content = network.children;
    if (content[0].children[0].children.length === 0) {
        $('#key').html('No data for ' + key);
        $('#content').html('<div class="row"><div class="col-md-offset-2">&nbsp;</div><div class="alert alert-warning span6 center"><strong>Sorry!</strong> Diseasecard could not find any matches for <span class="label label-inverse">' + key + '</span>. <br /> Try searching for something else instead.</div></div>');
        $('.mag').click();
    } else {
        var html = '<ul id="dc4_tree" class="tree">';
        $.each(content, function(i) {
            html += '<li class="open library"><span class="open"><i class="icon-folder-open"></i> ' + content[i].name + '</span><ul>';
            $.each(content[i].children, function(j) {
                if (content[i].children[j].children.length > 0) {
                    if (content[i].children[j].children.length === 1) {
                        html += '<li class="closed folder"><span class="open folder_block" rel="tooltip" data-animation="true" title="' + content[i].children[j].children.length + ' connection"><i class="icon-folder-open-alt folder_icon"></i> ' + content[i].children[j].name + '</span>';
                    } else {
                        html += '<li class="closed folder"><span class="open folder_block" rel="tooltip" data-animation="true" title="' + content[i].children[j].children.length + ' connections"><i class="icon-folder-open-alt folder_icon"></i> ' + content[i].children[j].name + '</span>';
                    }
                    html += '<ul>';
                    $.each(content[i].children[j].children, function(k) {
                        html += '<li id="dc4_t_' + content[i].children[j].children[k].id + '" class="point dc4_t" rel="tooltip" data-animation="true" title="Open \'' + content[i].children[j].children[k].id + '\'"><i class="icon-file-alt file_icon"></i> ' + content[i].children[j].children[k].name + '</li>';
                    });
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
            collapsed: false,
            control: "#dc4_tree_control",
            cookieID: "diseasecard_view" + key + "_tree"
        });


        if (window.location.hash) {
            updateButtons(false);
            var hash = window.location.hash.substring(1);
            if (!hash.startsWith('name')) {
                $('#frame_loading').fadeIn('slow');
                $.ajax({
                    url: path + '/services/frame/0',
                    success: function(data) {
                        $('#content').html(data);

                        if (hash.startsWith('http')) {
                            $('#_content').attr('src', hash);

                        } else {
                            $('#_content').attr('src', path + '/services/linkout/' + hash);
                            var select = '#dc4_t_' + hash.replace(':', '\\:');
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
                        $('#_content').load(function() {
                            $('#frame_loading').fadeOut('slow');
                        })
                    },
                    async: true
                });
            } else {
                init();
            }
        } else {
            init();
        }
        $('*[rel=tooltip]').tooltip();
        $('#sidebar_menu,#tree').fadeIn(1000);
    }
}
/**
 * Start custom HyperTree visualization.
 */
function init() {
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
        onBeforeCompute: function(node) {

        },
        //Attach event handlers and add text to the
        //labels. This method is only triggered on label
        //creation
        onCreateLabel: function(domElement, node) {
            domElement.innerHTML = node.name;
            $jit.util.addEvent(domElement, 'click', function() {
                ht.onClick(node.id, {
                    onComplete: function() {
                        ht.controller.onComplete();
                    }
                });
            });
        },
        //Change node styles when labels are placed
        //or moved.
        onPlaceLabel: function(domElement, node) {
            var style = domElement.style;
          /*  var count = 0;
            node.eachSubnode(function(n) {
                count++;
            });            
            if (count === 0) {
                style.opacity = '0.4';
            }*/
            style.display = '';
            style.cursor = 'pointer';
            if (node._depth <= 1) {
                style.fontSize = "0.8em";
                style.color = "#333333";

            } else if (node._depth == 2) {
                style.fontSize = "0.64em";
                style.color = "#444444";

            } else {
                style.display = 'none';
            }

            var left = parseInt(style.left);
            var w = domElement.offsetWidth;
            style.left = (left - w / 2) + 'px';
        },
        onComplete: function() {
        }
    });
    ht.loadJSON(jsontree);
    ht.refresh();
    ht.controller.onComplete();
}

/**
 * Toggle hypertree and external view buttons
 */
function updateButtons(status) {
    var ht = $('#dc4_disease_hypertree');
    var ext = $('#dc4_page_external');
    if (status) {
        if (!ht.hasClass('disabled')) {
            ht.addClass('disabled');
        }
        if (!ext.hasClass('disabled')) {
            ext.addClass('disabled');
        }
    } else {
        if (ht.hasClass('disabled')) {
            ht.removeClass('disabled');
        }
        if (ext.hasClass('disabled')) {
            ext.removeClass('disabled');
        }
    }
}

$(document).ready(function() {
    // set main content area width
    $('#content').width($('html').width() - $('#diseasebar').width());

    // update content width on window resize
    $(window).resize(function() {
        if (this.resizeTO)
            clearTimeout(this.resizeTO);
        this.resizeTO = setTimeout(function() {
            $(this).trigger('resizeEnd');
        }, 50);
    });

    $(window).bind('resizeEnd', function() {
        $('#content').width($('html').width() - $('#diseasebar').width());
    });

    // load query results, diseasebar and navigation content (watch out for async events!)
    query();

    /** event handler for URL # changes **/
    window.onhashchange = function(event) {
        if (window.location.hash.substring(1).indexOf(':') > 0)
        {
            if (window.location.hash.toString().indexOf('omim') > 0) {
                var token = window.location.hash.toString().replace('#omim:', 'entry/');
                window.location = 'http://bioinformatics.ua.pt/diseasecard/' + token;
            } else {
                $('#frame_loading').fadeIn('slow');
                $('#content').html(getFrame(window.location.hash.substring(1)));
                $('#_content').load(function() {
                    $('#frame_loading').fadeOut('slow');
                });
            }
        }
        if (window.location.hash === '') {
            $('#content').html('<div id="container"><div id="center-container"><div id="infovis"></div></div></div>');
            init();
        }
    };

    /** Top menu handlers **/
    /** search menu **/
    $('.mag').click(function() {
        showSearch($(this));
        setTimeout(function() {
            $('#text_search').focus();
        }, 400);

    });

    // diseasebar buttons actions
    // show navigation tree
    $('#dc4_disease_hypertree').click(function() {
        $('#content').html('<div id="container"><div id="center-container"><div id="infovis"></div></div></div>');
        init();
        updateButtons(true);
        window.location.hash = '';
    });

    // open content in external window
    $('#dc4_page_external').click(function() {
        if ($('#_content').length) {
            window.open($('#_content').attr('src'));
        }
    });

    $('#dc4_page_help').click(function() {
        window.location = path + '/about#faq';
    });


    /** Frame links in Tree **/
    $(document).on('click', '#tree .framer', function() {
        var link = $(this);
        window.location.hash = $(this).data('id');
        $('#tree').find('li').each(function() {
            $(this).removeClass('activepoint');
        });
        link.parent().addClass('activepoint');
        updateButtons(false);
        return false;
    });

    /** Frame links in HyperTree **/
    $(document).on('click', '#infovis .framer', function() {
        var select = ('#dc4_t_' + $(this).data('id')).replace(':', '\\:');
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
        updateButtons(false);
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
        content: "Press the accession number to quickly view the <strong>associated</strong> OMIM entries<br />"
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
