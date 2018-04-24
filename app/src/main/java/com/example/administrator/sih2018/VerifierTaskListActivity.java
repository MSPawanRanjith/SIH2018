package com.example.administrator.sih2018;

import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class VerifierTaskListActivity extends ListActivity {

    public ListView lv;
    private ArrayAdapter<String> arrayAdapter;
    String[] itemname ={

            "NAME: White rice #3\nBNR\n CODE:110111101100\nDES: Packaging: Loose or common packaging;  Quality Preparation: Non-parboiled (uncooked); Milling: Well-milled\n Share of broken rice: Low (less than 15%); Aromatic (fragrant): No; Enriched: No; Variety: Preferred local variety",
            "NAME: White rice #7\nPrepacked\nBL\nCODE: 110111101102\nDES: Packaging: Loose or common packaging\n Preparation: Parboiled (cooked) Milling: Extra well-milled; Share of broken rice: Very low (less than 5%) Aromatic (fragrant): No; Enriched: No; Variety: Preferred local variety",
            "NAME: Basmati rice\nWKB\nCODE: 110111101108\nDES: Packaging: Loose or common packaging Preparation: Parboiled (cooked) Milling: Extra well-milled; Share of broken rice: Very low (less than 5%) Aromatic (fragrant): No; Enriched: No; Variety: Preferred local variety",
            "NAME: Brown rice\nFamily pack\nBL\nCODE: 110111101190\nDES:Type: Long grain, brown rice (husked, whole-grain rice)Packaging: Pre-packed; paper or plastic bag ; Quality: Exclude: Premium rice (e.g. basmati rice, jasmine rice), sticky rice, quick cooking rice; Specify: Label, if any",
            "NAME: Brown rice\nLoose\nCODE: 110111101123\nDES:  brown rice (husked, whole-grain rice)Packaging: Pre-packed; paper or plastic bag ; Quality: Exclude: Premium rice (e.g. basmati rice, jasmine rice), sticky rice, quick cooking rice; Specify: Label, if any",
            "NAME: Long-grain rice\nParboiled\nWKB\nCODE:110111101176\nDES: Type: Long grain, brown rice (husked, whole-grain rice)Packaging: Pre-packed; paper or plastic bag ; Quality: Exclude: Premium rice (e.g. basmati rice, jasmine rice), sticky rice, quick cooking rice; Specify: Label, if any"
    };
    String[] item = {
            "White rice #3","White rice #7","Premium rice","Basmati rice","Brown rice","Brown rice","Long-grain rice"
    };
    String[] type = {
            "BNR",
            "Prepacked\nBL",
            "BNR",
            "WKB",
            "Family pack\nBL",
            "Loose",
            "Parboiled\nWKB"
    };
    String[] code = {
            "110111101100","110111101102","110111101103","110111101108","110111101190","110111101123","110111101176"
    };
    Integer[] imgid={
            R.drawable.image_ic1,
            R.drawable.image_ic2,
            R.drawable.image_ic3,
            R.drawable.image_ic4,
            R.drawable.image_ic5,
            R.drawable.image_ic6,
            R.drawable.image_ic7
    };
    /*
    String[] code ={
            "110111101100",
            "110111101120",
            "110111101120",
            "110111101120",
            "110111101120",
            "110111101120",
            "110111101120",
            "110111101120"
    };
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verifier_task_list);

//        this.setListAdapter(new ArrayAdapter<String>(
//                this, R.layout.mylist,
//                R.id.Pname,itemname));


//        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_2, itemname);
//        lv.setAdapter(arrayAdapter);
//
//       lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                final int pos =i;
//                Log.d("List Item Clicked",(String)adapterView.getAdapter().getItem(i));
//
//                //Toast.makeText(getBaseContext(),Toast.LENGTH_SHORT).show();
//            }
//        });
            MySimpleArrayAdapter adapter = new MySimpleArrayAdapter(this, itemname,type,code,imgid);
            setListAdapter(adapter);
            lv=getListView();

         lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
             @Override
             public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                 //Intent myIntent = new Intent(VerifierTaskListActivity.this,SqliteDatabaseActivity.class);
                 Intent myIntent = new Intent(VerifierTaskListActivity.this,VerifierPriceActivity.class);
                 startActivity(myIntent);
             }
         });




    }
}
