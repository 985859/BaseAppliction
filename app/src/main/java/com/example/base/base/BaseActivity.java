package com.example.base.base;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;


public abstract class BaseActivity<DB extends ViewDataBinding> extends AppCompatActivity {

    protected DB binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        beforeCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        if (getLayoutId() != 0) {
            binding = DataBindingUtil.setContentView(this, getLayoutId());
            binding.setLifecycleOwner(this);
        }

        initView();
        initData();
    }

    protected void beforeCreate(Bundle savedInstanceState) {
    }

    protected abstract int getLayoutId();

    protected abstract void initView();

    protected void initData() {
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (binding != null) {
            binding.unbind();
        }

    }
}