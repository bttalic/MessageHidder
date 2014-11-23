

import java.util.Scanner;

public class Message {
	
	/*
	 * the max length is determined by the size of the picture
	 * max length of the message is also determined by the fact that the
	 * largest value of a 8 bit number is 255, and the data about the message length is
	 * encoded in the first 2 bytes of the image 
	 */
	private int maxLength;
	private int messageLength;
	private String message;
	private byte[] messageBytes;
	
	public Message(int maxSize){
		this.maxLength = maxSize > 255 ? 255 : maxSize;
	}
	
	/**
	 * Setting the message from standard input
	 */
	public void setMessage(){
		setMessage(readMessage());
	}
	/**
	 * Sets the message we will encode, making sure the message
	 * size does not exceed the allowed size
	 * @param message the message we want to encode
	 */
	public void setMessage(String message){
		this.message = message;
		this.messageLength = this.message.length();
		while(this.messageLength > this.maxLength){
			System.out.printf("Message exceeds allowed size of %d characters\n", this.maxLength);
			this.message = readMessage();
			this.messageLength = this.message.length();
		}
		encodeMessage();
	}
	
	/**
	 * Reads the message string from standard input
	 * @return String message input by user
	 */
	public String readMessage(){
		Scanner in = new Scanner(System.in);
		System.out.println("Enter the message: ");
		String message = in.nextLine();
		return message;
	}
	
	public int getMessageLength(){
		return this.messageLength;
	}
	
	/**
	 * Turns the string message into byte array
	 */
	private void encodeMessage(){
		this.messageBytes = this.message.getBytes();
	}
	
	/**
	 * 
	 * @return String messageBytes the message as a byte array
	 */
	public byte[] getMessageBytes(){
		return messageBytes;
	}
	
	
}
