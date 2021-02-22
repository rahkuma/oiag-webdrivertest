package com.tibco.automation.oiag.common.utils;

/**
 * Common file utility methods extended from org.apache.commons.io.FileUtils.
 */
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Collection;
import java.util.Random;
import java.util.Vector;

import javax.activation.MimetypesFileTypeMap;
import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.output.FileWriterWithEncoding;

public class FileUtil extends FileUtils {
	private static int counter = -1; /* Protected by tmpFileLock */
	
	public enum DatePattern {
		ddMMMyyyyHHmmssZZZZ("[0-9]\\{2\\}\\/[a-Z]\\{3\\}\\/[0-9]\\{4\\}:[0-9]\\{2\\}:[0-9]\\{2\\}:[0-9]\\{2\\} \\?[+-][0-9]\\{4\\}"),
		yyyyMMddHHmmss("[0-9]\\{4\\}-[0-9]\\{2\\}-[0-9]\\{2\\} \\?[0-9]\\{2\\}:[0-9]\\{2\\}:[0-9]\\{2\\}"),
		EMMMddyyyyHHmmss("[a-Z]\\{3\\} \\?[a-Z]\\{3\\} \\?[0-9]\\{2\\} \\?[0-9]\\{4\\} \\?[0-9]\\{2\\}:[0-9]\\{2\\}:[0-9]\\{2\\}"),
		MMddyyyyHHmmss("[0-9]\\{2\\}\\/[0-9]\\{2\\}\\/[0-9]\\{4\\},[0-9]\\{2\\}:[0-9]\\{2\\}:[0-9]\\{2\\}"),
		yyyyMMddTHHmmss("[0-9]\\{4\\}-[0-9]\\{2\\}-[0-9]\\{2\\}T[0-9]\\{2\\}:[0-9]\\{2\\}:[0-9]\\{2\\}");
		
		String pattern;
		
		private DatePattern(String pattern) {
			this.pattern = pattern;
		}
		
		public String convert() {
			return pattern.replace("\\", "").replace("[a-Z]", "\\w").replace("[0-9]", "\\d").replace(" ", "\\s");
		}
		
		public String getPattern() {
			return pattern;
		}
	}
	
	public enum DateFormat {
		ddMMMyyyyHHmmssZZZZ("'+%d\\/%b\\/%Y:%H:%M:%S %z'"), 
		yyyyMMddHHmmss("-d'-7 hour ago' '+%Y-%m-%d %H:%M:%S'"),
		EMMMddyyyyHHmmss("-d'-7 hour ago' '+%a %b %d %Y %H:%M:%S'"), 
		MMddyyyyHHmmss("-d'-7 hour ago' '+%m\\/%d\\/%Y,%H:%M:%S'"),
		yyyyMMddTHHmmss("-d'6 month ago' '+%Y-%m-%dT%H:%M:%S'");
		
		String format;
		
		private DateFormat(String format) {
			this.format = format;
		}
		
		public String getFormat() {
			return format;
		}
	}

	public static String saveImageFile(String base64Str, String prefix, String dir) throws Exception {
		byte[] decodedScreenshot = Base64.decodeBase64(base64Str.getBytes());// new
		// Base64Encoder().decode(base64Str);
		File file = generateFile(prefix, ".png", dir);
		FileOutputStream fos = new FileOutputStream(file);
		fos.write(decodedScreenshot);
		fos.close();
		return file.getName();
	}

	public static String saveImageFile(BufferedImage bImag, String prefix, String dir) throws Exception {
		// Base64Encoder().decode(base64Str);
		File file = generateFile(prefix, ".png", dir);
		ImageIO.write(bImag, "png", file);
		return file.getName();
	}

	public static String getContentType(File f) {
		MimetypesFileTypeMap fileTypeMap = new MimetypesFileTypeMap();
		FileNameMap fileNameMap = URLConnection.getFileNameMap();
		String ct = fileNameMap.getContentTypeFor(f.getName());
		return StringUtil.isBlank(ct) ? fileTypeMap.getContentType(f) : ct;
	}

	/**
	 * Check and Create a directory if not exist; all non-existent ancestor
	 * directories will automatically created
	 * 
	 * @param dir
	 * @return
	 */
	public static boolean checkCreateDir(String dir) {
		File tdir = new File(dir);
		boolean dirExists = tdir.exists();
		if (!dirExists) {
			dirExists = tdir.mkdirs();
		}
		return dirExists;
	}

	/**
	 * Method to get relative file path Ex: String filePath =
	 * "/var/data/images/xyz.png"; String root = "/var/data"; return value will
	 * be "images/xyz.png"
	 * 
	 * @param root
	 * @param filePath
	 * @return
	 */
	public static String getReletivePath(String root, String filePath) {
		String relative = new File(root).toURI().relativize(new File(filePath).toURI()).getPath();
		return relative;
	}

