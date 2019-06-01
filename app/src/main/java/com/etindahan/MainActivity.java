package com.etindahan;

        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.darwindeveloper.horizontalscrollmenulibrary.custom_views.HorizontalScrollMenuView;
        import com.darwindeveloper.horizontalscrollmenulibrary.extras.MenuItem;

public class MainActivity extends AppCompatActivity {

    HorizontalScrollMenuView menu;
    TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        menu = (HorizontalScrollMenuView)findViewById(R.id.menu);
        textView = (TextView)findViewById(R.id.txtText);

        //Menu Sample
        initMenu();
    }

    private void initMenu() {
        menu.addItem("Transaction",R.drawable.ic_money);
        menu.addItem("Payment",R.drawable.ic_payment);
        menu.addItem("Account",R.drawable.ic_acc);
        menu.addItem("Support",R.drawable.ic_done);

        menu.setOnHSMenuClickListener(new HorizontalScrollMenuView.OnHSMenuClickListener() {
            @Override
            public void onHSMClick(MenuItem menuItem, int position) {
                Toast.makeText(MainActivity.this, ""+menuItem, Toast.LENGTH_SHORT).show();
                textView.setText(menuItem.getText());
            }
    });
    }
}
