package com.imams.core.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<VB: ViewBinding>: AppCompatActivity() {

    private var _binding: ViewBinding? = null

    protected fun getViewBinding(): VB = _binding as VB

    abstract fun setBindingFactory(layoutInflater: LayoutInflater): VB

    @LayoutRes
    open fun getLayoutId(): Int? {return null}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inflateBinding()
        initContent()
        setListener()
        observeViewModel()
        fetchData()
    }

    private fun inflateBinding() {
        _binding = setBindingFactory(layoutInflater)
        _binding?.let { setContentView(it.root) }
    }

    protected open fun initContent() {}

    protected open fun setListener() {}

    protected open fun observeViewModel() {}

    protected open fun fetchData() {}

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    open fun printLog(msg: String) {
        if (!BuildConfig.DEBUG) return
        Log.d(javaClass.simpleName, msg)
    }

}