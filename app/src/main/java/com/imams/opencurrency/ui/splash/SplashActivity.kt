package com.imams.opencurrency.ui.splash

import  android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.imams.core.ui.BaseActivity
import com.imams.opencurrency.databinding.ActivitySplashScreenBinding
import com.imams.opencurrency.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity: BaseActivity<ActivitySplashScreenBinding>() {

    private val viewModel: SplashScreenViewModel by viewModels()
    private var doneFetchLatest = false
    private var doneFetchCurrencies = false

    override fun setBindingFactory(layoutInflater: LayoutInflater): ActivitySplashScreenBinding {
        return ActivitySplashScreenBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.generateService()
        val time = System.currentTimeMillis()
        printLog("currentTimeMillis $time")
    }

    override fun observeViewModel() {
        viewModel.splashViewState.observe(this) {
            it?.let {
                when (it) {
                    is SplashScreenViewModel.SplashViewState.ProcessCurrencies -> {
                        doneFetchCurrencies = it.done
                        openMain(doneFetchCurrencies, doneFetchLatest)
                        printLog("openMain 1 $doneFetchCurrencies")
                    }
                    is SplashScreenViewModel.SplashViewState.ProcessLatest -> {
                        doneFetchLatest = it.done
                        openMain(doneFetchCurrencies, doneFetchLatest)
                        printLog("openMain 2 $doneFetchLatest")
                    }
                }
            }
        }
        viewModel.shouldFetchData.observe(this) {
            it?.let {
                if (!it) openMain(p1 = true, p2 = true)
            }
        }
    }

    private fun openMain(p1: Boolean = doneFetchCurrencies, p2: Boolean = doneFetchLatest) {
        printLog("openMain 3 $p1 $p2")
        if (p1 && p2) {
            lifecycleScope.launch {
                delay(1000)
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                finish()
            }
        }
    }

    override fun printLog(msg: String) {
        println("SplashScreen $msg")
    }

}