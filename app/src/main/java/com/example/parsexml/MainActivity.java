package com.example.parsexml;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ListView list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list = findViewById(R.id.listView);
        XmlPullParserFactory parserfactory ;
        ArrayList<Product> products;
        ArrayList<String> Liste = new ArrayList<>();
        String res;
        try {
            parserfactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser =parserfactory.newPullParser();
            InputStream is = getAssets().open("data.xml");
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES,false);
            parser.setInput(is,null);
            products = Parsing(parser);
            for(Product pro : products)
            {
                res="N° : "+pro.getId()+"   |    "+pro.getDesignation()+"    |     "+pro.getPrice() + " MAD";
                Log.e("MainActivity",res);
                Liste.add(res);
                ListAdapter listAdapter=new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,Liste);
                list.setAdapter(listAdapter);
            }
        } catch (XmlPullParserException e) { e.printStackTrace();
        } catch (IOException e) { e.printStackTrace();
        }
    }


    private ArrayList<Product> Parsing(XmlPullParser parser){
        ArrayList<Product> products = new ArrayList<>();
        try {
            int eventType=parser.getEventType();
            Product currentproduct=null;
            while(eventType != XmlPullParser.END_DOCUMENT){
                String eltName = null;
                switch(eventType)
                {
                    case XmlPullParser.START_TAG:
                        eltName = parser.getName();
                        if("product".equals(eltName)){
                            currentproduct = new Product();
                            products.add(currentproduct);
                        }else if(currentproduct!=null){
                            if("id".equals(eltName)){
                                currentproduct.setId(Integer.parseInt(parser.nextText()));
                            }else if("designation".equals(eltName)){
                                currentproduct.setDesignation(parser.nextText());
                            }else if("price".equals(eltName)){
                                currentproduct.setPrice(Float.parseFloat(parser.nextText()));
                            }
                        }
                        break;
                }
                eventType=parser.next();
            }
        } catch (XmlPullParserException e) { e.printStackTrace();
        } catch (IOException e) { e.printStackTrace();
        }
        return(products);

    }

}