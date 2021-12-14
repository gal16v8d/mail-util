package com.gsdd.mail.util.model;

import java.io.Serializable;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author Great System Development Dynamic (<b>GSDD</b>) <br>
 *         Alexander Galvis Grisales <br>
 *         alex.galvis.sistemas@gmail.com <br>
 */
@Getter
@Setter
public class MailBody implements Serializable {

  private static final long serialVersionUID = 1L;
  private String mailSender;
  private String passSender;
  private List<String> recipients;
  private String[] attachments;
  private String image;
  private String message;
  private String subject;

}
