<%@ page session="false"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page import="com.ingsw.flyingdutchman.model.mo.User" %>

<%
  User loggedUser = (User) request.getAttribute("loggedUser");
  String auctionID = (String) request.getAttribute("auctionID");
  String action = (loggedUser !=null) ? "modify" : "insert";
%>
<!DOCTYPE html>
<html>
  <head>
      <%@include file="../include/htmlHead.jsp"%>
    <title></title>
  </head>
  <style>
    /* Allinea gli elementi del form in colonne */
    .field {
      display: flex;
      flex-direction: column;
      margin-bottom: 10px;
    }

    /* Aggiusta lo stile delle etichette dei campi */
    .field label {
      font-weight: bold;
    }

    /* Stile degli input */
    .field input[type="text"],
    .field input[type="password"],
    .field input[type="date"],
    .field input[type="email"],
    .field input[type="tel"] {
      padding: 5px;
      border: 1px solid #ccc;
      border-radius: 4px;
      font-size: 14px;
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

    /* Aggiusta il margine superiore del titolo della sezione */
    #pageTitle h1 {
      margin-top: 0;
      font-size: 24px;
    }
  </style>
  <body>
    <%@include file="../include/header.jsp"%>
    <main>
    <section id="pageTitle">
      <h1>
        <%=(action.equals("modify")) ? "Modifica i tuoi dati" : "Inserisci i tuoi dati"%>
      </h1>
    </section>
    
    <section id="insModFormSection">
      <form name="insModForm" action="<%=action.equals("insert") ? "/userManagement/register" : "/userManagement/modify"%>" method="post">
        
        <div class="field clearfix">
          <label for="username">Username</label>
          <input type="text" id="username" name="username"
                 value="<%=(action.equals("modify")) ? loggedUser.getUsername() : ""%>"
                 required size="20" maxlength="40"/>
        </div>
        <div class="field clearfix">
          <label for="password">Password</label>
          <input type="password" id="password" name="password"
                 value="<%=(action.equals("modify")) ? loggedUser.getPassword() : ""%>"
                 required size="20" maxlength="40"/>
        </div>
        <div class="field clearfix">
          <label for="firstname">Nome</label>
          <input type="text" id="firstname" name="firstname"
            value="<%=(action.equals("modify")) ? loggedUser.getFirstname() : ""%>"
            required size="20" maxlength="40"/>
        </div>
        <div class="field clearfix">
          <label for="surname">Cognome</label>
          <input type="text" id="surname" name="surname"
                 value="<%=(action.equals("modify")) ? loggedUser.getSurname() : ""%>"
                 required size="20" maxlength="40"/>
        </div>
        <div class="field clearfix">
          <label for="birthdate">Data di Nascita</label>
          <input type="date" id="birthdate" name="birthdate"
                 value="<%=(action.equals("modify")) ? loggedUser.getBirthdate() : ""%>"
                 required size="20" maxlength="40"/>
        </div>
        <div class="field clearfix">
          <label for="address">Indirizzo</label>
          <input type="text" id="address" name="address"
                 value="<%=(action.equals("modify")) ? loggedUser.getAddress() : ""%>"
                 required size="20" maxlength="40"/>
        </div>
        <div class="field clearfix">
          <label for="civic_number">Numero Civico</label>
          <input type="number" id="civic_number" name="civic_number"
                 value="<%=(action.equals("modify")) ? loggedUser.getCivic_number() : ""%>"
                 required size="20" maxlength="40" min="0" step="1"/>
        </div>
        <div class="field clearfix">
          <label for="cap">CAP</label>
          <input type="number" id="cap" name="cap"
                 value="<%=(action.equals("modify")) ? loggedUser.getCap() : ""%>"
                 required size="20" maxlength="40" min="0" max="98169" step="1"/>
        </div>
        <div class="field clearfix">
          <label for="city">Città</label>
          <input type="text" id="city" name="city"
                 value="<%=(action.equals("modify")) ? loggedUser.getCity() : ""%>"
                 required size="20" maxlength="40"/>
        </div>
        <div class="field clearfix">
          <label for="state">Stato</label>
          <input type="text" id="state" name="state"
                 value="<%=(action.equals("modify")) ? loggedUser.getState() : ""%>"
                 required size="20" maxlength="40"/>
        </div>
        <div class="field clearfix">
          <label for="email">Email</label>
          <input type="email" id="email" name="email"
                 value="<%=(action.equals("modify")) ? loggedUser.getEmail() : ""%>"
                 required size="20" maxlength="40"/>
        </div>
        <div class="field clearfix">
          <label for="cel_number">Numero di Telefono</label>
          <input type="tel" id="cel_number" name="cel_number"
                 value="<%=(action.equals("modify")) ? loggedUser.getCel_number() : ""%>"
                 required size="20" maxlength="40"/>
        </div>

        <input type="hidden" id="role" name="role" value="<%=(action.equals("modify")) ? loggedUser.getRole() : "Default"%>"/>
        <input type="hidden" id="deleted" name="deleted" value="<%=(action.equals("modify")) ? loggedUser.isDeleted() : "N"%>"/>

        <div class="field clearfix">
          <label>&#160;</label>
          <input type="submit" class="button" value="Invia"/>

          <form name="backForm" method="get" action="/userManagement/view">
            <input type="hidden" name="auctionID" value="<%=auctionID%>">
            <input type="submit" name="backButton" class="button" value="Annulla"/>
          </form>
        </div>

        <input type="hidden" name="auctionID" value="-1">
      </form>
    </section>
    
  </main>
  <%@include file="../include/footer.inc"%>
  </body>
</html>