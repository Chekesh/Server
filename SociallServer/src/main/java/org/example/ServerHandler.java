package org.example;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;



import org.snf4j.core.handler.AbstractStreamHandler;
import org.snf4j.core.handler.SessionEvent;
import org.snf4j.core.session.IStreamSession;

public class ServerHandler extends AbstractStreamHandler {

    private static Integer USERID = 0;

    //private static String YOUID = "[you]";

    // Создание словаря имеющего названию строки ссесию
    static final Map<Long, IStreamSession> sessions = new HashMap<Long, IStreamSession>();

    //Срабатывает при получении данных из потока
    @Override
    public void read(Object msg) {
        //Gson
        String jsonString = new String((byte[]) msg);
        System.out.println(jsonString);
//
//
//        Request request = gson.fromJson(json, Request.class);
//
//        System.out.println("Request: " + request.getRequest());
//        System.out.println("Username: " + request.getUsername());
//        System.out.println("Password: " + request.getPassword());
//
//
//        if (request.getRequest().equals("Сравнение")) {
//            comparison(request);

        }

        /*ArraysList<User> userList = request.getList();
        if (userList != null && !userList.isEmpty()) {
            User user = userList.get(0);
            System.out.println("Nested User - Username: " + user.getUsername() + ", Password: " + user.getPassword());
        }*/

        //byte[] data = (byte[])msg;
        //String s = new String((byte[])msg);
        // выводим данные
        //send(s);
        // если пришло "bye"
        /*if ("bye".equalsIgnoreCase(s)) {
            // закрываем сессию
            getSession().close();
        }*/


    // событие (добаление, удаление клиента)
    @SuppressWarnings("incomplete-switch")
    @Override
    public void event(SessionEvent event) {
        switch (event) {
            case OPENED -> {
                // добавление в словарь id ссесии и саму ссесию
                sessions.put(getSession().getId(), getSession());
                System.out.println(getSession().getId() + "{connected}");
                // добавление имени клиента
                //getSession().getAttributes().put(USERID, "[Шайлушай (не я (кент))]");
                // Вывод подключения
                //send("{connected}");
            }
            case CLOSED -> {
                // удаление из словаря ссесии по id
                sessions.remove(getSession().getId());
                // Вывод отключения
                System.out.println(getSession().getId() + "{disconnected}");
                //send("{disconnected}");
            }
        }
    }

    // Метод отправки ссообщения всем подключеным клиентам
//    private void send(String message) {
//        // получение id сессии
//        long youId = getSession().getId();
//        // получение имени клиента
//        String userId = (String) getSession().getAttributes().get(USERID);
//
//        // проход по всем подключенным клиентам
//        for (IStreamSession session: sessions.values()) {
//            // проверка на отправителя
//            if(session.getId() != youId)
//                // отправление сообщениея
//                session.write(("%s %d %s".formatted(userId, USERID, message)).getBytes());
//        }
//    }

    public void comparison(Request request) {
        if (request.getUsername().equals("ArtikDemonik") && request.getPassword().equals("1234")) {
            amswer("заебись");
        } else {
            amswer("не заебись");

        }
    }

    private void amswer(String message) {
        // получение id сессии
        getSession().write(("%s".formatted(message)).getBytes());
    }

}