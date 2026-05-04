import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

public class TrenoNaLadeira extends JFrame {

    // Campos onde o usuário digita os valores
    private JTextField txtMassa;
    private JTextField txtInclinacao;
    private JTextField txtAtritoEstatico;
    private JTextField txtAtritoCinetico;

    // Resultados
    private JLabel lblResA;
    private JLabel lblResB;
    private JLabel lblResC;

    public Main() {
        setTitle("Trenó na Ladeira - Calculadora Física");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Divide a tela em duas partes (entrada e resultado)
        setLayout(new GridLayout(1, 2, 10, 10));

        // LADO ESQUERDO (ENTRADAS)
        JPanel panelEsquerdo = new JPanel(new BorderLayout(10, 10)); 
        panelEsquerdo.setBorder(BorderFactory.createTitledBorder("ENTRADAS"));
        panelEsquerdo.setBackground(new Color(240, 245, 250));

        // GridBagLayout pra alinhar os campos lado a lado
        JPanel panelInputs = new JPanel(new GridBagLayout());
        panelInputs.setBackground(new Color(240, 245, 250));
        panelInputs.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);      
        gbc.weightx = 1.0;                        
        gbc.weighty = 1.0;                        

        // Campo peso/massa
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.6;
        panelInputs.add(new JLabel("Massa do objeto (P) em N:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 0.4;
        txtMassa = new JTextField();
        panelInputs.add(txtMassa, gbc);

        // Campo inclinação
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.6;
        panelInputs.add(new JLabel("Inclinação (\u03B8) em graus:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 0.4;
        txtInclinacao = new JTextField();
        panelInputs.add(txtInclinacao, gbc);

        // Atrito estático
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.6;
        panelInputs.add(new JLabel("Coef. de atrito estático (\u03BCE):"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 0.4;
        txtAtritoEstatico = new JTextField();
        panelInputs.add(txtAtritoEstatico, gbc);

        // Atrito cinético
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0.6;
        panelInputs.add(new JLabel("Coef. de atrito cinético (\u03BCC):"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; gbc.weightx = 0.4;
        txtAtritoCinetico = new JTextField();
        panelInputs.add(txtAtritoCinetico, gbc);

        // Botões de ação
        JPanel panelBotoes = new JPanel(new FlowLayout());
        panelBotoes.setBackground(new Color(240, 245, 250));
        
        JButton btnCalcular = new JButton("CALCULAR");
        btnCalcular.setForeground(Color.BLACK);
        
        JButton btnLimpar = new JButton("LIMPAR CAMPOS");

        panelBotoes.add(btnCalcular);
        panelBotoes.add(btnLimpar);

        // Texto com as regras de entrada
        JTextArea txtCondicoes = new JTextArea(
                "CONDIÇÕES DE ENTRADA:\n" +
                "• 1 <= Peso/Massa <= 10000\n" +
                "• 10° <= \u03B8 <= 60°\n" +
                "• 0.1 <= \u03BC <= 1\n" +
                "• Entradas devem ser apenas números."
        );
        txtCondicoes.setEditable(false);
        txtCondicoes.setBackground(new Color(230, 230, 230));
        txtCondicoes.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.setBackground(new Color(240, 245, 250));
        panelInferior.add(panelBotoes, BorderLayout.NORTH);
        panelInferior.add(txtCondicoes, BorderLayout.SOUTH);

        panelEsquerdo.add(panelInputs, BorderLayout.CENTER); 
        panelEsquerdo.add(panelInferior, BorderLayout.SOUTH); 

        // LADO DIREITO (RESULTADOS)
        JPanel panelDireito = new JPanel();
        panelDireito.setLayout(new GridLayout(3, 1, 10, 10));
        panelDireito.setBorder(BorderFactory.createTitledBorder("RESULTADOS"));
        panelDireito.setBackground(Color.WHITE);

        lblResA = criarLabelResultado("(a) Força mínima para impedir o trenó de deslizar:");
        lblResB = criarLabelResultado("(b) Força mínima para o trenó começar a subir:");
        lblResC = criarLabelResultado("(c) Força para o trenó subir com velocidade constante:");

        panelDireito.add(lblResA);
        panelDireito.add(lblResB);
        panelDireito.add(lblResC);

        add(panelEsquerdo);
        add(panelDireito);

        // Conecta os botões às suas ações
        btnCalcular.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                realizarCalculos();
            }
        });

        btnLimpar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limparCampos();
            }
        });
    }

    // Monta o visual padrão de cada label de resultado
    private JLabel criarLabelResultado(String texto) {
        JLabel label = new JLabel("<html><b>" + texto + "</b><br><span style='font-size:16px; color:blue;'>-- N</span></html>");
        label.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        return label;
    }

    // Lê os campos, valida e calcula as três forças
    private void realizarCalculos() {

        // Verifica se algum campo está vazio
        if (txtMassa.getText().trim().isEmpty() || 
            txtInclinacao.getText().trim().isEmpty() || 
            txtAtritoEstatico.getText().trim().isEmpty() || 
            txtAtritoCinetico.getText().trim().isEmpty()) {
            
            mostrarErro("Algum campo ficou vazio. Preencha todos antes de calcular.");
            return;
        }

        try {
            // Lê os valores digitados.
            double p = Double.parseDouble(txtMassa.getText().replace(",", "."));
            double theta = Double.parseDouble(txtInclinacao.getText().replace(",", "."));
            double muE = Double.parseDouble(txtAtritoEstatico.getText().replace(",", "."));
            double muC = Double.parseDouble(txtAtritoCinetico.getText().replace(",", "."));

            // Checa se os valores estão dentro dos limites permitidos
            if (p < 1 || p > 10000) {
                mostrarErro("O peso precisa estar entre 1 e 10.000 N.");
                return;
            }
            if (theta < 10 || theta > 60) {
                mostrarErro("A inclinação precisa estar entre 10° e 60°.");
                return;
            }
            if (muE < 0.1 || muE > 1 || muC < 0.1 || muC > 1) {
                mostrarErro("Os coeficientes de atrito precisam estar entre 0,1 e 1.");
                return;
            }

            // decompõe o peso e calcula as forças de atrito
            double thetaRad = Math.toRadians(theta);
            double sinTheta = Math.sin(thetaRad);
            double cosTheta = Math.cos(thetaRad);

            double px = p * sinTheta;
            double fatEstaticoMax = muE * p * cosTheta;
            double fatCinetico = muC * p * cosTheta;

            // Calcula as três forças pedidas
            double fA = 0;
            if (px > fatEstaticoMax) {
                fA = px - fatEstaticoMax;
            }

            double fB = px + fatEstaticoMax;
            double fC = px + fatCinetico;

            // Exibe os resultados formatados
            DecimalFormat df = new DecimalFormat("#,##0.0");

            lblResA.setText("<html><b>(a) Força mínima para impedir o trenó de deslizar:</b><br><span style='font-size:16px; color:blue;'>" + df.format(fA) + " N</span></html>");
            lblResB.setText("<html><b>(b) Força mínima para o trenó começar a subir:</b><br><span style='font-size:16px; color:blue;'>" + df.format(fB) + " N</span></html>");
            lblResC.setText("<html><b>(c) Força para o trenó subir com velocidade constante:</b><br><span style='font-size:16px; color:blue;'>" + df.format(fC) + " N</span></html>");

        } catch (NumberFormatException ex) {
            mostrarErro("Só são aceitos números nos campos. Verifique se digitou letras ou símbolos.");
        }
    }

    // apaga os campos e reseta os resultados
    private void limparCampos() {
        txtMassa.setText("");
        txtInclinacao.setText("");
        txtAtritoEstatico.setText("");
        txtAtritoCinetico.setText("");
        
        lblResA.setText("<html><b>(a) Força mínima para impedir o trenó de deslizar:</b><br><span style='font-size:16px; color:blue;'>-- N</span></html>");
        lblResB.setText("<html><b>(b) Força mínima para o trenó começar a subir:</b><br><span style='font-size:16px; color:blue;'>-- N</span></html>");
        lblResC.setText("<html><b>(c) Força para o trenó subir com velocidade constante:</b><br><span style='font-size:16px; color:blue;'>-- N</span></html>");
    }

    // Exibe uma janela de aviso com a mensagem de erro
    private void mostrarErro(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem, "Aviso importante", JOptionPane.WARNING_MESSAGE);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            new Main().setVisible(true);
        });
    }
}
