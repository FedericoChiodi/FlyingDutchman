<%@ page session="false"%>
<%@ page import="com.ingsw.flyingdutchman.model.mo.User" %>

<%
    int i = 0;
    boolean loggedOn = (Boolean) request.getAttribute("loggedOn");
    User loggedUser = (User) request.getAttribute("loggedUser");
    String applicationMessage = (String) request.getAttribute("applicationMessage");
    User user = (User) request.getAttribute("user");
    String action = (user!=null) ? "modify" : "insert";
    String menuActiveLink = (loggedUser!=null) ? "Utente" : "Registrati";
%>
<!DOCTYPE html>
    <html>
    <head>
        <%@include file="/include/htmlHead.jsp"%>
        <style>
            main {
                display: flex;
                justify-content: space-between;
                flex-direction: column;
            }
            /* Stile dei pulsanti */
            .button {
                padding: 12px 24px;
                font-size: 16px;
                cursor: pointer;
                transition: background-color 0.3s ease;
                margin-bottom: 12px;
            }

            /* Stile specifico per il pulsante "Registrati" */
            #insertUserButton {
                background-color: #28a745;
            }

            #insertUserButton:hover {
                background-color: #1f9233;
            }

            /* Stile specifico per il pulsante "Modifica i miei dati" */
            #modifyUserButton {
                background-color: #28a745;
            }

            #modifyUserButton:hover {
                background-color: #1f9233;
            }

            /* Stile specifico per il pulsante "Cancella il mio Account" */
            #deleteUserButton {
                background-color: #dc3545;
            }

            #deleteUserButton:hover {
                background-color: #b72230;
            }
        </style>
        <script>
            function insertUser(){
                document.insertForm.submit();
            }
            function mainOnLoadHandler(){
                document.querySelector("#newUserButton").addEventListener("click",insertUser);
            }
            function modifyUser(userID){
                document.modifyForm.userID.value = userID;
                document.modifyForm.submit();
            }
            function deleteUser(userID){
                document.deleteForm.userID.value = userID;
                if(confirm("Attenzione! Questa azione e' irreversibile. Vuoi procedere?")){
                    document.deleteForm.submit();
                }
            }
        </script>
    </head>
    <body>
        <%@include file="/include/header.jsp"%>
    <main>
        <section id="pageTitle">
            <h1>Centro Gestione degli Utenti</h1>
        </section>

        <%if (!loggedOn){%>
            <section id="insertUserButtonSelection">
                <input type="button" id="insertUserButton" name="insertUserButton"
                       class="button" value="Registrati" onclick="insertUser()"/>
            </section>
        <%}%>

        <%if (loggedOn){%>
            <section id="modifyUserButtonSelection">
                <input type="button" id="modifyUserButton" name="modifyUserButton"
                       class="button" value="Modifica i miei dati" onclick="modifyUser(<%=loggedUser.getUserID()%>)"/>
            </section>

            <section id="deleteUserButtonSelection">
                <input type="button" id="deleteUserButton" name="deleteUserButton"
                       class="button" value="Cancella il mio Account" onclick="deleteUser(<%=loggedUser.getUserID()%>)"/>
            </section>
        <%}%>
        <form name="insertForm" method="post" action="Dispatcher">
            <input type="hidden" name="controllerAction" value="UserManagement.insertView"/>
        </form>

        <form name="modifyForm" method="post" action="Dispatcher">
            <input type="hidden" name="userID"/>
            <input type="hidden" name="controllerAction" value="UserManagement.modifyView"/>
        </form>

        <form name="deleteForm" method="post" action="Dispatcher">
            <input type="hidden" name="userID"/>
            <input type="hidden" name="controllerAction" value="UserManagement.delete"/>
        </form>
    </main>
    <%@include file="/include/footer.inc"%>
    </body>
</html>
