package org.mealsApp;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ModifyPopUpAndConfirmation extends JFrame {

    private final String mealName;
    private final String area;
    private final String category;
    private final String instructions;

    private JFrame jf;

    public ModifyPopUpAndConfirmation(String mealName,
                                      String area,
                                      String category,String instructions){
        this.area=area;
        this.mealName = mealName;
        this.category = category;
        this.instructions =instructions;

        jf = new JFrame("Επεξεργασία "+mealName);
        jf.setVisible(true);
        jf.setSize(1400,900);
        JPanel buttonPanel = new JPanel();
        jf.add(buttonPanel,BorderLayout.PAGE_END);
        JPanel jp =new JPanel(new BorderLayout());
        jp.setBackground(new Color(83, 83, 83));
        jf.add(jp);
        Object data [][] ={{mealName, area, category, instructions}};
        String column [] ={"Name","Area","Category","Instructions"};
        JTable jt = new JTable(data,column);
        jt.setPreferredScrollableViewportSize(jt.getPreferredSize());
        // set the column widths to fixed values
        jt.getColumnModel().getColumn(0).setPreferredWidth(100);
        jt.getColumnModel().getColumn(1).setPreferredWidth(100);
        jt.getColumnModel().getColumn(2).setPreferredWidth(100);
        jt.getColumnModel().getColumn(3).setPreferredWidth(200);

        jt.getColumnModel().getColumn(3).setCellRenderer(new CustomTableCellRenderer());


        JScrollPane scrollPane = new JScrollPane(jt);
        jp.add(scrollPane,BorderLayout.CENTER);
        jt.setCellSelectionEnabled(true);
//        Garides Saganaki

        JButton Save = new JButton("Αποθήκευση");
//        Save.setBounds(100,800,150,40);
        JButton Cancel = new JButton("Κλείσιμο");
//        Cancel.setBounds(700,800,150,40);

        Save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TableColumnModel columnModel = jt.getColumnModel();
                int columnCount = columnModel.getColumnCount();

                String mealName= null;
                String mealArea = null;
                String mealCategory = null;
                String mealInstructions = null;

                for (int i = 0; i < columnCount; i++) {
                    TableColumn col = columnModel.getColumn(i);
                    String columnName = (String) col.getHeaderValue();
                    System.out.println("Column " + i + " name: " + columnName + ", value: " + jt.getValueAt(0,i));
                    if (columnName.equals("Name")){
                        System.out.println(jt.getValueAt(0,i).toString()+"grammh 75 ");
                        mealName = jt.getValueAt(0,i).toString();
                    } else if (columnName.equals("Area")) {
                        mealArea = jt.getValueAt(0,i).toString();
                    }else if (columnName.equals("Category")){
                        mealCategory = jt.getValueAt(0,i).toString();
                    } else if (columnName.equals("Instructions")) {
                        mealInstructions = jt.getValueAt(0,i).toString();
                    }
                }

                new ModifyConfirmation(mealName,mealArea,mealCategory,mealInstructions);
            }
        });

        Cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jf.dispose();
            }
        });


        buttonPanel.add(Save);
        buttonPanel.add(Cancel);

    }
    private static class CustomTableCellRenderer extends DefaultTableCellRenderer {
        private final JTextArea area;

        public CustomTableCellRenderer() {
            area = new JTextArea();
            area.setLineWrap(true);
            area.setWrapStyleWord(true);
            area.setBackground(Color.WHITE);
            area.setBorder(null);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            area.setText(value != null ? value.toString() : "");
            TableColumn tableColumn = table.getColumnModel().getColumn(column);
            area.setSize(new Dimension(tableColumn.getWidth(), Integer.MAX_VALUE));
            int preferredHeight = area.getPreferredSize().height;
            table.setRowHeight(row, preferredHeight);
            return area;
        }
    }










}
