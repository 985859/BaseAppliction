package com.example.base.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;

public abstract class BaseFragment<DB extends ViewDataBinding> extends Fragment {

    protected DB binding;
    private View rootView;
    protected boolean isLazy = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getLayoutId() != 0) {
            if (rootView == null) {
                binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
                binding.setLifecycleOwner(this);
                rootView = binding.getRoot();
                initView();
            }
            return rootView;
        } else {
            return super.onCreateView(inflater, container, savedInstanceState);
        }
    }

    protected abstract int getLayoutId();

    protected abstract void initView();

    protected void lazyData() {

    }


    @Override
    public void onResume() {
        super.onResume();
        if (isLazy) {
            isLazy = false;
            lazyData();
        }


    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (null != rootView) {
            if (rootView.getParent() != null) {
                ((ViewGroup) rootView.getParent()).removeView(rootView);
            }
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        binding.unbind();
    }
}
