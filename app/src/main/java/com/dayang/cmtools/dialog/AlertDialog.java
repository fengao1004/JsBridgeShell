package com.dayang.cmtools.dialog;

import android.content.Context;
import android.text.TextPaint;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dayang.cmtools.R;

import java.util.List;

/**
 * Created by 冯傲 on 2016/8/14.
 * e-mail 897840134@qq.com
 */
public class AlertDialog extends android.app.Dialog {
    public AlertDialog(Context context) {
        super(context);
    }

    public AlertDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {
        private Context context;
        public onUpClickListener l = null;
        private TextView dialog_content;
        private TextView dialog_content_update_log;


        public Builder(Context context) {
            this.context = context;
        }

        String title;
        String contant;

        public void setTitle(String title) {
            this.title = title;
        }

        public void setContant(String contant) {
            this.contant = contant;
        }

        public void setText(String text) {
            dialog_content.setText(text);
        }

        public AlertDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final AlertDialog dialog = new AlertDialog(context, R.style.DialogUpdate);
            View layout = inflater.inflate(R.layout.dialog_alert, null);
            dialog_content = (TextView) layout.findViewById(R.id.dialog_content);
            dialog_content.setMovementMethod(ScrollingMovementMethod.getInstance());
            dialog.addContentView(layout, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            dialog_content.setText(contant);
            TextView tv_dialog_title = (TextView) layout.findViewById(R.id.tv_dialog_title);
            tv_dialog_title.setText(title);
            layout.findViewById(R.id.enter).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    l.onEnterClick();
                }
            });
            dialog.setContentView(layout);
            TextView tv = (TextView) layout.findViewById(R.id.tv_dialog_title);
            TextPaint tp = tv.getPaint();
            tp.setFakeBoldText(true);
            return dialog;
        }

        public void setOnClick(onUpClickListener l) {
            this.l = l;
        }


    }

    public interface onUpClickListener {
        void onEnterClick();

    }
}
