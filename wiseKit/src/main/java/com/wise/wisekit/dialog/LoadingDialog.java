package com.wise.wisekit.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.wise.wisekit.R;

public class LoadingDialog extends Dialog{


    public LoadingDialog(Context context) {
        super(context);
    }

    public LoadingDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public static class Builder{

        private Context context;
        private String message;
        private boolean isShowMessage=true;
        private boolean isCancelable=false;
        private boolean isCancelOutside=false;
        private OnCancelListener cancelListener;


        public Builder(Context context) {
            this.context = context;
        }

        /**
         * Set Hint Message
         * @param message
         * @return
         */

        public Builder setMessage(String message){
            this.message=message;
            return this;
        }

        /**
         * Set is Show Hint Message
         * @param isShowMessage
         * @return
         */
        public Builder setShowMessage(boolean isShowMessage){
            this.isShowMessage=isShowMessage;
            return this;
        }

        /**
         * Set Could canceled by Return Button
         * @param isCancelable
         * @return
         */

        public Builder setCancelable(boolean isCancelable){
            this.isCancelable=isCancelable;
            return this;
        }

        /**
         * Set Can Cancelled by click other place.
         * @param isCancelOutside
         * @return
         */
        public Builder setCancelOutside(boolean isCancelOutside){
            this.isCancelOutside=isCancelOutside;
            return this;
        }

        /**
         * Set OnCancel Listener
         * @param listener
         * @return
         */
        public Builder setOnCancelListener(OnCancelListener listener) {
            this.cancelListener = listener;
            return this;
        }

        public LoadingDialog create(){

            LayoutInflater inflater = LayoutInflater.from(context);
            View view=inflater.inflate(R.layout.dialog_loading,null);
            LoadingDialog loadingDailog=new LoadingDialog(context,R.style.LoadingDialogStyle);
            TextView msgText= (TextView) view.findViewById(R.id.tipTextView);
            if(isShowMessage){
                msgText.setText(message);
            }else{
                msgText.setVisibility(View.GONE);
            }
            loadingDailog.setContentView(view);
            loadingDailog.setCancelable(isCancelable);
            loadingDailog.setCanceledOnTouchOutside(isCancelOutside);
            loadingDailog.setOnCancelListener(cancelListener);
            return  loadingDailog;

        }


    }
}
