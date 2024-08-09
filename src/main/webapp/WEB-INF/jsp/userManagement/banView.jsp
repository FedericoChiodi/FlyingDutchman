<%@ page session="false"%>
<%@ page import="com.ingsw.flyingdutchman.model.mo.User" %>
<%@ page import="java.util.List"%>

<%
  @SuppressWarnings("unchecked")
  List<User> usernames = (List<User>) request.getAttribute("usernames");
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
  <title></title>
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
      <form name="banForm" action="/userManagement/ban" method="post" autocomplete="off">
        <div class="field clearfix">
          <label for="username">Username dell'utente da bannare: </label>
          <input list="usernames" name="username" id="username">
          <datalist id="usernames">
            <%for(User user : usernames){%>
              <option value="<%=user.getUsername()%>"><%=user.getUsername()%></option>
            <%}%>
          </datalist>
        </div>
        <div class="field clearfix">
          <input type="submit" class="button" value="Banna"/>
        </div>
      </form>
    </section>
  </main>
  <%@include file="../include/footer.inc"%>
</body>
</html>
