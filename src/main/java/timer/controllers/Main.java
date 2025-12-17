package timer.controllers;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

//=====================================Таймер отключения компьютера==================================================
public class Main {

    @FXML
    Button startButton;
    @FXML
    Button pauseButton;
    @FXML
    Spinner<Integer> hourSpinner;
    @FXML
    Spinner<Integer> minutesSpinner;
    @FXML
    Label label1;
    @FXML
    Label label2;
    @FXML
    Label label3;
    @FXML
    Label secondsLabel;
    @FXML
    CheckBox timeCheckBox;
    @FXML
    Button timersButton;

    int hours;
    int minutes;
    long currentTime;// Разница времени (остаток времени)
    long givenDate;// Заданная дата в миллисекундах

    SpinnerValueFactory<Integer> hourValueFactory =
            new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 24, 0, 1);
    SpinnerValueFactory<Integer> minutesValueFactory =
            new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0, 1);

    public void initialize() {// Инициализация Spinner!!!
        hourSpinner.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_VERTICAL);// Изменение стиля
        hourSpinner.setValueFactory(hourValueFactory);
        minutesSpinner.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_VERTICAL);// Изменение стиля
        minutesSpinner.setValueFactory(minutesValueFactory);
        hourSpinner.getEditor().setOnAction(startButton.getOnAction());
        minutesSpinner.getEditor().setOnAction(startButton.getOnAction());
    }

    @FXML
    public void onActionStartButton() {// Кнопка СТАРТА

        if (pauseButton.getText().equals("Continue")) {// Изменение надписи кнопки
            pauseButton.setText("Pause");
        }
        label1.setText("");
        label2.setText("");

        if (hourSpinner.getEditor().getText().equals("")) {// С пустым значением не корректно работает после паузы
            hourSpinner.getEditor().setText("0");
        }

        if (minutesSpinner.getEditor().getText().isEmpty()) {// С пустым значением не корректно работает после паузы
            minutesSpinner.getEditor().setText("0");
        }

        try {
            hours = Integer.parseInt(hourSpinner.getEditor().getText());// Инициализация часов
            minutes = Integer.parseInt(minutesSpinner.getEditor().getText());// Инициализация минут
        } catch (Exception e) {
            label3.setText("Задайте время !!!");
        }

        if (hours < 0 || hours > 24 || minutes < 0 || minutes > 59 || // Ограничение на ввод времени
                hours == 0 && minutes == 0) {
            label3.setText("Задайте время !!!");
            return;
        } else {
            label3.setText("Оставшееся время :");
        }
        time();
        startButton.setDisable(true);// Кнопка Start - не активна
        hourSpinner.setDisable(true);
        minutesSpinner.setDisable(true);
        timeCheckBox.setDisable(true);// CheckBox - не активен
        pauseButton.setDisable(false);// Кнопка Pause - активна
        timer.start();
    }

    @FXML
    public void onActionPauseButton() {// Кнопка ПАУЗА
        timer.stop();

        if (pauseButton.getText().equals("Pause")) {// Изменение надписи кнопки
            pauseButton.setText("Continue");
            startButton.setDisable(false);// Кнопка Start - активна
            hourSpinner.setDisable(false);
            minutesSpinner.setDisable(false);
            timeCheckBox.setDisable(false);// CheckBox - активен
        } else {
            pauseButton.setText("Pause");
            givenDate = currentTime + Calendar.getInstance().getTimeInMillis();// Заданная дата в миллисекундах
            startButton.setDisable(true);// Кнопка Start - не активна
            hourSpinner.setDisable(true);
            minutesSpinner.setDisable(true);
            timeCheckBox.setDisable(true);// CheckBox - не активен
            timer.start();
        }
    }

    public void out() {// После оканчания времени
        timer.stop();
        try {
            Runtime.getRuntime().exec("7shutdown /s");
        } catch (IOException e) {
            label1.setText("Не удалось");
            label2.setText("выполнить!!!");
        }
    }

    public void time() { // Задание промежутка времени
        Calendar date = Calendar.getInstance();// Текущая дата
        Calendar calendar = new GregorianCalendar();// Заданная дата

        if (timeCheckBox.isSelected()) {
            calendar.set(Calendar.HOUR_OF_DAY, hours);// Задание часов
            calendar.set(Calendar.MINUTE, minutes);// Задание минут
            calendar.set(Calendar.SECOND, 0);
        } else {
            calendar.set(Calendar.HOUR_OF_DAY, date.get(Calendar.HOUR_OF_DAY) + hours);
            calendar.set(Calendar.MINUTE, date.get(Calendar.MINUTE) + minutes);
            calendar.set(Calendar.SECOND, date.get(Calendar.SECOND));
        }

        givenDate = calendar.getTimeInMillis();// Заданная дата в миллисекундах
    }

    public void action() {// Вычисление остатка ремени
        Calendar date = Calendar.getInstance();// Текущая дата
        currentTime = givenDate - date.getTimeInMillis();// Вычисление разницы времени
        SimpleDateFormat formater1 = new SimpleDateFormat("HH:mm:ss");
        Date date1 = new Date(currentTime - 3 * 60 * 60 * 1000); // Почему-то получается на 3 часа больше в date1
        // после создания объекта date1
        secondsLabel.setText(formater1.format(date1));// Вывод оставшегося времени

        if (currentTime >= 0 && currentTime < 1000) {// Условие выхода, 1000 миллисек потому что 0 не получается
            System.out.println(currentTime);
            out();// Остановка программы
        }
    }

    AnimationTimer timer = new AnimationTimer() {// Сам таймер
        @Override
        public void handle(long now) {
            action();
        }
    };

    @FXML
    public void onActionTimersButton() {// Открытие 2-го окна
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/views/timers.fxml"));

            AnchorPane anchorPane;
            anchorPane = (AnchorPane) fxmlLoader.load();
            Stage stage = (Stage) startButton.getScene().getWindow();
            Stage timersStage = new Stage();
            timersStage.initModality(Modality.WINDOW_MODAL);
            timersStage.initOwner(stage);
            timersStage.setMaxWidth(150);
            timersStage.setMaxHeight(250);

            Scene scene = new Scene(anchorPane);
            timersStage.setScene(scene);

            Timers timers = fxmlLoader.getController();
//            minutesSpinner.getEditor().setText(String.valueOf(timers.toString()));

            timersStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
