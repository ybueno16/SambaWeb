package com.br.SambaWebAPI.sambaconfig.controller;

import com.br.SambaWebAPI.config.Global;
import com.br.SambaWebAPI.config.ResponseEntity.DefaultResponseEntityFactory;
import com.br.SambaWebAPI.password.models.SudoAuthentication;
import com.br.SambaWebAPI.sambaconfig.models.SambaConfig;
import com.br.SambaWebAPI.sambaconfig.services.SambaConfigService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Global.API_URL_SAMBA + "/samba-config")
public class SambaConfigController {
  private final ObjectMapper objectMapper;
  private final SambaConfigService sambaConfigService;

  @Autowired
  public SambaConfigController(ObjectMapper objectMapper, SambaConfigService sambaConfigService) {
    this.objectMapper = objectMapper;
    this.sambaConfigService = sambaConfigService;
  }

  @PostMapping(path = "/writeSambaFile")
  public ResponseEntity<?> writeSambaFile(@RequestBody Map<String, Object> json) {
    SambaConfig sambaConfig = objectMapper.convertValue(json.get("sambaConfig"), SambaConfig.class);
    SudoAuthentication sudoAuthentication =
        objectMapper.convertValue(json.get("sudoAuthentication"), SudoAuthentication.class);
    try {
      sambaConfigService.sambaConfigWriteNewConfig(sambaConfig, sudoAuthentication);
      return DefaultResponseEntityFactory.create(
          "Configuração salva com sucesso!", sambaConfig, HttpStatus.OK);

    } catch (IOException e) {
      return DefaultResponseEntityFactory.create(
          "Ocorreu um erro na escrita do arquivo. " + e, null, HttpStatus.INTERNAL_SERVER_ERROR);
    } catch (Exception e) {
      return DefaultResponseEntityFactory.create(
          "Erro genérico. Ocorreu um erro desconhecido durante a escrita no arquivo."
              + Global.SMB_CONF_PATH
              + " "
              + e,
          null,
          HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PatchMapping(path = "/editSambaFile")
  public ResponseEntity<?> editSambaFile(@RequestBody Map<String, Object> json) {
    SambaConfig sambaConfig = objectMapper.convertValue(json.get("sambaConfig"), SambaConfig.class);
    SudoAuthentication sudoAuthentication =
        objectMapper.convertValue(json.get("sudoAuthentication"), SudoAuthentication.class);
    try {
      sambaConfigService.sambaConfigEditConfig(sambaConfig, sudoAuthentication);
      return DefaultResponseEntityFactory.create(
          "Configuração salva com sucesso!", sambaConfig, HttpStatus.OK);

    } catch (IOException e) {
      return DefaultResponseEntityFactory.create(
          "Ocorreu um erro na escrita do arquivo. " + e, null, HttpStatus.INTERNAL_SERVER_ERROR);
    } catch (Exception e) {
      return DefaultResponseEntityFactory.create(
          "Erro genérico. Ocorreu um erro desconhecido durante a escrita no arquivo."
              + Global.SMB_CONF_PATH
              + " "
              + e,
          null,
          HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @DeleteMapping(path = "/sectionParams")
  public ResponseEntity<?> removeSectionParamsSamba(@RequestBody Map<String, Object> json) {
    SambaConfig sambaConfig = objectMapper.convertValue(json.get("sambaConfig"), SambaConfig.class);
    SudoAuthentication sudoAuthentication =
        objectMapper.convertValue(json.get("sudoAuthentication"), SudoAuthentication.class);
    try {
      sambaConfigService.sambaConfigRemoveSectionParams(sambaConfig, sudoAuthentication);
      return DefaultResponseEntityFactory.create(
          "Configuração salva com sucesso!", sambaConfig, HttpStatus.OK);

    } catch (IOException e) {
      return DefaultResponseEntityFactory.create(
          "Ocorreu um erro na escrita do arquivo. " + e, null, HttpStatus.INTERNAL_SERVER_ERROR);
    } catch (Exception e) {
      return DefaultResponseEntityFactory.create(
          "Erro genérico. Ocorreu um erro desconhecido durante a escrita no arquivo."
              + Global.SMB_CONF_PATH
              + " "
              + e,
          null,
          HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @DeleteMapping(path = "/section")
  public ResponseEntity<?> removeSectionSamba(@RequestBody Map<String, Object> json) {
    SambaConfig sambaConfig = objectMapper.convertValue(json.get("sambaConfig"), SambaConfig.class);
    SudoAuthentication sudoAuthentication =
        objectMapper.convertValue(json.get("sudoAuthentication"), SudoAuthentication.class);
    try {
      sambaConfigService.sambaConfigRemoveSection(sambaConfig, sudoAuthentication);
      return DefaultResponseEntityFactory.create(
          "Configuração salva com sucesso!", sambaConfig, HttpStatus.OK);

    } catch (IOException e) {
      return DefaultResponseEntityFactory.create(
          "Ocorreu um erro na escrita do arquivo. " + e, null, HttpStatus.INTERNAL_SERVER_ERROR);
    } catch (Exception e) {
      return DefaultResponseEntityFactory.create(
          "Erro genérico. Ocorreu um erro desconhecido durante a escrita no arquivo."
              + Global.SMB_CONF_PATH
              + " "
              + e,
          null,
          HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
