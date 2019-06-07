package actions;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.MimeBodyPart;
import javax.mail.search.FlagTerm;

public class downloadAttachments {

	private String saveDirectory;

	public void setSaveDirectory(String dir) {
		this.saveDirectory = dir;
	}

	public void downloadEmailAttachments(String host, String port, String userName, String password)
			throws IOException {
		Properties properties = new Properties();

		// server setting
		properties.put("mail.pop3.host", host);
		properties.put("mail.pop3.port", port);

		// ssl setting
		properties.setProperty("mail.pop3.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		properties.setProperty("mail.pop3.socketFactory.fallback", "false");
		properties.setProperty("mail.pop3.socketFactory.port", String.valueOf(port));

		Session session = Session.getDefaultInstance(properties);

		try {
			Store store = session.getStore("pop3");
			store.connect(userName, password);

			Folder folderInbox = store.getFolder("INBOX");
			folderInbox.open(Folder.READ_ONLY);

//            Message[] arrayMessages = folderInbox.getMessages();

			Message[] arrayMessages = folderInbox.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));

			if (arrayMessages.length == 0)
				System.out.println("No messages found");
			// System.out.println(x);
			for (int i = 0; i < arrayMessages.length; i++) {
				Message message = arrayMessages[i];
				Address[] fromAddress = message.getFrom();
				String from = fromAddress[0].toString();
				String subject = message.getSubject();
				String sentDate = message.getSentDate().toString();

				String contentType = message.getContentType();
				String messageContent = "";

				// store attachment file name, separated by comma
				String attachFiles = "";

				if (contentType.contains("multipart")) {
					Multipart multiPart = (Multipart) message.getContent();
					int numberOfParts = multiPart.getCount();
					for (int partCount = 0; partCount < numberOfParts; partCount++) {
						MimeBodyPart part = (MimeBodyPart) multiPart.getBodyPart(partCount);
						if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
							// this part is attachment
							String fileName = part.getFileName();
							attachFiles += fileName + ", ";
							part.saveFile(saveDirectory + File.separator + fileName);
						} else {
							// this part may be the message content
							messageContent = part.getContent().toString();
						}
					}

					if (attachFiles.length() > 1) {
						attachFiles = attachFiles.substring(0, attachFiles.length() - 2);
					}
				} else if (contentType.contains("text/plain") || contentType.contains("text/html")) {
					Object content = message.getContent();
					if (content != null) {
						messageContent = content.toString();
					}
				}
				System.out.println("Message #" + (i + 1) + ":");
				System.out.println("\t From: " + from);
				System.out.println("\t Subject: " + subject);
				System.out.println("\t Sent Date: " + sentDate);
				System.out.println("\t Message: " + messageContent);
				System.out.println("\t Attachments: " + attachFiles);
			}
			// disconnect
			folderInbox.close(false);
			store.close();

		} catch (NoSuchProviderException ex) {
			System.out.println("No provider for pop3.");
			ex.printStackTrace();
		} catch (MessagingException ex) {
			System.out.println("Could not connect to the message store");
			ex.printStackTrace();
		}
	}
}
