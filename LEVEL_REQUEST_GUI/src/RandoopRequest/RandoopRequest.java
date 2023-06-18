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
        setTitle("RICHIESTA LIVELLI RANDOOP");
        setLayout(new BorderLayout());
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        JPanel parametroPanel = new JPanel();
        parametroPanel.setLayout(new FlowLayout());
        JLabel parametroLabel = new JLabel("Numero di Livelli:");
        parametroLabel.setSize(getPreferredSize());
        SpinnerNumberModel SNM = new SpinnerNumberModel(1, 1, 5, 1);
        parametroSpinner = new JSpinner(SNM);
        parametroPanel.add(parametroLabel);
        parametroPanel.add(parametroSpinner);
        mainPanel.add(parametroPanel);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        JButton inviaButton = new JButton("Invia richiesta");
        buttonPanel.add(inviaButton);
        mainPanel.add(buttonPanel);
        mainPanel.add(Box.createVerticalStrut(20));
        JPanel rispostaPanel = new JPanel();
        rispostaPanel.setLayout(new BorderLayout());
        JLabel rispostaLabel = new JLabel("Risposta:");
        rispostaTextArea = new JTextArea();
        rispostaTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(rispostaTextArea);
        scrollPane.setPreferredSize(new Dimension(800, 100)); // Modifica le dimensioni del box di testo
        rispostaPanel.add(rispostaLabel, BorderLayout.NORTH);
        rispostaPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(rispostaPanel);
        add(mainPanel);
        inviaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int parametro = (int) parametroSpinner.getValue();
                Thread httpRequestThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        HTTPRequest(parametro);
                    }
                });
                httpRequestThread.start();
            }
        });
        setSize(600, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public void HTTPRequest(int levels) {
        try {
            String urlAddress = "http://localhost:8080/randoop/generate";
            URL url = new URL(urlAddress);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            String jsonPayload = "{\"Livello\": \"" + levels + "\"}";
            DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
            outputStream.writeBytes(jsonPayload);
            outputStream.flush();
            outputStream.close();
            int responseCode = connection.getResponseCode();
            System.out.println("Codice di risposta: " + responseCode);
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            System.out.println("Risposta: " + response.toString());
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    rispostaTextArea.setText(response.toString());
                }
            });
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
