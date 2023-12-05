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
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public void read(Object msg) {
        String jsonStringRequest = new String((byte[]) msg);

        try {
            Request request = gson.fromJson(jsonStringRequest, Request.class);

            switch (request.getRequest()) {
                case "checkUser" -> сheckUserRequest(request);
                case "checkWorkerCredentials" -> checkWorkerCredentialsRequest(request);
                case "registerUser" -> registerUserRequest(request);
                case "registerWorker" -> registerWorkerRequest(request);
                case "mobileRegisterUser" -> mobileRegisterUserRequest(request);
                case "addUser" -> addUserRequest(request);
                case "addApplication" -> addApplicationRequest(request);
                case "addForm" -> addFormRequst(request);
                case "addSocOrganization" -> addSocOrganizationRequst(request);
                case "addDocument" -> addDocumentRequst(request);
                case "addRegion" -> addRegion(request);
                case "deleteWorker" -> deleteWorkerRequest(request);
                case "deleteUser" -> deleteUserRequest(request);
                case "deleteDocument" -> deleteDocumentRequest(request);
                case "deleteRegion" -> deleteRegionRequest(request);
                case "deleteUserData" -> deleteUserDataRequest(request);
                case "deleteUserAdditionalData" -> deleteUserAdditionalDataRequest(request);
                case "deleteSocialOrganization" -> deleteSocialOrganizationRequest(request);
                case "deleteForm" -> deleteFormRequest(request);
                case "deleteApplication" -> deleteApplicationRequest(request);
                case "updateWorkerProfile" -> updateWorkerProfileRequest(request);
                case "updateUserProfile" -> updateUserProfileRequest(request);
                case "updateDocument" -> updateDocumentRequest(request);
                case "updateRegion" -> updateRegionRequest(request);
                case "updateUserData" -> updateUserDataRequest(request);
                case "updateUserDataAdd" -> updateUserDataAddRequest(request);
                case "updateSocOrganization" -> updateSocOrganizationRequest(request);
                case "statusEditApplication" -> statusEditApplicationRequest(request);
                case "updateForm" -> updateFormRequest(request);
                case "updateApplication" -> updateApplicationRequest(request);
            }
        } catch (Exception e) {;
            Answer AnswerSer = new Answer(Map.of("Ответ", "не правильный формат запроса"));
            answer(AnswerSer);
        }
    }

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

    public void сheckUserRequest(Request request) {
        Map<String, String> map = request.getMapAttributes();
        String answerbd = database.checkUser(map.get("snils"), map.get("enteredPassword"));
        Answer AnswerSer = new Answer(Map.of("answer", answerbd));
        answer(AnswerSer);
    }

    private void checkWorkerCredentialsRequest(Request request) {
        Map<String, String> map = request.getMapAttributes();
        String answerbd = database.checkWorkerCredentials(map.get("login"), map.get("password"));
        Answer AnswerSer = new Answer(Map.of("answer", answerbd));

        answer(AnswerSer);
    }

    private void registerUserRequest(Request request) {
        Map<String, String> map = request.getMapAttributes();
        String answerbd = database.registerUser(map.get("snils"), map.get("phone"), map.get("plainPassword"));
        Answer AnswerSer = new Answer(Map.of("answer", answerbd));
        answer(AnswerSer);
    }

    private void registerWorkerRequest(Request request) {
        Map<String, String> map = request.getMapAttributes();
        String answerbd = database.registerWorker(map.get("login"), map.get("plainPassword"),
                map.get("name"), map.get("surname"), map.get("patronymic"),
                map.get("post"), Boolean.parseBoolean(map.get("isAdmin")));
        Answer AnswerSer = new Answer(Map.of("answer", answerbd));

        answer(AnswerSer);
    }

    private void mobileRegisterUserRequest(Request request) {
        Map<String, String> map = request.getMapAttributes();
        String answerbd = database.mobileRegisterUser(map.get("snils"), map.get("name"),
                map.get("surname"), map.get("patronymic"), map.get("phone"),
                map.get("password"));
        Answer AnswerSer = new Answer(Map.of("answer", answerbd));

        answer(AnswerSer);
    }

    private void addUserRequest(Request request) throws ParseException {
        Map<String, String> map = request.getMapAttributes();
        String answerbd = database.addUser(map.get("snils"), map.get("documentName"),
                map.get("documentNumber"), map.get("name"), map.get("surname"),
                map.get("patronymic"), (Date) dateFormat.parse(map.get("birthdate")), map.get("phone"),
                map.get("email"), Integer.valueOf(map.get("regionId")), map.get("regionSmall"),
                map.get("city"), map.get("street"), map.get("home"), map.get("apartment"));
        Answer AnswerSer = new Answer(Map.of("answer", answerbd));

        answer(AnswerSer);
    }

    private void addApplicationRequest(Request request) {
        Map<String, String> map = request.getMapAttributes();
        String answerbd = database.addApplication(map.get("userSnils"), Integer.valueOf(map.get("userAddId")),
                Integer.valueOf(map.get("workerProfileId")), Integer.valueOf(map.get("socOrganizationOgrrn")),
                map.get("form"), map.get("reason"), Boolean.parseBoolean(map.get("domestic")),
                Boolean.parseBoolean(map.get("medical")), Boolean.parseBoolean(map.get("psychological")),
                Boolean.parseBoolean(map.get("pedagogical")), Boolean.parseBoolean(map.get("labour")),
                Boolean.parseBoolean(map.get("legal")), Boolean.parseBoolean(map.get("communication")),
                Boolean.parseBoolean(map.get("urgent")), map.get("family"), map.get("living"),
                Integer.valueOf(map.get("income")), map.get("status"));
        Answer AnswerSer = new Answer(Map.of("answer", answerbd));

        answer(AnswerSer);
    }

    private void addFormRequst(Request request) {
        Map<String, String> map = request.getMapAttributes();
        String answerbd = database.addForm(map.get("name"));
        Answer AnswerSer = new Answer(Map.of("answer", answerbd));

        answer(AnswerSer);

    }

    private void addSocOrganizationRequst(Request request) {
        Map<String, String> map = request.getMapAttributes();
        String answerbd = database.addSocOrganization(Integer.parseInt(map.get("ogrrn")), map.get("name"));
        Answer AnswerSer = new Answer(Map.of("answer", answerbd));

        answer(AnswerSer);
    }

    private void addDocumentRequst(Request request) {
        Map<String, String> map = request.getMapAttributes();
        String answerbd = database.addDocument(map.get("name"), map.get("regex"));
        Answer AnswerSer = new Answer(Map.of("answer", answerbd));
        answer(AnswerSer);
    }

    private void addRegion(Request request) {
        Map<String, String> map = request.getMapAttributes();
        String answerbd = database.addRegion(Integer.parseInt(map.get("id")), map.get("name"));
        Answer AnswerSer = new Answer(Map.of("answer", answerbd));
        answer(AnswerSer);
    }

    private void deleteWorkerRequest(Request request) {
        Map<String, String> map = request.getMapAttributes();
        String answerbd = database.deleteWorker(Integer.parseInt(map.get("workerId")));
        Answer AnswerSer = new Answer(Map.of("answer", answerbd));
        answer(AnswerSer);
    }

    private void deleteUserRequest(Request request) {
        Map<String, String> map = request.getMapAttributes();
        String answerbd = database.deleteUser(map.get("snils"));
        Answer AnswerSer = new Answer(Map.of("answer", answerbd));
        answer(AnswerSer);
    }

    private void deleteDocumentRequest(Request request) {
        Map<String, String> map = request.getMapAttributes();
        String answerbd = database.deleteDocument(map.get("documentName"));
        Answer AnswerSer = new Answer(Map.of("answer", answerbd));
        answer(AnswerSer);
    }

    private void deleteRegionRequest(Request request) {
        Map<String, String> map = request.getMapAttributes();
        String answerbd = database.deleteRegion(Integer.parseInt(map.get("regionId")));
        Answer AnswerSer = new Answer(Map.of("answer", answerbd));
        answer(AnswerSer);
    }

    private void deleteUserDataRequest(Request request) {
        Map<String, String> map = request.getMapAttributes();
        String answerbd = database.deleteUserData(map.get("snils"));
        Answer AnswerSer = new Answer(Map.of("answer", answerbd));
        answer(AnswerSer);
    }

    private void deleteUserAdditionalDataRequest(Request request) {
        Map<String, String> map = request.getMapAttributes();
        String answerbd = database.deleteUserAdditionalData(Integer.parseInt(map.get("dataId")));
        Answer AnswerSer = new Answer(Map.of("answer", answerbd));
        answer(AnswerSer);
    }

    private void deleteSocialOrganizationRequest(Request request) {
        Map<String, String> map = request.getMapAttributes();
        String answerbd = database.deleteSocialOrganization(Integer.parseInt(map.get("ogrrn")));
        Answer AnswerSer = new Answer(Map.of("answer", answerbd));
        answer(AnswerSer);
    }

    private void deleteFormRequest(Request request) {
        Map<String, String> map = request.getMapAttributes();
        String answerbd = database.deleteForm(map.get("formName"));
        Answer AnswerSer = new Answer(Map.of("answer", answerbd));
        answer(AnswerSer);
    }

    private void deleteApplicationRequest(Request request) {
        Map<String, String> map = request.getMapAttributes();
        String answerbd = database.deleteApplication(Integer.parseInt(map.get("applicationId")));
        Answer AnswerSer = new Answer(Map.of("answer", answerbd));
        answer(AnswerSer);
    }

    private void updateWorkerProfileRequest(Request request) {
        Map<String, String> map = request.getMapAttributes();
        String answerbd = database.updateWorkerProfile(Integer.parseInt(map.get("id")), map.get("login"),
                map.get("plainPassword"), map.get("name"), map.get("surname"),
                map.get("patronymic"), map.get("post"), Integer.valueOf(map.get("admin")));
        Answer AnswerSer = new Answer(Map.of("answer", answerbd));
        answer(AnswerSer);
    }

    private void updateUserProfileRequest(Request request) {
        Map<String, String> map = request.getMapAttributes();
        String answerbd = database.updateUserProfile(map.get("snils"), map.get("phone"),
                map.get("plainPassword"));
        Answer AnswerSer = new Answer(Map.of("answer", answerbd));
        answer(AnswerSer);
    }

    private void updateDocumentRequest(Request request) {
        Map<String, String> map = request.getMapAttributes();
        String answerbd = database.updateDocument(map.get("name"), map.get("newRegex"));
        Answer AnswerSer = new Answer(Map.of("answer", answerbd));
        answer(AnswerSer);
    }

    private void updateRegionRequest(Request request) {
        Map<String, String> map = request.getMapAttributes();
        String answerbd = database.updateRegion(Integer.parseInt(map.get("id")), map.get("name"));
        Answer AnswerSer = new Answer(Map.of("answer", answerbd));
        answer(AnswerSer);
    }

    private void updateUserDataRequest(Request request) throws ParseException {
        Map<String, String> map = request.getMapAttributes();
        String answerbd = database.updateUserData(map.get("snils"), map.get("documentName"),
                map.get("documentNumber"), map.get("name"), map.get("surname"),
                map.get("patronymic"), (Date) dateFormat.parse(map.get("birthdate")),
                map.get("phone"), map.get("email"), Integer.valueOf(map.get("regionId")),
                map.get("regionSmall"), map.get("city"), map.get("street"),
                map.get("home"), map.get("apartment"));
        Answer AnswerSer = new Answer(Map.of("answer", answerbd));
        answer(AnswerSer);
    }

    private void updateUserDataAddRequest(Request request) {
        Map<String, String> map = request.getMapAttributes();
        String answerbd = database.updateUserDataAdd(Integer.valueOf(map.get("ID")), map.get("DocumentName"),
                map.get("DocumentNumber"), map.get("Name"), Integer.valueOf(map.get("AddressRegionId")),
                map.get("AddressSmallRegion"), map.get("AddressCity"),
                map.get("AddressStreet"), map.get("AddressHome"), Integer.valueOf(map.get("AddressApartment")));
        Answer AnswerSer = new Answer(Map.of("answer", answerbd));
        answer(AnswerSer);
    }

    private void updateSocOrganizationRequest(Request request) {
        Map<String, String> map = request.getMapAttributes();
        String answerbd = database.updateSocOrganization(Integer.parseInt(map.get("ogrrn")), map.get("newName"));
        Answer AnswerSer = new Answer(Map.of("answer", answerbd));
        answer(AnswerSer);
    }

    private void statusEditApplicationRequest(Request request) {
        Map<String, String> map = request.getMapAttributes();
        String answerbd = database.statusEditApplication(Integer.parseInt(map.get("id")), Integer.valueOf(map.get("status")));
        Answer AnswerSer = new Answer(Map.of("answer", answerbd));
        answer(AnswerSer);
    }

    private void updateFormRequest(Request request) {
        Map<String, String> map = request.getMapAttributes();
        String answerbd = database.updateForm(map.get("currentName"), map.get("newName"));
        Answer AnswerSer = new Answer(Map.of("answer", answerbd));
        answer(AnswerSer);
    }

    private void updateApplicationRequest(Request request) {
        Map<String, String> map = request.getMapAttributes();
        String answerbd = database.updateApplication(Integer.valueOf(map.get("id")), map.get("userDataSnils"),
                Integer.valueOf(map.get("userDataAddId")), Integer.valueOf(map.get("workerProfileId")),
                Integer.valueOf(map.get("socOrganizationOgrrn")), map.get("form"),
                map.get("reason"), Boolean.parseBoolean(map.get("domestic")),
                Boolean.parseBoolean(map.get("medical")), Boolean.parseBoolean(map.get("psychological")),
                Boolean.parseBoolean(map.get("pedagogical")), Boolean.parseBoolean(map.get("labour")),
                Boolean.parseBoolean(map.get("legal")), Boolean.parseBoolean(map.get("communication")),
                Boolean.parseBoolean(map.get("urgent")), map.get("famaly"),
                map.get("living"), Integer.valueOf(map.get("income")),
                map.get("status"));
        Answer AnswerSer = new Answer(Map.of("Ответ", answerbd));
        answer(AnswerSer);
    }

    private void answer(Answer answer) {
        String jsonStringAnswer = gson.toJson(answer);
        getSession().write(("%s\n".formatted(jsonStringAnswer)).getBytes(StandardCharsets.UTF_8));

    }
}