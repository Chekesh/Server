package org.example;

import java.nio.charset.StandardCharsets;
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
                case "CheckUser" -> CheckUserRequest(request);
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
                case "UpdateWorkerProfile" -> UpdateWorkerProfileRequest(request);
                case "UpdateUserProfile" -> UpdateUserProfileRequest(request);
                case "UpdateDocument" -> UpdateDocumentRequest(request);
                case "UpdateRegion" -> UpdateRegionRequest(request);
                case "UpdateUserData" -> UpdateUserDataRequest(request);
                case "UpdateUserDataAdd" -> UpdateUserDataAddRequest(request);
                case "UpdateSocOrganization" -> UpdateSocOrganizationRequest(request);
                case "UpdateForm" -> UpdateFormRequest(request);
                case "UpdateApplication" -> UpdateApplicationRequest(request);
            }
        } catch (Exception e) {;
            System.out.println("не правильный формат запроса");
            AnswerSer = new Answer(Map.of("Ответ", "не правильный формат запроса"));
            answer(AnswerSer);
            //e.printStackTrace();
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
                //answer(new Answer(Map.of("Ответ", "ты лох")));
                //answer(new Answer(Map.of("Ответ", "Ровная мужитска строчка")));
            }
            case CLOSED -> {
                System.out.println(getSession().getId() + "{disconnected}" + getSession());
            }
        }
    }

    public void CheckUserRequest(Request request) {

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

    // Я не знаю как сработает переход в дату
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
        String answerbd = database.addSocOrganization(Integer.parseInt(request.getMapAttributes().get("ID Организации")),
                request.getMapAttributes().get("Название"));
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

    private void UpdateWorkerProfileRequest(Request request) {
        String answerbd = database.updateWorkerProfile(Integer.parseInt(request.getMapAttributes().get("ID")), request.getMapAttributes().get("Login"),
                request.getMapAttributes().get("Password"), request.getMapAttributes().get("Name"), request.getMapAttributes().get("Surname"),
                request.getMapAttributes().get("Patronymic"),request.getMapAttributes().get("Post"), Integer.valueOf(request.getMapAttributes().get("Admin")));
        AnswerSer = new Answer(Map.of("Ответ", answerbd));
        answer(AnswerSer);
    }

    private void UpdateUserProfileRequest(Request request) {
        String answerbd = database.updateUserProfile(request.getMapAttributes().get("Snils"), request.getMapAttributes().get("Phone"),
                request.getMapAttributes().get("Password"));
        AnswerSer = new Answer(Map.of("Ответ", answerbd));
        answer(AnswerSer);
    }

    private void UpdateDocumentRequest(Request request) {
        String answerbd = database.updateDocument(request.getMapAttributes().get("Name"), request.getMapAttributes().get("NewRegex"));
        AnswerSer = new Answer(Map.of("Ответ", answerbd));
        answer(AnswerSer);
    }


    private void UpdateRegionRequest(Request request) {
        String answerbd = database.updateRegion(Integer.parseInt(request.getMapAttributes().get("ID")), request.getMapAttributes().get("Name"));
        AnswerSer = new Answer(Map.of("Ответ", answerbd));
        answer(AnswerSer);
    }


    private void UpdateUserDataRequest(Request request) throws ParseException {
        String answerbd = database.updateUserData(request.getMapAttributes().get("Snils"), request.getMapAttributes().get("DocumentName"),
                request.getMapAttributes().get("DocumentNumber"), request.getMapAttributes().get("Name"), request.getMapAttributes().get("Surname"),
                request.getMapAttributes().get("Patronymic"), (Date) dateFormat.parse(request.getMapAttributes().get("Birthdate")),
                request.getMapAttributes().get("Phone"), request.getMapAttributes().get("Email"), Integer.valueOf(request.getMapAttributes().get("RegionId")),
                request.getMapAttributes().get("RegionSmall"), request.getMapAttributes().get("City"), request.getMapAttributes().get("Street"),
                request.getMapAttributes().get("Home"), request.getMapAttributes().get("Apartment"));
        AnswerSer = new Answer(Map.of("Ответ", answerbd));
        answer(AnswerSer);
    }

    private void UpdateUserDataAddRequest(Request request) {
        String answerbd = database.updateUserDataAdd(Integer.valueOf(request.getMapAttributes().get("ID")), request.getMapAttributes().get("DocumentName"),
                request.getMapAttributes().get("DocumentNumber"), request.getMapAttributes().get("Name"), Integer.valueOf(request.getMapAttributes().get("AddressRegionId")),
                request.getMapAttributes().get("AddressSmallRegion"), request.getMapAttributes().get("AddressCity"),
                request.getMapAttributes().get("AddressStreet"), request.getMapAttributes().get("AddressHome"), Integer.valueOf(request.getMapAttributes().get("AddressApartment")));
        AnswerSer = new Answer(Map.of("Ответ", answerbd));
        answer(AnswerSer);
    }

    private void UpdateSocOrganizationRequest(Request request) {
        String answerbd = database.updateSocOrganization(Integer.parseInt(request.getMapAttributes().get("ID")), request.getMapAttributes().get("NewName"));
        AnswerSer = new Answer(Map.of("Ответ", answerbd));
        answer(AnswerSer);
    }

    private void UpdateFormRequest(Request request) {
        String answerbd = database.updateForm(request.getMapAttributes().get("CurrentName"), request.getMapAttributes().get("NewName"));
        AnswerSer = new Answer(Map.of("Ответ", answerbd));
        answer(AnswerSer);
    }

    private void UpdateApplicationRequest(Request request) {
        String answerbd = database.updateApplication(Integer.valueOf(request.getMapAttributes().get("ID")), request.getMapAttributes().get("UserDataSnils"),
                Integer.valueOf(request.getMapAttributes().get("UserDataAddId")), Integer.valueOf(request.getMapAttributes().get("WorkerProfileId")),
                Integer.valueOf(request.getMapAttributes().get("SocOrganizationOgrrn")), request.getMapAttributes().get("Form"),
                request.getMapAttributes().get("Reason"), Boolean.parseBoolean(request.getMapAttributes().get("Domestic")),
                Boolean.parseBoolean(request.getMapAttributes().get("Medical")), Boolean.parseBoolean(request.getMapAttributes().get("Psychological")),
                Boolean.parseBoolean(request.getMapAttributes().get("Pedagogical")), Boolean.parseBoolean(request.getMapAttributes().get("labour")),
                Boolean.parseBoolean(request.getMapAttributes().get("legal")), Boolean.parseBoolean(request.getMapAttributes().get("Communication")),
                Boolean.parseBoolean(request.getMapAttributes().get("Urgent")), request.getMapAttributes().get("Famaly"),
                request.getMapAttributes().get("Living"), Integer.valueOf(request.getMapAttributes().get("Income")),
                request.getMapAttributes().get("Status"));
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
        getSession().write(("%s\n".formatted(jsonStringAnswer)).getBytes(StandardCharsets.UTF_8));

    }
}