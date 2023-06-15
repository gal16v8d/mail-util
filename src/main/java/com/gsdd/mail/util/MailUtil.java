package com.gsdd.mail.util;

import com.gsdd.cipher.CipherAlgorithm;
import com.gsdd.cipher.CipherUtil;
import com.gsdd.cipher.DigestAlgorithm;
import com.gsdd.constants.MailConstants;
import com.gsdd.constants.RegexConstants;
import com.gsdd.exception.TechnicalException;
import com.gsdd.mail.util.model.MailBody;
import com.gsdd.mail.util.model.MailConfig;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Great System Development Dynamic (<b>GSDD</b>) <br>
 *     Alexander Galvis Grisales <br>
 *     alex.galvis.sistemas@gmail.com <br>
 */
@AllArgsConstructor
@Getter
@Setter
public class MailUtil {

  private static final Pattern MAIL_PATTERN = Pattern.compile(RegexConstants.MAIL);
  private MailBody body;
  private MailConfig configs;

  public boolean mailEnabled() {
    return this.configs.isFlag();
  }

  private void initProperties() {
    try {
      if (this.configs.getProp() == null) {
        Properties pr = new Properties();
        pr.put(MailConstants.MAIL_PROP_HOST, this.configs.getHost());
        if (this.configs.getSendType().equals(MailConstants.MAIL_TLS)) {
          pr.put(MailConstants.MAIL_PROP_TLS, this.configs.getConnection());
          pr.put(MailConstants.MAIL_PROP_PORT, this.configs.getPort());
        } else {
          pr.put(MailConstants.MAIL_PROP_SFP, this.configs.getPort());
          pr.put(MailConstants.MAIL_PROP_SFC, this.configs.getConnection());
        }
        pr.put(MailConstants.MAIL_PROP_USER, decodeValue(this.body.getMailSender()));
        pr.put(MailConstants.MAIL_PROP_AUTH, this.configs.getAuth());
        this.configs.setProp(pr);
      }
    } catch (Exception e) {
      throw new TechnicalException(e);
    }
  }

  private void initSession() {
    try {
      if (this.configs.getMailSession() == null) {
        this.configs.setMailSession(Session.getDefaultInstance(this.configs.getProp(), null));
        this.configs.getMailSession().setDebug(this.configs.isDebug());
      }
    } catch (Exception e) {
      throw new TechnicalException(e);
    }
  }

  private void initTransport() {
    try {
      if (this.configs.getTr() == null || !this.configs.getTr().isConnected()) {
        this.configs.setTr(
            this.configs.getMailSession().getTransport(MailConstants.MAIL_PROP_TRANSPORT));
        this.configs
            .getTr()
            .connect(
                decodeValue(this.body.getMailSender()), decodeValue(this.body.getPassSender()));
      }
    } catch (Exception e) {
      throw new TechnicalException(e);
    }
  }

  private String decodeValue(String data) {
    return CipherUtil.decode(data, null, DigestAlgorithm.SHA512, CipherAlgorithm.DESEDE);
  }

  private void initMail() {
    try {
      initProperties();
      initSession();
      initTransport();
    } catch (Exception e) {
      throw new TechnicalException(e);
    }
  }

  public void closeTransport() {
    if (this.configs.getTr() != null) {
      try {
        this.configs.getTr().close();
      } catch (MessagingException me) {
        throw new TechnicalException(me);
      }
    }
  }

  public boolean validateMailAddress(String email) {
    return Optional.ofNullable(email)
        .map(
            (String input) -> {
              Matcher matcher = MAIL_PATTERN.matcher(email);
              return matcher.matches();
            })
        .orElse(false);
  }

  public List<InternetAddress> checkAndPrepareRecipients(List<String> recipients) {
    try {
      List<InternetAddress> validAddress = new ArrayList<>();
      if (recipients != null) {
        for (String s : recipients) {
          if ((s != null) && (validateMailAddress(s))) {
            validAddress.add(new InternetAddress((String) s));
          }
        }
      }
      return validAddress;
    } catch (Exception e) {
      throw new TechnicalException(e);
    }
  }

  public boolean sendMail() {
    initMail();
    try {
      if (mailEnabled()) {
        List<InternetAddress> recipients = checkAndPrepareRecipients(this.body.getRecipients());
        if (!recipients.isEmpty()) {
          MimeMessage message = new MimeMessage(this.configs.getMailSession());
          message.setFrom(new InternetAddress(decodeValue(this.body.getMailSender())));
          Address[] addressArray = new Address[recipients.size()];
          addressArray = (Address[]) recipients.toArray(addressArray);
          RecipientType r = getRecipientType();
          message.addRecipients(r, addressArray);
          message.setSubject(this.body.getSubject(), this.getConfigs().getEncoding());
          MimeMultipart mp = new MimeMultipart();
          attachImage(message, mp, this.body.getImage());
          attachFile(
              message,
              mp,
              this.body.getAttachments(),
              this.body.getMessage(),
              this.configs.getEncoding());
          if (!this.configs.getTr().isConnected()) {
            initTransport();
          }
          this.configs.getTr().sendMessage(message, message.getAllRecipients());
          return true;
        }
      }
      return false;
    } catch (Exception e) {
      throw new TechnicalException(e);
    }
  }

  public RecipientType getRecipientType() {
    RecipientType recipientType;
    if (MailConstants.MAIL_RT_CC.equals(this.configs.getType())) {
      recipientType = Message.RecipientType.CC;
    } else if (MailConstants.MAIL_RT_BCC.equals(this.configs.getType())) {
      recipientType = Message.RecipientType.BCC;
    } else {
      recipientType = Message.RecipientType.TO;
    }
    return recipientType;
  }

  public MimeMessage attachImage(MimeMessage message, MimeMultipart mp, String imagePath) {
    try {
      if (imagePath != null) {
        DataSource source = new FileDataSource(imagePath);
        BodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent(
            MailConstants.MAIL_HTML_ITAG + MailConstants.MAIL_IMG_ID + MailConstants.MAIL_HTML_FTAG,
            MailConstants.MAIL_IMG_COD);
        mp.addBodyPart(messageBodyPart);
        messageBodyPart = new MimeBodyPart();
        messageBodyPart.setDataHandler(new DataHandler(source));
        messageBodyPart.setHeader(
            MailConstants.MAIL_HEADER,
            MailConstants.MAIL_HTML_MAYOR
                + MailConstants.MAIL_IMG_ID
                + MailConstants.MAIL_HTML_MENOR);
        mp.addBodyPart(messageBodyPart);
        message.setContent(mp);
      }
      return message;
    } catch (Exception e) {
      throw new TechnicalException(e);
    }
  }

  public MimeMessage attachFile(
      MimeMessage mimeMessage,
      MimeMultipart mp,
      String[] attachments,
      String textMessage,
      String encoding) {
    try {
      if (attachments != null) {
        BodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setText(textMessage);
        mp.addBodyPart(messageBodyPart);
        for (String attach : attachments) {
          File f = new File(attach);
          if (f.exists()) {
            MimeBodyPart attachment = new MimeBodyPart();
            attachment.attachFile(f);
            mp.addBodyPart(attachment);
            mimeMessage.setContent(mp);
          }
        }
        mimeMessage.setContent(mp);
      } else {
        mimeMessage.setText(textMessage, encoding);
      }
      return mimeMessage;
    } catch (Exception e) {
      throw new TechnicalException(e);
    }
  }
}
