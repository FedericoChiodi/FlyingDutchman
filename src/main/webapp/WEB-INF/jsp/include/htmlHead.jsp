<meta charset="utf-8"/>

<!-- Linking styles -->
<link rel="stylesheet" href="${pageContext.request.contextPath}css/flyingdutchman.css" type="text/css" media="screen">
<title>Flying Dutchman: <%=menuActiveLink%></title>
<link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}images/favicon.ico">
<script>
    let applicationMessage;
    <%if (applicationMessage != null) {%>
    applicationMessage="<%=applicationMessage%>";
  <%}%>
  function onLoadHandler() {
    headerOnLoadHandler();
    try { mainOnLoadHandler(); } catch (e) {}
    if (applicationMessage!==undefined) alert(applicationMessage);
  }

  window.addEventListener("load", onLoadHandler);

  function formatFloat(value) {
      const number = parseFloat(value);
      if (!isNaN(number)) {
          return '€' + number.toFixed(2);
      } else {
          return value;
      }
  }

  // Funzione per formattare tutti i numeri float nella pagina
  function formatAllFloats() {
      const floatElements = document.getElementsByClassName('float-value');
      for (let i = 0; i < floatElements.length; i++) {
          const element = floatElements[i];
          const value = element.textContent;
          element.textContent = formatFloat(value);
    }
  }

  // Chiamata alla funzione formatAllFloats al caricamento della pagina
      window.onload = function() {
      formatAllFloats();
  };

</script>