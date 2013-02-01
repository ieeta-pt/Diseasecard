<%@include file="/final/layout/taglib.jsp" %>
<c:set var="this" value="${actionBean}" />
<s:layout-render name="/final/layout/html.jsp">
    <s:layout-component name="title">About Diseasecard</s:layout-component>
    <s:layout-component name="custom_scripts">
        <script>    
            $(document).ready(function(){                
                $('.mag').click(function() {
                    toggleTopButton('mag');
                });                
                
                $('.cancel').click(function(){
                    $('.search').toggle();
                    $('.mag').data('active', 'false');
                    $('.mag').toggleClass('mag_enabled');                                     
                    
                });
                
            });            
        </script>
    </s:layout-component>
    <s:layout-component name="body">
        <div id="top">
            <div class="info">
                <div class="logo"></div>
                <div class="omim">About</div>
                <div class="description">Help section</div>
                <div class="search menu">
                    <form>
                        <input type="text" id="text_search" placeholder="Search..." type="search" />
                        <div class="cancel">
                        </div>
                    </form>
                </div>         
            </div>
            <div class="mag" data-active="false" style="float: right;"></div>
        </div>
        <div id="wrap">
            <div id="about">
                <h1>Further information available soon</h1>
                <br /><br /><br/><br /><br /><br/>
               
                <div class="support">
                    <a href="http://www.ieeta.pt/" target="_blank"><img src="/dc4/final/assets/image/logo/logo_ieeta.png" /></a>
                    <a href="http://www.ua.pt/" target="_blank"><img src="/dc4/final/assets/image/logo/logo_ua.png" /></a><br />
                    <a href="http://www.gen2phen.org/" target="_blank"><img src="/dc4/final/assets/image/logo/logo_gen2phen.png" /></a>
                    <a href="http://www.isciii.es/" target="_blank"><img src="/dc4/final/assets/image/logo/logo_isciii.gif" /></a>
                    <a href="http://www.infobiomed.org/" target="_blank"><img src="/dc4/final/assets/image/logo/logo_infobiomed.gif" /></a>
                    <a href="http://infogenmed.ieeta.pt/" target="_blank"><img src="/dc4/final/assets/image/logo/logo_infogenmed.gif" /></a>
                </div>
            </div>
        </div>
    </s:layout-component>
</s:layout-render>
