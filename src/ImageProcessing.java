
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;


import javax.imageio.ImageIO;

public class ImageProcessing {

	private String path;
	private String newFileName;
	private byte[] imageBytes;
	private BufferedImage originalImage;
	private int bitSize;
	private String extension;
	private int offset;
	
	/**
	 * Sets the values for class variables
	 * @param path path to the image file we will process
	 * @throws IOException in case the file is not readable or does not exist
	 */
	public ImageProcessing(String path) throws IOException {
		// retrieve file from path
		File imageFile = new File(path);
		this.originalImage = ImageIO.read(imageFile);
		WritableRaster raster = originalImage.getRaster();
		DataBufferByte data = (DataBufferByte) raster.getDataBuffer();
		this.imageBytes = data.getData();
		this.extension = path.substring(path.lastIndexOf(".") + 1);
		this.newFileName = path.substring(path.lastIndexOf("\\") + 1,
				path.lastIndexOf("."))
				+ "_edited" + "." + extension;
		this.path = path.substring(0, path.lastIndexOf("\\") + 1);
		this.offset = 0;
		this.bitSize = 7;
	}

	/**
	 * Returns the path to the file
	 * @return String path
	 */
	public String getPath() {
		return path;
	}
	
	/**
	 * Returns the name of the image with the hidden message
	 * the new name will be <original_name>_edited.<extension>
	 * @return String newFileName
	 */
	public String getNewFileName() {
		return newFileName;
	}

	/**
	 * encodes a single integer value in the image bytes
	 * @param value
	 */
	private void setBit(int value) {
		for (int bit = bitSize; bit >= 0; bit--, offset++) {
			// get the next bit
			int b = (value >>> bit) & 1;
			// make sure the last bit of the current image byte is 0
			// then set it to the current messageLength bit
			imageBytes[offset] = (byte) ((imageBytes[offset] & 0xFE) | b);
		}
	}
	
	/**
	 * Encode the byte array message into the image
	 * @param messageBytes
	 * @throws IOException in case we are not able to save the new file
	 */
	public void encodeMessage(String message) throws IOException{
		
		Message myMessage = new Message(this.getImageByteSize());
		myMessage.setMessage(message);
		setMessage(myMessage.getMessageBytes());
		saveChanges();
	}
	/**
	 * Decodes the message from the given image and returns it as a string
	 * @return String the message
	 */
	public String decodeMessage(){
		return getMessage();
	}

	/**
	 * encodes the message length information in the image bytes
	 * @param messageLength
	 */
	private void setMessageLength(int messageLength) {
		setBit(messageLength);
	}
	
	/**
	 * Encodes the message into the image bytes
	 * @param messageBytes the message in byte form
	 */
	private void setMessage(byte[] messageBytes) {
		setMessageLength(messageBytes.length);
		for (int i = 0; i < messageBytes.length; i++) {
			int current = messageBytes[i];
			setBit(current);
		}
	}

	/**
	 * Retrieve data on the message length
	 * so we don't get random string values later on
	 * @return
	 */
	private int getMessageLength() {
		int messageLength = 0;
			for (int bit = bitSize; bit >= 0; bit--, offset++) {
				byte thisByte = (byte) (imageBytes[offset] & 0x01);
				messageLength = (byte) ((messageLength << 1) | thisByte);
		}
			//if the value has exceeded 8 bits we want to shorten it back
			messageLength = (messageLength & 0xff);
		return messageLength;
	}

	/**
	 * Extracts the message from the image file
	 * @return String message
	 */
	private String getMessage() {
		int messageLength = getMessageLength();
		byte[] message = new byte[messageLength];
		for (int i = 0; i < messageLength; i++) {
			for (int bit = bitSize; bit >= 0; bit--, offset++) {
				//making sure we get only the last bit of the byte
				byte thisByte = (byte) (imageBytes[offset] & 0x01);
				message[i] = (byte) ((message[i] << 1) | thisByte);
			}
		}
		return new String(message);
	}

	/**
	 * Prints data about the image
	 * size, how many bytes it has and how many characters we can store
	 */
	public void printImageData() {
		System.out.println("Image width x height: "
				+ this.originalImage.getWidth() + " x "
				+ this.originalImage.getHeight());
		System.out.println("Image bytes length: " + this.imageBytes.length);
		System.out.println("The image can hold: "
				+ (getImageByteSize() > 255 ? 255 : getImageByteSize() ) + " chars");
	}

	private int getImageByteSize() {
		/*
		 * the first 8 bytes hold data about the message size each byte of the
		 * image holds only 1 bit
		 */
		return (imageBytes.length / (bitSize + 1) - 2);
	}

	/**
	 * Saving the changes to the image file
	 * @throws IOException
	 */
	private void saveChanges() throws IOException {
		ImageIO.write(this.originalImage, extension, new File(this.path,
				this.newFileName));
	}

}
