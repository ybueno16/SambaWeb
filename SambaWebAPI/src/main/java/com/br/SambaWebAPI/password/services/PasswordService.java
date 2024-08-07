package com.br.SambaWebAPI.password.services;

import com.br.SambaWebAPI.adapter.ProcessBuilderAdapter;
import com.br.SambaWebAPI.password.factory.PasswordCreationFactory;
import com.br.SambaWebAPI.password.models.SudoAuthentication;
import com.br.SambaWebAPI.user.models.User;
import com.br.SambaWebAPI.utils.CommandConstants;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PasswordService {
  private final ProcessBuilderAdapter processBuilderAdapter;

  @Autowired
  public PasswordService(ProcessBuilderAdapter processBuilderAdapter) {
    this.processBuilderAdapter = processBuilderAdapter;
  }

  public boolean createPassword(User user, SudoAuthentication sudoAuthentication) throws Exception {
    List<Process> processes =
        ProcessBuilder.startPipeline(
            List.of(
                new ProcessBuilder(CommandConstants.ECHO, sudoAuthentication.getSudoPassword())
                    .redirectOutput(ProcessBuilder.Redirect.PIPE),
                new ProcessBuilder(
                        CommandConstants.SUDO,
                        CommandConstants.SUDO_STDIN,
                        CommandConstants.BASH,
                        CommandConstants.EXECUTE_COMMAND,
                        "echo '" + user.getUser() + ":" + user.getPassword() + "' | chpasswd")
                    .redirectInput(ProcessBuilder.Redirect.PIPE)
                    .redirectError(ProcessBuilder.Redirect.INHERIT)));
    for (Process process : processes) {
      int exitCode = process.waitFor();
      if (exitCode != 0) {
        throw PasswordCreationFactory.createException(exitCode);
      }
    }

    return true;
  }
}
