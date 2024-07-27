<%@ page session="false"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.ingsw.flyingdutchman.model.mo.User" %>
<%@ page import="com.ingsw.flyingdutchman.model.mo.Auction" %>

<%
    int i = 0;
    boolean loggedOn = (Boolean) request.getAttribute("loggedOn");
    String applicationMessage = (String) request.getAttribute("applicationMessage");
    String menuActiveLink = "Aste";
    User loggedUser = (User) request.getAttribute("loggedUser");
    Auction auction = (Auction) request.getAttribute("auction");
%>
<!DOCTYPE html>
<html>
<head>
    <%@include file="/include/htmlHead.jsp"%>
</head>
<style>
    /* Allinea gli elementi del form in colonne */
    .field {
        display: flex;
        flex-direction: column;
        margin-bottom: 10px;
    }

    /* Aggiusta lo stile delle etichette dei campi */
    .field label {
        font-weight: bold;
    }

    /* Stile dei pulsanti */
    .field input[type="submit"],
    .field input[type="button"] {
        padding: 10px 20px;
        background-color: #007bff;
        color: #fff;
        border: none;
        border-radius: 4px;
        font-size: 14px;
        cursor: pointer;
        margin-top: 10px;
    }

    /* Stile del pulsante "Annulla" */
    .field input[name="backButton"] {
        background-color: #dc3545;
    }

    /* Allinea il pulsante "Annulla" a destra */
    .field label:last-child {
        display: flex;
        justify-content: flex-end;
        align-items: center;
    }

    /* Aggiusta il margine superiore del titolo della sezione */
    #pageTitle h1 {
        margin-top: 0;
        font-size: 24px;
    }

    .large-text{
        font-size: large;
    }
</style>
<script>
    function submitAuction(){
        let f;
        f = document.ModForm;
        f.controllerAction.value = "ThresholdManagement.checkOnUpdate";
    }
    function goBack(){
        document.backForm.submit();
    }
    function mainOnLoadHandler(){
        document.ModForm.addEventListener("submit",submitAuction);
        document.ModForm.backButton.addEventListener("click", goBack);
    }
</script>
<body>
<%@include file="/include/header.jsp"%>
<main>
    <section id="pageTitle">
        <h1>
            Abbassa il Prezzo del Prodotto
        </h1>
    </section>

    <section id="ModFormSection">
        <form name="ModForm" action="Dispatcher" method="post">
            <span class="large-text">Attualmente il Prodotto costa: </span><br/>
            <span class="float-value"><%=auction.getProduct_auctioned().getCurrent_price()%></span><br/>
            <span class="large-text">Il prezzo minimo impostato vale: </span><br/>
            <span class="float-value"><%=auction.getProduct_auctioned().getMin_price()%></span><br/>

            <div class="field clearfix">
                <label for="price">Nuovo prezzo (&euro;)</label>
                <input type="number" id="price" name="price"
                       required size="20" maxlength="24" min="<%=auction.getProduct_auctioned().getMin_price()%>" max="<%=auction.getProduct_auctioned().getCurrent_price()%>"
                       step="0.01"/>
            </div>

            <div class="field clearfix">
                <label>&#160;</label>
                <input type="submit" class="button" value="Invia"/>
                <input type="button" name="backButton" class="button" value="Annulla"/>
            </div>

            <input type="hidden" name="controllerAction"/>
            <input type="hidden" name="pageToReturn" value="auctionManagement/view">
            <input type="hidden" name="auctionID" value="<%=auction.getAuctionID()%>"/>
        </form>
    </section>

    <form name="backForm" method="post" action="Dispatcher">
        <input type="hidden" name="controllerAction" value="AuctionManagement.viewMyAuctions">
    </form>

</main>
<%@include file="/include/footer.inc"%>
</body>
</html>
