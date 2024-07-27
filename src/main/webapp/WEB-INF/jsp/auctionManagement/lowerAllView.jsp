<%@ page session="false"%>
<%@page import="com.ingsw.flyingdutchman.model.mo.User"%>

<%
    int i = 0;
    boolean loggedOn = (Boolean) request.getAttribute("loggedOn");
    User loggedUser = (User) request.getAttribute("loggedUser");
    String applicationMessage = (String) request.getAttribute("applicationMessage");
    String menuActiveLink = "Abbassa";
%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
    <head>
        <%@include file="/include/htmlHead.jsp"%>
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
    </head>
    <body>
    <%@include file="/include/header.jsp"%>
        <main>
            <section id="contentSection">
                <button id="lowerButton" onclick="lowerAll()">
                    Abbassa i Prezzi!
                </button>
            </section>


            <form name="lowerForm" method="post" action="Dispatcher">
                <input type="hidden" name="controllerAction" value="ThresholdManagement.checkOnUpdate">
                <input type="hidden" name="pageToReturn" value="auctionManagement/lowerAllView">
            </form>
        </main>
    <%@include file="/include/footer.inc"%>
    </body>
</html>
