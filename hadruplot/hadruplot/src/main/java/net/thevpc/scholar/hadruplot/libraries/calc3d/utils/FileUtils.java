package net.thevpc.scholar.hadruplot.libraries.calc3d.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import javax.swing.filechooser.*;

public class FileUtils {

	/**Extension for simulation file*/
	public final static String SIM_EXTENSION = "c3d";
	
	public final static String jpeg = "jpeg";
	public final static String jpg = "jpg";
	public final static String gif = "gif";
	public final static String tiff = "tiff";
	public final static String tif = "tif";
	public final static String png = "png";
	public final static String bmp = "bmp";

	/*
	 * Returns the file's extension in lowercase, or "" if the file has no
	 * extension
	 */
	public static String getExtension(String file) {
		String ext = "";
		int i = file.lastIndexOf('.');

		if (i > 0 && i < file.length() - 1) {
			ext = file.substring(i + 1).toLowerCase();
		}
		return ext;
	}

	/*
	 * Get filename without extension from full path
	 */
	public static String fileNameWithoutExtension(String file) {
		String fname = fileNameWithExtension(file);
		int pos = fname.lastIndexOf(".");
		if (pos > 0) {
			fname = fname.substring(0, pos);
		} else {
			return fname;
		}
		return fname;
	}

	/**
	 * 
	 * @param file
	 * @return
	 */
	public static String fileNameWithExtension(String file) {
		return file.substring(file.lastIndexOf(File.separator) + 1);
	}

	
	 /**
	   * Shortens a filename to a specified length by adding path ellipsis ("..."). Applies a heuristic
	   * to work for both Windows and Unix style paths.
	   *
	   * @param filename the filename to shorten
	   * @param maxlen the maximum number of characters to return
	   * @return the truncated string with ellipsis added
	   */
	  public static String getPathEllipsis(final String filename, final int maxlen) {
	    return getPathEllipsis(filename, maxlen, "...");
	  }
	  
	 /**
	   * Shortens a filename to a specified length by adding path ellipsis. Applies a heuristic to work
	   * for both Windows and Unix style paths.
	   *
	   * @param filename the filename to shorten
	   * @param maxlen the maximum number of characters to return
	   * @param ellipsis the string to be used as an ellipsis
	   * @return the truncated string with ellipsis added
	   */
	  public static String getPathEllipsis(
	      final String filename, final int maxlen, final String ellipsis) {
	    final int len = filename.length();
	    final int ellLen = ellipsis.length();
	   // Preconditions.checkArgument(maxlen >= (4 * ellLen), "Maximum length too short");

	    if (len <= maxlen) {
	      return filename;
	    }

	    // Heuristic to detect path separator used
	    int i = 0;
	    char sep = File.separatorChar;
	    boolean found = false;
	    while (!found && (i < len)) {
	      sep = filename.charAt(i);
	      found = ((sep == '/') || (sep == '\\'));
	      i++;
	    }

	    final StringBuilder result = new StringBuilder();
	    if (!found) {
	      // Filename only, just truncate and add ellipsis
	      result.append(filename.substring(0, maxlen - ellLen));
	      result.append(ellipsis);
	    } else {
	      // Since filenames contain valuable information, split the string
	      // right before the filename and truncate both halves.
	      final int lastComp = filename.lastIndexOf(sep);

	      final int splitLen = maxlen - (len - lastComp);
	      final int splitPos = (splitLen / 2) - (ellLen / 2);
	      if (splitPos > 0) {
	        // Left half
	        result.append(filename.substring(0, (splitPos - (ellLen % 2)) + (splitLen % 2)));
	        // Ellipsis
	        result.append(ellipsis);
	        // Right half and filename
	        result.append(filename.substring((lastComp - splitPos) + (ellLen % 2)));
	      } else {
	        // The split point is negative, this means the filename is too
	        // long. Handle this specially by keeping the first 3
	        // characters, add an ellipsis and truncate the rest. This is
	        // done because the first few characters can convey useful
	        // information (on Windows, for example, the drive letter).
	        result.append(filename.substring(0, 3));
	        result.append(ellipsis);
	        result.append(filename.substring(lastComp, (lastComp + maxlen) - 3 - (2 * ellLen)));
	        result.append(ellipsis);
	      }
	    }

	    return result.toString();
	  }
	