	public static File generateFile(String prefix, String suffix, String dir) throws IOException {
		if (counter == -1) {
			counter = new Random().nextInt() & 0xffff;
		}
		counter++;
		return new File(dir, prefix + Integer.toString(counter) + suffix);
	}

	public static File[] listFilesAsArray(File directory, FilenameFilter filter, boolean recurse) {
		Collection<File> files = listFiles(directory, filter, recurse);
		File[] arr = new File[files.size()];
		return files.toArray(arr);
	}

	public static Collection<File> listFiles(File directory, String fname, StringComparator c, boolean recurse) {
		return listFiles(directory, getFileFilterFor(fname, c), recurse);
	}

	public static File[] listFilesAsArray(File directory, String fname, StringComparator c, boolean recurse) {
		Collection<File> files = listFiles(directory, fname, c, recurse);
		File[] arr = new File[files.size()];
		return files.toArray(arr);
	}

	public static Collection<File> listFiles(File directory, FilenameFilter filter, boolean recurse) {
		// List of files / directories
		Vector<File> files = new Vector<File>();

		// Get files / directories in the directory
		File[] entries = directory.listFiles();

		// Go over entries
		for (File entry : entries) {
			if ((filter == null) || filter.accept(directory, entry.getName())) {
				files.add(entry);
			}

			// If the file is a directory and the recurse flag
			// is set, recurse into the directory
			if (recurse && entry.isDirectory()) {
				files.addAll(listFiles(entry, filter, recurse));
			}
		}

		// Return collection of files
		return files;
	}

	public static String getBase64String(File f) throws IOException {

		return new String(Base64.encodeBase64(readFileToByteArray(f)));
	}


