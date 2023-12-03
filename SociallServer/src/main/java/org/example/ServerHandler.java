package org.example;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


import com.google.gson.Gson;
import org.snf4j.core.handler.AbstractStreamHandler;
import org.snf4j.core.handler.SessionEvent;

public class ServerHandler extends AbstractStreamHandler {
    Gson gson = new Gson();
    Database database = new Database();
    Answer AnswerSer;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public void read(Object msg) {
        //получние запроса
        String jsonStringRequest = new String((byte[]) msg);
        System.out.println("Пришедший запрос: " + jsonStringRequest);

        try {
            Request request = gson.fromJson(jsonStringRequest, Request.class);

            System.out.println("Request: " + request.getRequest());

            Map<String, String> attributes = request.getMapAttributes();
            if (attributes != null && !attributes.isEmpty()) {
                for (String attribut : attributes.keySet()) {
                    System.out.println("Ключ " + attribut + " - Значение " + attributes.get(attribut));
                }
            }

            switch (request.getRequest()) {
                case "Сравнение" -> comparison(request);
                case "Проверка работника" -> EmployeeVerification(request);
                case "Регистрация пользователя с хешированным паролем и солью" -> RegUserHashedAndSaltPass(request);
                case "Регистрация работника с хешированным паролем и солью" -> RegWorkerHashedAndSaltPass(request);
                case "Добавить пользователя" -> AddUserRequest(request);
                case "Заявление" -> Zaiv(request);
                case "Добавить форму" -> AddFormRequst(request);
                case "Новоя социальноя организация" -> NewSocOrganizathion(request);
                case "Добавление нового документа" -> NewDocument(request);
                case "Добавление нового региона" -> NewRegion(request);
                case "Удаление работника" -> deleteWorkerRequest(request);
                case "Удаление пользователя" -> deleteUserRequest(request);
                case "Удаление документа" -> deleteDocumentRequest(request);
                case "Удаление региона" -> deleteRegionRequest(request);
                case "Удаление данных пользователя" -> deleteUserDataRequest(request);
                case "Удаление из чего-то" -> deleteUserAdditionalDataRequest(request);
                case "Удаление организации" -> deleteSocialOrganizationRequest(request);
                case "Удаление формы" -> deleteFormRequest(request);
                case "Заявление удаление" -> deleteApplicationRequest(request);
            }
        } catch (Exception e) {
            System.out.println("не правильный формат запроса");
            e.printStackTrace();
        }
    }

    // событие (добаление, удаление клиента)
    @SuppressWarnings("incomplete-switch")
    @Override
    public void event(SessionEvent event) {
        switch (event) {
            case CREATED -> {
                System.out.println(getSession().getId() + "{created}" + getSession());
            }
            case OPENED -> {
                System.out.println(getSession().getId() + "{connected}" + getSession());
            }
            case CLOSED -> {
                System.out.println(getSession().getId() + "{disconnected}" + getSession());
            }
        }
    }

    public void comparison(Request request) {

        // для варианта со строками
        //String answerbd = database.CheckUser(request.getListAttributes().get(1), request.getListAttributes().get(0));
        //AnswerSer = new Answer(new ArrayList<>(Arrays.asList(answerbd)));

        // для варианта с словарем
        String answerbd = database.CheckUser(request.getMapAttributes().get("Снилс"), request.getMapAttributes().get("Пороль"));
        AnswerSer = new Answer(Map.of("Ответ", answerbd));

        answer(AnswerSer);
    }

    private void EmployeeVerification(Request request) {

        String answerbd = database.checkWorkerCredentials(request.getMapAttributes().get("Логин"), request.getMapAttributes().get("Пороль"));
        AnswerSer = new Answer(Map.of("Ответ", answerbd));

        answer(AnswerSer);
    }

    // договориться что делаем
    private void RegUserHashedAndSaltPass(Request request) {
        String answerbd = database.registerUser(request.getMapAttributes().get("Снилс"), request.getMapAttributes().get("Телефон"), request.getMapAttributes().get("Пароль"));
        AnswerSer = new Answer(Map.of("Ответ", answerbd));

        answer(AnswerSer);
    }

    // изменить булевое значение
    private void RegWorkerHashedAndSaltPass(Request request) {
        String answerbd = database.registerWorker(request.getMapAttributes().get("Логин"), request.getMapAttributes().get("Телефон"),
                request.getMapAttributes().get("Имя"), request.getMapAttributes().get("Фамилия"), request.getMapAttributes().get("Отчество"),
                request.getMapAttributes().get("Пост"), Boolean.parseBoolean(request.getMapAttributes().get("Админ")));
        AnswerSer = new Answer(Map.of("Ответ", answerbd));

        answer(AnswerSer);
    }

    private void AddUserRequest(Request request) throws ParseException {
        String answerbd = database.addUser(request.getMapAttributes().get("Снилс"), request.getMapAttributes().get("документ имя"),
                request.getMapAttributes().get("Документ телефон"), request.getMapAttributes().get("Имя"), request.getMapAttributes().get("Фамилия"),
                request.getMapAttributes().get("Отчество"), (Date) dateFormat.parse(request.getMapAttributes().get("День рождения")), request.getMapAttributes().get("Телефон"),
                request.getMapAttributes().get("Эмаил"), Integer.valueOf(request.getMapAttributes().get("Регион")), request.getMapAttributes().get("Регион Смал"),
                request.getMapAttributes().get("Город"), request.getMapAttributes().get("Стрит"), request.getMapAttributes().get("Дом"),
                request.getMapAttributes().get("Квартира"));
        AnswerSer = new Answer(Map.of("Ответ", answerbd));

        answer(AnswerSer);
    }

