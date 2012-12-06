<%@include file="/final/layout/taglib.jsp" %>
<s:layout-render name="/final/layout/html.jsp">
    <s:layout-component name="title">${actionBean.query} - Diseasecard Search</s:layout-component>
    <s:layout-component name="custom_scripts">
        <script src="<c:url value="/final/assets/script/dc4.search.js" />"></script>
        <script>
            $(document).ready(function(){
                loadResults('${actionBean.query}');  
            });            
        </script>
    </s:layout-component>
    <s:layout-component name="body">

        <div id="top">

            <div class="info">
                <div class="logo tooltip" data-tooltip="Go to Diseasecard home"></div>
                <div class="omim">Search</div>
                <div class="description">${actionBean.query}</div>

            </div>
            <div class="right_box">
                <div class="about tooltip" data-tooltip="Go to About page">
                    <a href="${path}/about" target="_top">about</a>
                </div>
                <div class="mag tooltip" data-tooltip="Launch search box" data-active="false"></div>
            </div>
            <div class="search menu">
                <form>
                    <input type="text" id="text_search" placeholder="Search..." type="search" />
                    <div class="cancel">
                    </div>
                </form>
            </div>         
        </div>
        <div id="wrap">
            <div id="results_search">
                <div id="results_size"></div>
                <div class="filter">
                    <span class="label">Live Filter</span><input class="tooltip" data-tooltip="Type here to filter disease search results" type="text" id="filter" />
                </div>
                <div id="results_content">
                    <div id="warning">
                        <h1>Diseasecard results are loading...</h1>
                        <div id="process"></div>
                    </div>    
                </div> 
            </div>
        </div>

    </s:layout-component>
</s:layout-render>
