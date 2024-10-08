package com.br.SambaWebAPI.group.services;

import com.br.SambaWebAPI.adapter.ProcessBuilderAdapter;
import com.br.SambaWebAPI.group.enums.AddUserToGroupErrorCode;
import com.br.SambaWebAPI.group.enums.CreateGroupErrorCode;
import com.br.SambaWebAPI.group.enums.DeleteGroupErrorCode;
import com.br.SambaWebAPI.group.exceptions.AddUserToGroupException;
import com.br.SambaWebAPI.group.exceptions.CreateGroupException;
import com.br.SambaWebAPI.group.exceptions.DeleteGroupException;
import com.br.SambaWebAPI.group.models.Group;
import com.br.SambaWebAPI.password.models.SudoAuthentication;
import com.br.SambaWebAPI.user.models.User;
import com.br.SambaWebAPI.utils.CommandConstants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.OutputStream;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

class GroupServiceTest {

    @Mock
    private SudoAuthentication sudoAuthentication;


    @Mock
    private User user;

    @Mock
    private Group group;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        sudoAuthentication.setSudoPassword("sudo_password");
        user.setUser("user_name");
        group.setName("group_name");

    }

    @Test
    @DisplayName("""
                Given a group creation process,
                When you create the group successfully,
                then it should return true
            """)
    void createGroup() throws Exception {
        when(sudoAuthentication.getSudoPassword()).thenReturn("sudo_password");
        when(user.getUser()).thenReturn("user_name");
        when(user.getUser()).thenReturn("group_name");


        String[] commandArgs = new String[] {
                CommandConstants.SUDO,
                CommandConstants.SUDO_STDIN,
                CommandConstants.GROUP_ADD,
                group.getName()
        };

        ProcessBuilderAdapter processBuilderAdapter = Mockito.mock(ProcessBuilderAdapter.class);

        ProcessBuilder processBuilder = Mockito.mock(ProcessBuilder.class);
        when(processBuilderAdapter.command(commandArgs)).thenReturn(processBuilder);

        Process process = Mockito.mock(Process.class);
        when(processBuilder.start()).thenReturn(process);

        when(process.getOutputStream()).thenReturn(mock(OutputStream.class));

        GroupService groupService = new GroupService(processBuilderAdapter);

        boolean result = groupService.createGroup(group,sudoAuthentication);

        assertTrue(result);

        verify(processBuilderAdapter, times(1)).command(commandArgs);
        verify(processBuilder, times(1)).start();
        verify(process, times(2)).waitFor();
    }


    @Test
    @DisplayName("""
                Given a group creation process with different exit codes,
                When creating the group,
                then it should throw exception with correct error code
            """)
    public void createGRoupFailWithDifferentErrorCodes() throws Exception {
        when(sudoAuthentication.getSudoPassword()).thenReturn("sudo_password");
        when(user.getUser()).thenReturn("user_name");
        when(user.getUser()).thenReturn("group_name");


        String[] commandArgs = new String[] {
                CommandConstants.SUDO,
                CommandConstants.SUDO_STDIN,
                CommandConstants.GROUP_ADD,
                group.getName()
        };

        ProcessBuilderAdapter processBuilderAdapter = Mockito.mock(ProcessBuilderAdapter.class);
        ProcessBuilder processBuilder = Mockito.mock(ProcessBuilder.class);
        when(processBuilderAdapter.command(commandArgs)).thenReturn(processBuilder);

        Process process = Mockito.mock(Process.class);
        when(processBuilder.start()).thenReturn(process);
        when(process.getOutputStream()).thenReturn(mock(OutputStream.class));

        GroupService groupService = new GroupService(processBuilderAdapter);

        int[] exitCodes = new int[] {
                4,9,10
        };

        CreateGroupErrorCode[] errorCodes = new CreateGroupErrorCode[] {
                CreateGroupErrorCode.GID_ALREADY_EXISTS,
                CreateGroupErrorCode.GROUP_NAME_NOT_UNIQUE,
                CreateGroupErrorCode.CANT_UPDT_GROUP_FILE,
        };

        for (int i = 0; i < exitCodes.length; i++) {
            when(process.waitFor()).thenReturn(exitCodes[i]);
            try {
                groupService.createGroup(group,sudoAuthentication);
                Assertions.fail("Should have thrown a custom exception");
            } catch (CreateGroupException e) {
                Assertions.assertEquals(errorCodes[i], e.getErrorCode());
            }


        }

        when(process.waitFor()).thenReturn(999);
        try {
            groupService.createGroup(group,sudoAuthentication);
            Assertions.fail("Should have thrown a exception");
        } catch (CreateGroupException e) {
            Assertions.assertEquals(CreateGroupErrorCode.GENERIC_ERROR, e.getErrorCode());
        }

        verify(processBuilderAdapter, times(exitCodes.length + 1)).command(commandArgs);
        verify(processBuilder, times(exitCodes.length + 1)).start();
        verify(process, times((exitCodes.length + 1) * 2)).waitFor();
    }

    @Test
    @DisplayName("""
                Given a process of adding the user to a group,
                When you successfully add the user to the group,
                then it should return true
            """)
    void addUserToGroup() throws Exception {
        when(sudoAuthentication.getSudoPassword()).thenReturn("sudo_password");
        when(user.getUser()).thenReturn("user_name");
        when(user.getUser()).thenReturn("group_name");


        String[] commandArgs = new String[] {
                CommandConstants.SUDO,
                CommandConstants.SUDO_STDIN,
                CommandConstants.USER_MOD,
                CommandConstants.ADD_GROUP_OPTION,
                group.getName(),
                user.getUser()
        };

        ProcessBuilderAdapter processBuilderAdapter = Mockito.mock(ProcessBuilderAdapter.class);

        ProcessBuilder processBuilder = Mockito.mock(ProcessBuilder.class);
        when(processBuilderAdapter.command(commandArgs)).thenReturn(processBuilder);

        Process process = Mockito.mock(Process.class);
        when(processBuilder.start()).thenReturn(process);

        when(process.getOutputStream()).thenReturn(mock(OutputStream.class));

        GroupService groupService = new GroupService(processBuilderAdapter);

        boolean result = groupService.addUserToGroup(group,user,sudoAuthentication);

        assertTrue(result);

        verify(processBuilderAdapter, times(1)).command(commandArgs);
        verify(processBuilder, times(1)).start();
        verify(process, times(1)).waitFor();
    }

    @Test
    @DisplayName("""
                Given a process of adding a user to the group with different exit codes,
                When adding the user to the group,
                then it should throw exception with correct error code
            """)
    public void addUsertoGoupFailWithDifferentErrorCodes() throws Exception {
        when(sudoAuthentication.getSudoPassword()).thenReturn("sudo_password");
        when(user.getUser()).thenReturn("user_name");
        when(user.getUser()).thenReturn("group_name");


        String[] commandArgs = new String[] {
                CommandConstants.SUDO,
                CommandConstants.SUDO_STDIN,
                CommandConstants.USER_MOD,
                CommandConstants.ADD_GROUP_OPTION,
                group.getName(),
                user.getUser()
        };

        ProcessBuilderAdapter processBuilderAdapter = Mockito.mock(ProcessBuilderAdapter.class);
        ProcessBuilder processBuilder = Mockito.mock(ProcessBuilder.class);
        when(processBuilderAdapter.command(commandArgs)).thenReturn(processBuilder);

        Process process = Mockito.mock(Process.class);
        when(processBuilder.start()).thenReturn(process);
        when(process.getOutputStream()).thenReturn(mock(OutputStream.class));

        GroupService groupService = new GroupService(processBuilderAdapter);

        int[] exitCodes = new int[] {
                4,6,8,9,10,11,12
        };

        AddUserToGroupErrorCode[] errorCodes = new AddUserToGroupErrorCode[] {
                AddUserToGroupErrorCode.UID_ALREADY_EXISTS,
                AddUserToGroupErrorCode.LOGIN_DOES_NOT_EXIST,
                AddUserToGroupErrorCode.LOGIN_IN_USE,
                AddUserToGroupErrorCode.NEW_LOGNAME_ALREADY_EXISTS,
                AddUserToGroupErrorCode.CANT_UPDATE_GROUP_DB,
                AddUserToGroupErrorCode.INSUFFICIENT_SPACE,
                AddUserToGroupErrorCode.CANT_MOVE_HOME_DIR,
        };

        for (int i = 0; i < exitCodes.length; i++) {
            when(process.waitFor()).thenReturn(exitCodes[i]);
            try {
                groupService.addUserToGroup(group,user,sudoAuthentication);
                Assertions.fail("Should have thrown a custom exception");
            } catch (AddUserToGroupException e) {
                Assertions.assertEquals(errorCodes[i], e.getErrorCode());
            }
        }

        when(process.waitFor()).thenReturn(999);
        try {
            groupService.addUserToGroup(group,user,sudoAuthentication);
            Assertions.fail("Should have thrown a exception");
        } catch (AddUserToGroupException e) {
            Assertions.assertEquals(AddUserToGroupErrorCode.GENERIC_ERROR, e.getErrorCode());
        }

        verify(processBuilderAdapter, times(exitCodes.length + 1)).command(commandArgs);
        verify(processBuilder, times(exitCodes.length + 1)).start();
        verify(process, times((exitCodes.length + 1))).waitFor();
    }

    @Test
    @DisplayName("""
                Given a group removal process,
                when remove the group successfully,
                then it should return true
            """)
    void deleteUserToGroup() throws Exception {
        when(sudoAuthentication.getSudoPassword()).thenReturn("sudo_password");
        when(user.getUser()).thenReturn("user_name");
        when(user.getUser()).thenReturn("group_name");


        String[] commandArgs = new String[] {
                CommandConstants.SUDO,
                CommandConstants.SUDO_STDIN,
                CommandConstants.GROUP_DEL,
                group.getName()
        };

        ProcessBuilderAdapter processBuilderAdapter = Mockito.mock(ProcessBuilderAdapter.class);

        ProcessBuilder processBuilder = Mockito.mock(ProcessBuilder.class);
        when(processBuilderAdapter.command(commandArgs)).thenReturn(processBuilder);

        Process process = Mockito.mock(Process.class);
        when(processBuilder.start()).thenReturn(process);

        when(process.getOutputStream()).thenReturn(mock(OutputStream.class));

        GroupService groupService = new GroupService(processBuilderAdapter);

        boolean result = groupService.deleteGroup(group,sudoAuthentication);

        assertTrue(result);

        verify(processBuilderAdapter, times(1)).command(commandArgs);
        verify(processBuilder, times(1)).start();
        verify(process, times(1)).waitFor();
    }

    @Test
    @DisplayName("""
                Given a process of adding a user to the group with different exit codes,
                When adding the user to the group,
                then it should throw exception with correct error code
            """)
    public void deleteGroupFailWithDifferentErrorCodes() throws Exception {
        when(sudoAuthentication.getSudoPassword()).thenReturn("sudo_password");
        when(user.getUser()).thenReturn("user_name");
        when(user.getUser()).thenReturn("group_name");


        String[] commandArgs = new String[] {
                CommandConstants.SUDO,
                CommandConstants.SUDO_STDIN,
                CommandConstants.GROUP_DEL,
                group.getName()
        };

        ProcessBuilderAdapter processBuilderAdapter = Mockito.mock(ProcessBuilderAdapter.class);
        ProcessBuilder processBuilder = Mockito.mock(ProcessBuilder.class);
        when(processBuilderAdapter.command(commandArgs)).thenReturn(processBuilder);

        Process process = Mockito.mock(Process.class);
        when(processBuilder.start()).thenReturn(process);
        when(process.getOutputStream()).thenReturn(mock(OutputStream.class));

        GroupService groupService = new GroupService(processBuilderAdapter);

        int[] exitCodes = new int[] {
                6,8,10
        };

        DeleteGroupErrorCode[] errorCodes = new DeleteGroupErrorCode[] {
                DeleteGroupErrorCode.GROUP_DOESNT_EXIST,
                DeleteGroupErrorCode.CANT_REMOVE_PRIMARY_GROUP,
                DeleteGroupErrorCode.CANT_UPDT_GROUP_FILE,
        };

        for (int i = 0; i < exitCodes.length; i++) {
            when(process.waitFor()).thenReturn(exitCodes[i]);
            try {
                groupService.deleteGroup(group,sudoAuthentication);
                Assertions.fail("Should have thrown a custom exception");
            } catch (DeleteGroupException e) {
                Assertions.assertEquals(errorCodes[i], e.getErrorCode());
            }
        }

        when(process.waitFor()).thenReturn(999);
        try {
            groupService.deleteGroup(group,sudoAuthentication);
            Assertions.fail("Should have thrown a exception");
        } catch (DeleteGroupException e) {
            Assertions.assertEquals(DeleteGroupErrorCode.GENERIC_ERROR, e.getErrorCode());
        }

        verify(processBuilderAdapter, times(exitCodes.length + 1)).command(commandArgs);
        verify(processBuilder, times(exitCodes.length + 1)).start();
        verify(process, times((exitCodes.length + 1))).waitFor();
    }
}