<%@ page session="false"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.ingsw.flyingdutchman.model.mo.User" %>
<%@ page import="com.ingsw.flyingdutchman.model.mo.Threshold" %>
<%@ page import="com.ingsw.flyingdutchman.model.mo.Auction" %>

<%
    int i = 0;
    boolean loggedOn = (Boolean) request.getAttribute("loggedOn");
    User loggedUser = (User) request.getAttribute("loggedUser");
    String applicationMessage = (String) request.getAttribute("applicationMessage");
    String menuActiveLink = "Prenota";
    Threshold threshold = (Threshold) request.getAttribute("threshold");
    Auction auction = (Auction) request.getAttribute("auction");
    String action = (threshold!=null) ? "modify" : "insert";
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
</style>
<script>
    let status  = "<%=action%>";

    function submitThreshold(){
        let f;
        f = document.insModForm;
        f.controllerAction.value = "ThresholdManagement."+status;
        <%if(action.equals("modify")){%>
            f.thresholdID.value = <%=threshold.getThresholdID()%>;
        <%}%>
        <%if(action.equals("insert")){%>
            f.auctionID.value = <%=auction.getAuctionID()%>;
        <%}%>
    }
    function goBack(){
        <%if(action.equals("modify")){%>
            document.backForm.controllerAction.value = "ThresholdManagement.view";
        <%}%>
        <%if(action.equals("insert")){%>
            document.backForm.auctionID.value = <%=auction.getAuctionID()%>;
            document.backForm.controllerAction.value = "AuctionManagement.inspectAuction";
        <%}%>
        document.backForm.submit();
    }
    function mainOnLoadHandler(){
        document.insModForm.addEventListener("submit",submitThreshold);
        document.insModForm.backButton.addEventListener("click", goBack);
    }
</script>
<body>
<%@include file="/include/header.jsp"%>
<main>
    <section id="pageTitle">
        <h1>
            <%=(action.equals("modify")) ? "Modifica il prezzo" : "Inserisci il prezzo"%>
        </h1>
    </section>

    <section id="insModFormSection">
        <form name="insModForm" action="Dispatcher" method="post">

            <div class="field clearfix">
                <span>Prezzo corrente del Prodotto: </span>
                <span class="float-value"><%=(action.equals("modify")) ? threshold.getAuction().getProduct_auctioned().getCurrent_price() : auction.getProduct_auctioned().getCurrent_price()%></span><br/>

                <%if(action.equals("modify")){%>
                    <span>Prezzo corrente della Prenotazione: </span>
                    <span class="float-value"><%=threshold.getPrice()%></span><br/>
                <%}%>

                <label for="price"><%=(action.equals("modify")) ? "Nuovo Prezzo da fissare" : "Prezzo da fissare"%>(&euro;)</label>
                <input type="number" id="price" name="price"
                       required size="20" maxlength="40" max="<%=(action.equals("modify")) ? threshold.getAuction().getProduct_auctioned().getCurrent_price()-0.01 : auction.getProduct_auctioned().getCurrent_price()-0.01%>"
                       min="0" step="0.01"/>
            </div>

            <div class="field clearfix">
                <label>&#160;</label>
                <input type="submit" class="button" value="Prenota"/>
                <input type="button" name="backButton" class="button" value="Annulla"/>
            </div>

            <input type="hidden" name="auctionID"/>
            <input type="hidden" name="thresholdID"/>
            <input type="hidden" name="controllerAction"/>
        </form>
    </section>

    <form name="backForm" method="post" action="Dispatcher">
        <input type="hidden" name="controllerAction">
        <input type="hidden" name="auctionID"/>
    </form>

</main>
<%@include file="/include/footer.inc"%>
</body>
</html>
