<%@ page session="false"%>
<%@page import="com.ingsw.flyingdutchman.model.mo.Auction"%>
<%@ page import="java.io.File" %>
<%@ page import="com.ingsw.flyingdutchman.model.mo.Category" %>
<%
    Auction[] auctions = (Auction[]) request.getAttribute("auctions");
    Boolean canEditReq = (Boolean) request.getAttribute("canEdit");
    boolean canEdit;
    if(canEditReq == null)
        canEdit = false;
    else
        canEdit = canEditReq;
    Category[] categories = (Category[]) request.getAttribute("categories");
    if (categories.length == 0){
        categories[0] = new Category();
        categories[0].setName("esempio...");
    }
%>
<!DOCTYPE html>
<html>
    <head>
        <%@include file="../include/htmlHead.jsp"%>
        <script>
            function insertAuction(){
                document.insertForm.submit();
            }
            function viewMyAuctions(){
                document.viewMyAuctionsForm.submit();
            }
            function viewAllAuctions(){
                document.viewAllAuctionsForm.submit();
            }
            function editAuction(auctionID){
                document.editForm.auctionID.value = auctionID;
                document.editForm.submit();
            }
            function mainOnLoadHandler(){
                document.querySelector("#insertAuctionButton").addEventListener("click",insertAuction);
            }
            function inspectAuction(auctionID){
                document.inspectForm.auctionID.value = auctionID;
                document.inspectForm.submit();
            }

        </script>
        <style>
            /* Stile dei pulsanti */
            .button {
                padding: 12px 24px;
                font-size: 16px;
                cursor: pointer;
                transition: background-color 0.3s ease;
                background-color: #28a745;
            }
            #auctions{
                display: grid;
                grid-template-columns: repeat(3, 1fr);
                justify-items: center;
            }
            #auctions button{
                float: left;
                width: 250px;
                border-width: 1px;
                border-style: solid;
                border-radius: 10px;
                border-color: #1f9233;
                padding: 10px 10px 10px 10px;
                margin: 0 18px 16px 0;
                background: linear-gradient(to top, #4cf63b, #39ce29);
                box-shadow: 0 3px 2px #777;
            }
            #auctionButton:hover{
                background: #38b52a;
                cursor: pointer;
            }
            #auctionButton:active{
                background: #28a745;
                cursor: pointer;
            }
            #auctionListBreak hr{
                width: auto;
                color: #39ce29;
                height: auto;
                margin-bottom: 10px;
            }
            #productImg{
                width: 175px;
                height: 175px;
                object-fit: scale-down;
                border: 1px solid #3b3b37;
            }
            #productDescription{
                font-size: large;
            }
            #productPrice{
                font-size: x-large;
            }
            #headContainer{
                display: flex;
                flex-direction: row;
                margin-bottom: 12px;
            }
            #insertAuctionButtonSelection{
                margin-right: 12px;
            }
            #searchFormSection{
                margin-left: auto;
                align-self: end;
                display: flex;
                flex-direction: row;
                align-items: start;
            }
            #searchCategoryForm{
                margin-right: 25px;
            }
            .hidden-label{
                display: none;
            }
        </style>
        <title></title>
    </head>
    <body>
        <%@include file="../include/header.jsp"%>
    <main>
        <section id="pageTitle">
            <h1>Aste in Corso</h1>
        </section>

        <section id="headContainer">
            <article id="insertAuctionButtonSelection">
                <input type="button" id="insertAuctionButton" name="insertAuctionButton"
                       class="button" value="Metti in asta un Prodotto" onclick="insertAuction()"/>
            </article>

            <%if(!canEdit){%>
            <article id="viewMyAuctionsButtonSelection">
                <input type="button" id="viewMyAuctionsButton" name="viewAuctionsButton"
                       class="button" value="Visualizza le tue Aste" onclick="viewMyAuctions()"/>
            </article>

            <article id="searchFormSection">
                <form id="searchCategoryForm" name="searchCategoryForm" action="/auctionManagement/searchCategory" method="post">
                    <label for="categoryID" class="hidden-label">Elenco delle categorie: </label>
                    <select id="categoryID" name="categoryID">
                        <%for(Category category : categories){%>
                        <option value="<%=category.getCategoryID()%>"><%=category.getName()%></option>
                        <%}%>
                    </select>
                    <label for="searchCategoryButton" class="hidden-label">Cerca per Categoria:</label>
                    <input type="submit" id="searchCategoryButton" name="searchCategoryButton" value="Cerca"/>
                </form>

                <form id="searchForm" name="searchForm" action="/auctionManagement/search" method="post">
                    <label for="auctionName" class="hidden-label">Cerca un'asta: </label>
                    <input type="text" id="auctionName" name="auctionName" required size="15" maxlength="200" minlength="5"
                           placeholder="Cerca un'asta..." autocomplete="off"/>
                    <input type="submit" id="searchButton" name="searchButton" value="Cerca"/>
                </form>
            </article>

            <%}%>
            <%if(canEdit){%>
            <article id="viewAuctionsButtonSelection">
                <input type="button" id="viewAuctionsButton" name="viewAuctionsButton"
                       class="button" value="Visualizza tutte le aste" onclick="viewAllAuctions()"/>
            </article>
            <%}%>


        </section>

        <section id="auctionListBreak">
            <hr/>
        </section>

        <%if(auctions.length > 0){%>
            <section id="auctions" class="clearfix">
                <%for (Auction auction : auctions){%>
                        <button id="auctionButton" onclick="inspectAuction(<%=auction.getAuctionID()%>)">
                            <b><span id="productDescription" class="description"><%=auction.getProduct_auctioned().getDescription()%></span></b><br/>
                            <span id="productPrice" class="float-value"><%=auction.getProduct_auctioned().getCurrent_price()%></span><br/>
                            <br/>
                            <%
                                String imgPath = "/Uploads/" + auction.getProduct_auctioned().getOwner().getUsername() + "/" + auction.getProduct_auctioned().getDescription() + ".png";
                                String imgPathAbs = "/home/sanpc/tomcat/webapps" + imgPath;
                                File file = new File(imgPathAbs);
                                if (!file.exists()) {
                                    imgPath = "/Uploads/default.png";
                                }
                            %>
                            <img id="productImg" src="<%=imgPath%>" alt="Immagine del Prodotto">
                        </button>
                <%}%>
            </section>
        <%}%>
        <%if(auctions.length == 0){%>
            <section id="noAuctions">
                <%if(canEdit){%>
                    <h2>
                        Non hai ancora inserito nessun'asta!
                    </h2>
                <%}%>
                <%if(!canEdit){%>
                    <h2>
                        Non sono state trovate aste in corso!
                    </h2>
                <%}%>
            </section>
        <%}%>

        <form name="insertForm" method="get" action="/auctionManagement/view">
        </form>

        <form name="inspectForm" method="get" action="/auctionManagement/inspect">
            <input type="hidden" name="auctionID"/>
        </form>

        <form name="viewMyAuctionsForm" method="get" action="/auctionManagement/myAuctions">
        </form>

        <form name="viewAllAuctionsForm" method="get" action="/auctionManagement/view">
        </form>

        <form name="editForm" method="get" action="/auctionManagement/update">
            <input type="hidden" name="auctionID"/>
        </form>
    </main>
        <%@include file="../include/footer.inc"%>
    </body>
</html>
