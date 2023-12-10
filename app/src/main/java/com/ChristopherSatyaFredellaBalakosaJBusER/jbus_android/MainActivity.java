package com.ChristopherSatyaFredellaBalakosaJBusER.jbus_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionBarPolicy;

import com.ChristopherSatyaFredellaBalakosaJBusER.jbus_android.model.Bus;
import com.ChristopherSatyaFredellaBalakosaJBusER.jbus_android.request.BaseApiService;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
/**
 * MainActivity Class
 * Represents the main screen of the JBusER Android application.
 * It displays a list of buses and provides pagination functionality for the bus list
 * @author Christopher Satya
 */
public class MainActivity extends AppCompatActivity {
    private ListView listview_main;
    private Button[] btns;
    private BaseApiService mApiService;
    private Context mContext;
    private int currentPage = 0;
    private int pageSize = 5; // kalian dapat bereksperimen dengan field ini
    private int listSize;
    private int noOfPages;
    public static int busIndex;
    public static List<Bus> listBus = new ArrayList<>();
    private Button prevButton = null;
    private Button nextButton = null;
    private ListView busListView = null;
    private HorizontalScrollView pageScroll = null;

    public MainActivity() {
    }
    // Fields and methods of the MainActivity class...

    /**
     * Initializes the activity and sets up the user interface components.
     * @param savedInstanceState The saved state of the activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listview_main = findViewById(R.id.listview_main);
        BusArrayAdapter busArrayAdapter = new BusArrayAdapter(this, R.layout.bus_view_list, Bus.sampleBusList(5));
        listview_main.setAdapter(busArrayAdapter);

        prevButton = findViewById(R.id.prev_page);
        nextButton = findViewById(R.id.next_page);
        pageScroll = findViewById(R.id.page_number_scroll);
        busListView = findViewById(R.id.listview_main);
        listBus = Bus.sampleBusList(20);
        listSize = listBus.size();
        paginationFooter();
        goToPage(currentPage);
        prevButton.setOnClickListener(v -> {
            currentPage = currentPage != 0? currentPage-1 : 0;
            goToPage(currentPage);
        });
        nextButton.setOnClickListener(v -> {
            currentPage = currentPage != noOfPages -1? currentPage+1 : currentPage;
            goToPage(currentPage);
        });
    }
    /**
     * Initializes the action bar menu and handles menu item selections.
     * @param menu The options menu in which items are placed.
     * @return `true` if the menu item is selected, `false` otherwise.
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar_menu, menu);
        return true;
    }
    /**
     * Handles actions when a menu item is selected.
     * @param item The selected menu item.
     * @return `true` if the action is handled, `false` otherwise.
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.search_button) {
            Toast.makeText(MainActivity.this, "button", Toast.LENGTH_SHORT).show();
            return true;
        } else if (item.getItemId() == R.id.profile_account) {
            Intent intent = new Intent(this, AboutMeActivity.class);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.payment_checker) {
            Toast.makeText(MainActivity.this, "payment", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, PaymentActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Initializes the pagination footer for the bus list and handles button clicks for navigation.
     */
    private void paginationFooter() {
        int val = listSize % pageSize;
        val = val == 0 ? 0:1;
        noOfPages = listSize / pageSize + val;
        LinearLayout ll = findViewById(R.id.btn_layout);
        btns = new Button[noOfPages];
        if (noOfPages <= 6) {
            ((FrameLayout.LayoutParams) ll.getLayoutParams()).gravity =
                    Gravity.CENTER;
        }
        for (int i = 0; i < noOfPages; i++) {
            btns[i]=new Button(this);
            btns[i].setBackgroundColor(getResources().getColor(android.R.color.transparent));
            btns[i].setText(""+(i+1));
            btns[i].setTextColor(getResources().getColor(R.color.black));
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(100, 100);
            ll.addView(btns[i], lp);
            final int j = i;
            btns[j].setOnClickListener(v -> {
                currentPage = j;
                goToPage(j);
            });
        }
    }
    /**
     * Navigates to the specified page in the paginated bus list.
     * @param index The index of the page to navigate to.
     */
    private void goToPage(int index) {
        for (int i = 0; i< noOfPages; i++) {
            if (i == index) {
                btns[index].setBackgroundDrawable(getResources().getDrawable(R.drawable.circle));
                btns[i].setTextColor(getResources().getColor(android.R.color.white));
                scrollToItem(btns[index]);
                viewPaginatedList(listBus, currentPage);
            } else {
                btns[i].setBackgroundColor(getResources().getColor(android.R.color.transparent));
                btns[i].setTextColor(getResources().getColor(android.R.color.black));
            }
        }
    }
    /**
     * Scrolls to the specified item in the pagination footer.
     * @param item The item to scroll to.
     */
    private void scrollToItem(Button item) {
        int scrollX = item.getLeft() - (pageScroll.getWidth() - item.getWidth()) / 2;
        pageScroll.smoothScrollTo(scrollX, 0);
    }
    /**
     * Displays the paginated list of buses on the main screen.
     * @param listBus The list of buses to display.
     * @param page    The current page to display.
     */
    private void viewPaginatedList(List<Bus> listBus, int page) {
        int startIndex = page * pageSize;
        int endIndex = Math.min(startIndex + pageSize, listBus.size());
        List<Bus> paginatedList = listBus.subList(startIndex, endIndex);
        BusArrayAdapter paginatedAdapter = new BusArrayAdapter(this, R.layout.bus_view_list, paginatedList);
        listview_main.setAdapter(paginatedAdapter);

    }
}
