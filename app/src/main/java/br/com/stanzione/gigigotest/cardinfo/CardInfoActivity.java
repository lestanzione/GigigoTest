package br.com.stanzione.gigigotest.cardinfo;

import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import br.com.stanzione.gigigotest.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CardInfoActivity extends AppCompatActivity {

    @BindView(R.id.coordinator)
    CoordinatorLayout coordinatorLayout;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.cardInfoNumberEditText)
    EditText cardInfoNumberEditText;

    @BindView(R.id.cardInfoNameEditText)
    EditText cardInfoNameEditText;

    @BindView(R.id.cardInfoValidEditText)
    EditText cardInfoValidEditText;

    @BindView(R.id.cardInfoCvvEditText)
    EditText cardInfoCvvEditText;

    @BindView(R.id.finishPurchaseButton)
    Button finishPurchaseButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_info);

        setupUi();
    }

    private void setupUi(){
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.title_card_information);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        cardInfoValidEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                cardInfoValidEditText.removeTextChangedListener(this);

                if(editable.toString().length() == 2){
                    editable.append("/");
                }

                cardInfoValidEditText.addTextChangedListener(this);

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            default:
                return false;
        }

    }

}
