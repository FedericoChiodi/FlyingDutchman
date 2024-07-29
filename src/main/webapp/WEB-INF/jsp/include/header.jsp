<%response.setHeader("Cache-Control", "no-cache");%>

<%
  String menuActiveLink;
  String menuActiveLinkReq = (String) request.getAttribute("menuActiveLink");
  if (menuActiveLinkReq == null || menuActiveLinkReq.isEmpty()) {
    menuActiveLink = "Home";
  }
  else{
    menuActiveLink = menuActiveLinkReq;
  }
%>

<script>
  function headerOnLoadHandler() {
    const usernameTextField = document.querySelector("#usernameLogin");
    const usernameTextFieldMsg = "Lo username \xE8 obbligatorio.";
    const passwordTextField = document.querySelector("#passwordLogin");
    const passwordTextFieldMsg = "La password \xE8 obbligatoria.";

    if (usernameTextField !== undefined && passwordTextField !== undefined ) {
      usernameTextField.setCustomValidity(usernameTextFieldMsg);
      usernameTextField.addEventListener("change", function () {
        this.setCustomValidity(this.validity.valueMissing ? usernameTextFieldMsg : "");
      });
      passwordTextField.setCustomValidity(passwordTextFieldMsg);
      passwordTextField.addEventListener("change", function () {
       this.setCustomValidity(this.validity.valueMissing ? passwordTextFieldMsg : "");
      });
    }
  }
</script>
<style>
  #logoContainer{
    display: flex;
    flex-direction: row;
    align-items: start;
  }
  #logoImage{
    margin-left: 25px;
    filter: drop-shadow(10px 14px 10px #58e34a);
  }
</style>

<header class="clearfix">

  <section id="logoContainer">
    <h1 class="logo">Flying Dutchman</h1>
    <img id="logoImage" src="${pageContext.request.contextPath}images/logoSimple.png" height="115" width="115" loading="eager" alt="Immagine Logo del Sito">
  </section>

  <form name="logoutForm" action="${pageContext.request.contextPath}/logout" method="post"></form>

  <nav>
    <ul>
      <li <%=menuActiveLink.equals("Home")?"class=\"active\"":""%>>
        <a href="${pageContext.request.contextPath}/">Home</a>
      </li>
      <%if (!loggedOn) {%>
      <li <%=menuActiveLink.equals("Registrati")?"class=\"active\"":""%>>
        <a href="${pageContext.request.contextPath}/userManagement/register">Registrati</a>
      </li>
      <%}%>
      <%if (loggedOn){%>
      <li <%=menuActiveLink.equals("Utente")?"class=\"active\"":""%>>
        <a href="${pageContext.request.contextPath}/userManagement">Utente</a>
      </li>
      <li <%=menuActiveLink.equals("Prodotti")?"class=\"active\"":""%>>
        <a href="${pageContext.request.contextPath}/productManagement">Prodotti</a>
      </li>
      <li <%=menuActiveLink.equals("Aste")?"class=\"active\"":""%>>
        <a href="${pageContext.request.contextPath}/auctionManagement">Aste</a>
      </li>
      <li <%=menuActiveLink.equals("Ordini")?"class=\"active\"":""%>>
        <a href="${pageContext.request.contextPath}/orderManagement">Ordini</a>
      </li>
      <%String role = loggedUser.getRole();%>
      <%if(role.equals("Premium") || role.equals("Admin") || role.equals("SuperAdmin")){%>
      <li <%=menuActiveLink.equals("Prenota")?"class=\"active\"":""%>>
        <a href="${pageContext.request.contextPath}/thresholdManagement">Prenota</a>
      </li>
      <%}%>
      <%if(role.equals("Admin") || role.equals("SuperAdmin")){%>
      <li <%=menuActiveLink.equals("Banna")?"class=\"active\"":""%>>
        <a href="${pageContext.request.contextPath}/userManagement/ban">Banna</a>
      </li>
      <%}%>
      <%if(role.equals("SuperAdmin")){%>
      <li <%=menuActiveLink.equals("Abbassa")?"class=\"active\"":""%>>
        <a href="${pageContext.request.contextPath}/auctionManagement/lowerAll">Abbassa</a>
      </li>
      <%}%>
      <li>
        <a href="javascript:logoutForm.submit()">Logout</a>
      </li>
      <%}%>
    </ul>
  </nav>

  <%if (!loggedOn) {%>
    <section id="login" class="clearfix">
      <form name="logonForm" action="${pageContext.request.contextPath}/login" method="post">
        <label for="usernameLogin">Utente</label>
        <input type="text" id="usernameLogin" name="username" maxlength="40" required autocomplete="off">

        <label for="passwordLogin">Password</label>
        <input type="password" id="passwordLogin" name="password" maxlength="40" required>

        <input type="submit" value="Ok">
      </form>
    </section>
  <%}%>
</header>