	/* ImageFilter.java is used by FileChooserDemo2.java. */
	public FileFilter getImageFilter() {
		return new FileFilter() {

			@Override
			public boolean accept(File f) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public String getDescription() {
				// TODO Auto-generated method stub
				return null;
			}

		};
	}

	public class ImageFilter extends FileFilter {

		// Accept all directories and all gif, jpg, tiff, or png files.
		public boolean accept(File f) {
			if (f.isDirectory()) {
				return true;
			}

			String extension = getExtension(f.getName());
			if (extension != null) {
				if (extension.equals(tiff) || extension.equals(tif)
						|| extension.equals(gif) || extension.equals(jpeg)
						|| extension.equals(jpg) || extension.equals(png)||extension.equals(bmp)) {
					return true;
				} else {
					return false;
				}
			}

			return false;
		}

		// The description of this filter
		public String getDescription() {
			return "Just Images";
		}
	}

	/************************** ZIP Utilities **********************************/

	/**
	 * Returns temporary directory for current OS running application
	 * @return
	 */
	public static String getTempDir()  {
		String tempDir = System.getProperty("java.io.tmpdir");
		return tempDir;
	}
	
	/**
	 * 
	 * @param zipOutputFile File for output zip
	 * @param inputFiles
	 * @return Empty String if files are added successfully else returns error
	 *         string
	 */
	public static String addToZip(File zipOutputFile, File... inputFiles) {
		// String to hold error info
		String errInfo = "";
		final FileOutputStream fos;
		final ZipOutputStream zos;
		try {
			fos = new FileOutputStream(zipOutputFile.getAbsolutePath());
			zos = new ZipOutputStream(fos);

			for (File file : inputFiles) {
				errInfo += addToZipFile(file,
						fileNameWithExtension(file.getName()), zos)
						+ "\n";
			}
			zos.close();
			fos.close();
		} catch (Exception e) {
			errInfo = e.getMessage();
		}
		return errInfo;
	}

	/**
	 * Adds file to ZipOutput Stream
	 * 
	 * @param file  File to be added
	 * @param name  name of file that apperas in zip
	 * @param zos   Already Opened Zip output stream
	 * @return Null if file is added succesfully else returns Error Message
	 */
	private static String addToZipFile(File file, String name,	ZipOutputStream zos) {
		// String to hold error info String errInfo="";
		try {
			FileInputStream fis = new FileInputStream(file);
			ZipEntry zipEntry = new ZipEntry(name);
			zos.putNextEntry(zipEntry);

			byte[] bytes = new byte[1024];
			int length;
			while ((length = fis.read(bytes)) >= 0) {
				zos.write(bytes, 0, length);
			}

			zos.closeEntry();
			fis.close();
		} catch (Exception e) {
			return e.getMessage();
		}

		return "";
	}
	
	/**
	 * 
	 * @param zippedFile
	 * @return
	 * @throws ZipException
	 * @throws IOException
	 */
	public static File extractZipToTempDir(File zippedFile, String outputDir) throws ZipException, IOException {
		String tempDir = System.getProperty("java.io.tmpdir");
        File xmlFile=null;
			ZipFile zipFile = new ZipFile(zippedFile);
			Enumeration<?> enu = zipFile.entries();
			while (enu.hasMoreElements()) {
				ZipEntry zipEntry = (ZipEntry) enu.nextElement();

				String name = zipEntry.getName();
				long size = zipEntry.getSize();
				long compressedSize = zipEntry.getCompressedSize();
				System.out.printf("name: %-20s | size: %6d | compressed size: %6d\n", 
						name, size, compressedSize);

				File file = new File(tempDir, name);
				if (name.endsWith("xml"))xmlFile=file;
				file.deleteOnExit();
				//file=File.createTempFile(prefix, suffix)
				if (name.endsWith("/")) {
					file.mkdirs();
					continue;
				}
				File parent = file.getParentFile();
				if (parent != null) {
					parent.mkdirs();
				}

				InputStream is = zipFile.getInputStream(zipEntry);
				FileOutputStream fos = new FileOutputStream(file);
				byte[] bytes = new byte[1024];
				int length;
				while ((length = is.read(bytes)) >= 0) {
					fos.write(bytes, 0, length);
				}
				is.close();
				fos.close();
				file.deleteOnExit();
			}
			zipFile.close();
		return xmlFile;
	}
	
