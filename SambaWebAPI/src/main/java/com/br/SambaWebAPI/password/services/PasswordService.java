package com.br.SambaWebAPI.password.services;

import com.br.SambaWebAPI.adapter.ProcessBuilderAdapter;
import com.br.SambaWebAPI.user.models.User;
import com.br.SambaWebAPI.password.factory.PasswordCreationFactory;
import com.br.SambaWebAPI.utils.CommandConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.OutputStream;

@Service
public class PasswordService {
    private final ProcessBuilderAdapter processBuilderAdapter;

    @Autowired
    public PasswordService(ProcessBuilderAdapter processBuilderAdapter){
        this.processBuilderAdapter = processBuilderAdapter;
    }
    public boolean createPassword(User user) throws Exception {
        ProcessBuilder  processBuilder = processBuilderAdapter.command(
                CommandConstants.SUDO,
                CommandConstants.SUDO_STDIN,
                CommandConstants.PASSWD_ADD,
                user.getUser()).redirectInput(ProcessBuilder.Redirect.PIPE);

        Process process = processBuilder.start();

        OutputStream outputStream = process.getOutputStream();
        outputStream.write((user.getPassword() + "\n").getBytes());
        outputStream.write((user.getPassword() + "\n").getBytes());
        outputStream.flush();
        outputStream.close();
        process.waitFor();

        int exitCode = process.waitFor();
        if (exitCode!= 0) {
            throw PasswordCreationFactory.createException(exitCode);
        }

        return true;
    }
}
