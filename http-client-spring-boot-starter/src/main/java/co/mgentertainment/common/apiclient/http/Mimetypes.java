package co.mgentertainment.common.apiclient.http;

import java.io.*;
import java.util.HashMap;
import java.util.StringTokenizer;

/**
 * @author larry
 * @createTime 2023/2/2
 * @description Mimetypes
 */
public class Mimetypes {
    public static final String DEFAULT_MIMETYPE = "application/octet-stream";
    private static Mimetypes mimetypes = null;
    private HashMap<String, String> extensionToMimetypeMap = new HashMap();

    private Mimetypes() {
    }

    public static synchronized Mimetypes getInstance() {
        if (mimetypes != null) {
            return mimetypes;
        }
        mimetypes = new Mimetypes();
        InputStream is = mimetypes.getClass().getResourceAsStream("/mime.types");
        if (is != null) {
            try {
                mimetypes.loadMimetypes(is);
            } catch (IOException ex) {
                System.out.println("Failed to load mime types from file in the classpath: mime.types");
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
        }
        return mimetypes;
    }

    public void loadMimetypes(InputStream is) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line = null;

        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (!line.startsWith("#") && line.length() != 0) {
                StringTokenizer st = new StringTokenizer(line, " \t");
                if (st.countTokens() > 1) {
                    String extension = st.nextToken();
                    if (st.hasMoreTokens()) {
                        String mimetype = st.nextToken();
                        this.extensionToMimetypeMap.put(extension.toLowerCase(), mimetype);
                    }
                }
            }
        }

    }

    public String getMimetype(String fileName) {
        String mimeType = this.getMimetypeByExt(fileName);
        return mimeType != null ? mimeType : DEFAULT_MIMETYPE;
    }

    public String getMimetype(File file) {
        return this.getMimetype(file.getName());
    }

    public String getMimetype(File file, String key) {
        return this.getMimetype(file.getName(), key);
    }

    public String getMimetype(String primaryObject, String secondaryObject) {
        String mimeType = this.getMimetypeByExt(primaryObject);
        if (mimeType != null) {
            return mimeType;
        } else {
            mimeType = this.getMimetypeByExt(secondaryObject);
            return mimeType != null ? mimeType : DEFAULT_MIMETYPE;
        }
    }

    private String getMimetypeByExt(String fileName) {
        int lastPeriodIndex = fileName.lastIndexOf(".");
        if (lastPeriodIndex > 0 && lastPeriodIndex + 1 < fileName.length()) {
            String ext = fileName.substring(lastPeriodIndex + 1).toLowerCase();
            if (this.extensionToMimetypeMap.keySet().contains(ext)) {
                String mimetype = (String) this.extensionToMimetypeMap.get(ext);
                return mimetype;
            }
        }

        return null;
    }

}
