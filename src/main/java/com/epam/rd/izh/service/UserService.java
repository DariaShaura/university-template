package com.epam.rd.izh.service;

import com.epam.rd.izh.dto.ActiveUserDto;
import com.epam.rd.izh.dto.AuthorizedUserDto;
import com.epam.rd.izh.entity.AuthorizedUser;
import com.epam.rd.izh.exception.IncorrectDataException;
import com.epam.rd.izh.exception.UserAlreadyRegisteredException;
import com.epam.rd.izh.exception.UsersAgeCorrectnessException;

import java.util.List;
import java.util.Map;

public interface UserService {
    AuthorizedUser getAuthorizedUser(AuthorizedUserDto registeredUserDto) throws IncorrectDataException;

    AuthorizedUserDto getAuthorizedUserDto(String login);

    AuthorizedUserDto getAuthorizedUserDto(AuthorizedUser authorizedUser);

    ActiveUserDto getActiveUserDto(AuthorizedUser authorizedUser);

    boolean addAuthorizedUser(AuthorizedUser authorizedUser) throws UserAlreadyRegisteredException;

    long getAuthorizedUserId(String login);

    String getAuthorizedUserLogin(long id);

    boolean isLoginAvailable(String login) throws UserAlreadyRegisteredException, IncorrectDataException;

    boolean isBirthDateCorrect(String birthDate)  throws UsersAgeCorrectnessException;

    String getFullUserName(String login);

    List<ActiveUserDto> getAllAuthorizedUsers(String login) throws SecurityException;

    boolean deleteUser(long idUser);
}
