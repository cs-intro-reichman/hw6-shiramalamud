import java.awt.Color;

/** A library of image processing functions. */
public class Runigram {

	public static void main(String[] args) {
	    
		//// Hide / change / add to the testing code below, as needed.
		
		// Tests the reading and printing of an image:	
		Color[][] tinypic = read("tinypic.ppm");
		print(tinypic);

		// Creates an image which will be the result of various 
		// image processing operations:
		Color[][] image;

		// Tests the horizontal flipping of an image:
		image = flippedHorizontally(tinypic);
		System.out.println();
		print(image);
		
		//// Write here whatever code you need in order to test your work.
		//// You can continue using the image array.
	}

	/** Returns a 2D array of Color values, representing the image data
	 * stored in the given PPM file. */
	public static Color[][] read(String fileName) {
		In in = new In(fileName);
		// Reads the file header, ignoring the first and the third lines.
		in.readString();
		int numCols = in.readInt();
		int numRows = in.readInt();
		in.readInt();
		// Creates the image array
		Color[][] image = new Color[numRows][numCols];
		// Reads the RGB values from the file into the image array. 
		for(int i=0; i<numRows; i++)
		{
			for(int j=0; j<numCols; j++)
			{
				int r= in.readInt();
				int g= in.readInt();
				int b= in.readInt();
				image[i][j]= new Color (r,g,b);
			}
		}
		// For each pixel (i,j), reads 3 values from the file,
		// creates from the 3 colors a new Color object, and 
		// makes pixel (i,j) refer to that object.
		return image;
	}

    // Prints the RGB values of a given color.
	private static void print(Color c) {
	    System.out.print("(");
		System.out.printf("%3s,", c.getRed());   // Prints the red component
		System.out.printf("%3s,", c.getGreen()); // Prints the green component
        System.out.printf("%3s",  c.getBlue());  // Prints the blue component
        System.out.print(")  ");
	}

	// Prints the pixels of the given image.
	// Each pixel is printed as a triplet of (r,g,b) values.
	// This function is used for debugging purposes.
	// For example, to check that some image processing function works correctly,
	// we can apply the function and then use this function to print the resulting image.
	private static void print(Color[][] image) {
		//// Notice that all you have to so is print every element (i,j) of the array using the print(Color) function.
     
		for(int i=0; i<image.length; i++)
		{
			for(int j=0; j<image[0].length; j++)
			{
				int r=image[i][j].getRed();
				int g=image[i][j].getGreen();
				int b=image[i][j].getBlue();
				System.out.println("("+r+", "+g+", "+b+")");
			}
			System.out.println();
		}
	}
	
	/**
	 * Returns an image which is the horizontally flipped version of the given image. 
	 */
	public static Color[][] flippedHorizontally(Color[][] image)
	 {
		for(int i=0; i<image.length; i++)
		{
			for(int j=0; j<image[0].length/2; j++)
			{
				Color temp= image[i][j];
				image[i][j]=image[i][image[0].length-1-j];
				image[i][image[0].length-1-j]=temp;
			}
		}
		return image;
	}
	
	/**
	 * Returns an image which is the vertically flipped version of the given image. 
	 */
	public static Color[][] flippedVertically(Color[][] image)
	{
		for(int i=0; i<image.length/2; i++)
		{
			for(int j=0; j<image[0].length; j++)
			{
				Color[] temp = image[i];            
                image[i] = image[image.length - 1 - i];  
                image[image.length - 1 - i] = temp; 
			}
		}
		return image;
	}
	
	// Computes the luminance of the RGB values of the given pixel, using the formula 
	// lum = 0.299 * r + 0.587 * g + 0.114 * b, and returns a Color object consisting
	// the three values r = lum, g = lum, b = lum.
	private static Color luminance(Color pixel) 
	{
		int r=pixel.getRed();
		int g=pixel.getGreen();
		int b=pixel.getBlue();
		int lum=(int)((0.299*r)+(0.587*g)+(0.114*b));
		Color c1=new Color (lum,lum,lum);
		return c1;
	}
	
	/**
	 * Returns an image which is the grayscaled version of the given image.
	 */
	public static Color[][] grayScaled(Color[][] image)
	 {
		for(int i=0; i<image.length; i++)
		{
			for(int j=0; j<image[0].length; j++)
			{
				Color lum=luminance(image[i][j]);
				image[i][j]=lum;
			}
		}
		return image;
	}	
	
