<%@include file="/beta/layout/taglib.jsp" %>
<s:layout-render name="/beta/layout/html.jsp">
    <s:layout-component name="title">${actionBean.query} - Diseasecard</s:layout-component>
    <s:layout-component name="custom_scripts">
        <script>      
            var results_item_markup = '<div class="results_item" data-id="\${id}"><span class="id disease_link" data-omim="\${id}">\${id}</span><span class="name" data-omim="\${id}">\${name}</span><a href="http://omim.org/entry/\${id}" class="omim" target="_blank" title="\${name}">OMIM</a><a class="links" data-id="\${id}">Links</a><div class="info" id="info-\${id}"></div></div>';
               
            function loadLinks() {
                $('.links').each(function () {
                    var id = $(this).data('id');
                    $('#info-' + id).load('${path}/service/links/' + id + '.html');
                });
            }
            
            function loadResults(id) {
                $.getJSON('${path}/view/results/browse/' + id, function(data) {
                    if(data.status === '110') {
                        $('#content').html('<div id="error"><h1>Diseasecard found no matches for "${actionBean.query}"</h1><div id="busy"></div></div>');
                    } else if (data.status === "121") {
                        window.location = '${path}/disease/' + data.id;
                    } else {
                        $('#content').html('<div id="results"></div>');
                        $('#disease_id').html(data.size + ' results');
                        $.tmpl('results', data.results).appendTo('#results');                  
                        $.tmpl('results_others', data.results).appendTo('#others');
                        $('#disease_info').fadeIn('slow');
                        loadLinks();
                    }                    
                }); 
            }
            
            $(document).ready(function(){
                $.template('results', results_item_markup);
                $('#disease_info').fadeIn('slow');
                loadResults('${actionBean.query}');
                
                $('.disease_link').live('click', function(){
                    window.location = '${path}/disease/' + $(this).data('omim');
                });
                
                $('#filter').keyup(function(){
                    var value = $(this).attr('value');
                    if(value.length >= 3) {
                        $('.results_item').each(function(){
                            if ($(this).find('.name').html().toLowerCase().indexOf(value) === -1) {
                                $(this).hide();
                            }
                        });
                    } else {
                        $('.results_item').show();
                    }
                });
         
                
                $('.char').click(function(){
                    window.location = '${path}/browse/' + $(this).html();
                });
                
                $('.mag').click(function() {
                    if( ($(this).data('active')).toString() == 'false') {
                        $('.tools').removeClass('tools_enabled');
                        $('.menu').each(function () {
                            $(this).hide();
                            $(this).data('active', 'false');                            
                        });
                        $('.search').show();
                        $(this).addClass('mag_enabled');
                        $(this).data('active', 'true');
                    } else if(($(this).data('active')).toString() == 'true') {
                        $('.search').hide();
                        $(this).data('active', 'false');
                        $(this).removeClass('mag_enabled');
                    }
                });
                
                
                $('.cancel').click(function(){
                    $('.search').toggle();
                    $('.mag').data('active', 'false');
                    $('.mag').toggleClass('mag_enabled');                                     
                    
                });
                
                $('.links').live('click', function() {
                    $('#info-' + $(this).data('id')).slideToggle();
                });
                
                $('.name').live('click', function(){
                    window.location = '${path}/disease/' + $(this).data('omim');
                });
            });
        </script>
    </s:layout-component>
    <s:layout-component name="body">

        <div id="top">
            <div class="mag" data-active="false"></div>
            <div class="info">
                <div class="logo"></div>
                <div class="omim">Browse</div>
                <div class="description">${actionBean.query}</div>
                <div class="search menu">
                    <form>
                        <input type="text" id="text_search" placeholder="Search..." type="search" />
                        <div class="cancel">
                        </div>
                    </form>
                </div>         
            </div>

            <div class="about">
                <a href="${path}/about" target="_top">about</a>
            </div>
        </div>
        <div id="wrap">
            <div id="diseasebar">
                <div id="disease_info">
                    <div id="disease_name">${actionBean.query}</div> 
                    <div id="browse">
                        <span class="char">A</span>
                        <span class="char">B</span>
                        <span class="char">C</span>
                        <span class="char">D</span>
                        <span class="char">E</span>
                        <span class="char">F</span>
                        <span class="char">G</span>
                        <span class="char">H</span>
                        <span class="char">I</span>
                        <span class="char">J</span>
                        <span class="char">K</span>
                        <span class="char">L</span>
                        <span class="char">M</span>
                        <span class="char">N</span>
                        <span class="char">O</span>
                        <span class="char">P</span>
                        <span class="char">Q</span>
                        <span class="char">R</span>
                        <span class="char">S</span>
                        <span class="char">T</span>
                        <span class="char">U</span>
                        <span class="char">V</span>
                        <span class="char">W</span>
                        <span class="char">X</span>
                        <span class="char">Y</span>
                        <span class="char">Z</span>
                    </div>
                    <div class="filter">
                        <span class="label">Filter</span><input type="text" id="filter" />
                    </div>
                </div>

            </div>
            <div id="content">
                <div id="warning">
                    <h1>Diseasecard is loading "${actionBean.query}" diseases</h1>
                    <div id="process"></div>
                </div>  
            </div>
        </div>

    </s:layout-component>
</s:layout-render>
