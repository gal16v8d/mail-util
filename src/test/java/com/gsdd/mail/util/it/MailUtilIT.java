package com.gsdd.mail.util.it;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import com.gsdd.mail.util.MailUtil;
import com.gsdd.mail.util.model.MailBody;
import com.gsdd.mail.util.model.MailConfig;

class MailUtilIT {

  private static final String UTF_8 = "UTF-8";
  private static final String MEME_JPG = "meme.jpg";
  private static final String LOG4J2_XML = "log4j2.xml";
  private static final List<String> RECIPIENTS = new ArrayList<>();
  static {
    RECIPIENTS.add(System.getenv("RECIPIENT_1"));
    RECIPIENTS.add(System.getenv("RECIPIENT_2"));
  }
  private static final String TEST_MSG = "Test mail-util";
  private static final String HOST_NAME = "smtp.gmail.com";
  private static final String STRING_TRUE = "true";

  @Test
  void testMailSSLSend() {
    File meme = new File(getClass().getClassLoader().getResource(MEME_JPG).getFile());
    File attachment = new File(getClass().getClassLoader().getResource(LOG4J2_XML).getFile());
    MailUtil testMU = new MailUtil(
        createMailBody(meme.getAbsolutePath(), attachment.getAbsolutePath()), sslMailConfig());
    Assertions.assertTrue(testMU.sendMail());
  }

  @Test
  void testMailTLSSend() {
    MailUtil testMU = new MailUtil(createMailBody(null), tlsMailConfig());
    Assertions.assertTrue(testMU.sendMail());
  }

  private MailBody createMailBody(String imgPath, String... attachments) {
    MailBody inputMO = new MailBody();
    inputMO.setMailSender(System.getenv("SENDER"));
    inputMO.setPassSender(System.getenv("CRED"));
    inputMO.setRecipients(RECIPIENTS);
    inputMO.setImage(imgPath);
    inputMO.setSubject(TEST_MSG);
    inputMO.setMessage(TEST_MSG);
    inputMO.setAttachments(attachments);
    return inputMO;
  }

  private MailConfig sslMailConfig() {
    MailConfig inputMS = new MailConfig();
    inputMS.setHost(HOST_NAME);
    inputMS.setSendType("SSL");
    inputMS.setConnection("javax.net.ssl.SSLSocketFactory");
    inputMS.setAuth(STRING_TRUE);
    inputMS.setType("BCC");
    inputMS.setPort("465");
    inputMS.setEncoding(UTF_8);
    inputMS.setDebug(true);
    inputMS.setFlag(true);
    return inputMS;
  }

  private MailConfig tlsMailConfig() {
    MailConfig inputMS = new MailConfig();
    inputMS.setHost(HOST_NAME);
    inputMS.setSendType("TLS");
    inputMS.setConnection(STRING_TRUE);
    inputMS.setAuth(STRING_TRUE);
    inputMS.setType("CC");
    inputMS.setPort("587");
    inputMS.setEncoding(UTF_8);
    inputMS.setDebug(false);
    inputMS.setFlag(true);
    return inputMS;
  }

}
