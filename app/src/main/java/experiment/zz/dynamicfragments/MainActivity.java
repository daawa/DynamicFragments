package experiment.zz.dynamicfragments;

import android.app.Fragment;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;

import dalvik.system.DexClassLoader;

public class MainActivity extends AppCompatActivity {

    DexClassLoader dexClassLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", new View.OnClickListener(){
                            @Override
                            public void onClick(View v) {

                            }
                        }).show();

                if(dexClassLoader != null){
                    try {
                        int i = new Random().nextInt()%2;
                        String claz = i == 0? "zz.experiment.fragmentslib.BlankFragment1":"zz.experiment.fragmentslib.BlankFragment2";
                        Class c = dexClassLoader.loadClass(claz);
                        Fragment f = (Fragment)c.newInstance();
                        getFragmentManager().beginTransaction().replace(R.id.content_frame, f).commit();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } catch (InstantiationException e){
                        e.printStackTrace();
                    }catch (IllegalAccessException e){
                        e.printStackTrace();
                    }
                }
            }
        });


        File dex = new File("/sdcard/fragments.dex");
        if(dex.exists()){
            dexClassLoader = new DexClassLoader("/sdcard/fragments.dex",getCacheDir().getAbsolutePath(),null, getClassLoader());
        } else {
            File f = createFileFromInputStream();

            dexClassLoader = new DexClassLoader(f.getAbsolutePath(),getCacheDir().getAbsolutePath(),null, getClassLoader());
        }

        try {
            Class c = dexClassLoader.loadClass("zz.experiment.fragmentslib.BlankFragment1");
            Fragment f = (Fragment)c.newInstance();
            getFragmentManager().beginTransaction().add(R.id.content_frame, f).commit();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e){
            e.printStackTrace();
        }catch (IllegalAccessException e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private File createFileFromInputStream() {
        try{
            AssetManager am = getAssets();
            InputStream inputStream = am.open("fragments.dex");

            File f = new File(getFilesDir(),"fragments.dex");
            OutputStream outputStream = new FileOutputStream(f);
            byte buffer[] = new byte[1024];
            int length = 0;

            while((length=inputStream.read(buffer)) > 0) {
                outputStream.write(buffer,0,length);
            }

            outputStream.close();
            inputStream.close();

            return f;
        }catch (IOException e) {
            //Logging exception
        }

        return null;
    }
}
