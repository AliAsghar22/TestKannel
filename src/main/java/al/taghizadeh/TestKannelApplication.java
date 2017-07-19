package al.taghizadeh;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.UnsupportedEncodingException;
import java.util.Scanner;

@SpringBootApplication
public class TestKannelApplication {
	private static String smsBoxUrl = "localhost";
	private static String smsBoxPort = "13013";

	final static Logger logger = LoggerFactory.getLogger(TestKannelApplication.class);
	final static Scanner scanner = new Scanner(System.in);

	public static void main(String[] args){
		SpringApplication.run(TestKannelApplication.class, args);


		logger.info("Welcom to Testing kannel");

		while (true) {
			logger.info("Select from menu:");
			logger.info("1.send message");
			logger.info("2.exit");

			String select = scanner.nextLine();
			switch (select) {
				case "1":
					sendMessage();
					break;
				case "2":
					System.exit(0);
			}
		}


	}

	private static void sendMessage() {
		MessageSender sender = new MessageSender();
		logger.info("please insert sim count");
		int simcount = Integer.valueOf(scanner.nextLine());
		logger.info("please insert test id:");
		String testId = scanner.nextLine();
		logger.info("please insert messages count:");
		int messageCount = Integer.valueOf(scanner.nextLine());
		logger.info("please insert message body:");
		String messageBody = scanner.nextLine();
		logger.info("please insert receivers seperated by comma");
		String [] receivers = scanner.nextLine().split(",");
		logger.info("should insert test id in message?(true, false)");
		boolean insertTestId = Boolean.parseBoolean(scanner.nextLine());
		logger.info("should insert message counter?(true, false)");
		boolean insertCounter = Boolean.parseBoolean(scanner.nextLine());
		String prefix = null;
		try {
			for (int i = 1; i <= messageCount; i++) {
				for (int j = 1; j <= receivers.length; j++) {
					if (insertTestId && insertCounter) {
						prefix = testId + " " + i + " ";
					} else if (insertTestId) {
						prefix = testId + " ";
					} else if (insertCounter) {
						prefix = i + " ";
					} else {
						prefix = null;
					}
					sender.send(smsBoxUrl, smsBoxPort, receivers[j-1], prefix + messageBody, 0, sender.getDlrUrl(i + "-" + j), null, simcount);
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
}
