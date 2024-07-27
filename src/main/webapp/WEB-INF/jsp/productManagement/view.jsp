<%@ page session="false"%>
<%@page import="com.ingsw.flyingdutchman.model.mo.User"%>
<%@page import="com.ingsw.flyingdutchman.model.mo.Product"%>

<%
    int i = 0;
    boolean loggedOn = (Boolean) request.getAttribute("loggedOn");
    User loggedUser = (User) request.getAttribute("loggedUser");
    String applicationMessage = (String) request.getAttribute("applicationMessage");
    String menuActiveLink = "Prodotti";
    Product[] products = (Product[]) request.getAttribute("products");
    Boolean soldProductsAction = (Boolean) request.getAttribute("soldProductsAction");
    User[] buyers = (User[]) request.getAttribute("buyers");
%>

<!DOCTYPE html>
<html>
    <head>
        <%@include file="/include/htmlHead.jsp"%>
        <script>
            function insertProduct(){
                document.insertForm.submit();
            }
            function viewProduct(){
                document.viewProductForm.submit();
            }
            function viewSoldProducts(){
                document.viewSoldProductsForm.submit();
            }
            function mainOnLoadHandler(){
                document.querySelector("#newProductButton").addEventListener("click",insertProduct);
            }
            function deleteProduct(productID){
                document.deleteForm.productID.value = productID;
                if(confirm("Attenzione! Questa azione e' irreversibile. Vuoi procedere?")){
                    document.deleteForm.submit();
                }
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
            #insertProductButtonSelection{
                margin-right: 12px;
            }
            #products{
                margin: 12px 0;
            }
            #products article{
                float: left;
                width: 250px;
                border-width: 1px;
                border-style: solid;
                border-radius: 10px;
                border-color: #1f9233;
                padding: 10px 8px 10px 20px;
                margin: 0 18px 16px 0;
                background: linear-gradient(to right, #39ce29, #4cf63b);
                box-shadow: 0 3px 2px #777;
            }
            #products article a{
                color: #28a745;
            }
            #trashcan{
                float: right;
            }
            #productListBreak hr{
                width: auto;
                color: #39ce29;
                height: auto;
            }
            #buttons{
                display: flex;
                flex-direction: row;
                margin-bottom: 12px;
            }
        </style>
    </head>
    <body>
        <%@include file="/include/header.jsp"%>
    <main>
        <%if(!soldProductsAction){%>
            <section id="pageTitle">
                <h1>Centro Gestione dei Prodotti</h1>
            </section>
        <%}%>
        <%if(soldProductsAction){%>
        <section id="pageTitle">
            <h1>Elenco dei Prodotti Venduti</h1>
        </section>
        <%}%>

        <section id="buttons">
            <article id="insertProductButtonSelection">
                <input type="button" id="insertProductButton" name="insertProductButton"
                       class="button" value="Inserisci un Prodotto nel tuo Account" onclick="insertProduct()"/>
            </article>

            <%if(!soldProductsAction){%>
                <article id="viewSoldProductsSelection">
                    <input type="button" id="viewSoldProductsButton" name="viewSoldProductsButton"
                           class="button" value="Visualizza i Prodotti Venduti" onclick="viewSoldProducts()"/>
                </article>
            <%}%>
            <%if(soldProductsAction){%>
                <article id="viewProductsSelection">
                    <input type="button" id="viewProductsButton" name="viewProductsButton"
                           class="button" value="Visualizza i prodotti nel tuo account" onclick="viewProduct()"/>
                </article>
            <%}%>
        </section>

        <section id="productListBreak">
            <hr>
        </section>

        <%if(products.length > 0){%>
            <section id="products" class="clearfix">
                <%for (i = 0; i < products.length; i++){%>
                    <article>
                        <%if(!soldProductsAction){%>
                            <a href="javascript:deleteProduct(<%=products[i].getProductID()%>)">
                                <img id="trashcan" src="images/trashcan.png" width="24" height="24" alt="X">
                            </a>
                        <%}%>
                        <b><span class="description"><%=products[i].getDescription()%></span></b>
                        <br/>
                        <%if(soldProductsAction){%>
                            <label for="who_sold">Venduto a: </label>
                            <span id="who_sold"><%=buyers[i].getUsername()%></span><br/>
                            <label for="current_price_sold">Venduto per: </label>
                            <span id="current_price_sold" class="float-value"><%=products[i].getCurrent_price()%></span>
                        <%}%>
                        <%if(!soldProductsAction){%>
                        <label for="current_price">Attualmente costa: </label>
                            <span id="current_price" class="float-value"><%=products[i].getCurrent_price()%></span>
                        <%}%>
                    </article>
                <%}%>
            </section>
        <%}%>

        <form name="insertForm" method="post" action="Dispatcher">
            <input type="hidden" name="controllerAction" value="ProductManagement.insertView"/>
        </form>

        <form name="viewSoldProductsForm" method="post" action="Dispatcher">
            <input type="hidden" name="controllerAction" value="ProductManagement.viewSoldProducts"/>
        </form>

        <form name="viewProductForm" method="post" action="Dispatcher">
            <input type="hidden" name="controllerAction" value="ProductManagement.view"/>
        </form>

        <form name="deleteForm" method="post" action="Dispatcher">
            <input type="hidden" name="productID"/>
            <input type="hidden" name="controllerAction" value="ProductManagement.delete"/>
        </form>
    </main>
        <%@include file="/include/footer.inc"%>
    </body>
</html>
