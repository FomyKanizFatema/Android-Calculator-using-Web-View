package fomy.fomy.fomy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	Context context;
	WebView w;
	TextView txt;
	String fileName;
	String fileContents;
	FileOutputStream fout;
	InputStream fin;
	ConnectionDetector con;
	String sourceURL;
	File saveTo;
	File dir;
	File exDir;
	String updatedData;
	StringBuilder c;
	StringBuilder content;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        content = new StringBuilder();
        
        context=getApplicationContext();
        con=new ConnectionDetector(context);
        w=(WebView)findViewById(R.id.web);
        txt=(TextView)findViewById(R.id.txt);
        
        w.getSettings().setJavaScriptEnabled(true);
       
        fileName="Calculator.html";
        dir=context.getFilesDir();
        fileContents="<html><body><h1>hihihi</h1></body></html>";
        sourceURL="https://drive.google.com/uc?export=download&id=1MS2QwbO7U1-STu6zy7DOyX8lJndeqauF";
        
      InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
      imm.hideSoftInputFromWindow(w.getWindowToken(), 0);
      txt.setText("Loaded From Saved File");
      w.loadUrl("file:///"+getApplicationContext().getFilesDir().getAbsolutePath()+'/'+fileName)    ;  
         	
        if(con.isConnected()==true){
        	new Download().execute(sourceURL);        	
        }
       // w.loadUrl("file:///"+context.getFilesDir().toString()+'/'+fileName)    ;  
      
        
    }
  
      public class Download extends AsyncTask<String, String, String>{
    	  
    	  protected void onPreExecute(){
    		  txt.setText("Loaded From Saved File");
    		  w.loadUrl("file:///"+getApplicationContext().getFilesDir().getAbsolutePath()+'/'+fileName)    ; 
    		 // w.loadUrl("file:///"+context.getFilesDir().toString()+'/'+fileName)    ; 
    	  }
    	  
		@Override
		protected String doInBackground(String... urls) {
			txt=(TextView)findViewById(R.id.txt);
	        try
	        {
	          
	          URL url = new URL("https://drive.google.com/uc?export=download&id=1MS2QwbO7U1-STu6zy7DOyX8lJndeqauF");
	          HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
	         // URLConnection urlConnection = url.openConnection();
	          BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
	          String line;

	          while ((line = bufferedReader.readLine()) != null)
	          {
	            content.append(line + "\n");
	            fileContents=content.toString();
	            Log.i("Writting","gddwefer");
	          }
	         
	          bufferedReader.close();
	        	}
	        catch(Exception e)
	        {
	          e.printStackTrace();
	        }
			return content.toString();
	            
		}
		
		protected void onProgressUpdate(){
			
		}
		protected void onPostExecute(String result){
			String output=result;
			txt.setText("Downloaded Version"+fileName);
			writeFile(fileName,output);
		}
    	
    }
    
      
 public void setContent(String s){
	 this.fileContents=s;
 }
 public String getContent(){
	 return fileContents;
 }
    public String readFile(String name){
    	 BufferedReader reader;
         String line="";
         String s ="";
       
         try {  
       	  fin = openFileInput(name);
             reader = new BufferedReader(new InputStreamReader(fin,"UTF-8"));
             line = reader.readLine();
             while (line != null) 
             {
              s = s + line;
              s =s+"\n";
              line = reader.readLine();              
           }
             return s;
         } catch (UnsupportedEncodingException e1) {
             // TODO Auto-generated catch block
             e1.printStackTrace();
         } catch (FileNotFoundException e) {
        	 // TODO Auto-generated catch block
        	 e.printStackTrace();
         } catch (IOException e) {
        	 // TODO Auto-generated catch block
        	 e.printStackTrace();
   	}
		 return "nothing Found";
    }
    
    public boolean writeFile(String fileName,String contents){
    	
    	try {
    		
            fout = openFileOutput(fileName, Context.MODE_PRIVATE);
            fout.write(contents.getBytes());
            fout.close();
            
            return true;
        } catch (Exception e) {
        	txt.setText("updating Error");
            e.printStackTrace();
        }
    	return false;
    }
    
}