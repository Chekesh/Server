package org.example;

import java.sql.*;

import org.mindrot.jbcrypt.BCrypt;

public class Database {
    protected String dbHost = "localhost";
    protected String dbPort = "3306";
    protected String dbUser = "root";
    protected String dbPass = "1234";
    protected String dbName = "socservices";
    Connection dbConnection;
    {
        try {
            dbConnection = getDbConnection();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Connection getDbConnection()
            throws ClassNotFoundException, SQLException {
        String connectionString = "jdbc:mysql://" + dbHost + ":"
                + dbPort + "/" + dbName;
        Class.forName("com.mysql.cj.jdbc.Driver");
        dbConnection = DriverManager.getConnection(connectionString, dbUser, dbPass);
        return dbConnection;
    }
    public String checkUser(String snils, String enteredPassword) {
        String request = "SELECT hashed_password, salt_password FROM user_profile WHERE snils = ?";
        String answerbd = null;

        try (PreparedStatement prSt = dbConnection.prepareStatement(request)) {
            prSt.setString(1, snils);
            ResultSet resultSet = prSt.executeQuery();

            int schet = 0;

            while (resultSet.next()) {
                String hashedPasswordFromDB = resultSet.getString("hashed_password");
                String saltFromDB = resultSet.getString("salt_password");

                if (BCrypt.checkpw(enteredPassword, hashedPasswordFromDB)) {
                    answerbd = "1";
                    schet++;
                }
            }
            if (schet == 0) {
                answerbd = "0";
            }
        } catch (SQLException e) {
            answerbd = "0";
            throw new RuntimeException(e);
        }
        finally {
            return answerbd;
        }
    }

    public String checkWorkerCredentials(String login, String password) {
        String request = "SELECT hashed_password, salt_password FROM worker_profile WHERE login = ?";
        String answerbd = null;

        try (PreparedStatement prSt = dbConnection.prepareStatement(request)) {
            prSt.setString(1, login);
            ResultSet resultSet = prSt.executeQuery();
            if (resultSet.next()) {
                String hashedPasswordFromDB = resultSet.getString("hashed_password");
                String saltFromDB = resultSet.getString("salt_password");

                if (BCrypt.checkpw(password, hashedPasswordFromDB)) {
                    answerbd = "1";
                }
                else {
                    answerbd = "0";
                }
            }
        } catch (SQLException e) {
            answerbd = "0";
            throw new RuntimeException(e);
        }finally {
            return answerbd;
        }
    }

    public String registerUser(String snils, String phone, String plainPassword) {
        String salt = BCrypt.gensalt();
        String hashedPassword = BCrypt.hashpw(plainPassword, salt);
        String request = "INSERT INTO user_profile (snils, phone, hashed_password, salt_password) VALUES (?, ?, ?, ?)";
        String answerbd = null;

        try (PreparedStatement prSt = dbConnection.prepareStatement(request)) {
            prSt.setString(1, snils);
            prSt.setString(2, phone);
            prSt.setString(3, hashedPassword);
            prSt.setString(4, salt);

            prSt.executeUpdate();
            answerbd = "1";
        } catch (SQLException e) {
            answerbd = "0";
            throw new RuntimeException(e);
        }
        finally {
            return answerbd;
        }
    }

    public String registerWorker(String login, String plainPassword, String name, String surname, String patronymic, String post, boolean isAdmin) {
        String salt = BCrypt.gensalt();
        String hashedPassword = BCrypt.hashpw(plainPassword, salt);

        String request = "INSERT INTO worker_profile (login, hashed_password, salt_password, name, surname, patronymic, post, admin) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        String answerbd = null;

        try (PreparedStatement prSt = dbConnection.prepareStatement(request)) {
            prSt.setString(1, login);
            prSt.setString(2, hashedPassword);
            prSt.setString(3, salt);
            prSt.setString(4, name);
            prSt.setString(5, surname);
            prSt.setString(6, patronymic);
            prSt.setString(7, post);
            prSt.setBoolean(8, isAdmin);

            prSt.executeUpdate();
            answerbd = "1";

        } catch (SQLException e) {
            answerbd = "0";
            throw new RuntimeException(e);
        }
        finally {
            return answerbd;
        }
    }

    public String mobileRegisterUser(String snils, String name, String surname, String patronymic,
                                     String phone, String password){
        String salt = BCrypt.gensalt();
        String hashedPassword = BCrypt.hashpw(password, salt);
        String request = "INSERT INTO user_profile (snils, phone, hashed_password, salt_password) VALUES (?, ?, ?, ?);"+    "INSERT INTO user_data (snils, name, surname, patronymic, phone) VALUES (?, ?, ?, ?)";
        String answerbd = null;
        try (PreparedStatement prSt = dbConnection.prepareStatement(request)) {
            prSt.setString(1, snils);
            prSt.setString(2, phone);
            prSt.setString(3, hashedPassword);
            prSt.setString(4, salt);
            prSt.setString(5, snils);
            prSt.setString(6, name);
            prSt.setString(7, surname);
            prSt.setString(8, patronymic);
            prSt.setString(9, phone);
            prSt.executeUpdate();
            answerbd = "1";
        } catch (SQLException e) {
            answerbd= "0";
            throw new RuntimeException(e);
        }
        finally {
            return answerbd;
        }
    }

    public String addUser(String snils, String documentName, String documentNumber,
                          String name, String surname, String patronymic,
                          Date birthdate, String phone, String email, //?
                          int regionId, String regionSmall,
                          String city, String street, String home, String apartment) {
        String request = "INSERT INTO user_data (snils, document_name, document_number, name, surname, patronymic, brithdate, phone, email, region_id, region_small, city, street, home, apartment) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String answerbd = null;

        try (PreparedStatement prSt = dbConnection.prepareStatement(request)) {
            prSt.setString(1, snils);
            prSt.setString(2, documentName);
            prSt.setString(3, documentNumber);
            prSt.setString(4, name);
            prSt.setString(5, surname);
            prSt.setString(6, patronymic);
            prSt.setDate(7, birthdate);
            prSt.setString(8, phone);
            prSt.setString(9, email);
            prSt.setInt(10, regionId);
            prSt.setString(11, regionSmall);
            prSt.setString(12, city);
            prSt.setString(13, street);
            prSt.setString(14, home);
            prSt.setString(15, apartment);

            prSt.executeUpdate();
            answerbd = "1";
        } catch (SQLException e) {
            answerbd = "0";
            throw new RuntimeException(e);
        }
        finally {
            return answerbd;
        }
    }

    public String addApplication(String userSnils, Integer userAddId, Integer workerProfileId,
                                 int socOrganizationOgrrn, String form, String reason,
                                 boolean domestic, boolean medical, boolean psychological,
                                 boolean pedagogical, boolean labour, boolean legal,
                                 boolean communication, boolean urgent, String family,
                                 String living, int income, String status) {
        String request = "INSERT INTO application (user_data_snils, user_data_add_, worker_profile_id, soc_organization_ogrrn, form, reason, " +
                "domestic, medical, psychological, pedagogical, labour, legal, communication, urgent, famaly, living, income, status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String answerbd = null;

        try (PreparedStatement prSt = dbConnection.prepareStatement(request)) {
            prSt.setString(1, userSnils);
            prSt.setObject(2, userAddId);  // Может быть NULL, поэтому используем setObject
            prSt.setObject(3, workerProfileId);  // Может быть NULL, поэтому используем setObject
            prSt.setInt(4, socOrganizationOgrrn);
            prSt.setString(5, form);
            prSt.setString(6, reason);
            prSt.setBoolean(7, domestic);
            prSt.setBoolean(8, medical);
            prSt.setBoolean(9, psychological);
            prSt.setBoolean(10, pedagogical);
            prSt.setBoolean(11, labour);
            prSt.setBoolean(12, legal);
            prSt.setBoolean(13, communication);
            prSt.setBoolean(14, urgent);
            prSt.setString(15, family);
            prSt.setString(16, living);
            prSt.setInt(17, income);
            prSt.setString(18, status);

            prSt.executeUpdate();
            answerbd = "1";
        } catch (SQLException e) {
            answerbd = "0";
            throw new RuntimeException(e);
        }
        finally {
            return answerbd;
        }
    }

    public String addForm(String name) {
        String request = "INSERT INTO form (name) VALUES (?)";
        String answerbd = null;

        try (PreparedStatement prSt = dbConnection.prepareStatement(request)) {
            prSt.setString(1, name);

            prSt.executeUpdate();
            answerbd = "1";
        } catch (SQLException e) {
            answerbd = "0";
            throw new RuntimeException(e);
        }
        finally {
            return answerbd;
        }
    }

    public String addSocOrganization(int ogrrn, String name) {
        String request = "INSERT INTO soc_organization (ogrrn, name) VALUES (?, ?)";
        String answerbd = null;

        try (PreparedStatement prSt = dbConnection.prepareStatement(request)) {
            prSt.setInt(1, ogrrn);
            prSt.setString(2, name);

            prSt.executeUpdate();
            answerbd = "1";
        } catch (SQLException e) {
            answerbd = "0";
            throw new RuntimeException(e);
        }
        finally {
            return answerbd;
        }
    }

    public String addDocument(String name, String regex) {
        String request = "INSERT INTO document (name, regex) VALUES (?, ?)";
        String answerbd = null;

        try (PreparedStatement prSt = dbConnection.prepareStatement(request)) {
            prSt.setString(1, name);
            prSt.setString(2, regex);

            prSt.executeUpdate();
            answerbd = "1";
        } catch (SQLException e) {
            answerbd = "0";
            throw new RuntimeException(e);
        }
        finally {
            return answerbd;
        }
    }

    public String addRegion(int id, String name) {
        String request = "INSERT INTO region (id, name) VALUES (?, ?)";
        String answerbd = null;

        try (PreparedStatement prSt = dbConnection.prepareStatement(request)) {
            prSt.setInt(1, id);
            prSt.setString(2, name);

            prSt.executeUpdate();
            answerbd = "1";
        } catch (SQLException e) {
            answerbd = "0";
            throw new RuntimeException(e);
        }
        finally {
            return answerbd;
        }
    }

    public String deleteWorker(int workerId) {
        String request = "DELETE FROM worker_profile WHERE id = ?";
        String answerbd = null;

        try (PreparedStatement prSt = dbConnection.prepareStatement(request)) {
            prSt.setInt(1, workerId);

            int rowsDeleted = prSt.executeUpdate();

            if (rowsDeleted > 0) {
                answerbd = "1";
            } else {
                answerbd = "0";
            }
        } catch (SQLException e) {
            answerbd = "0";
            throw new RuntimeException(e);
        }
        finally {
            return answerbd;
        }
    }

    public String deleteUser(String snils) {
        String request = "DELETE FROM user_profile WHERE snils = ?";
        String answerbd = null;

        try (PreparedStatement prSt = dbConnection.prepareStatement(request)) {
            prSt.setString(1, snils);
            int rowsDeleted = prSt.executeUpdate();

            if (rowsDeleted > 0) {
                answerbd = "1";
            } else {
                answerbd = "0";
            }

        } catch (SQLException e) {
            answerbd = "0";
            throw new RuntimeException(e);
        }
        finally {
            return answerbd;
        }
    }

    public String deleteDocument(String documentName) {
        String request = "DELETE FROM document WHERE name = ?";
        String answerbd = null;

        try (PreparedStatement prSt = dbConnection.prepareStatement(request)) {
            prSt.setString(1, documentName);

            int rowsDeleted = prSt.executeUpdate();

            if (rowsDeleted > 0) {
                answerbd = "1";
            } else {
                answerbd = "0";
            }

        } catch (SQLException e) {
            answerbd = "0";
            throw new RuntimeException(e);
        }
        finally {
            return answerbd;
        }
    }

    public String deleteRegion(int regionId) {
        String request = "DELETE FROM region WHERE id = ?";
        String answerbd = null;

        try (PreparedStatement prSt = dbConnection.prepareStatement(request)) {
            prSt.setInt(1, regionId);
            int rowsDeleted = prSt.executeUpdate();

            if (rowsDeleted > 0) {
                answerbd = "1";
            } else {
                answerbd = "0";
            }

        } catch (SQLException e) {
            answerbd = "0";
            throw new RuntimeException(e);
        }
        finally {
            return answerbd;
        }
    }

    public String deleteUserData(String snils) {
        String request = "DELETE FROM user_data WHERE snils = ?";
        String answerbd = null;

        try (PreparedStatement prSt = dbConnection.prepareStatement(request)) {
            prSt.setString(1, snils);

            int rowsDeleted = prSt.executeUpdate();

            if (rowsDeleted > 0) {
                answerbd = "1";
            } else {
                answerbd = "0";
            }
        } catch (SQLException e) {
            answerbd = "0";
            throw new RuntimeException(e);
        }
        finally {
            return answerbd;
        }
    }

    public String deleteUserAdditionalData(int dataId) {
        String request = "DELETE FROM user_data_add WHERE id = ?";
        String answerbd = null;

        try (PreparedStatement prSt = dbConnection.prepareStatement(request)) {
            prSt.setInt(1, dataId);

            int rowsDeleted = prSt.executeUpdate();

            if (rowsDeleted > 0) {
                answerbd = "1";
            } else {
                answerbd = "0";
            }

        } catch (SQLException e) {
            answerbd = "0";
            throw new RuntimeException(e);
        }
        finally {
            return answerbd;
        }
    }

    public String deleteSocialOrganization(int ogrrn) {
        String request = "DELETE FROM soc_organization WHERE ogrrn = ?";
        String answerbd = null;

        try (PreparedStatement prSt = dbConnection.prepareStatement(request)) {
            prSt.setInt(1, ogrrn);

            int rowsDeleted = prSt.executeUpdate();

            if (rowsDeleted > 0) {
                answerbd = "1";
            } else {
                answerbd = "0";
            }

        } catch (SQLException e) {
            answerbd = "0";
            throw new RuntimeException(e);
        }
        finally {
            return answerbd;
        }
    }

    public String deleteForm(String formName) {
        String request = "DELETE FROM form WHERE name = ?";
        String answerbd = null;

        try (PreparedStatement prSt = dbConnection.prepareStatement(request)) {
            prSt.setString(1, formName);

            int rowsDeleted = prSt.executeUpdate();

            if (rowsDeleted > 0) {
                answerbd = "1";
            } else {
                answerbd = "0";
            }

        } catch (SQLException e) {
            answerbd = "0";
            throw new RuntimeException(e);
        }
        finally {
            return answerbd;
        }
    }

    public String deleteApplication(int applicationId) {
        String request = "DELETE FROM application WHERE id = ?";
        String answerbd = null;

        try (PreparedStatement prSt = dbConnection.prepareStatement(request)) {
            prSt.setInt(1, applicationId);

            int rowsDeleted = prSt.executeUpdate();

            if (rowsDeleted > 0) {
                answerbd = "1";
            } else {
                answerbd = "0";
            }

        } catch (SQLException e) {
            answerbd = "0";
            throw new RuntimeException(e);
        }
        finally {
            return answerbd;
        }
    }

    public String updateWorkerProfile(int id, String login, String plainPassword, String name, String surname, String patronymic, String post, int admin) {
        String salt = BCrypt.gensalt();
        String hashedPassword = BCrypt.hashpw(plainPassword, salt);
        String request = "UPDATE worker_profile SET login = ?, hashed_password = ?, salt_password = ?, name = ?, surname = ?, patronymic = ?, post = ?, admin = ? WHERE id = ?";
        String answerbd = null;

        try (PreparedStatement prSt = dbConnection.prepareStatement(request)) {
            prSt.setString(1, login);
            prSt.setString(2, hashedPassword);
            prSt.setString(3, salt);
            prSt.setString(4, name);
            prSt.setString(5, surname);
            prSt.setString(6, patronymic);
            prSt.setString(7, post);
            prSt.setInt(8, admin);
            prSt.setInt(9, id);

            int rowsUpdated = prSt.executeUpdate();

            if (rowsUpdated > 0) {
                answerbd = "1";
            } else {
                answerbd = "0";
            }

        } catch (SQLException e) {
            answerbd = "0";
            throw new RuntimeException(e);
        }
        finally {
            return answerbd;
        }
    }

    public String updateUserProfile(String snils, String phone, String plainPassword) {
        String salt = BCrypt.gensalt();
        String hashedPassword = BCrypt.hashpw(plainPassword, salt);
        String request = "UPDATE user_profile SET phone = ?, hashed_password = ?, salt_password = ? WHERE snils = ?";
        String answerbd = null;

        try (PreparedStatement prSt = dbConnection.prepareStatement(request)) {
            prSt.setString(1, phone);
            prSt.setString(2, hashedPassword);
            prSt.setString(3, salt);
            prSt.setString(4, snils);

            int rowsUpdated = prSt.executeUpdate();

            if (rowsUpdated > 0) {
                answerbd = "1";
            } else {
                answerbd = "0";
            }

        } catch (SQLException e) {
            answerbd = "0";
            throw new RuntimeException(e);
        }
        finally {
            return answerbd;
        }
    }

    public String updateDocument(String name, String newRegex) {
        String request = "UPDATE document SET regex = ? WHERE name = ?";
        String answerbd = null;

        try (PreparedStatement prSt = dbConnection.prepareStatement(request)) {
            prSt.setString(1, newRegex);
            prSt.setString(2, name);

            int rowsUpdated = prSt.executeUpdate();

            if (rowsUpdated > 0) {
                answerbd = "1";
            } else {
                answerbd = "0";
            }
        } catch (SQLException e) {
            answerbd = "0";
            throw new RuntimeException(e);
        }
        finally {
            return answerbd;
        }
    }

    public String updateRegion(int id, String name) {
        String request = "UPDATE region SET name = ? WHERE id = ?";
        String answerbd = null;

        try (PreparedStatement prSt = dbConnection.prepareStatement(request)) {
            prSt.setString(1, name);
            prSt.setInt(2, id);

            int rowsUpdated = prSt.executeUpdate();

            if (rowsUpdated > 0) {
                answerbd = "1";
            } else {
                answerbd = "0";
            }
        } catch (SQLException e) {
            answerbd = "0";
            throw new RuntimeException(e);
        }
        finally {
            return answerbd;
        }
    }

    public String updateUserData(String snils, String documentName, String documentNumber, String name, String surname, String patronymic,
                                 Date birthdate, String phone, String email, int regionId, String regionSmall, String city, String street,
                                 String home, String apartment) { //!
        String request = "UPDATE user_data SET document_name = ?, document_number = ?, name = ?, surname = ?, patronymic = ?, brithdate = ?, " +
                "phone = ?, email = ?, region_id = ?, region_small = ?, city = ?, street = ?, home = ?, apartment = ? " +
                "WHERE snils = ?";
        String answerbd = null;

        try (PreparedStatement prSt = dbConnection.prepareStatement(request)) {
            prSt.setString(1, documentName);
            prSt.setString(2, documentNumber);
            prSt.setString(3, name);
            prSt.setString(4, surname);
            prSt.setString(5, patronymic);
            prSt.setDate(6, birthdate);
            prSt.setString(7, phone);
            prSt.setString(8, email);
            prSt.setInt(9, regionId);
            prSt.setString(10, regionSmall);
            prSt.setString(11, city);
            prSt.setString(12, street);
            prSt.setString(13, home);
            prSt.setString(14, apartment);
            prSt.setString(15, snils);

            int rowsUpdated = prSt.executeUpdate();

            if (rowsUpdated > 0) {
                answerbd = "1";
            } else {
                answerbd = "0";
            }
        } catch (SQLException e) {
            answerbd = "0";
            throw new RuntimeException(e);
        }
        finally {
            return answerbd;
        }
    }

    public String updateUserDataAdd(int id, String documentName, String documentNumber, String name, int addressRegionId, String addressSmallRegion,
                                    String addressCity, String addressStreet, String addressHome, int addressApartment) {
        String request = "UPDATE user_data_add SET document_name = ?, document_number = ?, name = ?, address_region_id = ?, " +
                "address_small_region = ?, address_city = ?, address_street = ?, address_home = ?, address_apartment = ? " +
                "WHERE id = ?";
        String answerbd = null;

        try (PreparedStatement prSt = dbConnection.prepareStatement(request)) {
            prSt.setString(1, documentName);
            prSt.setString(2, documentNumber);
            prSt.setString(3, name);
            prSt.setInt(4, addressRegionId);
            prSt.setString(5, addressSmallRegion);
            prSt.setString(6, addressCity);
            prSt.setString(7, addressStreet);
            prSt.setString(8, addressHome);
            prSt.setInt(9, addressApartment);
            prSt.setInt(10, id);

            int rowsUpdated = prSt.executeUpdate();

            if (rowsUpdated > 0) {
                answerbd = "1";
            } else {
                answerbd = "0";
            }

        } catch (SQLException e) {
            answerbd = "0";
            throw new RuntimeException(e);
        }
        finally {
            return answerbd;
        }
    }

    public String updateSocOrganization(int ogrrn, String newName) {
        String request = "UPDATE soc_organization SET name = ? WHERE ogrrn = ?";
        String answerbd = null;

        try (PreparedStatement prSt = dbConnection.prepareStatement(request)) {
            prSt.setString(1, newName);
            prSt.setInt(2, ogrrn);

            int rowsUpdated = prSt.executeUpdate();

            if (rowsUpdated > 0) {
                answerbd = "1";
            } else {
                answerbd = "0";
            }
        } catch (SQLException e) {
            answerbd = "0";
            throw new RuntimeException(e);
        }
        finally {
            return answerbd;
        }
    }


    public String statusEditApplication(int id, int status) {
        String request = "UPDATE application SET status = ? WHERE id = ?";
        String answerbd = null;

        try (PreparedStatement prSt = dbConnection.prepareStatement(request)) {
            prSt.setInt(1, status);
            prSt.setInt(19, id);

            int rowsUpdated = prSt.executeUpdate();

            if (rowsUpdated > 0) {
                answerbd = "1";
            } else {
                answerbd = "0";
            }

        } catch (SQLException e) {
            answerbd = "0";
            throw new RuntimeException(e);
        }
        finally {
            return answerbd;
        }
    }

    public String updateForm(String currentName, String newName) {
        String request = "UPDATE form SET name = ? WHERE name = ?";
        String answerbd = null;

        try (PreparedStatement prSt = dbConnection.prepareStatement(request)) {
            prSt.setString(1, newName);
            prSt.setString(2, currentName);

            int rowsUpdated = prSt.executeUpdate();

            if (rowsUpdated > 0) {
                answerbd = "1";
            } else {
                answerbd = "0";
            }

        } catch (SQLException e) {
            answerbd = "0";
            throw new RuntimeException(e);
        }
        finally {
            return answerbd;
        }
    }

    public String updateApplication(int id, String userDataSnils, int userDataAddId, int workerProfileId,
                                    int socOrganizationOgrrn, String form, String reason, boolean domestic, boolean medical,
                                    boolean psychological, boolean pedagogical, boolean labour, boolean legal, boolean communication,
                                    boolean urgent, String famaly, String living, int income, String status) {
        String request = "UPDATE application SET user_data_snils = ?, user_data_add_ = ?, worker_profile_id = ?, " +
                "soc_organization_ogrrn = ?, form = ?, reason = ?, domestic = ?, medical = ?, psychological = ?, " +
                "pedagogical = ?, labour = ?, legal = ?, communication = ?, urgent = ?, famaly = ?, living = ?, " +
                "income = ?, status = ? WHERE id = ?";
        String answerbd = null;

        try (PreparedStatement prSt = dbConnection.prepareStatement(request)) {
            prSt.setString(1, userDataSnils);
            prSt.setObject(2, userDataAddId, Types.INTEGER);
            prSt.setObject(3, workerProfileId, Types.INTEGER);
            prSt.setInt(4, socOrganizationOgrrn);
            prSt.setString(5, form);
            prSt.setString(6, reason);
            prSt.setBoolean(7, domestic);
            prSt.setBoolean(8, medical);
            prSt.setBoolean(9, psychological);
            prSt.setBoolean(10, pedagogical);
            prSt.setBoolean(11, labour);
            prSt.setBoolean(12, legal);
            prSt.setBoolean(13, communication);
            prSt.setBoolean(14, urgent);
            prSt.setString(15, famaly);
            prSt.setString(16, living);
            prSt.setInt(17, income);
            prSt.setString(18, status);
            prSt.setInt(19, id);

            int rowsUpdated = prSt.executeUpdate();

            if (rowsUpdated > 0) {
                answerbd = "1";
            } else {
                answerbd = "0";
            }

        } catch (SQLException e) {
            answerbd = "0";
            throw new RuntimeException(e);
        }
        finally {
            return answerbd;
        }
    }
}