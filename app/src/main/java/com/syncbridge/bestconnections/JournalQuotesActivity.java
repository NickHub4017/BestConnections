package com.syncbridge.bestconnections;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Classes.CustomAdapter;
import Classes.ListModel;
import DBHelper.Option;
import DBHelper.OptionDataSource;
import DBHelper.Quote;
import DBHelper.QuoteDataSource;


public class JournalQuotesActivity extends Activity {

    public Context m_context;
    String SECTION;

    ListView list;
    CustomAdapter adapter;
    public JournalQuotesActivity CustomListView = null;
    public ArrayList<ListModel> CustomListViewValuesArr = new ArrayList<ListModel>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_quotes);

        m_context = this.getApplicationContext();
        CustomListView = this;
        SECTION = getIntent().getExtras().getString("SECTION");

        /******** Take some data in Arraylist ( CustomListViewValuesArr ) ***********/
        setListData();

        Resources res =getResources();
        list= ( ListView )findViewById( R.id.list );  // List defined in XML ( See Below )

        /**************** Create Custom Adapter *********/
        adapter=new CustomAdapter( CustomListView, CustomListViewValuesArr,res );
        list.setAdapter(adapter);
    }

    /****** Function to set data in ArrayList *************/
    public void setListData()
    {
        CustomListViewValuesArr.clear();

        try {
            QuoteDataSource quoteDataSource = new QuoteDataSource(m_context);
            quoteDataSource.open();

            if(SECTION.equals("CS")) {
                for(String SUB_SECTION : new String[]{"CSS", "CSH", "CSM"}){
                    for (Quote quote : getLikedQuotes(SUB_SECTION)) {
                        final ListModel sched = new ListModel();

                        /******* Firstly take data in model object ******/
                        sched.setQuote(quote.getQuote());
                        sched.setPersonSource(quote.getPerson_source());

                        /******** Take Model Object in ArrayList **********/
                        CustomListViewValuesArr.add(sched);
                    }
                }
            } else {
                for (Quote quote : getLikedQuotes(SECTION)) {
                    final ListModel sched = new ListModel();

                    /******* Firstly take data in model object ******/
                    sched.setQuote(quote.getQuote());
                    sched.setPersonSource(quote.getPerson_source());

                    /******** Take Model Object in ArrayList **********/
                    CustomListViewValuesArr.add(sched);
                }
            }
        } catch (SQLException ex) {

        }
    }

    private List<Quote> getLikedQuotes(String section) {
        List<Quote> quotes = new ArrayList<>();
        try {
            QuoteDataSource quoteDataSource = new QuoteDataSource(m_context);
            quoteDataSource.open();
            OptionDataSource optionDataSource = new OptionDataSource(m_context);
            optionDataSource.open();

            for (Quote quote : quoteDataSource.getAllEntries(section, true)) {
                if(quote.getAnswered()) {
                    Option option = optionDataSource.getEntry(quote.getUnique_id());

                    if(!option.getIs_bullseye() && !option.getIs_like()) {
                        continue;
                    }

                    quotes.add(quote);
                }
            }

        } catch (SQLException ex) {

        }

        return  quotes;
    }

    /*****************  This function used by adapter ****************/
    public void onItemClick(int mPosition)
    {
        ListModel tempValues = (ListModel) CustomListViewValuesArr.get(mPosition);


        // SHOW ALERT

    }


    //region gen

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_journal_quotes, menu);
        return false;
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

    //endregion
}
