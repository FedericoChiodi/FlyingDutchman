<%@ page session="false"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page import="com.ingsw.flyingdutchman.model.mo.Category" %>
<%@ page import="java.util.List"%>

<%
    @SuppressWarnings("unchecked")
    List<Category> categories = (List<Category>) request.getAttribute("categories");
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
        margin-bottom: 15px;
    }

    /* Stile degli input */
    .field input[type="text"],
    .field input[type="number"]
    {
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
    #preview{
        width: 250px;
        height: 250px;
    }
    #insertForm div{
        margin-bottom: 12px;
    }
</style>
<script>
    function submitProduct(){
        document.insertForm.current_price.value = document.insertForm.starting_price.value;
    }
    function goBack(){
        document.backForm.submit();
    }
    function mainOnLoadHandler(){
        document.insertForm.addEventListener("submit",submitProduct);
        document.insertForm.backButton.addEventListener("click", goBack);
        document.getElementById("min_price").addEventListener("input", validateForm);
        document.getElementById("starting_price").addEventListener("input", validateForm);
    }
    function previewFile() {
        const preview = document.querySelector('#preview');
        const file = document.querySelector('input[type=file]').files[0];
        const reader = new FileReader();

        reader.onloadend = function () {
            preview.src = reader.result;
        }
        if (file) {
            reader.readAsDataURL(file);
        } else {
            preview.src = "";
        }
    }

    function validateForm() {
        const minPriceInput = document.getElementById("min_price");
        const startingPriceInput = document.getElementById("starting_price");
        const minPrice = parseFloat(minPriceInput.value);
        const startingPrice = parseFloat(startingPriceInput.value);

        if (startingPrice < minPrice) {
            startingPriceInput.setCustomValidity("Il Prezzo di Partenza non può essere minore del Prezzo Minimo!");
        } else {
            startingPriceInput.setCustomValidity("");
        }
    }
</script>
<body>
<%@include file="../include/header.jsp"%>
<main>
    <section id="pageTitle">
        <h1>
            Inserisci i dati del Prodotto che vuoi vendere
        </h1>
    </section>

    <section id="insertFormSection">
        <form id="insertForm" name="insertForm" action="/Upload" method="post" enctype="multipart/form-data">
            <div class="field clearfix">
                <label for="description">Descrizione</label>
                <input type="text" id="description" name="description"
                       required size="20" maxlength="200" placeholder="Inserisci una breve descrizione..."/>
            </div>

            <div class="field clearfix">
                <label for="min_price">Prezzo Minimo (&euro;)</label>
                <input type="number" id="min_price" name="min_price"
                       required size="20" min="0" step="0.01"/>
            </div>

            <div class="field clearfix">
                <label for="starting_price">Prezzo di Partenza (&euro;)</label>
                <input type="number" id="starting_price" name="starting_price"
                       required size="20" min="0" step="0.01"/>
            </div>

            <div>
                <label for="categoryID">Categoria: </label>
                <select id="categoryID" name="categoryID">
                    <%for(Category category : categories){%>
                        <option value="<%=category.getCategoryID()%>"><%=category.getName()%></option>
                    <%}%>
                </select>
            </div>

            <div class="field clearfix">
                <label for="image">Immagine del prodotto</label>
                <input type="file" id="image" name="image" onchange="previewFile()" required accept="image/png"/>
                <img id="preview" src="" alt="Immagine del Prodotto">
            </div>

            <div class="field clearfix">
                <label>&#160;</label>
                <input type="submit" class="button" value="Invia"/>
                <input type="button" name="backButton" class="button" value="Annulla"/>
            </div>

            <input type="hidden" id="current_price" name="current_price"/>
            <input type="hidden" id="ownerID" name="ownerID"/>
        </form>
    </section>

    <form name="backForm" method="get" action="/productManagement/view">
    </form>

</main>
<%@include file="../include/footer.inc"%>
</body>
</html>
