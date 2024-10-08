<%@ page session="false"%>
<%@page import="com.ingsw.flyingdutchman.model.mo.User"%>
<%@page import="com.ingsw.flyingdutchman.model.mo.Product"%>
<%@page import="java.util.List" %>

<%
    @SuppressWarnings("unchecked")
    List<Product> products = (List<Product>) request.getAttribute("products");
    Boolean soldProductsAction = (Boolean) request.getAttribute("soldProductsAction");
%>

<!DOCTYPE html>
<html>
    <head>
        <%@include file="../include/htmlHead.jsp"%>
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
        <title></title>
    </head>
    <body>
        <%@include file="../include/header.jsp"%>
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

        <%if(!products.isEmpty()){%>
            <section id="products" class="clearfix">
                <%int i = 0;%>
                <%for (Product product : products){%>
                    <article>
                        <%if(!soldProductsAction){%>
                            <a href="javascript:deleteProduct(<%=product.getProductID()%>)">
                                <img id="trashcan" src="/images/trashcan.png" width="24" height="24" alt="X">
                            </a>
                        <%}%>
                        <b><span class="description"><%=product.getDescription()%></span></b>
                        <br/>
                        <%if(soldProductsAction){%>
                            <label for="current_price_sold">Venduto per: </label>
                            <span id="current_price_sold" class="float-value"><%=product.getCurrent_price()%></span>
                        <%}%>
                        <%if(!soldProductsAction){%>
                        <label for="current_price">Attualmente costa: </label>
                            <span id="current_price" class="float-value"><%=product.getCurrent_price()%></span>
                        <%}%>
                    </article>
                    <%i++;%>
                <%}%>
            </section>
        <%}%>

        <form name="insertForm" method="get" action="/productManagement/insertView">
        </form>

        <form name="viewSoldProductsForm" method="post" action="/productManagement/viewSold">
        </form>

        <form name="viewProductForm" method="get" action="/productManagement/view">
        </form>

        <form name="deleteForm" method="post" action="/productManagement/delete">
            <input type="hidden" name="productID"/>
        </form>
    </main>
        <%@include file="../include/footer.inc"%>
    </body>
</html>
