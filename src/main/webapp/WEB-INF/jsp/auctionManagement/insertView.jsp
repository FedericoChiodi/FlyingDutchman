<%@ page session="false"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.ingsw.flyingdutchman.model.mo.User" %>
<%@ page import="com.ingsw.flyingdutchman.model.mo.Product" %>

<%
    int i = 0;
    boolean loggedOn = (Boolean) request.getAttribute("loggedOn");
    User loggedUser = (User) request.getAttribute("loggedUser");
    String applicationMessage = (String) request.getAttribute("applicationMessage");
    String menuActiveLink = "Aste";
    Product[] products = (Product []) request.getAttribute("products");
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
        margin-bottom: 15px;
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

    #pageTitle h1 {
        margin-top: 0;
        font-size: 24px;
    }
    #insertForm div{
        margin-top: 12px;
        margin-bottom: 12px;
    }
    #productID{
        font-size: large;
    }
</style>
<script>
    function submitAuction(){
        const currentDate = new Date();
        const datetime
            = currentDate.getFullYear() + "-"
            + (currentDate.getMonth()+1)  + "-"
            + currentDate.getDate() + " "
            + currentDate.getHours() + ":"
            + currentDate.getMinutes() + ":"
            + currentDate.getSeconds();

        document.querySelector("#opening_timestamp").value = datetime;

        var f;
        f = document.insertForm;
        f.controllerAction.value = "AuctionManagement.insert";
    }
    function goBack(){
        document.backForm.submit();
    }
    function mainOnLoadHandler(){
        document.insertForm.addEventListener("submit",submitAuction);
        document.insertForm.backButton.addEventListener("click", goBack);
    }
</script>
<body>
<%@include file="/include/header.jsp"%>
<main>

    <%if(products.length > 0){%>
        <label id="pageTitle" for="productID">
            <h1>
                Seleziona un prodotto da vendere
            </h1>
        </label>
    <%}%>

    <section id="insertFormSection">
        <form id="insertForm" name="insertForm" action="Dispatcher" method="post">

            <%if(products.length > 0){%>
                <div id="productsList">
                    <select id="productID" name="productID">
                        <%for(i = 0; i < products.length ; i++){%>
                            <option value="<%=products[i].getProductID()%>"><%=products[i].getDescription()%></option>
                        <%}%>
                    </select>
                </div>

                <div class="field clearfix">
                    <input type="hidden" id="opening_timestamp" name="opening_timestamp"/>
                </div>

                <div class="field clearfix">
                    <label>&#160;</label>
                    <input type="submit" class="button" value="Apri l'asta!"/>
                    <input type="button" name="backButton" class="button" value="Annulla"/>
                </div>
            <%}%>
            <%if(products.length == 0){%>
                <section id="noProducts">
                    <h1>
                        Non ci sono prodotti nel tuo account o sono tutti gi&aacute; all'asta!
                    </h1>
                </section>
            <%}%>

            <input type="hidden" name="controllerAction"/>
        </form>
    </section>

    <form name="backForm" method="post" action="Dispatcher">
        <input type="hidden" name="controllerAction" value="AuctionManagement.view">
    </form>

</main>
<%@include file="/include/footer.inc"%>
</body>
</html>
