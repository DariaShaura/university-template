package com.epam.rd.izh.service;

import com.epam.rd.izh.dto.ActiveUserDto;
import com.epam.rd.izh.dto.AuthorizedUserDto;
import com.epam.rd.izh.entity.AuthorizedUser;
import com.epam.rd.izh.exception.IncorrectDataException;
import com.epam.rd.izh.exception.UsersAgeCorrectnessException;
import com.epam.rd.izh.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.epam.rd.izh.exception.UserAlreadyRegisteredException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserServiceMapper implements UserService {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepository;

    @Autowired
    DateTimeFormatter dateTimeFormatter;

    @Autowired
    RoleService roleService;


    @Override
    public AuthorizedUser getAuthorizedUser(AuthorizedUserDto authorizedUserDto) throws IncorrectDataException {

        if(authorizedUserDto.getLogin().equals("") || authorizedUserDto.getPassword().equals("") ||
        authorizedUserDto.getRole().equals("") || authorizedUserDto.getBirthDate().equals("")){
            throw new IncorrectDataException("Есть незаполненные свойства", authorizedUserDto);
        }

        LocalDate birth = LocalDate.parse(authorizedUserDto.getBirthDate(), dateTimeFormatter);
        String role = roleService.getRole(authorizedUserDto.getRole());

        AuthorizedUser authorizedUser = new AuthorizedUser().builder()
                                                .login(authorizedUserDto.getLogin())
                                                .password(passwordEncoder.encode(authorizedUserDto.getPassword()))
                                                .firstName(authorizedUserDto.getFirstName())
                                                .secondName(authorizedUserDto.getSecondName())
                                                .lastName(authorizedUserDto.getLastName())
                                                .birthDate(birth)
                                                .role(role)
                                                .build();

        return authorizedUser;
    }

    @Override
    public AuthorizedUserDto getAuthorizedUserDto(AuthorizedUser authorizedUser){

        AuthorizedUserDto authorizedUserDto = new AuthorizedUserDto().builder()
                                                    .login(authorizedUser.getLogin())
                                                    .password(authorizedUser.getPassword())
                                                    .firstName(authorizedUser.getFirstName())
                                                    .secondName(authorizedUser.getSecondName())
                                                    .lastName(authorizedUser.getLastName())
                                                    .birthDate(dateTimeFormatter.format(authorizedUser.getBirthDate()))
                                                    .role(authorizedUser.getRole())
                                                    .build();
        return authorizedUserDto;
    }

    @Override
    public ActiveUserDto getActiveUserDto(AuthorizedUser authorizedUser){
        ActiveUserDto activeUserDto = new ActiveUserDto().builder()
                                                    .idUser(authorizedUser.getId())
                                                    .login(authorizedUser.getLogin())
                                                    .role(authorizedUser.getRole())
                                                    .firstName(authorizedUser.getFirstName())
                                                    .secondName(authorizedUser.getSecondName())
                                                    .lastName(authorizedUser.getLastName())
                                                    .birthDate(dateTimeFormatter.format(authorizedUser.getBirthDate()))
                                                        .build();

        return activeUserDto;
    }

    @Override
    public AuthorizedUserDto getAuthorizedUserDto(String login){

        AuthorizedUser authorizedUser = userRepository.getUserByLogin(login);

        AuthorizedUserDto authorizedUserDto = getAuthorizedUserDto(authorizedUser);

        return authorizedUserDto;
    }

    @Override
    public boolean addAuthorizedUser(AuthorizedUser authorizedUser)
                    throws UserAlreadyRegisteredException{

        return userRepository.addUser(authorizedUser);
    }

    @Override
    public long getAuthorizedUserId(String login){
        return userRepository.getUserIdByLogin(login);
    }

    @Override
    public String getAuthorizedUserLogin(long id){
        return userRepository.getLoginById(id);
    }

    @Override
    public boolean isLoginAvailable(String login)
                throws UserAlreadyRegisteredException, IncorrectDataException{

        if(login.trim().equals("")){
            throw new IncorrectDataException("Логин не может быть пустым", login);
        }
        boolean isUserInDB = userRepository.IsUserInDB(login);

        if(isUserInDB == true){
            throw new UserAlreadyRegisteredException("Пользователь с таким логином уже зарегистрирован!");
        }

        return !isUserInDB;
    }

    @Override
    public boolean isBirthDateCorrect(String birthDate)
                throws UsersAgeCorrectnessException {

        LocalDate birth = LocalDate.parse(birthDate, dateTimeFormatter);

        if (LocalDate.now().getYear() - birth.getYear() < 15) {
            throw new UsersAgeCorrectnessException("Возраст пользователя должен быть больше 15 лет!");
        }

        return true;
    }

    @Override
    public String getFullUserName(String login){
        return userRepository.getFullUserName(login);
    }

    @Override
    public List<ActiveUserDto> getAllAuthorizedUsers(String login)
        throws SecurityException{
        AuthorizedUserDto authorizedUserDto = getAuthorizedUserDto(login);

        if(!authorizedUserDto.getRole().equals("ADMIN")){
            throw new SecurityException();
        }
        else{
            List<AuthorizedUser> authorizedUserList = userRepository.getAllUsers();

            return authorizedUserList.stream().map(user -> getActiveUserDto(user)).collect(Collectors.toList());
        }
    }

    @Override
    public boolean deleteUser(long idUser){
        return userRepository.deleteUser(idUser);
    }
}
