<%@ page session="false"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page import="com.ingsw.flyingdutchman.model.mo.User" %>
<%@ page import="com.ingsw.flyingdutchman.model.mo.Auction" %>
<%
    int i = 0;
    boolean loggedOn = (Boolean) request.getAttribute("loggedOn");
    User loggedUser = (User) request.getAttribute("loggedUser");
    String applicationMessage = (String) request.getAttribute("applicationMessage");
    String menuActiveLink = "Ordini";
    Auction auction = (Auction) request.getAttribute("auction");
    Boolean isPremium = (Boolean) request.getAttribute("isPremium");
    if(isPremium == null){
        isPremium = false;
    }
%>
<!DOCTYPE html>
<html>
    <head>
        <%@include file="../include/htmlHead.jsp"%>
        <title></title>
    </head>
    <script>
        function submitOrder(){
            let f;
            f = document.insertForm;
            f.auctionID.value = <%=auction.getAuctionID()%>;
        }
        function goBack(){
            document.backForm.auctionID.value = <%=auction.getAuctionID()%>;
            document.backForm.submit();
        }
        function modify(){
           document.modifyForm.submit();
        }
        function mainOnLoadHandler(){
            document.insertForm.addEventListener("submit",submitOrder);
            document.insertForm.backButton.addEventListener("click", goBack);
            document.insertForm.modifyButton.addEventListener("click", modify);
        }
    </script>
    <style>
        #orderInfo {
            display: flex;
            flex-direction: column;
            align-items: flex-start;
            margin-bottom: 20px;
            font-size: 22px;
        }

        #orderInfo h2 {
            font-size: 1.2em;
            margin-bottom: 10px;
        }

        #orderInfo span {
            margin-bottom: 5px;
        }

        #insertForm {
            display: flex;
            justify-content: center;
            margin-bottom: 20px;
        }

        .button {
            padding: 10px 20px;
            color: #fff;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            margin-right: 10px;
            font-size: larger;
        }

        #confirmButton{
            background-color: #337ab7;
        }
        #backButton{
            background-color: #dc3545;
        }
        #modifyButton{
            background-color: darkorange;
        }

        #insertForm .button:last-child {
            margin-right: 0;
        }

    </style>
    <body>
        <%@include file="../include/header.jsp"%>
        <main>
            <section id="orderInfo">
                <h2>Stai per acquistare: </h2><br/>
                <span><%=auction.getProduct_auctioned().getDescription()%></span><br/>
                <h2>Pagherai: </h2>
                <span class="float-value"><%=auction.getProduct_auctioned().getCurrent_price()%></span><br/>

                <h2>Il tuo articolo verr&agrave; spedito a: </h2><br/>
                <span><%=loggedUser.getFirstname()%> <%=loggedUser.getSurname()%></span><br/>
                <span>
                    <%=loggedUser.getAddress()%> <%=loggedUser.getCivic_number()%> - <%=loggedUser.getCity()%>
                    <%=loggedUser.getCap()%> - <%=loggedUser.getState()%>
                </span><br/>
            </section>

            <form id="insertForm" name="insertForm" action="<%=isPremium ? "orderManagement/premium" : "orderManagement/pay"%>" method="post">
                <input type="submit" id="confirmButton" name="confirmButton" class="button" value="Paga"/>
                <input type="button" id="modifyButton" name="modifyButton" class="button" value="Modifica i miei Dati"/>
                <input type="button" id="backButton" name="backButton" class="button" value="Annulla"/>
                <input type="hidden" name="auctionID">
            </form>

            <form name="backForm" method="get" action="auctionManagement/inspect">
                <input type="hidden" name="auctionID"/>
            </form>

            <form name="modifyForm" method="get" action="userManagement/modify">
                <input type="hidden" name="auctionID" value="<%=auction.getAuctionID()%>">
            </form>

        </main>
        <%@include file="../include/footer.inc"%>
    </body>
</html>
