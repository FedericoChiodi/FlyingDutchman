<%
  response.setHeader("Cache-Control", "no-cache");
  String menuActiveLinkReq = (String) request.getAttribute("menuActiveLink");
  Boolean loggedOnHeader = (Boolean) request.getAttribute("loggedOn");

  String menuActiveLink;
  if (menuActiveLinkReq == null || menuActiveLinkReq.isEmpty()) {
    menuActiveLink = "Home";
  } else {
    menuActiveLink = menuActiveLinkReq;
  }
%>

<script>
  function headerOnLoadHandler() {
    const usernameTextField = document.querySelector("#usernameLogin");
    const usernameTextFieldMsg = "Lo username \xE8 obbligatorio.";
    const passwordTextField = document.querySelector("#passwordLogin");
    const passwordTextFieldMsg = "La password \xE8 obbligatoria.";

    if (usernameTextField !== null && passwordTextField !== null ) {
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
    <img id="logoImage" src="/images/logoSimple.png" height="115" width="115" loading="eager" alt="Immagine Logo del Sito">
  </section>

  <form name="logoutForm" action="/logout" method="post"></form>

  <nav>
    <ul>
      <li <%=menuActiveLink.equals("Home")?"class=\"active\"":""%>>
        <a href="/homeManagement/view">Home</a>
      </li>
      <%if (!loggedOnHeader) {%>
      <li <%=menuActiveLink.equals("Registrati")?"class=\"active\"":""%>>
        <a href="/userManagement/insert">Registrati</a>
      </li>
      <%}%>
      <%if (loggedOnHeader){%>
      <li <%=menuActiveLink.equals("Utente")?"class=\"active\"":""%>>
        <a href="/userManagement/view">Utente</a>
      </li>
      <li <%=menuActiveLink.equals("Prodotti")?"class=\"active\"":""%>>
        <a href="/productManagement/view">Prodotti</a>
      </li>
      <li <%=menuActiveLink.equals("Aste")?"class=\"active\"":""%>>
        <a href="/auctionManagement/view">Aste</a>
      </li>
      <li <%=menuActiveLink.equals("Ordini")?"class=\"active\"":""%>>
        <a href="/orderManagement/view">Ordini</a>
      </li>
      <%String role = loggedUser.getRole();%>
      <%if(role.equals("Premium") || role.equals("Admin") || role.equals("SuperAdmin")){%>
      <li <%=menuActiveLink.equals("Prenota")?"class=\"active\"":""%>>
        <a href="/thresholdManagement/view">Prenota</a>
      </li>
      <%}%>
      <%if(role.equals("Admin") || role.equals("SuperAdmin")){%>
      <li <%=menuActiveLink.equals("Banna")?"class=\"active\"":""%>>
        <a href="/userManagement/ban">Banna</a>
      </li>
      <%}%>
      <%if(role.equals("SuperAdmin")){%>
      <li <%=menuActiveLink.equals("Abbassa")?"class=\"active\"":""%>>
        <a href="/auctionManagement/lowerAll">Abbassa</a>
      </li>
      <%}%>
      <li>
        <a href="javascript:logoutForm.submit()">Logout</a>
      </li>
      <%}%>
    </ul>
  </nav>

  <%if (!loggedOnHeader) {%>
    <section id="login" class="clearfix">
      <form name="logonForm" action="/login" method="post">
        <label for="usernameLogin">Utente</label>
        <input type="text" id="usernameLogin" name="username" maxlength="40" required autocomplete="off">

        <label for="passwordLogin">Password</label>
        <input type="password" id="passwordLogin" name="password" maxlength="40" required>

        <input type="submit" value="Ok">
      </form>
    </section>
  <%}%>
</header>