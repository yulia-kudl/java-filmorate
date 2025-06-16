package ru.yandex.practicum.filmorate.controller;


//здесь нет тестов на валидацию полей, тк они проверяются через аннотации

// // убрала тесты тк неактуально для этого задания. По ТЗ юнит тесты писать не требуется

class UserControllerTest { /*
    private final UserController userController = new UserController();

    @Test
    void addUser() {
        User user = User.builder()
                .name("Elene")
                .login("lena")
                .email("qqq@qqq.qqq")
                .birthday(LocalDate.of(1990, 1,1))
                .build();

        User user1 = userController.addUser(user);
        assertEquals(1, user1.getId(), "incorrect id");
        assertEquals("Elene", user1.getName(), "incorrect name");
        assertEquals(LocalDate.of(1990, 1, 1), user1.getBirthday(), "incorrect date");
        assertEquals("lena", user1.getLogin(), "incorrect login");
        assertEquals("qqq@qqq.qqq", user1.getEmail(), "incorrect email");
        assertEquals(1, userController.findAllUsers().size(), "incorrect users size");
        assertEquals("Elene", userController.findAllUsers().stream().toList().getFirst().getName(), "incorrect name");
    }


    @Test
    void addUserNoNameUser() {
        User user = User.builder()
                .login("lena")
                .email("qqq@qqq.qqq")
                .birthday(LocalDate.of(1990, 1,1))
                .build();

        User user1 = userController.addUser(user);
        assertEquals(1, user1.getId(), "incorrect id");
        assertEquals("lena", user1.getName(), "incorrect name");
        assertEquals(LocalDate.of(1990, 1, 1), user1.getBirthday(), "incorrect date");
        assertEquals("lena", user1.getLogin(), "incorrect login");
        assertEquals("qqq@qqq.qqq", user1.getEmail(), "incorrect email");
        assertEquals(1, userController.findAllUsers().size(), "incorrect users size");
        assertEquals("lena", userController.findAllUsers().stream().toList().getFirst().getName(), "incorrect name");
    }

    @Test
    void updateUserOk() {
        User user = User.builder()
                .name("Elene")
                .login("lena")
                .email("qqq@qqq.qqq")
                .birthday(LocalDate.of(1990, 1,1))
                .build();

        User user1 = userController.addUser(user);

        User user2 = User.builder()
                .name("Elen")
                .login("len")
                .birthday(LocalDate.of(1993, 1,1))
                .id(1)
                .build();

        User user3 = userController.updateUser(user2);

        assertEquals(1, user3.getId(), "incorrect id");
        assertEquals("Elen", user3.getName(), "incorrect name");
        assertEquals(LocalDate.of(1993, 1, 1), user3.getBirthday(), "incorrect date");
        assertEquals("len", user3.getLogin(), "incorrect login");
        assertEquals("qqq@qqq.qqq", user3.getEmail(), "incorrect email");
        assertEquals(1, userController.findAllUsers().size(), "incorrect users size");
        assertEquals("Elen", userController.findAllUsers().stream().toList().getFirst().getName(), "incorrect name");

    }

    @Test
    void updateUserNoId() {
        User user = User.builder()
                .name("Elene")
                .login("lena")
                .email("qqq@qqq.qqq")
                .birthday(LocalDate.of(1990, 1,1))
                .build();

        User user1 = userController.addUser(user);

        User user2 = User.builder()
                .name("Elen")
                .login("len")
                .birthday(LocalDate.of(1993, 1,1))
                .build();

        assertThrows(ValidationException.class, () -> {
            userController.updateUser(user2);
        });
    }

    @Test
        void updateUserIncorrectId() {
            User user = User.builder()
                    .name("Elene")
                    .login("lena")
                    .email("qqq@qqq.qqq")
                    .birthday(LocalDate.of(1990, 1,1))
                    .build();

            User user1 = userController.addUser(user);

            User user2 = User.builder()
                    .name("Elen")
                    .login("len")
                    .birthday(LocalDate.of(1993, 1,1))
                    .id(10)
                    .build();

            assertThrows(ValidationException.class, () -> {
                userController.updateUser(user2);
            });
    } */
}