package com.coroutinerequester.app.ui.restaurants

import android.widget.Toast
import androidx.lifecycle.Observer
import com.coroutinerequester.app.R
import com.coroutinerequester.app.presentation.view.BaseFrag
import com.coroutinerequester.app.util.linearLayoutManager
import kotlinx.android.synthetic.main.include_recycler_view_refreshable.*
import org.koin.android.viewmodel.ext.android.viewModel

class RestaurantsFrag : BaseFrag<RestaurantsVm>() {

    override val vm: RestaurantsVm by viewModel()
    override var layoutId: Int = R.layout.frag_search

    companion object {
        fun newInstance(): RestaurantsFrag {
            return RestaurantsFrag()
        }
    }

    override fun doOnViewCreated() {
        super.doOnViewCreated()
        setupVm()
        rv.linearLayoutManager(context)

        loadRestaurants()

        swipeRefresh.setOnRefreshListener { loadRestaurants() }
    }

    private fun setupVm() {
        vm.toggleLoading.observe(this, Observer {
            swipeRefresh.isRefreshing = it
        })

        vm.showError.observe(this, Observer {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        })

        vm.showErrorRes.observe(this, Observer {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        })
    }

    private fun loadRestaurants() {
        vm.restaurants.observe(this, Observer {
            rv.adapter = RestaurantsAdapter(list = it)
            rv.scheduleLayoutAnimation()
        })
        vm.restaurants()
    }
}
