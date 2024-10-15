package com.gsdd.mail.util.model;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Great System Development Dynamic (<b>GSDD</b>) <br>
 *     Alexander Galvis Grisales <br>
 *     alex.galvis.sistemas@gmail.com <br>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MailBody implements Serializable {

  @Serial private static final long serialVersionUID = 1L;
  private String mailSender;
  private String passSender;
  private List<String> recipients;
  private String[] attachments;
  private String image;
  private String message;
  private String subject;
}
