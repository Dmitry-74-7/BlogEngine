package main.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Optional;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import main.api.request.RestoreRequest;
import main.api.response.chekResponse.Result;
import main.model.repositories.UsersRepository;
import main.model.tables.Users;
import main.other.RandomGenerator;
import org.springframework.stereotype.Service;

@Service
public class RestoreService {
  private final UsersRepository usersRepository;

  public RestoreService(UsersRepository usersRepository) {
    this.usersRepository = usersRepository;
  }

  public Result restore(RestoreRequest restoreRequest, HttpServletRequest request) {
    String email = restoreRequest.getEmail();
    Optional<Users> user = usersRepository.findByEmail(email);
    if (!user.isPresent()) {
      return new Result(false);
    }

    String pass = sendEmail(email, request);
    Users userFound = user.get();
    userFound.setCode(pass);
    usersRepository.save(userFound);
    return new Result(true);

  }

  private String sendEmail (String email, HttpServletRequest request) {
    String code = RandomGenerator.randomCode(50);
    String fullURL = request.getRequestURL().toString();
    String massage = fullURL.substring(0,fullURL.lastIndexOf(request.getRequestURI())) +
        "/login/change-password/" + code;

    Properties properties = new Properties();

    try {
      properties.load(new FileInputStream(System.getProperty("user.dir") + "/src/main/resources/mail.properties"));
    } catch (IOException e) {
      e.printStackTrace();
    }

    Session session = Session.getDefaultInstance(properties, new Authenticator() {
      @Override
      protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(properties.getProperty("userName"), properties.getProperty("password"));
      }}); // default session

    try {
      MimeMessage message = new MimeMessage(session); // email message

      message.setFrom(new InternetAddress(properties.getProperty("userName"), "DevPub")); // setting header fields

      message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));

      message.setSubject("Авторизация при потерянном пароле"); // subject line

      message.setContent("<a href= " + massage + ">" + massage, "text/html");
    //  message.setText("Чтобы зайти, пройдите по ссылке " + massage, );

      // Send message
      Transport.send(message); System.out.println("Email Sent successfully....");
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
      System.out.println("Ошибка отправки email");
    } catch (MessagingException mex){ mex.printStackTrace();
      System.out.println("Ошибка отправки email");
    }
    return code;

  }
}
