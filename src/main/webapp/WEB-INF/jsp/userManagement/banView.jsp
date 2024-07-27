<%@ page session="false"%>
<%@ page import="com.ingsw.flyingdutchman.model.mo.User" %>

<%
  int i = 0;
  boolean loggedOn = (Boolean) request.getAttribute("loggedOn");
  User loggedUser = (User) request.getAttribute("loggedUser");
  String applicationMessage = (String) request.getAttribute("applicationMessage");
  String menuActiveLink = "Banna";
  User[] usernames = (User[]) request.getAttribute("usernames");
%>
<!DOCTYPE html>
<html>
<head>
  <%@include file="../include/htmlHead.jsp"%>
  <style>
    main {
      display: flex;
      justify-content: space-between;
      flex-direction: column;
    }
    #banFormSection{
      font-size: large;
    }
    .button {
      font-size: 24px;
      cursor: pointer;
      background-color: #dc3545;
      transition: background-color 0.3s ease;
      margin-top: 12px;
      padding: 7px 7px;
    }
    #Break hr{
      width: auto;
      color: #39ce29;
      height: auto;
      margin-bottom: 10px;
    }
  </style>
  <script>
    function deleteUser(){
      document.banForm.controllerAction.value = "UserManagement.ban";
      document.banForm.submit();
    }
    function mainOnLoadHandler(){
      document.banForm.addEventListener("submit",deleteUser);
    }
  </script>
</head>
<body>
  <%@include file="../include/header.jsp"%>
  <main>
    <section id="pageTitle">
      <h1>Banna un Utente</h1>
    </section>

    <section id="Break">
      <hr/>
    </section>

    <section id="banFormSection">
      <form name="banForm" action="Dispatcher" method="post" autocomplete="off">
        <div class="field clearfix">
          <label for="username">Username dell'utente da bannare: </label>
          <input list="usernames" name="username" id="username">
          <datalist id="usernames">
            <%for(i = 0; i < usernames.length ; i++){%>
              <option value="<%=usernames[i].getUsername()%>"><%=usernames[i].getUsername()%></option>
            <%}%>
          </datalist>
        </div>
        <div class="field clearfix">
          <input type="submit" class="button" value="Banna"/>
        </div>
        <input type="hidden" name="controllerAction"/>
      </form>
    </section>
  </main>
  <%@include file="../include/footer.inc"%>
</body>
</html>
