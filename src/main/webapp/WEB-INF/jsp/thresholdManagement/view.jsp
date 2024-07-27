<%@ page session="false"%>
<%@page import="com.ingsw.flyingdutchman.model.mo.User"%>
<%@ page import="com.ingsw.flyingdutchman.model.mo.Threshold" %>

<%
    int i = 0;
    boolean loggedOn = (Boolean) request.getAttribute("loggedOn");
    User loggedUser = (User) request.getAttribute("loggedUser");
    String applicationMessage = (String) request.getAttribute("applicationMessage");
    String menuActiveLink = "Prenota";
    Threshold[] thresholds = (Threshold[]) request.getAttribute("thresholds");
%>

<!DOCTYPE html>
<html>
<head>
    <%@include file="/include/htmlHead.jsp"%>
    <script>
        function deleteThreshold(thresholdID){
            document.deleteForm.thresholdID.value = thresholdID;
            if(confirm("Attenzione! Questa azione e' irreversibile. Vuoi procedere?")){
                document.deleteForm.submit();
            }
        }
        function editThreshold(thresholdID){
            document.editForm.thresholdID.value = thresholdID;
            document.editForm.submit();
        }
        function goToAuctions(){
            document.goToAuctionsForm.submit();
        }
    </script>
    <style>
        /* Stile dei pulsanti */
        #auctionsButton {
            padding: 12px 24px;
            font-size: 16px;
            cursor: pointer;
            transition: background-color 0.3s ease;
            background-color: #28a745;
        }
        #thresholds{
            margin: 12px 0;
        }
        #thresholds article{
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
        #pencil{
            float: right;
            margin-right: 7px;
        }
        #thresholdListBreak hr{
            width: auto;
            color: #39ce29;
            height: auto;
        }
        #noThresholds{
            margin-top: 20px;
            font-size: x-large;
        }
    </style>
</head>
<body>
<%@include file="/include/header.jsp"%>
<main>

    <section id="pageTitle">
        <h1>Centro gestione Prenotazioni</h1>
    </section>

    <section id="thresholdListBreak">
        <hr>
    </section>

    <%if(thresholds.length > 0){%>
        <section id="thresholds" class="clearfix">
            <%for (i = 0; i < thresholds.length; i++){%>
                <%if(!thresholds[i].getAuction().isDeleted()){%>
                    <article>
                        <a href="javascript:deleteThreshold(<%=thresholds[i].getThresholdID()%>)">
                            <img id="trashcan" src="images/trashcan.png" width="24" height="24" alt="X">
                        </a>
                        <a href="javascript:editThreshold(<%=thresholds[i].getThresholdID()%>)">
                            <img id="pencil" src="images/pencil.png" width="24" height="24" alt="E">
                        </a>
                        <b><span class="description"><%=thresholds[i].getAuction().getProduct_auctioned().getDescription()%></span></b>
                        <br/>
                        <span class="float-value"><%=thresholds[i].getPrice()%></span>
                    </article>
                <%}%>
            <%}%>
        </section>
    <%}%>
    <%if(thresholds.length == 0){%>
        <h1 id ="noThresholds">
            Non hai ancora inserito nessuna prenotazione!
        </h1>
        <br/>
        <input type="button" id="auctionsButton" name="auctionsButton"
               class="button" value="Inizia Subito!" onclick="goToAuctions()"/>
    <%}%>

    <form name="deleteForm" method="post" action="Dispatcher">
        <input type="hidden" name="thresholdID"/>
        <input type="hidden" name="controllerAction" value="ThresholdManagement.delete"/>
    </form>

    <form name="editForm" method="post" action="Dispatcher">
        <input type="hidden" name="thresholdID"/>
        <input type="hidden" name="controllerAction" value="ThresholdManagement.modifyView"/>
    </form>

    <form name="goToAuctionsForm" method="post" action="Dispatcher">
        <input type="hidden" name="controllerAction" value="AuctionManagement.view"/>
    </form>

</main>
<%@include file="/include/footer.inc"%>
</body>
</html>
