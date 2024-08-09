<%@ page session="false"%>
<%@ page import="com.ingsw.flyingdutchman.model.mo.Threshold" %>
<%@ page import="java.util.List"%>

<%
    @SuppressWarnings("unchecked")
    List<Threshold> thresholds = (List<Threshold>) request.getAttribute("thresholds");
%>

<!DOCTYPE html>
<html>
<head>
    <%@include file="../include/htmlHead.jsp"%>
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
    <title></title>
</head>
<body>
<%@include file="../include/header.jsp"%>
<main>

    <section id="pageTitle">
        <h1>Centro gestione Prenotazioni</h1>
    </section>

    <section id="thresholdListBreak">
        <hr>
    </section>

    <%if(!thresholds.isEmpty()){%>
        <section id="thresholds" class="clearfix">
            <%for (Threshold threshold : thresholds){%>
                <%if(threshold.getAuction().getDeleted() == 'N'){%>
                    <article>
                        <a href="javascript:deleteThreshold(<%=threshold.getThresholdID()%>)">
                            <img id="trashcan" src="/images/trashcan.png" width="24" height="24" alt="X">
                        </a>
                        <a href="javascript:editThreshold(<%=threshold.getThresholdID()%>)">
                            <img id="pencil" src="/images/pencil.png" width="24" height="24" alt="E">
                        </a>
                        <b><span class="description"><%=threshold.getAuction().getProduct_auctioned().getDescription()%></span></b>
                        <br/>
                        <span class="float-value"><%=threshold.getPrice()%></span>
                    </article>
                <%}%>
            <%}%>
        </section>
    <%}%>
    <%if(thresholds.isEmpty()){%>
        <h1 id ="noThresholds">
            Non hai ancora inserito nessuna prenotazione!
        </h1>
        <br/>
        <input type="button" id="auctionsButton" name="auctionsButton"
               class="button" value="Inizia Subito!" onclick="goToAuctions()"/>
    <%}%>

    <form name="deleteForm" method="post" action="/thresholdManagement/delete">
        <input type="hidden" name="thresholdID"/>
    </form>

    <form name="editForm" method="get" action="/thresholdManagement/modify">
        <input type="hidden" name="thresholdID"/>
    </form>

    <form name="goToAuctionsForm" method="get" action="/auctionManagement/view">
    </form>

</main>
<%@include file="../include/footer.inc"%>
</body>
</html>
