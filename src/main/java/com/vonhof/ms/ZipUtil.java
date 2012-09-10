package com.vonhof.ms;

import java.io.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.io.IOUtils;

public class ZipUtil {

    /**
     * Creates a zip file at the specified path with the contents of the specified directory. NB:
     *
     * @param directoryPath The path of the directory where the archive will be created. eg. c:/temp
     * @param zipPath The full path of the archive to create. eg. c:/temp/archive.zip
     * @throws IOException If anything goes wrong
     */
    public static void createZip(String directoryPath, String zipPath) throws IOException {
        FileOutputStream fOut = null;
        BufferedOutputStream bOut = null;
        ZipArchiveOutputStream tOut = null;

        try {
            fOut = new FileOutputStream(new File(zipPath));
            bOut = new BufferedOutputStream(fOut);
            tOut = new ZipArchiveOutputStream(bOut);
            addFileToZip(tOut, directoryPath, "");
        } finally {
            tOut.finish();
            tOut.close();
            bOut.close();
            fOut.close();
        }

    }

    /**
     * Creates a zip entry for the path specified with a name built from the base passed in and the file/directory name.
     * If the path is a directory, a recursive call is made such that the full directory is added to the zip.
     *
     * @param zOut The zip file's output stream
     * @param path The filesystem path of the file/directory being added
     * @param base The base prefix to for the name of the zip file entry
     *
     * @throws IOException If anything goes wrong
     */
    private static void addFileToZip(ZipArchiveOutputStream zOut, String path, String base) throws IOException {
        File f = new File(path);
        String entryName = base + f.getName();
        ZipArchiveEntry zipEntry = new ZipArchiveEntry(f, entryName);

        zOut.putArchiveEntry(zipEntry);

        if (f.isFile()) {
            FileInputStream fInputStream = null;
            try {
                fInputStream = new FileInputStream(f);
                IOUtils.copy(fInputStream, zOut);
                zOut.closeArchiveEntry();
            } finally {
                IOUtils.closeQuietly(fInputStream);
            }

        } else {
            zOut.closeArchiveEntry();
            File[] children = f.listFiles();

            if (children != null) {
                for (File child : children) {
                    addFileToZip(zOut, child.getAbsolutePath(), entryName + "/");
                }
            }
        }
    }

    /**
     * Extract zip file at the specified destination path. NB:archive must consist of a single root folder containing
     * everything else
     *
     * @param archivePath path to zip file
     * @param destinationPath path to extract zip file to. Created if it doesn't exist.
     */
    public static void extractZip(String archivePath, String destinationPath) {
        File archiveFile = new File(archivePath);
        File unzipDestFolder = null;

        try {
            unzipDestFolder = new File(destinationPath);
            String[] zipRootFolder = new String[]{null};
            unzipFolder(archiveFile, archiveFile.length(), unzipDestFolder, zipRootFolder);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Unzips a zip file into the given destination directory.
     *
     * The archive file MUST have a unique "root" folder. This root folder is skipped when unarchiving.
     *
     * @return true if folder is unzipped correctly.
     */
    @SuppressWarnings("unchecked")
    private static boolean unzipFolder(File archiveFile,
            long compressedSize,
            File zipDestinationFolder,
            String[] outputZipRootFolder) {

        ZipFile zipFile = null;
        try {
            zipFile = new ZipFile(archiveFile);
            byte[] buf = new byte[65536];

            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry zipEntry = entries.nextElement();
                String name = zipEntry.getName();
                name = name.replace('\\', '/');
                int i = name.indexOf('/');
                if (i > 0) {
                    outputZipRootFolder[0] = name.substring(0, i);
                }
                name = name.substring(i + 1);


                File destinationFile = new File(zipDestinationFolder, name);
                if (name.endsWith("/")) {
                    if (!destinationFile.isDirectory() && !destinationFile.mkdirs()) {
                        log("Error creating temp directory:" + destinationFile.getPath());
                        return false;
                    }
                    continue;
                } else if (name.indexOf('/') != -1) {
                    // Create the the parent directory if it doesn't exist
                    File parentFolder = destinationFile.getParentFile();
                    if (!parentFolder.isDirectory()) {
                        if (!parentFolder.mkdirs()) {
                            log("Error creating temp directory:" + parentFolder.getPath());
                            return false;
                        }
                    }
                }

                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(destinationFile);
                    int n;
                    InputStream entryContent = zipFile.getInputStream(zipEntry);
                    while ((n = entryContent.read(buf)) != -1) {
                        if (n > 0) {
                            fos.write(buf, 0, n);
                        }
                    }
                } finally {
                    if (fos != null) {
                        fos.close();
                    }
                }

                return true;
            }

        } catch (IOException e) {
            log("Unzip failed:" + e.getMessage());
        } finally {
            if (zipFile != null) {
                try {
                    zipFile.close();
                } catch (IOException e) {
                    log("Error closing zip file");
                }
            }
        }

        return false;
    }

    private static void log(String msg) {
        System.out.println(msg);
    }

    /**
     * Method for testing zipping and unzipping.
     *
     * @param args
     */
    public static void main(String[] args) throws IOException {
        createZip("c:/temp/99/target", "c:/temp/99/output2.zip");
        extractZip("c:/temp/99/output2.zip", "c:/temp/99/1");
    }
}
