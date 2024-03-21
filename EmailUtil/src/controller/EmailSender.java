package controller;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.ws.rs.core.MediaType;

import model.EmailStructure;
import models.Attachment;


public class EmailSender {

	public void send(EmailStructure emailStructure) {
		
        //String from = "lufthansia@virgilio.it";
        //String[] to = n ew String[] {"fabiofilippo7@gmail.com"};

        String host = "smtp.virgilio.it";
        //int port = 587; porta gmail
        int port = 465;

        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.ssl.enable", "true"); //virgilio

        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
            	//password app gmail = "btww anpw ungp gfrr"
                return new PasswordAuthentication("lufthansia@virgilio.it", "Nmki89ol."); 
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);

            message.setFrom(new InternetAddress(emailStructure.getSender()));
            //destinatario singolo
//            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            
            //destinatario multiplo
            
            String[] recipients = emailStructure.getRecipients();
            InternetAddress[] recipientAddress = new InternetAddress[recipients.length];
            for (int index = 0; index < recipients.length; index++) {
            	recipientAddress[index] = new InternetAddress(recipients[index]);
            }
            message.setRecipients(Message.RecipientType.TO, recipientAddress);
            
            
            message.setSubject(emailStructure.getSubject());

            MimeMultipart multipart = new MimeMultipart();

            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setText(emailStructure.getBody());

            /*
            MimeBodyPart attachmentPart = new MimeBodyPart();
            
            Attachment[] attachments = emailStructure.getAttachments();
            if (null != attachments && attachments.length > 0) {
	            for (Attachment attachment : attachments) {
	            	attachmentPart.setContent(attachment.getContent()
	            							, MediaType.APPLICATION_OCTET_STREAM);
	            	attachmentPart.setFileName(attachment.getName() 
	            							+ attachment.getDocumentType().getExtension());
	            }
	            multipart.addBodyPart(attachmentPart);
            }
			*/

            multipart.addBodyPart(textPart);
//			multipart.addBodyPart(attachmentPart);

            message.setContent(multipart);

            Transport.send(message);
            System.out.println("MAIL MANDATA!");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }
	
	
}