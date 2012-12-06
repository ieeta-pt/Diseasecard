<%@include file="/final/layout/taglib.jsp" %>
<s:layout-definition>
    <!DOCTYPE html>
    <html>
        <head>
            <link rel="author" href="<c:url value="/humans.txt" />" />
            <link rel="icon" type="image/ico" href="<c:url value="/final/assets/image/dc4_icon.ico" />"></link> 
            <title><s:layout-component name="title">Diseasecard</s:layout-component></title>
            <s:layout-component name="style">
                <jsp:include page="/final/layout/style.jsp" />
            </s:layout-component>
        </head>
        <body>
            <div id="dc4_feedback_content">
                <div class="feedback_form">
                    <p class="email">  
                        <input type="text" name="email" id="dc4_feedback_email" />  
                        <label for="web">Email</label>  
                    </p>  
                    <p class="text">  
                        <textarea name="message" id="dc4_feedback_message"></textarea>  
                    </p>  
                    <p class="submit">  
                        <input id="dc4_feedback_submit" type="submit" value="Send" />  
                    </p>  
                    <p class="small" id="dc4_feedback_warning"></p>
                </div>
            </div>
            <div class="feedback small">
                <span id="dc4_feedback" class="tooltip" data-tooltip="Send feedback" href="#">FEEDBACK</span>
            </div>
            <noscript>
            Diseasecard requires that you enable Javascript.<br /><br /><br /><a href="http://support.google.com/bin/answer.py?hl=en&answer=23852" target="_blank">Take a look here if you do not know how.</a>
            </noscript>
            <s:layout-component name="body">
            </s:layout-component>
        </body>
        <script type="text/javascript">
            var _gaq = _gaq || [];
            _gaq.push(['_setAccount', 'UA-12230872-8']);
            _gaq.push(['_trackPageview']);

            (function() {
                var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
                ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
                var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
            })();
        </script>
        <s:layout-component name="scripts">
            <jsp:include page="/final/layout/script.jsp" />
        </s:layout-component>
        <s:layout-component name="custom_scripts">
        </s:layout-component>
    </html>
</s:layout-definition>