    private void Zaiv(Request request) {
        String answerbd = database.addApplication(request.getMapAttributes().get("Снилс"), Integer.valueOf(request.getMapAttributes().get("какое-от ID")),
                Integer.valueOf(request.getMapAttributes().get("Рабочий профиль id")), Integer.valueOf(request.getMapAttributes().get("СоцОрганизация Орган")),
                request.getMapAttributes().get("Форм"), request.getMapAttributes().get("Реасон"), Boolean.parseBoolean(request.getMapAttributes().get("Доместик")),
                Boolean.parseBoolean(request.getMapAttributes().get("Медицина")), Boolean.parseBoolean(request.getMapAttributes().get("FFFFFF")),
                Boolean.parseBoolean(request.getMapAttributes().get("Педагог")), Boolean.parseBoolean(request.getMapAttributes().get("Лабор")),
                Boolean.parseBoolean(request.getMapAttributes().get("Легал")), Boolean.parseBoolean(request.getMapAttributes().get("Коммуникация")),
                Boolean.parseBoolean(request.getMapAttributes().get("Ургант")), request.getMapAttributes().get("Фамилия"), request.getMapAttributes().get("Жизнь"),
                Integer.valueOf(request.getMapAttributes().get("Кома")), request.getMapAttributes().get("Статус"));
        AnswerSer = new Answer(Map.of("Ответ", answerbd));

        answer(AnswerSer);
    }

    private void AddFormRequst(Request request) {

        String answerbd = database.addForm(request.getMapAttributes().get("Форма"));
        AnswerSer = new Answer(Map.of("Ответ", answerbd));

        answer(AnswerSer);

    }


    private void NewSocOrganizathion(Request request) {
        String answerbd = database.addSocOrganization(Integer.parseInt(request.getMapAttributes().get("Организация")),
                request.getMapAttributes().get("Имя"));
        AnswerSer = new Answer(Map.of("Ответ", answerbd));

        answer(AnswerSer);
    }


    private void NewDocument(Request request) {
        String answerbd = database.addDocument(request.getMapAttributes().get("Имя"),
                request.getMapAttributes().get("регес"));
        AnswerSer = new Answer(Map.of("Ответ", answerbd));
        answer(AnswerSer);
    }


    private void NewRegion(Request request) {
        String answerbd = database.addRegion(Integer.parseInt(request.getMapAttributes().get("ID")),
                request.getMapAttributes().get("Имя"));
        AnswerSer = new Answer(Map.of("Ответ", answerbd));
        answer(AnswerSer);
    }

    private void deleteWorkerRequest(Request request) {
        String answerbd = database.deleteWorker(Integer.parseInt(request.getMapAttributes().get("РабочийID")));
        AnswerSer = new Answer(Map.of("Ответ", answerbd));
        answer(AnswerSer);
    }

    private void deleteUserRequest(Request request) {
        String answerbd = database.deleteUser(request.getMapAttributes().get("Снилс"));
        AnswerSer = new Answer(Map.of("Ответ", answerbd));
        answer(AnswerSer);
    }

    private void deleteDocumentRequest(Request request) {
        String answerbd = database.deleteDocument(request.getMapAttributes().get("Документимя"));
        AnswerSer = new Answer(Map.of("Ответ", answerbd));
        answer(AnswerSer);
    }

    private void deleteRegionRequest(Request request) {
        String answerbd = database.deleteRegion(Integer.parseInt(request.getMapAttributes().get("Регион")));
        AnswerSer = new Answer(Map.of("Ответ", answerbd));
        answer(AnswerSer);
    }

    private void deleteUserDataRequest(Request request) {
        String answerbd = database.deleteUserData(request.getMapAttributes().get("Снилс"));
        AnswerSer = new Answer(Map.of("Ответ", answerbd));
        answer(AnswerSer);
    }


    private void deleteUserAdditionalDataRequest(Request request) {
        String answerbd = database.deleteUserAdditionalData(Integer.parseInt(request.getMapAttributes().get("Снилс")));
        AnswerSer = new Answer(Map.of("Ответ", answerbd));
        answer(AnswerSer);
    }


    private void deleteSocialOrganizationRequest(Request request) {
        String answerbd = database.deleteSocialOrganization(Integer.parseInt(request.getMapAttributes().get("Организация")));
        AnswerSer = new Answer(Map.of("Ответ", answerbd));
        answer(AnswerSer);
    }


    private void deleteFormRequest(Request request) {
        String answerbd = database.deleteForm(request.getMapAttributes().get("формИмя"));
        AnswerSer = new Answer(Map.of("Ответ", answerbd));
        answer(AnswerSer);
    }

    private void deleteApplicationRequest(Request request) {
        String answerbd = database.deleteApplication(Integer.parseInt(request.getMapAttributes().get("приложениеID")));
        AnswerSer = new Answer(Map.of("Ответ", answerbd));
        answer(AnswerSer);
    }






    /*private void RegUserHashedAndSaltPass(Request request) {
        String answerbd = database.registerUser(request.getMapAttributes().get("Снилс"), request.getMapAttributes().get("Телефон"), request.getMapAttributes().get("Пароль"));
        AnswerSer = new Answer(Map.of("Ответ", answerbd));

        answer(AnswerSer);
    }*/

    // Отправка ответа
    private void answer(Answer answer) {
        String jsonStringAnswer = gson.toJson(answer);
        getSession().write(("%s".formatted(jsonStringAnswer)).getBytes());

    }
}