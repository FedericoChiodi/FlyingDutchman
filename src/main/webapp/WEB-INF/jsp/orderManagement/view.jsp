<%@ page session="false"%>
<%@ page import="com.ingsw.flyingdutchman.model.mo.Order" %>
<%@ page import="java.util.List"%>

<%
    @SuppressWarnings("unchecked")
    List<Order> orders = (List<Order>) request.getAttribute("orders");
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
                <%if(!orders.isEmpty()){%>
                    <%for(Order order : orders){%>
                        <article id="orderContainer" class="clearfix">
                            <span id="counter" class="counter">- </span>
                            <b><span id="productDescription" class="description"><%=order.getProduct().getDescription()%></span></b><br/>
                            <span id="order_time" class="order_time"><%=order.getOrderTime().toString().substring(0,10)%> -
                                <%=order.getOrderTime().toString().substring(10, 16)%>
                            </span><br/>
                            <span id="productPrice" class="float-value"><%=order.getSellingPrice()%></span><br/>
                            <%if(order.getProduct().getProductID() != 1){%> <!-- Premium -->
                                <span id="seller" class="seller">Comprato da: <%=order.getProduct().getOwner().getUsername()%></span><br/>
                                <%if(order.getBoughtFromThreshold() == 'Y'){%>
                                    <span id="boughtFromThreshold" class="boughtFromThreshold">Questo Prodotto &egrave; stato comprato da una Prenotazione!</span><br/>
                                <%}%>
                            <%}%>
                            <span id="separator" class="separator">---</span>
                        </article>
                    <%}%>
                <%}%>
            </section>
            <%if(orders.isEmpty()){%>
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
