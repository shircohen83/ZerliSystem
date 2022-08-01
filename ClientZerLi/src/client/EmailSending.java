package client;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * this class handle with sending email notiflication to the client , in some cases 
 * we using external lib for this functionality , javax.mail jar and activation jar 
 * @author Dvir Bublil
 *
 */


public class EmailSending {
	
	public static void sendMail(String reciverEmail,String phone,String msg,String title) throws Exception {
		System.out.println("Sending email");
		Properties properties = new Properties();
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.host", "smtp.gmail.com");
		properties.put("mail.smtp.port", "587");
		String myEmail = "zerlig7@gmail.com";
		String password = "rjagipksmgavibdw";
		
		Session session = Session.getInstance(properties, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(myEmail, password);
			}
		});
		
		session.getProperties().put("mail.smtp.ssl.trust", "smtp.gmail.com");
		
		javax.mail.Message message = prepareMessage(session, myEmail,reciverEmail,phone,msg,title);
		try {
		Transport.send(message);
		}
		catch (com.sun.mail.smtp.SMTPSendFailedException e) {
			System.out.println("Sending Email from our company email address failed,we really sory.");
	
			//lets do popup if its dosent work on the simulation
		}
		System.out.println("Email sent successfuly!");
	
		
	}
	
	/**
	 * This method prepares the message that is being sent via email
	 * to the user.
	 * 
	 * @param session
	 * @param myEmail -l.
	 * @param reciever - 
	 * 
	 * @throws MessagingException in case the setFrom() method doesn't work.
	 * @return javax.mail.Message - the message we want to send to the customer
	 */
	private static javax.mail.Message prepareMessage(Session session, String myEmail,String reciever,String phone,String msg,String title) {
		try {
			javax.mail.Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(myEmail));
			message.setRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(reciever));
			message.setSubject(title);
			
			String body = msg;

			message.setText("Email Notiflication from Zerli G7 to account mail:"+reciever+", Phone number: "+phone+"\n"+body+ "\n"+ "Thank you , Zerli G7");
			return message;
		}catch(Exception ex) {
			System.out.println("sending email failed");
		
		}
		return null;
	}
}
