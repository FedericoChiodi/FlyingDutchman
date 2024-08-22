# FlyingDutchman
## Aste olandesi online!

FLyingDutchman è un'applicazione web che permette agli utenti di mettere in vendita i propri prodotti attraverso il sistema delle aste olandesi, ovvero al ribasso.

## Specifiche e Features

- Un'accattivante interfaccia grafica colorata e ordinata
- Una pagina home principale a cui possono accedere tutti gli utenti
- Un menù disponibile ovunque nell'applicazione che permette agli utenti di navigare nel sito
- Una pagina dedicata agli Utenti; dove essi possono registrarsi, modificare i propri dati, fare login e logout e cancellare il proprio account
- Una pagina dedicata ai Prodotti caricati nel proprio account. Un utente può caricare un qualsiasi numero di prodotti, che può successivamente decidere di mettere all'asta. E' possibile anche vedere i prodotti caricati precedentemente e venduti.
- Una pagina dedicata alle Aste, dove è possibile visualizzare tutte le aste in corso create da altri utenti. Sono presenti anche funzioni di ricerca per nome di un prodotto o per categoria di prodotti. In questa pagina è anche possibile creare una nuova asta scegliendo uno dei prodotti caricati nel proprio account o visualizzare le aste in corso dell'utente. E' possibile poi eliminare le proprie aste.
- Una pagina Ordini che elenca tutte le aste vinte con le relative informazioni e i relativi costi
- Ogni utente ha la possibilità di diventare un utente ✨Premium✨
- Un utente premium paga una quota di iscrizione al servizio e sblocca la possibilità di inserire delle Prenotazioni sulle aste!
- Gli utenti premium sbloccano nel menù dell'applicazione una pagina Prenotazioni che permette di visualizzare tutte le prenotazioni inserite in precedenza. Per inserire una prenotazione è sufficiente aprire l'asta desiderata, premere sul bottone "Prenota" e inserire un prezzo a piacimento. Quando o se l'asta arriverà al prezzo inserito (o al di sotto) verrà automaticamente creato un ordine per conto dell'utente che ha inserito la prenotazione. Ricorda che le aste olandesi sono al ribasso! Si parte da un prezzo alto e il primo che fa un'offerta vince in automatico!
- Gli utenti amministratori hanno la possibilità di Bannare un qualsiasi altro utente dal sito
- Un utente SuperAmministratore (wow) ha la possibilità di abbassare il prezzo di tutte le aste attualmente in corso di una percentuale casuale compresa tra il 5% e il 20% del prezzo originale! Il gioco si fa interessante ora!

## Tecnologie

FlyingDutchman usa una serie di tecnologie:

- Java
- Spring Framework
- Maven
- Jacoco
- Mockito
- JUnit
- Selenium
- Docker
- Docker Compose
- Github Actions

E ovviamente FlyingDutchman stesso è open source con una [repository](https://github.com/FedericoChiodi/FlyingDutchman) su GitHub

## Installation

Per installare FlyingDutchman è sufficiente clonare la repo

```sh
cd /path/to/your-dir
git clone https://github.com/FedericoChiodi/FlyingDutchman
```

Costruire il progetto con Maven

```sh
maven clean install
```

E farne il deploy su un web server come [Tomcat](https://tomcat.apache.org/).
Il progetto dispone di un database MySQL di test, che viene usato durante i test

### Oppure, usando Docker

```sh
cd /path/to/your-dir
git clone https://github.com/FedericoChiodi/FlyingDutchman
```
```sh
docker compose build
```
```sh
docker compose up -d
```
E il gioco è fatto!

## Credits
Realizzato da [Federico Chiodi](mailto:federico.chiodi@edu.unife.it) per il corso di [Ingegneria del Software avanzata](https://unife.coursecatalogue.cineca.it/insegnamenti/2023/49587/2010/9999/10427?annoOrdinamento=2010&coorte=2023) @ [Unife](https://de.unife.it/it)
