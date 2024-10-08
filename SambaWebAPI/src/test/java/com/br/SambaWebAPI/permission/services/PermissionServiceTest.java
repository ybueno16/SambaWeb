package com.br.SambaWebAPI.permission.services;

import com.br.SambaWebAPI.adapter.ProcessBuilderAdapter;
import com.br.SambaWebAPI.folder.models.Folder;
import com.br.SambaWebAPI.folder.services.FolderService;
import com.br.SambaWebAPI.password.models.SudoAuthentication;
import com.br.SambaWebAPI.permission.exceptions.PermissionAddException;
import com.br.SambaWebAPI.permission.models.GroupPermission;
import com.br.SambaWebAPI.permission.models.OwnerPermission;
import com.br.SambaWebAPI.permission.models.PublicPermission;
import com.br.SambaWebAPI.utils.PermissionCodeCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

class PermissionServiceTest {
    @Mock
    private SudoAuthentication sudoAuthentication;

    @Mock
    private Folder folder;

    @Mock
    private OwnerPermission ownerPermission;

    @Mock
    private PublicPermission publicPermission;

    @Mock
    private GroupPermission groupPermission;

    @Mock
    ProcessBuilderAdapter processBuilderAdapter;

    @Mock
    PermissionCodeCalculator permissionCodeCalculator;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("""
                Given a permission calculation process,
                When you successfully calculate the permission correctly,
                then it must return the permission value
            """)
    void chmodCalculator() throws IOException, InterruptedException {
        FolderService folderService = Mockito.mock(FolderService.class);
        Mockito.when(folderService.getHomeDir()).thenReturn("/home");

        PermissionCodeCalculator permissionCodeCalculator = new PermissionCodeCalculator();
        PermissionService permissionService = new PermissionService(processBuilderAdapter, folderService,permissionCodeCalculator);

        OwnerPermission ownerPermission = new OwnerPermission();
        ownerPermission.setExecute(1);
        ownerPermission.setRead(1);
        ownerPermission.setWrite(1);

        GroupPermission groupPermission = new GroupPermission();
        groupPermission.setExecute(1);
        groupPermission.setRead(1);
        groupPermission.setWrite(1);

        PublicPermission publicPermission = new PublicPermission();
        publicPermission.setExecute(1);
        publicPermission.setRead(1);
        publicPermission.setWrite(1);

        assertEquals("777", permissionService.chmodCalculator(ownerPermission, groupPermission, publicPermission));
    }

    @Test
    @DisplayName("""
                Given a process of giving permission to a folder,
                When you successfully complete the process of giving permission to a folder,
                then it should return true
               """)
    void managePermission() throws Exception, PermissionAddException {
        when(sudoAuthentication.getSudoPassword()).thenReturn("sudo_password");
        when(folder.getPath()).thenReturn("/foo/bar");

        ProcessBuilderAdapter processBuilderAdapter = Mockito.mock(ProcessBuilderAdapter.class);
        ProcessBuilder processBuilder = Mockito.mock(ProcessBuilder.class);
        Process process = Mockito.mock(Process.class);

        when(processBuilderAdapter.command(any(String[].class))).thenReturn(processBuilder);
        when(processBuilder.start()).thenReturn(process);

        InputStream inputStream = new ByteArrayInputStream("home_dir".getBytes());
        when(process.getInputStream()).thenReturn(inputStream);
        when(process.getOutputStream()).thenReturn(mock(OutputStream.class));
        when(process.waitFor()).thenReturn(0);

        FolderService folderService = new FolderService(processBuilderAdapter);

        PermissionService permissionService = new PermissionService(processBuilderAdapter, folderService,permissionCodeCalculator);

        boolean result = permissionService.managePermission(ownerPermission, groupPermission, publicPermission, sudoAuthentication, folder);

        assertTrue(result);

        verify(processBuilderAdapter, times(4)).command(any(String[].class));
    }
}