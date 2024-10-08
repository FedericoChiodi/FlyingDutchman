<%@ page session="false"%>

<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <%@include file="../include/htmlHead.jsp"%>
        <script>
            function lowerAll(){
                document.lowerForm.submit();
            }
        </script>
        <style>
            #lowerButton{
                color: #fff;
                border: none;
                border-radius: 5px;
                cursor: pointer;
                padding: 12px 24px;
                transition: background-color 0.3s ease;
                background-color: #dc3545;
                font-size: larger;
                margin: 15px 15px;
            }

            main{
                display: flex;
                flex-direction: row;
                font-size: x-large;
                align-items: center;
                justify-content: center;
            }

        </style>
        <title></title>
    </head>
    <body>
    <%@include file="../include/header.jsp"%>
        <main>
            <section id="contentSection">
                <button id="lowerButton" onclick="lowerAll()">
                    Abbassa i Prezzi!
                </button>
            </section>

            <form name="lowerForm" method="get" action="/thresholdManagement/update">
                <input type="hidden" name="pageToReturn" value="/auctionManagement/lowerAllView">
            </form>
        </main>
    <%@include file="../include/footer.inc"%>
    </body>
</html>
