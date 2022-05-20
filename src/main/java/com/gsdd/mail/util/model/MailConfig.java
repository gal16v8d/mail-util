package com.gsdd.mail.util.model;

import java.io.Serializable;
import java.util.Properties;
import javax.mail.Session;
import javax.mail.Transport;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Great System Development Dynamic (<b>GSDD</b>) <br>
 *     Alexander Galvis Grisales <br>
 *     alex.galvis.sistemas@gmail.com <br>
 */
@Getter
@Setter
public class MailConfig implements Serializable {

  private static final long serialVersionUID = 7861857655738866455L;
  private String host;
  private String port;
  private String type;
  private String sendType;
  private String connection;
  private String auth;
  private String encoding;
  private boolean debug;
  private boolean flag;
  private Properties prop;
  private Session mailSession;
  private Transport tr;
}
