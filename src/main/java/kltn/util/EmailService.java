package kltn.util;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class EmailService {
	@Autowired
	private JavaMailSender emailSender;

	@Autowired
	private Constants constants;

	@Async
	public void sendSimpleMessage(String to, String subject, String text) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom("springboot");
		message.setTo(to);
		message.setSubject(subject);
		message.setText(text);
		emailSender.send(message);

	}

	@Async
	public void sendRegisterMessage(String to, String otp) {
		String toAddress = "thongmap0909310872@gmail.com";
		String content = "<div style=\"font-family: Helvetica,Arial,sans-serif;min-width:1000px;overflow:auto;line-height:2\">\r\n"
				+ "  <div style=\"margin:50px auto;width:70%;padding:20px 0\">\r\n"
				+ "    <div style=\"border-bottom:1px solid #eee\">\r\n"
				+ "      <a href=\"\" style=\"font-size:1.4em;color: #00466a;text-decoration:none;font-weight:600\">[[title]]</a>\r\n"
				+ "    </div>\r\n" + "    <p style=\"font-size:1.1em\">Hi,</p>\r\n"
				+ "    <p>Thank you for registering Hi Shop. Use the following OTP to complete your Sign Up procedures. OTP is valid for 5 minutes</p>\r\n"
				+ "    <h2 style=\"background: #00466a;margin: 0 auto;width: max-content;padding: 0 10px;color: #fff;border-radius: 4px;\">[[otp]]</h2>\r\n"
				+ "    <p style=\"font-size:0.9em;\">Thank you,<br />[[title]]</p>\r\n"
				+ "    <hr style=\"border:none;border-top:1px solid #eee\" />\r\n"
				+ "    <div style=\"float:right;padding:8px 0;color:#aaa;font-size:0.8em;line-height:1;font-weight:300\">\r\n"
				+ "      <p>Hi Shop Inc</p>\r\n" + "      <p>Linh Trung, Thủ Đức, TP.HCM</p>\r\n"
				+ "      <p>Việt Nam</p>\r\n" + "    </div>\r\n" + "  </div>\r\n" + "</div>";

		String fromAddress = constants.getEmailSend();
		String senderName = "Hi Shop Support";
		String subject = "Please verify your registration";
		MimeMessage message = emailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);

		try {
			helper.setFrom(fromAddress, senderName);
			helper.setTo(to);
			helper.setSubject(subject);

			content = content.replace("[[title]]", "Hi Shop");
			content = content.replace("[[otp]]", otp);
			helper.setText(content, true);
			System.out.println("ok");
			emailSender.send(message);

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	@Async
	public void sendOtpMessage(String to, String otp) {
		String toAddress = "thongmap0909310872@gmail.com";
		String content = "<div style=\"font-family: Helvetica,Arial,sans-serif;min-width:1000px;overflow:auto;line-height:2\">\r\n"
				+ "  <div style=\"margin:50px auto;width:70%;padding:20px 0\">\r\n"
				+ "    <div style=\"border-bottom:1px solid #eee\">\r\n"
				+ "      <a href=\"\" style=\"font-size:1.4em;color: #00466a;text-decoration:none;font-weight:600\">[[title]]</a>\r\n"
				+ "    </div>\r\n" + "    <p style=\"font-size:1.1em\">Hi,</p>\r\n"
				+ "    <p>Thank you for using Hi Shop. Use the following OTP to complete your forgot password procedures. OTP is valid for 5 minutes</p>\r\n"
				+ "    <h2 style=\"background: #00466a;margin: 0 auto;width: max-content;padding: 0 10px;color: #fff;border-radius: 4px;\">[[otp]]</h2>\r\n"
				+ "    <p style=\"font-size:0.9em;\">Thank you,<br />[[title]]</p>\r\n"
				+ "    <hr style=\"border:none;border-top:1px solid #eee\" />\r\n"
				+ "    <div style=\"float:right;padding:8px 0;color:#aaa;font-size:0.8em;line-height:1;font-weight:300\">\r\n"
				+ "      <p>Hi Shop Inc</p>\r\n" + "      <p>Linh Trung, Thủ Đức, TP.HCM</p>\r\n"
				+ "      <p>Việt Nam</p>\r\n" + "    </div>\r\n" + "  </div>\r\n" + "</div>";

		String fromAddress = constants.getEmailSend();
		String senderName = "Hi Shop Support";
		String subject = "Please verify your forgot password";
		MimeMessage message = emailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);

		try {
			helper.setFrom(fromAddress, senderName);
			helper.setTo(to);
			helper.setSubject(subject);

			content = content.replace("[[title]]", "Hi Shop");
			content = content.replace("[[otp]]", otp);
			helper.setText(content, true);
			System.out.println("ok");
			emailSender.send(message);

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
