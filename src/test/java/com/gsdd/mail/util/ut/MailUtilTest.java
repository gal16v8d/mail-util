package com.gsdd.mail.util.ut;

import com.gsdd.constants.MailConstants;
import com.gsdd.mail.util.MailUtil;
import com.gsdd.mail.util.model.MailBody;
import com.gsdd.mail.util.model.MailConfig;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class MailUtilTest {

  @ParameterizedTest(name = "for email={0} validation should be true")
  @ValueSource(strings = {"im.a.test@gmail.com", "john.doe@gmail.com"})
  void validateMailAddressTrueTest(String email) {
    Assertions.assertTrue(
        new MailUtil(new MailBody(), new MailConfig()).validateMailAddress(email));
  }

  @ParameterizedTest(name = "for email={0} validation should be false")
  @NullAndEmptySource
  @ValueSource(strings = {"test@test@", "@gmail.com"})
  void validateMailAddressFalseTest(String email) {
    Assertions.assertFalse(
        new MailUtil(new MailBody(), new MailConfig()).validateMailAddress(email));
  }

  @ParameterizedTest(name = "for type={0} RecipientType should be {1}")
  @MethodSource("getRecipientTypeArgs")
  void getRecipientTypeTest(String type, RecipientType expected) {
    MailConfig config = new MailConfig();
    config.setType(type);
    RecipientType recipientType = new MailUtil(new MailBody(), config).getRecipientType();
    Assertions.assertEquals(expected, recipientType);
  }

  private static Stream<Arguments> getRecipientTypeArgs() {
    return Stream.of(
        Arguments.of(null, RecipientType.TO),
        Arguments.of(MailConstants.MAIL_RT_CC, RecipientType.CC),
        Arguments.of(MailConstants.MAIL_RT_BCC, RecipientType.BCC));
  }

  @Test
  void checkAndPrepareRecipientsTest() {
    List<String> recipients =
        Arrays.asList(new String[] {"test@test@", "@gmail.com", "algagris@gmail.com", null, ""});
    List<InternetAddress> validAddress =
        new MailUtil(new MailBody(), new MailConfig()).checkAndPrepareRecipients(recipients);
    Assertions.assertEquals(1, validAddress.size());
  }

  @Test
  void checkAndPrepareRecipientsNullInputTest() {
    List<InternetAddress> validAddress =
        new MailUtil(new MailBody(), new MailConfig()).checkAndPrepareRecipients(null);
    Assertions.assertEquals(0, validAddress.size());
  }
}
