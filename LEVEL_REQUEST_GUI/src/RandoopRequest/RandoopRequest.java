package RandoopRequest;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RandoopRequest extends JFrame {
    private JSpinner parametroSpinner;
    private JTextArea rispostaTextArea;

    public RandoopRequest() {
        // Imposta il titolo della finestra
        setTitle("RICHIESTA LIVELLI RANDOOP");

        // Imposta il layout della finestra
        setLayout(new BorderLayout());

        // Crea il pannello principale
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        // Crea il pannello per il parametro
        JPanel parametroPanel = new JPanel();
        parametroPanel.setLayout(new FlowLayout());
        JLabel parametroLabel = new JLabel("Numero di Livelli:");
        parametroLabel.setSize(getPreferredSize());
        SpinnerNumberModel SNM = new SpinnerNumberModel(1, 1, 5, 1);
        parametroSpinner = new JSpinner(SNM);
        parametroPanel.add(parametroLabel);
        parametroPanel.add(parametroSpinner);

        // Aggiungi il pannello dei parametri al pannello principale
        mainPanel.add(parametroPanel);

        // Crea il pannello per il pulsante
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        JButton inviaButton = new JButton("Invia richiesta");
        buttonPanel.add(inviaButton);

        // Aggiungi il pannello del pulsante al pannello principale
        mainPanel.add(buttonPanel);

        // Aggiungi spazio vuoto tra i pannelli
        mainPanel.add(Box.createVerticalStrut(20));

        // Crea il pannello per la risposta del server
        JPanel rispostaPanel = new JPanel();
        rispostaPanel.setLayout(new BorderLayout());
        JLabel rispostaLabel = new JLabel("Risposta:");
        rispostaTextArea = new JTextArea();
        rispostaTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(rispostaTextArea);
        scrollPane.setPreferredSize(new Dimension(800, 100)); // Modifica le dimensioni del box di testo
        rispostaPanel.add(rispostaLabel, BorderLayout.NORTH);
        rispostaPanel.add(scrollPane, BorderLayout.CENTER);

        // Aggiungi il pannello della risposta al pannello principale
        mainPanel.add(rispostaPanel);

        // Aggiungi il pannello principale alla finestra
        add(mainPanel);

        // Gestore dell'evento clic del pulsante
        inviaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int parametro = (int) parametroSpinner.getValue();

                // Avvia il thread per l'invio della richiesta HTTP
                Thread httpRequestThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        HTTPRequest(parametro);
                    }
                });
                httpRequestThread.start();
            }
        });

        // Imposta le dimensioni della finestra
        setSize(600, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public void HTTPRequest(int levels) {
        try {
            // Indirizzo del server locale e porta
            String urlAddress = "http://localhost:8080/randoop/generate";

            // Crea un oggetto URL dalla stringa dell'indirizzo
            URL url = new URL(urlAddress);

            // Apre una connessione HTTP verso l'URL
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Imposta il metodo della richiesta come POST
            connection.setRequestMethod("POST");

            // Imposta l'intestazione della richiesta per specificare il tipo di contenuto JSON
            connection.setRequestProperty("Content-Type", "application/json");

            // Abilita l'invio e la ricezione di dati
            connection.setDoOutput(true);
            connection.setDoInput(true);

            // Crea il payload JSON
            String jsonPayload = "{\"Livello\": \"" + levels + "\"}";

            // Ottiene l'output stream per scrivere il payload JSON nella richiesta
            DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
            outputStream.writeBytes(jsonPayload);
            outputStream.flush();
            outputStream.close();

            // Ottiene la risposta dal server
            int responseCode = connection.getResponseCode();
            System.out.println("Codice di risposta: " + responseCode);

            // Legge la risposta dal server
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // Stampa la risposta ricevuta
            System.out.println("Risposta: " + response.toString());

            // Aggiorna la textarea della risposta
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    rispostaTextArea.setText(response.toString());
                }
            });

            // Chiude la connessione
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                RandoopRequest gui = new RandoopRequest();
                gui.setVisible(true);
            }
        });
    }
}
