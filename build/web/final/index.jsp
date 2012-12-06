<%@include file="/final/layout/taglib.jsp" %>
<s:layout-render name="/final/layout/html.jsp">
    <s:layout-component name="title">Diseasecard</s:layout-component>
    <s:layout-component name="custom_scripts">
        <script src="<c:url value="/final/assets/script/dc4.index.js" />"></script>
    </s:layout-component>
    <s:layout-component name="body"> 
        <div id="index">
            <div class="logo">
                <a href="<c:url value="/about" />" title="About Diseasecard" class="tooltip" data-tooltip="About Diseasecard"><img src="<c:url value="/final/assets/image/logo.png" />" /></a>
            </div>
            <div class="tag tooltip" data-tooltip="You can search Diseasecard for disease names, OMIM disease codes or approved gene symbols.">
                search for rare diseases
            </div>
            <div class="search">
                <form id="search" class="form" >
                    <input type="text" id="text_search" class="searcher tooltip" data-tooltip="Search here" placeholder="Search here..." /><input type="button" id="button_search" value="GO" /><span data-id="" id="omim"></span>
                </form>
            </div>
            <div id="helpers">
                Need help? You can search Diseasecard for disease names, OMIM disease codes or approved gene symbols.<br /><br />Try <span class="search_help tooltip" data-tooltip="Add OMIM disease number to search box">114480</span>, <span class="search_help tooltip" data-tooltip="Add disease name to search box">breast cancer</span> or <span class="search_help tooltip" data-tooltip="Add gene HGNC symbol to search box">BRCA2</span>
            </div>
            <div id="bottom">
                <div class="about tooltip" data-tooltip="About Diseasecard">
                    <a href="${path}/about" target="_top">about</a>
                </div>
            </div>
        </s:layout-component>
    </s:layout-render>
