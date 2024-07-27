<%@ page session="false"%>
<%@page import="com.ingsw.flyingdutchman.model.mo.User"%>

<%
    int i = 0;
    boolean loggedOn = (Boolean) request.getAttribute("loggedOn");
    User loggedUser = (User) request.getAttribute("loggedUser");
    String applicationMessage = (String) request.getAttribute("applicationMessage");
    String menuActiveLink = "Ordini";
%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <%@include file="/include/htmlHead.jsp"%>
    <script>
        function tryThresholds(){
            document.tryForm.submit();
        }
    </script>
    <style>
        #tryButton{
            color: #fff;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            margin-top: 10px;
            padding: 12px 24px;
            transition: background-color 0.3s ease;
            background-color: #28a745;
            font-size: larger;
        }

        main{
            display: flex;
            flex-direction: row;
            font-size: larger;
            align-items: center;
            justify-content: center;
        }

        #contentSection{
            display: flex;
            flex-direction: column;
            align-items: center;
        }

    </style>
</head>
<body>
    <%@include file="/include/header.jsp"%>
    <main>
        <section id="confettiSectionLeft">
            <img src="images/confetti.png" width="300px" height="550px" alt="Image containing Confetti">
        </section>

        <section id="contentSection">
            <h1>Grazie, <%=loggedUser.getUsername()%>!</h1><br/>
            <button id="tryButton" onclick="tryThresholds()">
                Prova subito le Prenotazioni!
            </button>
        </section>

        <section id="confettiSectionRight">
            <img src="images/confetti.png" width="300px" height="550px" alt="Image containing Confetti">
        </section>

        <form name="tryForm" method="post" action="Dispatcher">
            <input type="hidden" name="controllerAction" value="ThresholdManagement.view">
        </form>
    </main>
    <%@include file="/include/footer.inc"%>
</body>
</html>
