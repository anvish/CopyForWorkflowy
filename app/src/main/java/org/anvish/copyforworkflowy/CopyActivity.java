package org.anvish.copyforworkflowy;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;

public class CopyActivity extends Activity {
    public static final String prefOpenWfApp = "prefOpenWfApp";
    public static final boolean defaultOpenWfApp = true;
    public static final String prefOpenWfSite = "prefOpenWfSite";
    public static final boolean defaultOpenWfSite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (copy()) {
            Toast.makeText(this, "Copied", Toast.LENGTH_SHORT).show();
            if (doOpenWfApp()) {
                openWfApp();
            }
            if(doOpenWfSite()){
                openWfSite();
            }
        } else {
            Toast.makeText(this, "Cannot copy", Toast.LENGTH_LONG).show();
        }

        finish();
    }

    private boolean doOpenWfApp() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        return preferences.getBoolean(prefOpenWfApp, defaultOpenWfApp);
    }

    private boolean doOpenWfSite() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        return preferences.getBoolean(prefOpenWfSite, defaultOpenWfSite);
    }

    private void openWfApp() {
        String packageName = "com.workflowy.android";
        Intent launchIntent = getPackageManager().getLaunchIntentForPackage(packageName);
        if (launchIntent != null) {
            startActivity(launchIntent);
        }
    }

    private void openWfSite() {
        String url = "https://workflowy.com";
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

    private boolean copy() {
        Intent intent = getIntent();
        if (intent == null) return false;
        String url = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (url == null) return false;
        String title = intent.getStringExtra(Intent.EXTRA_SUBJECT);
        if (title == null) return false;

        String opml = createOpml(title, url);
        setClip(opml);

        return true;
    }

    private static String createOpml(String title, String url) {
        String titleEscaped = escape(title);
        String urlEscaped = escape(url);
        return "<opml><body><outline text=\"" + titleEscaped + "\" _note=\"" + urlEscaped + "\" /></body></opml>";
    }

    private static String escape(String s) {
        return s.replaceAll("&", "&amp;")
                .replaceAll("'", "&apos;")
                .replaceAll("\"", "&quot;")
                .replaceAll("<", "&lt;")
                .replaceAll(">", "&gt;")
                .replaceAll("\n", "&#10;");
    }

    private void setClip(String s) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            setClip11(s);
        } else {
            setClip1(s);
        }
    }

    @TargetApi(android.os.Build.VERSION_CODES.HONEYCOMB)
    private void setClip11(String s) {
        android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(null, s);
        clipboard.setPrimaryClip(clip);
    }

    @SuppressWarnings("deprecation")
    @TargetApi(android.os.Build.VERSION_CODES.BASE)
    private void setClip1(String s) {
        android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        clipboard.setText(s);
    }
}