	/**
	 * Returns an image which is the scaled version of the given image. 
	 * The image is scaled (resized) to have the given width and height.
	 */
	public static Color[][] scaled(Color[][] image, int width, int height)
	 {
		Color[][] newImage = new Color[height][width];
		double rowRatio = (double) image.length / height;   
        double colRatio = (double) image[0].length / width;
		if (rowRatio > 1 || colRatio > 1) 
		{
			for (int i = 0; i < height; i++)
			 {
				for (int j = 0; j < width; j++) 
				{
					int currentRow=(int)(i*rowRatio);
					int currentCol=(int)(j*colRatio);
					if (currentCol < image[0].length && currentRow < image.length) 
					{
						  newImage[i][j]=image[currentRow][currentCol];
                    }
					 else 
					 {
						newImage[i][j] = new Color(255, 255, 255); 
					 }
					
				}
			}
		}
		else
		{
			for (int i = 0; i < height; i++)
			 {
				for (int j = 0; j < width; j++) 
				{
					int currentRow=(int)(i*rowRatio);
					int currentCol=(int)(j*colRatio);
					newImage[i][j]=image[currentRow][currentCol];
				}
			}
		}
		return newImage;
	}
	
	/**
	 * Computes and returns a blended color which is a linear combination of the two given
	 * colors. Each r, g, b, value v in the returned color is calculated using the formula 
	 * v = alpha * v1 + (1 - alpha) * v2, where v1 and v2 are the corresponding r, g, b
	 * values in the two input color.
	 */
	public static Color blend(Color c1, Color c2, double alpha) 
	{
		int r1=c1.getRed();
		int g1=c1.getGreen();
		int b1=c1.getBlue();

		int r2=c2.getRed();
		int g2=c2.getGreen();
		int b2=c2.getBlue();

		int r=(int)(alpha*r1+(1-alpha)*r2);
		int g=(int)(alpha*r1+(1-alpha)*r2);
		int b=(int)(alpha*r1+(1-alpha)*r2);
		Color pixel= new Color(r,g,b);
		return pixel;
	}
	
	/**
	 * Cosntructs and returns an image which is the blending of the two given images.
	 * The blended image is the linear combination of (alpha) part of the first image
	 * and (1 - alpha) part the second image.
	 * The two images must have the same dimensions.
	 */
	public static Color[][] blend(Color[][] image1, Color[][] image2, double alpha) 
	{
		Color[][] blendedImage = new Color[image1.length][image1[0].length];
		for(int i=0; i<image1.length; i++)
		{
			for(int j=0; j<image1[0].length; j++)
			{
				Color c1=image1[i][j];
				Color c2=image2[i][j];
				blendedImage[i][j]=	(blend(c1,c2,alpha));
			}
		}
		return blendedImage;
	}


	public static Color[][] resize(Color[][] image, int numRows, int numCols) 
	{
		Color[][] resizedImage = new Color[numRows][numCols];
		
		for (int i = 0; i < numRows; i++) {
			for (int j = 0; j < numCols; j++) 
			{
				int row = i * image.length / numRows;
				int col = j * image[0].length / numCols;
				resizedImage[i][j] = image[row][col];
			}
		}
		return resizedImage;
	}
	/**
	 * Morphs the source image into the target image, gradually, in n steps.
	 * Animates the morphing process by displaying the morphed image in each step.
	 * Before starting the process, scales the target image to the dimensions
	 * of the source image.
	 */
	public static void morph(Color[][] source, Color[][] target, int n) 
	{
		target=resize(target, source.length, source[0].length);
		Color[][] morphedImage = new Color[source.length][source[0].length];
		for (int step = 0; step <= n; step++) 
		{
			double alpha = (double) step / (n-1);
			for (int i = 0; i < morphedImage.length; i++) 
			{
				for (int j = 0; j < morphedImage[0].length; j++) 
				{
					Color c1 = source[i][j];
					Color c2 = target[i][j];
					Color blendedColor = blend(c1, c2, alpha); 
					morphedImage[i][j] = blendedColor;
				}
			}
			setCanvas(morphedImage); 
			display(morphedImage);
		}
		

	}
	
	/** Creates a canvas for the given image. */
	public static void setCanvas(Color[][] image) {
		StdDraw.setTitle("Runigram 2023");
		int height = image.length;
		int width = image[0].length;
		StdDraw.setCanvasSize(height, width);
		StdDraw.setXscale(0, width);
		StdDraw.setYscale(0, height);
        // Enables drawing graphics in memory and showing it on the screen only when
		// the StdDraw.show function is called.
		StdDraw.enableDoubleBuffering();
	}

	/** Displays the given image on the current canvas. */
	public static void display(Color[][] image) {
		int height = image.length;
		int width = image[0].length;
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				// Sets the pen color to the pixel color
				StdDraw.setPenColor( image[i][j].getRed(),
					                 image[i][j].getGreen(),
					                 image[i][j].getBlue() );
				// Draws the pixel as a filled square of size 1
				StdDraw.filledSquare(j + 0.5, height - i - 0.5, 0.5);
			}
		}
		StdDraw.show();
	}
}

