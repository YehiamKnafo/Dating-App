package forrealdatingapp.mangers;
 
 import java.util.prefs.BackingStoreException;
 import java.util.prefs.Preferences;

public class TokenManager {
    private Preferences prefs;
    private String sessionId;

    public TokenManager() {
        // We do NOTHING here. No folders created yet.
    }

    private void ensureNodeExists() {
        // Only create the folder if we don't have one or if it was killed
        if (prefs == null) {
            this.sessionId = String.valueOf(System.currentTimeMillis());
            this.prefs = Preferences.userRoot().node("forrealdatingapp/mangers/sessions/" + sessionId);
        }
    }

    public synchronized void saveToken(String _id, String token) {
        ensureNodeExists(); // Folder is born ONLY now
        prefs.put("token_" + _id, token);
        try {
            prefs.flush();
        } catch (BackingStoreException e) { e.printStackTrace(); }
    }

    public String getToken(String _id) {
        // If prefs is null, it means no login has happened this session
        if (prefs == null) return null;

        try {
            prefs.sync();
        } catch (BackingStoreException e) { e.printStackTrace(); }
        return prefs.get("token_" + _id, null);
    }

    public void clearToken(String _id) {
        if (prefs != null) {
            try {
                prefs.removeNode(); // Kill the folder
                prefs.flush();
                prefs = null; // Set to null so we know it's "dead"
                System.out.println("Registry cleaned and memory reset.");
            } catch (BackingStoreException e) {
                e.printStackTrace();
            }
        }
    }
}