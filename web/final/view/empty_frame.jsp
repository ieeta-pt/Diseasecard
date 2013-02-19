<%@include file="/final/layout/taglib.jsp" %>
<s:layout-render name="/final/layout/html.jsp">
    <s:layout-component name="body">
        <div id="container">
            <div class="row-fluid center">
                <div class="offset3"></div>
                <div class="alert alert-error span6" id="no_link">
                    <strong>Warning!</strong> Sorry, no data for requested <em>key</em>:<em>value</em> pair!
                </div> 
            </div>

        </div>
    </s:layout-component>
</s:layout-render>
