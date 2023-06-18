# Task 9 - Gruppo 27
## Randoop Level Generator

E' realizzato un servizio offrente le funzionalità di generazione di test Randoop su una data classe. I test generati sono divisi per livelli, ordinati per copertura crescente.

## Installazione

Prerequisiti:  sulla  macchina  dev’essere  preliminarmente  installato  Maven  e  Docker dev’essere in esecuzione.

Legenda:
1.      nome _immagine = nome da dare all’immagine dock che si sta creando
2.      porto = porto della macchina host su cui mettere in ascolto il servizio
3.      nome_container = nome da dare al container (istanza di un’immagine) che si sta creando
4.      cartella_da_montare: (path) deve contenere:
        I.          La cartella AUTSourceCode con la classe Java da testare
        II.         La cartella RobotTest che contiene la cartella vuota RandoopTest dove saranno generati i 
                    livelli di output (codice del test e report di copertura  dello stesso).
                    Tale cartella può essere situata sulla macchina host eseguente Docker, oppure in un volume di 
                    Docker stesso.

## Avvio installazione: 
Nella cartella RANDOOP_LEVEL_GENERATOR, da terminale, avviare i seguenti
comandi:

1.      mvn package
2.      docker build -t nome_immagine .


## Esecuzione:
ATTENZIONE: I LIVELLI PRECEDENTEMENTE CREATI SARANNO ELIMINATI!

1.      Avviare il comando
        docker run -it -p porto:8080 --name nome_container -v cartella_da_montare:/AUTName nome_immagine

2.      Avviare la generazione dei test utilizzando uno dei due successivi metodi:
        •        Utilizzando  la  GUI  fornita  (LEVEL_REQUEST_GUI.jar)  e  scegliendo  il
                 numero di livelli
        •        Inviando una POST HTTP all’indirizzo “http://localhost:8080/randoop/generate”, sostituendo “porto” a 8080.
                 La richiesta dev’essere in formato json:
                 
                 {
                 "Livello": numero_di_livelli_da_richiedere
                 }
         
3.       Attendere che nella cartella RandoopTest vengano generate le cartelle relative ai livelli generati.       
4.       Ognuna di esse conterrà il codice del test e il rispettivo report di copertura.

 
