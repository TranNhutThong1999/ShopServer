package kltn.util;

import java.util.Random;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

@Component
public class SMSSender {
	@Value("${otp.sms.account_sid}")
	private String ACCOUNT_SID;

	@Value("${otp.sms.auth_token}")
	private String AUTH_TOKEN = "61e19c4c1d652a8b357d26f13ac5a29d";

	@Value("${otp.sms.phone.from}")
	private String from;

	@PostConstruct
	public void initial() {
		Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
	}

	public void excute(String to, String otp) {
		String format = to.substring(1);
		format = "+84" + format;
		Message.creator(new PhoneNumber(format), // to
				new PhoneNumber(from), // from
				otp).create();
	}

}
