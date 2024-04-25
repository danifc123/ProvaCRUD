import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class tCad {

    private JPanel Princ;
    private JPanel Cabecalho;
    private JPanel ImportBD;
    private JPanel Listagem;
    private JTextField txtCaminho;
    private JButton btnImport;
    private JTable table1;
    private JScrollPane Scroll;
    private JPanel Cadastro;
    private JPanel opcoes;
    private JButton cadastrarButton;
    private JTextField txtID;
    private JTextField txtNome;
    private JTextField txtIdade;
    private JTextField txtEndereco;
    private JButton salvarButton;
    private JButton Alterar;
    private JButton Excluir;
    private JButton Listar;

    public tCad() {
        btnImport.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    load_table(txtCaminho.getText());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                txtCaminho.setEnabled(false);
                btnImport.setEnabled(false);
            }
        });
        salvarButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String ss = txtID.getText() + "," + txtNome.getText() + "," + txtIdade.getText() + "," + txtEndereco.getText();
                try {
                    ESCREVERARQUIVO(txtCaminho.getText(),ss);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                try {
                    load_table(txtCaminho.getText());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                txtID.setText("");
                txtNome.setText("");
                txtIdade.setText("");
                txtEndereco.setText("");
                Cadastro.setVisible(false);
                Listagem.setVisible(true);
            }
        });
        cadastrarButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Cadastro.setVisible(true);
                Listagem.setVisible(false);
            }
        });
        Listar.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    load_table(txtCaminho.getText());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                Cadastro.setVisible(false);
                Listagem.setVisible(true);
            }
        });

        Alterar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = table1.getSelectedRow();
                if (row == -1) {
                    JOptionPane.showMessageDialog(null, "Selecione um registro para alterar.");
                    return;
                }

                String id = (String) table1.getValueAt(row, 0);
                String nome = (String) table1.getValueAt(row, 1);
                String idade = (String) table1.getValueAt(row, 2);
                String endereco = (String) table1.getValueAt(row, 3);

                txtID.setText(id);
                txtNome.setText(nome);
                txtIdade.setText(idade);
                txtEndereco.setText(endereco);

                Cadastro.setVisible(true);
                Listagem.setVisible(false);

                int confirmacao = JOptionPane.showConfirmDialog(null, "Deseja salvar as alterações?", "Salvar Alterações", JOptionPane.YES_NO_OPTION);
                if (confirmacao == JOptionPane.YES_OPTION) {
                    DefaultTableModel model = (DefaultTableModel) table1.getModel();
                    model.removeRow(row);
                    try {
                        salvarTabelaEmArquivo(txtCaminho.getText(), model);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });

        Excluir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = table1.getSelectedRow();
                if (row == -1) {
                    JOptionPane.showMessageDialog(null, "Selecione um registro para excluir.");
                    return;
                }

                int confirmacao = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja excluir este registro?", "Confirmação de exclusão", JOptionPane.YES_NO_OPTION);
                if (confirmacao == JOptionPane.YES_OPTION) {
                    DefaultTableModel model = (DefaultTableModel) table1.getModel();
                    model.removeRow(row);
                    try {
                        salvarTabelaEmArquivo(txtCaminho.getText(), model);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });
    }

    public static List<String[]> LERARQUIVO(String ARQUIVO) throws IOException, FileNotFoundException {
        List<String[]> Cliente = new ArrayList<>();
        BufferedReader BR = new BufferedReader(new FileReader(ARQUIVO));

        String linha;
        while ((linha = BR.readLine()) != null) {
            String[] dados = linha.split(",");
            Cliente.add(dados);
        }

        BR.close();
        return Cliente;
    }

    public static void ESCREVERARQUIVO(String ARQUIVO, String TEXTO) throws IOException {
        OutputStream OS = new FileOutputStream(ARQUIVO, true);
        OutputStreamWriter OSW = new OutputStreamWriter(OS);
        BufferedWriter BW = new BufferedWriter(OSW);
        BW.write(TEXTO);
        BW.newLine();
        BW.close();
    }

    public static void salvarTabelaEmArquivo(String ARQUIVO, DefaultTableModel model) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(ARQUIVO));
        for (int i = 0; i < model.getRowCount(); i++) {
            for (int j = 0; j < model.getColumnCount(); j++) {
                bw.write(model.getValueAt(i, j).toString());
                if (j < model.getColumnCount() - 1) {
                    bw.write(",");
                }
            }
            bw.newLine();
        }
        bw.close();
    }

    public static void main(String[] args) {

        JFrame frame = new JFrame("tCad");
        frame.setContentPane(new tCad().Princ);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public void load_table(String Arquivo) throws IOException {
        List<String[]> clientes =  LERARQUIVO(Arquivo);

        String[] columnNames = {"ID", "Nome", "Idade", "Tipo Endereço"};

        DefaultTableModel model =  new DefaultTableModel(columnNames, 0);
        table1.setModel(model);

        for (String[] dados : clientes) {
            model.addRow(dados);
        }
    }

    public void limparTabela() {
        DefaultTableModel model = (DefaultTableModel) table1.getModel();
        model.setRowCount(0);
    }
}
