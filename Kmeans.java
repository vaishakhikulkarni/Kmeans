package SkipList;

//package uni.utd.fall2014.ML.assignment4;
/*** Author :Vibhav Gogate
 The University of Texas at Dallas
 *****/

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

public class Kmeans {
	public static void main(String[] args) {
		if (args.length < 3) {
			System.out
					.println("Usage: Kmeans <input-image> <k> <output-image>");
			return;
		}
		try {
			BufferedImage originalImage = ImageIO.read(new File(args[0]));
			int k = Integer.parseInt(args[1]);
			BufferedImage kmeansJpg = kmeans_helper(originalImage, k);
			ImageIO.write(kmeansJpg, "jpg", new File(args[2]));

		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	private static BufferedImage kmeans_helper(BufferedImage originalImage,
			int k) {
		int w = originalImage.getWidth();
		int h = originalImage.getHeight();
		BufferedImage kmeansImage = new BufferedImage(w, h,
				originalImage.getType());
		Graphics2D g = kmeansImage.createGraphics();
		g.drawImage(originalImage, 0, 0, w, h, null);
		// Read rgb values from the image
		int[] rgb = new int[w * h];
		int count = 0;
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				rgb[count++] = kmeansImage.getRGB(i, j);
			}
		}
		// Call kmeans algorithm: update the rgb values
		kmean(rgb, k, originalImage);

		// Write the new rgb values to the image
		count = 0;
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				kmeansImage.setRGB(i, j, rgb[count++]);
			}
		}
		return kmeansImage;
	}

	// Your k-means code goes here
	// Update the array rgb by assigning each entry in the rgb array to its
	// cluster center
	public static void kmean(int[] colorComb, int k, BufferedImage original) {

		int w = original.getWidth();
		int h = original.getHeight();

		// initialize k centroid
		double[] meanCentroids = new double[k];
		int count = 0;
		int[] rcentroid = new int[k];
		int[] gcentroid = new int[k];
		int[] bcentroid = new int[k];

		Random r = new Random();
		// Assign random value to cluster centroid
		for (int i = 0; i < k; i++) {
			Color c = new Color(original.getRGB(r.nextInt((w - 0)),
					r.nextInt((h - 0))));

			rcentroid[i] = c.getRed();
			bcentroid[i] = c.getBlue();
			gcentroid[i] = c.getGreen();

			System.out.println("" + c);
		}

		int[] rcalc = new int[k];
		int[] gcalc = new int[k];
		int[] bcalc = new int[k];

		int[] counterrgb = new int[k];
		int check = 0;
		while (true) {
			count = 0;

			for (int i = 0; i < k; i++) {
				rcalc[i] = gcalc[i] = bcalc[i] = 0;
				counterrgb[i] = 1;
			}
			int i = 0;
			while (i != w) {

				int j = 0;
				while (j != h) {
					int l = 0;

					while (l != k) {
						Color newcolor = new Color(original.getRGB(i, j));
						double tempred = rcentroid[l] - newcolor.getRed();
						double tempgreen = gcentroid[l] - newcolor.getGreen();
						double tempblue = bcentroid[l] - newcolor.getBlue();
						meanCentroids[l] = (Math.pow(tempred, 2)
								+ Math.pow(tempgreen, 2) + Math
								.pow(tempblue, 2));
						l++;

					}

					double min = Double.POSITIVE_INFINITY;
					int Index = 0;

					for (int m = 0; m < meanCentroids.length; m++) {
						if (meanCentroids[m] < min) {
							Index = m;
							min = meanCentroids[m];

						}
					}
					counterrgb[Index]++;
					Color store = new Color(original.getRGB(i, j));
					rcalc[Index] += store.getRed();
					gcalc[Index] += store.getGreen();
					bcalc[Index] += store.getBlue();
					colorComb[count] = new Color(rcentroid[Index],
							gcentroid[Index], bcentroid[Index]).getRGB();
					count++;

					j++;
				}
				i++;
			}
			//Calculate average
			for (i = 0; i < k; i++) {
				int rAvg = rcalc[i] / counterrgb[i];
				int gAvg = gcalc[i] / counterrgb[i];
				int bAvg = bcalc[i] / counterrgb[i];

				if (rAvg == rcentroid[i] && bAvg == bcentroid[i]
						&& gAvg == gcentroid[i])
					check++;

				rcentroid[i] = rAvg;
				gcentroid[i] = gAvg;
				bcentroid[i] = bAvg;
			}
			if (check >= k)
				break;
		}

	}

}
