package com.imams.opencurrency.ui.main

import android.content.res.Resources
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.imams.core.ui.BaseActivity
import com.imams.data.model.param.CurrencyViewParam
import com.imams.opencurrency.R
import com.imams.opencurrency.databinding.ActivityMainBinding
import com.skydoves.powermenu.MenuAnimation
import com.skydoves.powermenu.PowerMenu
import com.skydoves.powermenu.PowerMenuItem
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {

    private val viewModel: MainViewModel by viewModels()

    private var conversionJob: Job? = null

    private var inputAmount: Long = 0
    private var selectedCurrency: String = "USD"

    lateinit var powerMenu: PowerMenu

    private val adapter: CurrencyBoxAdapter by lazy {
        CurrencyBoxAdapter(listOf()) {
            // todo
        }
    }

    override fun setBindingFactory(layoutInflater: LayoutInflater): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun initContent() {
        super.initContent()
        powerMenu = PowerMenu.Builder(this)
            .setAnimation(MenuAnimation.DROP_DOWN)
            .setMenuRadius(10f)
            .setMenuShadow(10f)
            .setWidth(Resources.getSystem().displayMetrics.widthPixels)
            .setTextGravity(Gravity.CENTER)
            .setTextSize(12)
            .setDividerHeight(1)
            .build()

        powerMenu.setOnMenuItemClickListener { _, item ->
            selectedCurrency = item.tag.toString()
            getViewBinding().spinner.text = item.title
            performConversion(selectedCurrency)
            powerMenu.dismiss()
        }

        getViewBinding().spinner.setOnClickListener {
            powerMenu.showAsDropDown(getViewBinding().spinner)
        }

        with(getViewBinding()) {
            recyclerview.layoutManager = GridLayoutManager(this@MainActivity, 3)
            recyclerview.adapter = adapter

            editText.addTextChangedListener(object : TextWatcher{
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun afterTextChanged(p0: Editable?) {
                    p0?.let {
                        inputAmount = try { it.toString().toLong() } catch (e: Exception) {inputAmount}
                        performConversion(selectedCurrency, inputAmount)
                    }
                }
            })
        }
    }

    override fun observeViewModel() {
        with(viewModel) {
            currencies.observe(this@MainActivity) {
                it?.let { setBaseContent(it) }
            }

            currenciesRates.observe(this@MainActivity) {
                it?.let {
                    setConversionResult(it)
                }
            }

            latestTimeStamp.observe(this@MainActivity) {
                it?.let {
                    with(getViewBinding()) {
                        val label = resources.getString(R.string.label_latest_timestamp) + " $it"
                        tvTimestamp.text = label
                    }
                }
            }
        }
    }

    private fun performConversion(base: String, amount: Long = inputAmount) {
        conversionJob?.cancel()
        conversionJob = lifecycleScope.launch {
            viewModel.performConversion(
                amount = amount,
                base = base
            )
        }
        conversionJob?.start()
    }

    override fun fetchData() {
        viewModel.fetchAllCurrency()
    }

    private fun setBaseContent(list: List<CurrencyViewParam>) {
        powerMenu.addItemList(
            list.map {
                PowerMenuItem("${it.code} - ${it.name}", it.code)
            }
        )
    }

    private fun setConversionResult(list: List<CurrencyViewParam>) {
        adapter.update(list)
    }

}