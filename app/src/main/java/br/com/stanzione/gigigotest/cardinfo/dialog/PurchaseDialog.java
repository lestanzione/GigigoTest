package br.com.stanzione.gigigotest.cardinfo.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import br.com.stanzione.gigigotest.main.MainActivity;

public class PurchaseDialog extends DialogFragment {

    public static PurchaseDialog newInstance(){
        PurchaseDialog purchaseDialog = new PurchaseDialog();
        return purchaseDialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        setCancelable(false);

        return new AlertDialog.Builder(getActivity())
                .setTitle("Purchased!")
                .setMessage("You purchase has been processed successfully")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(getContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                })
                .create();
    }

}
