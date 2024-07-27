<%@page session="false"%>
<%@page import="com.ingsw.flyingdutchman.model.mo.Auction"%>
<%@page import="com.ingsw.flyingdutchman.model.mo.User"%>
<%@ page import="java.io.File" %>
<%
    int i = 0;
    boolean loggedOn = (Boolean) request.getAttribute("loggedOn");
    User loggedUser = (User) request.getAttribute("loggedUser");
    String applicationMessage = (String) request.getAttribute("applicationMessage");
    String menuActiveLink = "Aste";
    Auction auction = (Auction) request.getAttribute("auction");
%>
<!DOCTYPE html>
<html>
    <head>
        <%@include file="/include/htmlHead.jsp"%>
        <script>
            function buyProduct(auctionID){
                document.buyForm.auctionID.value = auctionID;
                document.buyForm.submit();
            }
            function goBack(){
                document.backForm.submit();
            }
            function lowerPrice(auctionID){
                document.lowerPriceForm.auctionID.value = auctionID;
                document.lowerPriceForm.submit();
            }
            function insertThreshold(auctionID){
                document.insertThresholdForm.auctionID.value = auctionID;
                document.insertThresholdForm.submit();
            }
            function deleteAuction(auctionID){
                document.deleteForm.auctionID.value = auctionID;
                if(confirm("Attenzione! Questa azione e' irreversibile. Vuoi procedere?")){
                    document.deleteForm.submit();
                }
            }
        </script>
        <style>
            #productContainer {
                display: flex;
                align-items: center;
                flex-direction: row;
            }
            #productInfoContainer{
                display: flex;
                align-items: center;
                flex-direction: column;
                margin-bottom: 15px;
                margin-top: 25px;
            }
            #productDescription, #productPrice{
                display: block;
                margin-top: 15px;
                font-size: xx-large;
            }
            #productSeller{
                font-size: medium;
            }
            #productInfoContainer button{
                font-size: large;
                margin-bottom: 10px;
            }
            #productImage {
                width: 300px;
                height: 300px;
                margin-right: 20px;
                margin-top: 25px;
                border: 1px solid #3b3b37;
                box-shadow: 0 4px 4px 2px #888;
            }
            #buyProductButton{
                padding: 10px 20px;
                background-color: #337ab7;
                color: #fff;
                border: none;
                border-radius: 5px;
                cursor: pointer;
                margin-right: 10px;
                margin-top: 10px;
            }
            #backButton{
                padding: 10px 20px;
                background-color: #dc3545;
                color: #fff;
                border: none;
                border-radius: 5px;
                cursor: pointer;
            }
            #deleteButton{
                padding: 10px 20px;
                background-color: #91101c;
                color: #fff;
                border: none;
                border-radius: 5px;
                cursor: pointer;
            }
            #thresholdButton{
                padding: 10px 20px;
                background-color: darkorange;
                color: #fff;
                border: none;
                border-radius: 5px;
                cursor: pointer;
            }
            #lowerPriceButton{
                padding: 10px 20px;
                background-color: #39ce29;
                color: #fff;
                border: none;
                border-radius: 5px;
                cursor: pointer;
                margin-top: 15px;
            }
        </style>
    </head>
    <body>
        <%@include file="/include/header.jsp"%>
        <main>
            <section id="productContainer">
                <article id="productImageContainer">
                    <%
                        String imgPath = "/Uploads/" + auction.getProduct_auctioned().getOwner().getUsername() + "/" + auction.getProduct_auctioned().getDescription() + ".png";
                        String imgPathAbs = "/home/sanpc/tomcat/webapps" + imgPath;
                        File file = new File(imgPathAbs);
                        if (!file.exists()) {
                            imgPath = "/Uploads/default.png";
                        }
                    %>
                    <img id="productImage" src="<%=imgPath + "?nocache=" + System.currentTimeMillis()%>" alt="Product_Image">
                </article>
                <article id="productInfoContainer">
                    <span id="productDescription"><%=auction.getProduct_auctioned().getDescription()%></span>
                    <br/>
                    <span id="productSeller">Venduto da: <%=auction.getProduct_auctioned().getOwner().getUsername()%></span>
                    <br/><br/>
                    <span id="productPrice" class="float-value"><%=auction.getProduct_auctioned().getCurrent_price()%></span>
                    <br/>
                    <%if(!auction.getProduct_auctioned().getOwner().getUsername().equals(loggedUser.getUsername())){%>
                        <button id="buyProductButton" onclick="buyProduct(<%=auction.getAuctionID()%>)">Compra questo Prodotto</button>
                        <%if(!loggedUser.getRole().equals("Default")){%>
                        <button id="thresholdButton" onclick="insertThreshold(<%=auction.getAuctionID()%>)">Prenota</button>
                        <%}%>
                    <%}%>
                    <%if(auction.getProduct_auctioned().getOwner().getUsername().equals(loggedUser.getUsername())){%>
                        <button id="lowerPriceButton" onclick="lowerPrice(<%=auction.getAuctionID()%>)">Abbassa il Prezzo</button>
                        <button id="deleteButton" onclick="deleteAuction(<%=auction.getAuctionID()%>)">Elimina</button>
                    <%}%>
                    <button id="backButton" onclick="goBack()">Torna Indietro</button>
                </article>
            </section>

            <form name="buyForm" method="post" action="Dispatcher">
                <input type="hidden" name="auctionID"/>
                <input type="hidden" name="controllerAction" value="AuctionManagement.buyProductAuctioned"/>
            </form>

            <form name="insertThresholdForm" method="post" action="Dispatcher">
                <input type="hidden" name="auctionID"/>
                <input type="hidden" name="controllerAction" value="ThresholdManagement.insertView"/>
            </form>

            <form name="lowerPriceForm" method="post" action="Dispatcher">
                <input type="hidden" name="auctionID"/>
                <input type="hidden" name="controllerAction" value="AuctionManagement.updateView"/>
            </form>

            <form name="backForm" method="post" action="Dispatcher">
                <input type="hidden" name="controllerAction" value="<%=auction.getProduct_auctioned().getOwner().getUsername().equals(loggedUser.getUsername()) ? "AuctionManagement.viewMyAuctions" : "AuctionManagement.view"%>">
            </form>

            <form name="deleteForm" method="post" action="Dispatcher">
                <input type="hidden" name="auctionID"/>
                <input type="hidden" name="controllerAction" value="AuctionManagement.delete"/>
            </form>
        </main>
        <%@include file="/include/footer.inc"%>
    </body>
</html>
