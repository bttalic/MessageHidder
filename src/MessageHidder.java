import java.io.IOException;
import java.util.Scanner;

/**
 * Since only the first 2 pixels of the image are reserved for the data 
 * on the message length the maximum length of the message is 255 characters
 * 2px * 4 bytes = 8 bytes, but we only change the least significant bit of each byte
 * @author bttalic
 *
 */
public class MessageHidder {
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String path="";
		String message="";
		int encodeOrDecode = 1;
		
		
		if( args.length < 1 || (args.length < 3 && args[0].equals(1) )){
			Scanner in = new Scanner(System.in);
			System.out.println("You have not provided the required arguments");
			System.out.println("Enter 1 for message encoding 2 otherwise");
			encodeOrDecode = Integer.parseInt(in.nextLine());
			System.out.println("Enter path:");
			path = in.nextLine();
			System.out.println("Path: " + path);
			if (encodeOrDecode == 1) {
				System.out.println("Enter message: ");
				message = in.nextLine();
			}
		} else {
			encodeOrDecode = Integer.parseInt(args[0]);
			path = args[1];
			if (encodeOrDecode == 1) {
				message = args[2];
			}
		}
	
		try {
			if (encodeOrDecode == 1) {
				ImageProcessing myImage = new ImageProcessing(path);
				myImage.printImageData();
				System.out.println("Your edited image will be called: "
						+ myImage.getNewFileName());
				System.out.println("And can be found at: " + myImage.getPath());
				System.out.println("Encoding...");
				
				myImage.encodeMessage(message);
			} else {

				System.out.println("Decoding...");
				ImageProcessing myEditedImage = new ImageProcessing(path);
				System.out.println(myEditedImage.decodeMessage());
			}

		} catch (IOException e) {
			System.out.println("Error: " + e.getMessage());
		}
	}

}
