<%@include file="/final/layout/taglib.jsp" %>
<s:layout-definition>
    <!DOCTYPE html>
    <html>
        <head>
            <link rel="author" href="<c:url value="/humans.txt" />" />
            <link rel="icon" type="image/ico" href="<c:url value="/favicon.ico" />"></link> 
            <title><s:layout-component name="title">Diseasecard</s:layout-component></title>
            <s:layout-component name="style">
                <jsp:include page="/final/layout/style.jsp" />
            </s:layout-component>
        </head>
        <body>
            <noscript>
            <div id="js">
                Diseasecard requires that you enable Javascript.<br /><br /><br /><a href="http://support.google.com/bin/answer.py?hl=en&answer=23852" target="_blank">Take a look here if you do not know how.</a>
            </div>
            </noscript>
            <s:layout-component name="body">
            </s:layout-component>


            <script type="text/javascript">
                var GoSquared = {};
                GoSquared.acct = "GSN-447581-N";
                (function(w) {
                    function gs() {
                        w._gstc_lt = +new Date;
                        var d = document, g = d.createElement("script");
                        g.type = "text/javascript";
                        g.src = "//d1l6p2sc9645hc.cloudfront.net/tracker.js";
                        var s = d.getElementsByTagName("script")[0];
                        s.parentNode.insertBefore(g, s);
                    }
                    w.addEventListener ?
                            w.addEventListener("load", gs, false) :
                            w.attachEvent("onload", gs);
                })(window);
            </script>
        </body>
        <script type="text/javascript">
            var _gaq = _gaq || [];
            var pluginUrl = '//www.google-analytics.com/plugins/ga/inpage_linkid.js';
            _gaq.push(['_require', 'inpage_linkid', pluginUrl]);
            _gaq.push(['_setAccount', 'UA-12230872-2']);
            _gaq.push(['_trackPageview']);

            (function() {
                var ga = document.createElement('script');
                ga.type = 'text/javascript';
                ga.async = true;
                ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
                var s = document.getElementsByTagName('script')[0];
                s.parentNode.insertBefore(ga, s);
            })();
        </script>
        <script type="text/javascript">
            var $buoop = {vs: {i: 9, f: 4, o: 11, s: 5, n: 9}}
            $buoop.ol = window.onload;
            window.onload = function() {
                try {
                    if ($buoop.ol)
                        $buoop.ol();
                } catch (e) {
                }
                var e = document.createElement("script");
                e.setAttribute("type", "text/javascript");
                e.setAttribute("src", "http://browser-update.org/update.js");
                document.body.appendChild(e);
            }
        </script> 
        <s:layout-component name="scripts">
            <jsp:include page="/final/layout/script.jsp" />
        </s:layout-component>
        <s:layout-component name="custom_scripts">
        </s:layout-component>
    </html>
</s:layout-definition>