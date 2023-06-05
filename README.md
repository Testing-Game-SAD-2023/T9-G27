# Task 9 - Gruppo 27
## Generazione ed esecuzione test Randoop e analisi della copertura con Jacoco

E' stato utilizzato il framework SpringBoot per la creazione di un microservizio offrente le funzionalità di generazione casuale dei test tramite Randoop su una data classe 
e della misura della coverage di tali test attraverso Jacoco. Per gestione delle dipendenze di progetto è stato utilizzato il tool di build automation Maven.

Per utilizzare il servizio è necessario avviare il comando "mvn spring-boot:run" nella cartella "RANDOOP_JACOCO_SERVICE" che metterà in esecuzione il servizio il quale sarà in
ascolto sul porto locale 8080. Alternativamente, attraverso il comando "mvn package", è possibile creare un file .jar per la distribuzione dell'eseguibile del servizio agli utenti. 
Per avviare la richiesta di generazione dei test dev'essere utilizzata un'API RESTFUL per fornire al microservizio i parametri di funzionamento.
La richiesta HTTP verso "http://localhost:8080/randoop/generate" di tipo POST dev'essere in formato json e, in particolare deve contenere i seguenti campi:
    
    "classpath": Path della classe da testare
    "JarPath": Path dei .jar necessari alla generazione di test e copertura
    "classname": Nome della classe da testare
    "outputpath": Path di output (Test e Report)
    "time_limit": Tempo di generazione dei test Randoop
    
La richiesta HTTP è effettuabile attraverso numerosi software, tra cui Postman (vedi "Configurazione Postman.png"). Alternativamente è possibile utilizzare l'interfaccia grafica in HTML fornita dal file
"richiesta.html" all'interno del quale è possibile compilare, in maniera user-friendly, la richiesta di generazione dei test.

Nella cartella di output, sono generati i Regression Test (test che eseguono correttamente) e gli Error Test (test che riportano errori). Nella sottocartella "Report" è generato un file "index.html" contenente i dati sulla copertura del codice. E' possibile visualizzare, oltre alla coverage totale, la copertura dei singoli metodi e delle singole linee di codice.
