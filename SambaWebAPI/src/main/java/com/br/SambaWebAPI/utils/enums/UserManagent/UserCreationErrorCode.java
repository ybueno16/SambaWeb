package com.br.SambaWebAPI.utils.enums.UserManagent;

import com.br.SambaWebAPI.utils.ErrorCode;
import org.springframework.http.HttpStatus;

public class UserCreationErrorCode extends ErrorCode {
    public static final UserCreationErrorCode CANT_UPDT_PASSWD_FILE = new UserCreationErrorCode("Não foi possível atualizar o arquivo de senha.", HttpStatus.BAD_REQUEST);
    public static final UserCreationErrorCode USR_ALREADY_EXISTS = new UserCreationErrorCode("O User ID já está sendo utilizado por outro usuário.", HttpStatus.CONFLICT);
    public static final UserCreationErrorCode SPECIFIED_GROUP_DONT_EXIST = new UserCreationErrorCode("O Grupo especificado não existe.", HttpStatus.BAD_REQUEST);
    public static final UserCreationErrorCode USR_ALREADY_IN_USE = new UserCreationErrorCode("O usuario já está em uso", HttpStatus.BAD_REQUEST);
    public static final UserCreationErrorCode CANT_UPDT_GROUP_FILE = new UserCreationErrorCode("O arquivo de grupos não pode ser atualizado.", HttpStatus.INTERNAL_SERVER_ERROR);
    public static final UserCreationErrorCode CANT_CREATE_HOME_DIR = new UserCreationErrorCode("Não foi possível criar a pasta home", HttpStatus.INTERNAL_SERVER_ERROR);
    public static final UserCreationErrorCode CANT_CREATE_MAIL_SPOOL = new UserCreationErrorCode("Não foi possível criar a poasta de correio.", HttpStatus.INTERNAL_SERVER_ERROR);
    public static final UserCreationErrorCode CANT_UPDATE_SELINUX = new UserCreationErrorCode("Não foi possível atualizar o mapeamento de usuários do SELinux", HttpStatus.BAD_REQUEST);
    public static final UserCreationErrorCode GENERIC_ERROR = new UserCreationErrorCode("Erro genérico. Ocorreu um erro desconhecido durante a criação do usuário.", HttpStatus.INTERNAL_SERVER_ERROR);




    private UserCreationErrorCode( String errorMessage, HttpStatus httpStatus) {
        super(errorMessage, httpStatus);
    }
}
