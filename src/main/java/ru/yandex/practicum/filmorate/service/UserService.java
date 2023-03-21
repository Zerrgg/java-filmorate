package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public User add(User user) {
        return userStorage.add(user);
    }

    public void addFriend(long idUser, long idUserFriend) {
        User user = userStorage.get(idUser);
        User userFriend = userStorage.get(idUserFriend);
        if (!user.getFriendsId().contains(idUserFriend)) {
            user.getFriendsId().add(idUserFriend);
            userFriend.getFriendsId().add(idUser);
            log.info("PUT запрос на добавление друзей обработан.");
        } else {
            throw new UserNotFoundException("Пользователь уже в друзьях");
        }
    }

    public List<User> getAll() {
        return userStorage.getAll();
    }

    public User get(long id) {
        return userStorage.get(id);
    }

    public List<User> getUserFriends(long idUser) {
        return userStorage.getFriends(idUser);
    }

    public List<User> getMutualFriends(long idUser, long idOtherUser) {
        User user = userStorage.get(idUser);
        User otherUser = userStorage.get(idOtherUser);
        List<User> friendsUser = new ArrayList<>(userStorage.getFriends(idUser));
        List<User> friendsOtherUser = new ArrayList<>(userStorage.getFriends(idOtherUser));
        Set<Long> idsUserFriends = user.getFriendsId();
        Set<Long> idsOtherUserFriends = otherUser.getFriendsId();
        if (idsUserFriends.equals(idsOtherUserFriends)) {
            log.info("GET запрос на получение общих друзей обработан. У пользователей все друзья общие");
            return userStorage.getFriends(idUser);
        } else {
            log.info("GET запрос на получение общих друзей обработан.");
            friendsUser.retainAll(friendsOtherUser);
            return friendsUser;
        }
    }

    public User update(User user) {
        return userStorage.update(user);
    }

    public void deleteFriend(long idUser, long idUserFriend) {
        User user = userStorage.get(idUser);
        User userFriend = userStorage.get(idUserFriend);
        user.getFriendsId().remove(idUserFriend);
        userFriend.getFriendsId().remove(idUser);
        log.info("DELETE запрос на удаление друга обработан.");
    }

}