	/**
	 * Enumerate files from path (Slow)
	 * @param resourcePath
	 * @return
	 */
	public static String[] enumerateFiles(String resourcePath){
		final String path = resourcePath;
		final File jarFile = new File(FileUtils.class.getProtectionDomain().getCodeSource().getLocation().getPath());
		ArrayList<String> files=new ArrayList<String>();
		if(jarFile.isFile()) {  // Run with JAR file
		    try{
		    	final JarFile jar = new JarFile(jarFile);
		    	final Enumeration<JarEntry> entries = jar.entries(); //gives ALL entries in jar
		    	while(entries.hasMoreElements()) {
		    		final String name = entries.nextElement().getName();
		    		if (name.startsWith(path + "/")) { //filter according to the path
		    			files.add(name);
		    			System.out.print(FileUtils.fileNameWithExtension(name)+",");
		    		}
		    	}
		   
		    	jar.close(); }
		    catch(Exception ex){
		    	ex.printStackTrace();
		    }
		} else { // Run with IDE
		    final URL url = FileUtils.class.getResource("/" + path);
		    if (url != null) {
		        try {
		            final File apps = new File(url.toURI());
		            for (File app : apps.listFiles()) {
		            	files.add(app.getName());
		            	System.out.print("\""+app.getName()+"\""+",");
		            }
		        } catch (Exception ex) {
		            // never happens
		        }
		    }
		}
		return files.toArray(new String[0]);
	}
	
	public static String readFile(String path) throws IOException,
			FileNotFoundException {
		FileInputStream stream = null;
		try {
			stream = new FileInputStream(new File(path));
			FileChannel fc = stream.getChannel();
			MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0,
					fc.size());
			return Charset.forName("UTF-8").decode(bb).toString();
		} finally {
			stream.close();
		}
	}
	
	 public static String loadStreamIntoString(InputStream stream) throws IOException {
		    if (stream == null) {
		      throw new java.io.IOException("null stream");
		    }
		    stream = new java.io.BufferedInputStream(stream);
		    int avail = stream.available();
		    byte[] data = new byte[avail];
		    int numRead = 0;
		    int pos = 0;
		    do {
		      if (pos + avail > data.length) {
		        byte[] newData = new byte[pos + avail];
		        System.arraycopy(data, 0, newData, 0, pos);
		        data = newData;
		      }
		      numRead = stream.read(data, pos, avail);
		      if (numRead >= 0) {
		        pos += numRead;
		      }
		      avail = stream.available();
		    } while (avail > 0 && numRead >= 0);
		    return new String(data, 0, pos, "US-ASCII");
		  }
	
	/**
	 * 
	 * @param resourcePath relative to utilities  directory
	 * @return
	 */
	public static String readFileFromResource(String resourcePath) {
		String s = "";
		try {
			InputStream is = FileUtils.class.getResourceAsStream(resourcePath);
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);

			String line;
			while ((line = br.readLine()) != null) {
				s += line + "\n";
			}
			br.close();
			isr.close();
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}
	

	public static void writeFile(String path,String text) throws IOException,
			FileNotFoundException {
		FileOutputStream stream = null;
		try {
			stream = new FileOutputStream(new File(path));
			FileChannel fc = stream.getChannel();
			ByteBuffer bb = Charset.forName("UTF-8").encode(text);
			fc.write(bb);
		} finally {
			stream.close();
		}
	}
	
	/**
	 * returns default save directory used to save simulations
	 * @param currSaveDir
	 * @return
	 */
	public static String getSaveDir(String currSaveDir){
		if(currSaveDir==null||currSaveDir.isEmpty()){
			currSaveDir=System.getProperty("user.dir");
			File f=new File(currSaveDir+File.separatorChar+"simulations");
			if(f.isDirectory() ||f.mkdir()){
				currSaveDir=currSaveDir+File.separatorChar+"simulations";
				System.out.println("directory exists "+f.getPath());
			}else{
				System.out.println("Unable tocreate directory "+f.getPath());
			}
		}
		return currSaveDir;
	}

	public static void copyFile(InputStream in, OutputStream out) throws IOException {
		 byte[] buffer = new byte[1024];
		    int read;
		    while((read = in.read(buffer)) != -1){
		      out.write(buffer, 0, read);
		    }
	}
	
	
}