	/**
	 * @param fname
	 *            : file name or part of file name to search
	 * @param comparator
	 *            : comparator to use while filtering file.
	 * @return: FilenameFilter Example:
	 *          <ol>
	 *          <li>getFileFilterFor(".properties", StringComparator.Suffix)
	 *          <li>getFileFilterFor("TC123", StringComparator.Prefix)
	 *          <li>getFileFilterFor("TC123.*.png", StringComparator.RegExp)
	 *          </ol>
	 */
	public static FilenameFilter getFileFilterFor(final String fname, StringComparator comparator) {
		final StringComparator fnamecomparator = null != comparator ? comparator : StringComparator.In;
		FilenameFilter filter = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return fnamecomparator.compareIgnoreCase(name, fname);
			}
		};

		return filter;
	}

	public static FilenameFilter getFileFilterFor(final String name) {
		return getFileFilterFor(name, null);
	}

	
	public static Collection<File> getFiles(File directory, String extension, boolean recurse) {
		// List of files / directories
		Vector<File> files = new Vector<File>();

		// Get files / directories in the directory
		File[] entries = directory.listFiles();
		FilenameFilter filter = getFileFilterFor(extension);
		// Go over entries
		for (File entry : entries) {
			if ((filter == null) || filter.accept(directory, entry.getName())) {
				files.add(entry);
			}

			// If the file is a directory and the recurse flag
			// is set, recurse into the directory
			if (recurse && entry.isDirectory()) {
				files.addAll(listFiles(entry, filter, recurse));
			}
		}

		// Return collection of files
		return files;
	}

	public static Collection<File> getFiles(File directory, String name, StringComparator c, boolean recurse) {
		// List of files / directories
		Vector<File> files = new Vector<File>();

		// Get files / directories in the directory
		File[] entries = directory.listFiles();
		FilenameFilter filter = getFileFilterFor(name, c);
		// Go over entries
		for (File entry : entries) {
			if ((filter == null) || filter.accept(directory, entry.getName())) {
				files.add(entry);
			}

			// If the file is a directory and the recurse flag
			// is set, recurse into the directory
			if (recurse && entry.isDirectory()) {
				files.addAll(listFiles(entry, filter, recurse));
			}
		}

		// Return collection of files
		return files;
	}

	/**
	 * @param fname
	 *            : (optional) filename and file extension as two separate args
	 *            or file name with extension..
	 * @return
	 * @throws IOException
	 */
	public static File createTempFile(String... fname) throws IOException {
		return createTempFile(null, fname);
	}

	public static File createTempFile(File dir, String... name) throws IOException {
		String fname = null, ext = null;
		if ((name != null)) {
			if ((name.length == 1) && StringUtil.isNotEmpty(name[0])) {
				// check contains extension?
				int dotSign = name[0].lastIndexOf(".");
				if ((dotSign >= 0) && (dotSign < name[0].length())) {
					fname = name[0].substring(0, dotSign);
					ext = name[0].substring(dotSign);
				} else {
					fname = name[0];
				}
			} else if (name.length > 1) {
				fname = name[0];
				ext = name[1].startsWith(".") ? name[1] : "." + name[1];
			}
		}
		if (StringUtil.isBlank(fname)) {
			fname = StringUtil.createRandomString("Auto");
		}
		File tmpfile = File.createTempFile(fname, ext, dir);
		tmpfile.deleteOnExit();
		return tmpfile;
	}
	
	public static String read( File file ) {

        String content = "";
        FileChannel channel = null;
        RandomAccessFile random = null;

        try {

            final int BUFFER_SIZE = 2097152;

            ByteBuffer byteBuf = ByteBuffer.allocate( BUFFER_SIZE );

            byte[] dst = new byte[BUFFER_SIZE];

            random = new RandomAccessFile( file, "r" );

            channel = random.getChannel();

            while ( channel.read( byteBuf ) != -1 ) {
                int size = byteBuf.position();
                byteBuf.rewind();
                byteBuf.get( dst );
                content += new String( dst, 0, size );
                byteBuf.clear();
            }
        }
        catch ( Exception e ) {
            e.printStackTrace();
        }
        finally {
            try {
                if ( channel != null ) {
                    channel.close();
                }
                if ( random != null ) {
                    random.close();
                }
            }
            catch ( Exception e2 ) {
                e2.printStackTrace();
            }

        }

        return content;
    }
	
	public static void write( File file, String content, char type ) {
		FileWriterWithEncoding writer = null;
        try {
            switch ( type ) {
                case 'w':
                    writer = new FileWriterWithEncoding( file, "UTF-8", false );
                    break;
                case 'a':
                    writer = new FileWriterWithEncoding( file, "UTF-8", true );
                    break;
                default:
                    break;
            }
            writer.write( content );
            writer.close();
        }
        catch ( IOException e ) {
            e.printStackTrace();
        }
        finally {
            if ( writer != null ) {
                try {
                    writer.close();
                }
                catch ( IOException e ) {
                    e.printStackTrace();
                }

            }
        }
    }
	
	public static void changeDatetimeInLogFileToNow(String filepath, DatePattern pattern) {
		String fullpath = PropertiesUtil.getBundle().getProperty("filepull.logs.path").toString() + filepath;
		String ip = PropertiesUtil.getBundle().getProperty("feeder.server.ip").toString(),
				username = PropertiesUtil.getBundle().getProperty("feeder.server.username").toString(),
				password = PropertiesUtil.getBundle().getProperty("feeder.server.passwd").toString();
		Backend backend = new Backend(ip, username, password);
		String command = "";
		switch (pattern) {
		case ddMMMyyyyHHmmssZZZZ:
			command = "sed -e \"s/" + pattern.getPattern() + "/`date " + DateFormat.ddMMMyyyyHHmmssZZZZ.getFormat()
					+ "`/\" " + fullpath + " > " + fullpath + "_NOW";
			break;
		case EMMMddyyyyHHmmss:
			command = "sed -e \"s/" + pattern.getPattern() + "/`date " + DateFormat.EMMMddyyyyHHmmss.getFormat()
					+ "`/\" " + fullpath + " > " + fullpath + "_NOW";
			break;
		case MMddyyyyHHmmss:
			command = "sed -e \"s/" + pattern.getPattern() + "/`date " + DateFormat.MMddyyyyHHmmss.getFormat() + "`/\" "
					+ fullpath + " > " + fullpath + "_NOW";
			break;
		case yyyyMMddHHmmss:
			command = "sed -e \"s/" + pattern.getPattern() + "/`date " + DateFormat.yyyyMMddHHmmss.getFormat() + "`/\" "
					+ fullpath + " > " + fullpath + "_NOW";
			break;
		case yyyyMMddTHHmmss:
			command = "sed -e \"s/" + pattern.getPattern() + "/`date " + DateFormat.yyyyMMddTHHmmss.getFormat()
					+ "`/\" " + fullpath + " > " + fullpath + "_NOW";
			break;
		default:
			break;
		}
		backend.execute(command);
		backend.close();
	}
	
	public static boolean checkLog(String logfile, String expected) {
		String content = read(new File(logfile));
		return content.contains(expected);
	}

	public static void main(String[] args) {
		String s = "test123.property";
		System.out.println(StringComparator.In.compare(s, ".proper"));
		System.out.println(StringComparator.RegExp.compareIgnoreCase("Test123aaa.png", "test123.*.png"));
		
		System.out.println(FileUtil.getContentType(new File("test.doc")));
		Vector<File> list = (Vector<File>) FileUtil.getFiles(new File("."), "selenium-server.*\\.jar",
				StringComparator.RegExp, true);
		System.out.println("found " + list.size() + " selenium server");
		File serverJar = (list == null) || list.isEmpty() ? new File("./server/selenium-server*.jar") : list.get(0);
		if (serverJar.exists()) {

			System.out.println("java -jar \"" + serverJar.getAbsolutePath() + "\"");
		}
		
		changeDatetimeInLogFileToNow("Cisco_ACS/acs_pass_auth.log", DatePattern.MMddyyyyHHmmss);
	}

}
