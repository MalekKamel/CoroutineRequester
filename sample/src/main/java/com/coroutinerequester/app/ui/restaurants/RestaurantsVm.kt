package com.coroutinerequester.app.ui.restaurants

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.coroutinerequester.app.data.DataManager
import com.coroutinerequester.app.data.mapper.ListMapperImpl
import com.coroutinerequester.app.data.model.Restaurant
import com.coroutinerequester.app.data.model.RestaurantMapper
import com.coroutinerequester.app.presentation.view.BaseViewModel
import com.sha.coroutinerequester.RequestOptions
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val restaurantsModule = module {
    viewModel { RestaurantsVm(get()) }
}

class RestaurantsVm(dataManager: DataManager) : BaseViewModel(dataManager) {

    var restaurants = MutableLiveData<List<Restaurant>>()

    fun restaurants() {
        viewModelScope.launch {
            val options = RequestOptions.create {
                inlineHandling = { false }
                showLoading = true
            }
           requester.request(options) {
               val result = dm.restaurantsRepo.all()
               val list: List<Restaurant> =  ListMapperImpl(RestaurantMapper()).map(result.restaurants)
               restaurants.value = list.toMutableList()
           }
        }
    }

}

