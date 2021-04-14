//////////////////////////////////////////////////////////////////////////////////////
// Лабораторная работа 1 по дисциплине ЛОИС
// Выполнена студентом группы 821703 БГУИР Дмитруком Алексеем Александровичем
// В данном файле реализован пользовательсий интерфейс формулы на СДНФ
// 31.01.2021 v1.0
//
import javax.swing.*;
import java.awt.*;

public class UserInterface extends JFrame {
    private final JButton button = new JButton("Выполнить проверку");
    private final JTextField input = new JTextField(30);
    private final JRadioButton radioButton1 = new JRadioButton("является");
    private final JRadioButton radioButton2 = new JRadioButton("не является");
    private final ButtonGroup buttonGroup = new ButtonGroup();
    private final JLabel label = new JLabel("Введите формулу для проверки на СДНФ: ");
    private final JLabel label2 = new JLabel("Введенная вами формула является СДНФ?");
    private final JPanel container = new JPanel();
    JPanel panelRadio = new JPanel(new GridLayout(0, 1, 0, 5));

    public UserInterface() {
        super("Проверка формулы на СДНФ");
        this.setBounds(600, 200, 380, 200);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);


        input.setFont(new Font("Dialog", Font.PLAIN, 14));

        panelRadio.setBorder(BorderFactory.createTitledBorder("Выберите"));

        radioButton1.setActionCommand("является");
        radioButton2.setActionCommand("не является");
        buttonGroup.add(radioButton1);
        buttonGroup.add(radioButton2);
        panelRadio.add(radioButton1);
        panelRadio.add(radioButton2);

        addButtonListener();

        container.add(label);
        container.add(input);
        container.add(label2);
        container.add(panelRadio);
        container.add(button);


        setContentPane(container);
        setVisible(true);
    }

    private String[] createAnswer() {
        String[] message;
        String checkKnowledge;
        boolean answer = CheckOnSDNF.isSDNF(input.getText());

        String s;
        try {
            s = buttonGroup.getSelection().getActionCommand();
            buttonGroup.clearSelection();
        } catch (Exception e) {
            s = "";
        }

        switch (s) {
            case "является":
                if (answer)
                    checkKnowledge = "- Ваш ответ верный";
                else checkKnowledge = "- Ваш ответ неверный";
                break;

            case "не является":
                if (!answer)
                    checkKnowledge = "- Ваш ответ верный";
                else checkKnowledge = "- Ваш ответ неверный";
                break;

            default:
                checkKnowledge = "- Ответ не был выбран";

        }

        if (answer) {
            message = new String[]{"Результат проверки :",
                    "- Данная формула является СДНФ",
                    checkKnowledge};

        } else {
            message = new String[]{"Результат проверки :",
                    "- Данная формула не является СДНФ",
                    checkKnowledge};
        }

        return message;
    }

    private void addButtonListener() {
        button.addActionListener(actionEvent ->
                JOptionPane.showMessageDialog(UserInterface.this,
                        createAnswer(),
                        null,
                        JOptionPane.INFORMATION_MESSAGE));
    }

    public static void main(String[] args) {
        new UserInterface();
    }
}
