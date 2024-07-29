<%@ page session="false"%>
<%@page import="com.ingsw.flyingdutchman.model.mo.User"%>

<%
  boolean loggedOn = (Boolean) request.getAttribute("loggedOn");
  User loggedUser = (User) request.getAttribute("loggedUser");
%>

<!DOCTYPE html>
<html>
<head>
  <title></title>
  <%@include file="../include/htmlHead.jsp"%>
  <script>
    function buyPremium(){
      document.buyPremiumForm.submit();
    }
  </script>
  <style>
    #welcome > p{
      text-align: justify;
      margin-top: 10px;
      font-size: large;
    }
    #premiumBannerSection{
      display: flex;
      justify-content: center;
      align-items: center;
      margin-bottom: 10px;
    }
  </style>
</head>
<body>
  <%@include file="../include/header.jsp"%>
  <main>

    <section id="premiumBannerSection">
      <%if(loggedOn){%>
        <%if (loggedUser.getRole().equals("Default")){%>
        <a href="javascript:buyPremium()">
          <img id="premiumBanner" src="${pageContext.request.contextPath}images/premiumBanner.png" alt="Premium Banner Image" height="175px" width="900px">
        </a>
        <%}%>
      <%}%>
    </section>

    <article id="welcome" name="welcome">
      <h1 id="welcomeMessage" name="welcomeMessage">
        <%if(loggedOn){%>
          Benvenuto <i><%=loggedUser.getUsername()%></i>, su Flying Dutchman!
        <%}%>
        <%if(!loggedOn){%>
          Benvenuto su Flying Dutchman!
        <%}%>
      </h1>
      <p>
        Flying Dutchman &egrave; una piattaforma di aste olandesi online.
        Un'asta olandese &egrave; un tipo di asta dove il prezzo iniziale &egrave; solitamente molto alto e
        con il passare del tempo esso va diminuendo sempre di pi&uacute;. Il primo partecipante all'asta che
        desidera comprare l'oggetto deve essere scaltro nell'offrire la somma attuale richiesta dall'asta prima
        dei suoi rivali. L'asta si chiude quindi dopo una sola singola offerta!
        Diventa quindi necessario offrire prima dei rivali se si vuole comprare un oggetto.
      </p>
      <p>
        Flying Dutchman permette d'inserire dei prodotti nel tuo account e successivamente di metterli all'asta.
        Sarai tu a decidere il prezzo iniziale del prodotto e il prezzo minimo che sei disposto ad accettare!
        Una volta aperta un'asta potrai modificare liberamente il prezzo al ribasso quando vorrai per incentivare
        la vendita del tuo prodotto.
        Attenzione! Ogni giorno il prezzo di tutti i prodotti all'asta verr&aacute; automaticamente abbassato di un
        valore compreso tra il 5% e il 20% del prezzo attuale dell'articolo!
        Non temere per&oacute;! Il prezzo dei tuoi articoli non andr&aacute; mai al di sotto del tuo prezzo minimo!
      </p>
      <p>
        Potrai inoltre osservare le aste aperte e decidere di fare un'offerta!
        Una volta confermato il pagamento l'articolo sar&agrave; tuo. Non resta che attendere
        una email di conferma da parte nostra che ti informer&agrave; dei dettagli di spedizione e tracciamento.
      </p>
      <p>
        Hai notato un articolo che desideri fortemente ma il prezzo &egrave; ancora troppo alto?
        Non temere! Il nostro programma Premium ti consente di avere accesso alla funzionalit&agrave;
        "Prenota", che ti permette di stabilire una soglia di prezzo.
        Se il prezzo dell'articolo cala fino alla tua prenotazione (o al di sotto) l'articolo
        verr&agrave; automaticamente acquistato per te! Ti accorgerai degli ordini effettuati in questo
        modo da una descrizione aggiuntiva nella tua schermata "Ordini".
      </p>
      <p>
        Buon divertimento su Flying Dutchman!
      </p>
    </article>

    <form name="buyPremiumForm" method="post" action="${pageContext.request.contextPath}/orderManagement/premium"></form>
  </main>
  <%@include file="../include/footer.inc"%>
</body>
</html>
