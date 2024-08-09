<%@ page session="false"%>
<%@ page import="com.ingsw.flyingdutchman.model.mo.Order" %>

<%
    Order[] orders = (Order[]) request.getAttribute("orders");
%>
<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <%@include file="../include/htmlHead.jsp"%>
        <style>
            #orders{
                display: flex;
                flex-direction: column;
                flex-flow: column;
            }

            #orderContainer{
                margin: 10px 10px;
            }

            #orderContainer span{
                font-size: large;
            }

            #orderListBreak hr{
                width: auto;
                color: #39ce29;
                height: auto;
                margin-bottom: 10px;
            }

            #boughtFromThreshold{
                color: #39ce29;
            }
        </style>
        <title></title>
    </head>
    <body>
        <%@include file="../include/header.jsp"%>
        <main>
            <section id="pageTitle">
                <h1>Ordini Effettuati</h1>
            </section>

            <section id="orderListBreak">
                <hr/>
            </section>

            <section id="orders">
                <%if(orders.length > 0){%>
                    <%for(int i = 0; i < orders.length; i++){%>
                        <article id="orderContainer" class="clearfix">
                            <span id="counter" class="counter"><%=i+1%>- </span>
                            <b><span id="productDescription" class="description"><%=orders[i].getProduct().getDescription()%></span></b><br/>
                            <span id="order_time" class="order_time"><%=orders[i].getOrderTime().toString().substring(0,10)%> -
                                <%=orders[i].getOrderTime().toString().substring(10, 16)%>
                            </span><br/>
                            <span id="productPrice" class="float-value"><%=orders[i].getSellingPrice()%></span><br/>
                            <%if(orders[i].getProduct().getProductID() != 1){%> <!-- Premium -->
                                <span id="seller" class="seller">Comprato da: <%=orders[i].getProduct().getOwner().getUsername()%></span><br/>
                                <%if(orders[i].getBoughtFromThreshold() == 'Y'){%>
                                    <span id="boughtFromThreshold" class="boughtFromThreshold">Questo Prodotto &egrave; stato comprato da una Prenotazione!</span><br/>
                                <%}%>
                            <%}%>
                            <span id="separator" class="separator">---</span>
                        </article>
                    <%}%>
                <%}%>
            </section>
            <%if(orders.length == 0){%>
                <section id="noOrders">
                    <h2>
                        Non hai ancora completato nessun ordine
                    </h2>
                </section>
            <%}%>
        </main>
        <%@include file="../include/footer.inc"%>
    </body>
</html>
