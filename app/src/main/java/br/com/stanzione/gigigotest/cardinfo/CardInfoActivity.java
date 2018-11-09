package br.com.stanzione.gigigotest.cardinfo;

import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import java.util.regex.Pattern;

import javax.inject.Inject;

import br.com.stanzione.gigigotest.App;
import br.com.stanzione.gigigotest.R;
import br.com.stanzione.gigigotest.cardinfo.dialog.PurchaseDialog;
import br.com.stanzione.gigigotest.data.CardInformation;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CardInfoActivity extends AppCompatActivity implements CardInfoContract.View {

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

    @Inject
    CardInfoContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_info);

        setupUi();
        setupInjector();
    }

    private void setupUi(){
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.title_card_information);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        cardInfoValidEditText.addTextChangedListener(new TextWatcher() {

            boolean isBack = false;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(i1 == 1){
                    isBack = true;
                }
                else{
                    isBack = false;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

                cardInfoValidEditText.removeTextChangedListener(this);

                if(!isBack) {
                    if (editable.toString().length() == 2) {
                        editable.append("/");
                    }
                }

                cardInfoValidEditText.addTextChangedListener(this);

            }
        });
    }

    private void setupInjector(){
        ((App) (getApplicationContext()))
                .getApplicationComponent()
                .inject(this);

        presenter.attachView(this);
    }

    @OnClick(R.id.finishPurchaseButton)
    public void onFinishPurchaseButtonClicked(){

        String number = cardInfoNumberEditText.getText().toString().trim();
        String name = cardInfoNameEditText.getText().toString().trim();
        String valid = cardInfoValidEditText.getText().toString().trim();
        String cvv = cardInfoCvvEditText.getText().toString().trim();

        if(number.length() != 16) {
            cardInfoNumberEditText.setError(getString(R.string.error_card_number_length));
            return;
        }

        if(TextUtils.isEmpty(name)) {
            cardInfoNameEditText.setError(getString(R.string.error_card_name_empty));
            return;
        }

        Pattern pattern = Pattern.compile("(1[0-2]|0[1-9]|\\d)\\/([0-9]\\d)");
        if(valid.length() != 5 || !pattern.matcher(valid).matches()){
            cardInfoValidEditText.setError(getString(R.string.error_card_valid));
            return;
        }

        if(cvv.length() != 3){
            cardInfoCvvEditText.setError(getString(R.string.error_card_cvv_length));
            return;
        }

        CardInformation cardInformation = new CardInformation();
        cardInformation.setNumber(number);
        cardInformation.setName(name);
        cardInformation.setValid(valid);
        cardInformation.setCvv(cvv);

        presenter.sendCardInformation(cardInformation);

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

    @Override
    public void showSuccessMessage() {
        PurchaseDialog purchaseDialog = PurchaseDialog.newInstance();
        purchaseDialog.show(getSupportFragmentManager(), PurchaseDialog.class.getSimpleName());
    }

    @Override
    public void setProgressBarVisible(boolean visible) {
        if (visible) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void showGeneralError() {
        Snackbar.make(coordinatorLayout, getResources().getString(R.string.message_general_error), Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showNetworkError() {
        Snackbar.make(coordinatorLayout , getResources().getString(R.string.message_network_error), Snackbar.LENGTH_LONG).show();
    }
}
