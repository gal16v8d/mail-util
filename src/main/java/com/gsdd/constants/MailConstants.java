package com.gsdd.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @author Great System Development Dynamic (<b>GSDD</b>) <br>
 *     Alexander Galvis Grisales <br>
 *     alex.galvis.sistemas@gmail.com <br>
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MailConstants {

  public static final String MAIL_PROP_TRANSPORT = "smtp";
  public static final String MAIL_RT_CC = "CC";
  public static final String MAIL_RT_BCC = "BCC";
  public static final String MAIL_RT_TO = "TO";
  public static final String MAIL_PROP_HOST = "mail.smtp.host";
  public static final String MAIL_PROP_PORT = "mail.smtp.port";
  public static final String MAIL_PROP_AUTH = "mail.smtp.auth";
  public static final String MAIL_PROP_USER = "mail.smtp.user";
  // TLS send
  public static final String MAIL_PROP_TLS = "mail.smtp.starttls.enable";
  // SSL send
  public static final String MAIL_PROP_SFP = "mail.smtp.socketFactory.port";
  public static final String MAIL_PROP_SFC = "mail.smtp.socketFactory.class";

  public static final String MAIL_HEADER = "Content-ID";
  // id for attached image
  public static final String MAIL_HTML_MAYOR = "<";
  public static final String MAIL_HTML_MENOR = ">";
  public static final String MAIL_IMG_ID = "image";
  public static final String MAIL_HTML_ITAG = "<img src=\"cid:";
  public static final String MAIL_HTML_FTAG = "\">";
  /** Define tipo de codificacion de imagen para procesar el mail. */
  public static final String MAIL_IMG_COD = "text/html";

  // possible properties for mail send
  public static final String MAIL_VALUE_HOST = "mail.smtp.host";
  public static final String MAIL_VALUE_PORT = "mail.smtp.port";
  public static final String MAIL_VALUE_TLS = "mail.smtp.starttls.enable";
  public static final String MAIL_VALUE_SFP = "mail.smtp.sfp";
  public static final String MAIL_VALUE_SFC = "mail.smtp.sfc";
  public static final String MAIL_VALUE_AUTH = "mail.smtp.auth";
  public static final String MAIL_VALUE_USER = "mail.smtp.user";
  public static final String MAIL_VALUE_PASS = "mail.smtp.pass";
  public static final String MAIL_VALUE_TO = "mail.smtp.to";
  public static final String MAIL_RECIPIENT_TYPE = "mail.smtp.rec.type";
  public static final String MAIL_DEBUG = "mail.debug";
  public static final String MAIL_FLAG = "mail.flag";
  public static final String MAIL_TLS = "TLS";
  public static final String MAIL_SSL = "SSL";
}
